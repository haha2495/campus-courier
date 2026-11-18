import json
from datetime import datetime, timezone
from typing import Optional

from sqlalchemy import Boolean, Column, DateTime, Float, Integer, String, Text, create_engine
from sqlalchemy.orm import Session, declarative_base, sessionmaker

from .config import settings

Base = declarative_base()


class DisputeRecord(Base):
    __tablename__ = "dispute_record"

    id = Column(Integer, primary_key=True, autoincrement=True)
    dispute_id = Column(String(64), unique=True, index=True, nullable=False)
    dispute_type = Column(String(32), nullable=False)
    order_amount = Column(Float, default=0.0)
    liable_party = Column(String(32), nullable=False)
    requester_ratio = Column(Float, default=0.5)
    runner_ratio = Column(Float, default=0.5)
    confidence = Column(Float, default=0.0)
    route = Column(String(32), nullable=False)
    need_human = Column(Boolean, default=True)
    coverage = Column(Float, default=0.0)
    gap = Column(Float, default=0.0)
    conflicts = Column(Integer, default=0)
    basis_json = Column(Text, default="[]")
    suggestion = Column(String(256), default="")
    created_at = Column(DateTime, default=lambda: datetime.now(timezone.utc))


class ReviewRecord(Base):
    __tablename__ = "review_record"

    id = Column(Integer, primary_key=True, autoincrement=True)
    dispute_id = Column(String(64), index=True, nullable=False)
    reviewer = Column(String(64), nullable=False)
    accepted = Column(Boolean, nullable=False)
    final_liable_party = Column(String(32), nullable=True)
    final_requester_ratio = Column(Float, nullable=True)
    final_runner_ratio = Column(Float, nullable=True)
    reason = Column(String(512), default="")
    created_at = Column(DateTime, default=lambda: datetime.now(timezone.utc))


_engine = None
_session_factory = None


def init_db(url: Optional[str] = None):
    global _engine, _session_factory
    target = url or settings.db_url
    connect_args = {"check_same_thread": False} if target.startswith("sqlite") else {}
    _engine = create_engine(target, future=True, connect_args=connect_args)
    _session_factory = sessionmaker(bind=_engine, future=True)
    Base.metadata.create_all(_engine)
    return _engine


def get_session() -> Session:
    if _session_factory is None:
        init_db()
    return _session_factory()


def save_decision(session: Session, req, result) -> DisputeRecord:
    record = DisputeRecord(
        dispute_id=req.dispute_id,
        dispute_type=req.dispute_type,
        order_amount=req.order_amount,
        liable_party=result.liable_party,
        requester_ratio=result.ratio.get("requester", 0.5),
        runner_ratio=result.ratio.get("runner", 0.5),
        confidence=result.confidence,
        route=result.route,
        need_human=result.need_human,
        coverage=result.coverage,
        gap=result.gap,
        conflicts=result.conflicts,
        basis_json=json.dumps(result.basis, ensure_ascii=False),
        suggestion=result.suggestion,
    )
    session.add(record)
    session.commit()
    return record


def find_by_dispute_id(session: Session, dispute_id: str) -> Optional[DisputeRecord]:
    return session.query(DisputeRecord).filter(DisputeRecord.dispute_id == dispute_id).first()


def save_review(session: Session, req, accepted_party_ratio) -> ReviewRecord:
    record = ReviewRecord(
        dispute_id=req.dispute_id,
        reviewer=req.reviewer,
        accepted=req.accepted,
        final_liable_party=req.final_liable_party,
        final_requester_ratio=req.final_requester_ratio if req.final_requester_ratio is not None else accepted_party_ratio.get("requester"),
        final_runner_ratio=req.final_runner_ratio if req.final_runner_ratio is not None else accepted_party_ratio.get("runner"),
        reason=req.reason,
    )
    session.add(record)
    session.commit()
    return record


def stats(session: Session) -> dict:
    total = session.query(DisputeRecord).count()
    auto = session.query(DisputeRecord).filter(DisputeRecord.route == "auto").count()
    human = total - auto
    review_count = session.query(ReviewRecord).count()
    accepted_count = session.query(ReviewRecord).filter(ReviewRecord.accepted.is_(True)).count()

    auto_rate = round(auto / total, 4) if total else 0.0
    human_rate = round(human / total, 4) if total else 0.0
    adoption_rate = round(accepted_count / review_count, 4) if review_count else 0.0

    return {
        "total": total,
        "auto": auto,
        "human": human,
        "auto_rate": auto_rate,
        "human_rate": human_rate,
        "review_count": review_count,
        "accepted_count": accepted_count,
        "adoption_rate": adoption_rate,
    }

