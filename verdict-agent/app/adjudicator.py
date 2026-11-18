from typing import List, Tuple

from . import arbitration, confidence, responsibility, scoring
from .extractor import enrich
from .models import AdjudicateRequest, AdjudicateResponse, Evidence


def score_all(req: AdjudicateRequest, use_llm: bool = True) -> List[Tuple[Evidence, float]]:
    items: List[Tuple[Evidence, float]] = []
    for ev in req.evidences:
        facts = enrich(ev, req.dispute_type, use_llm=use_llm)
        items.append((ev, scoring.score_evidence(ev, req.dispute_type, facts)))
    return items


def adjudicate(req: AdjudicateRequest, use_llm: bool = True) -> AdjudicateResponse:
    items = score_all(req, use_llm=use_llm)
    arb = arbitration.resolve(items)
    conf = confidence.compute(arb.contributions, arb.types_seen)
    resp = responsibility.compute(arb.contributions, arb.total, req.order_amount)

    return AdjudicateResponse(
        dispute_id=req.dispute_id,
        liable_party=resp.liable_party,
        ratio=resp.ratio,
        confidence=conf.value,
        need_human=conf.need_human,
        route=conf.route,
        coverage=conf.coverage,
        gap=conf.gap,
        conflicts=arb.conflicts,
        basis=arb.notes,
        suggestion=resp.suggestion,
    )
