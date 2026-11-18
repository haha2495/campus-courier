import json
from datetime import datetime

from fastapi import FastAPI, HTTPException

from . import store
from .adjudicator import adjudicate
from .cache import cache
from .llm import available
from .models import (
    AdjudicateRequest,
    AdjudicateResponse,
    ReviewRequest,
    ReviewResponse,
    StatsResponse,
)

app = FastAPI(title="VerdictAgent", version="0.2.0")


def _to_response(record) -> AdjudicateResponse:
    return AdjudicateResponse(
        dispute_id=record.dispute_id,
        liable_party=record.liable_party,
        ratio={"requester": record.requester_ratio, "runner": record.runner_ratio},
        confidence=record.confidence,
        need_human=record.need_human,
        route=record.route,
        coverage=record.coverage,
        gap=record.gap,
        conflicts=record.conflicts,
        basis=json.loads(record.basis_json or "[]"),
        suggestion=record.suggestion,
    )


@app.get("/health")
def health() -> dict:
    return {
        "status": "ok",
        "engine": "deepseek" if available() else "rule-engine",
        "cache": cache.backend,
    }


@app.post("/adjudicate", response_model=AdjudicateResponse)
def adjudicate_endpoint(req: AdjudicateRequest) -> AdjudicateResponse:
    session = store.get_session()
    try:
        existing = store.find_by_dispute_id(session, req.dispute_id)
        if existing:
            return _to_response(existing)

        lock_key = f"verdict:lock:{req.dispute_id}"
        if not cache.setnx(lock_key, "1", ttl=30):
            existing = store.find_by_dispute_id(session, req.dispute_id)
            if existing:
                return _to_response(existing)
            raise HTTPException(status_code=409, detail="同一纠纷正在处理中，请稍后重试")

        try:
            result = adjudicate(req)
            store.save_decision(session, req, result)
            return result
        finally:
            cache.delete(lock_key)
    finally:
        session.close()


@app.get("/disputes/{dispute_id}", response_model=AdjudicateResponse)
def get_dispute(dispute_id: str) -> AdjudicateResponse:
    session = store.get_session()
    try:
        record = store.find_by_dispute_id(session, dispute_id)
        if not record:
            raise HTTPException(status_code=404, detail="纠纷不存在")
        return _to_response(record)
    finally:
        session.close()


@app.post("/review", response_model=ReviewResponse)
def review_endpoint(req: ReviewRequest) -> ReviewResponse:
    session = store.get_session()
    try:
        record = store.find_by_dispute_id(session, req.dispute_id)
        if not record:
            raise HTTPException(status_code=404, detail="纠纷不存在，请先完成判责")

        ratios = {"requester": record.requester_ratio, "runner": record.runner_ratio}
        review = store.save_review(session, req, ratios)
        return ReviewResponse(
            dispute_id=review.dispute_id,
            accepted=review.accepted,
            reviewer=review.reviewer,
            reviewed_at=review.created_at.isoformat(),
        )
    finally:
        session.close()


@app.get("/stats", response_model=StatsResponse)
def stats_endpoint() -> StatsResponse:
    session = store.get_session()
    try:
        return StatsResponse(**store.stats(session))
    finally:
        session.close()
