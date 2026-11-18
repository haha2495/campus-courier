from typing import Dict

from .config import settings
from .models import Evidence
from .weights import EVIDENCE_WEIGHTS, RELEVANCE


def type_weight(ev_type: str) -> float:
    return EVIDENCE_WEIGHTS.get(ev_type, 0.3)


def relevance(ev_type: str, dispute_type: str) -> float:
    return RELEVANCE.get(dispute_type, {}).get(ev_type, 0.3)


def score_evidence(ev: Evidence, dispute_type: str, facts: Dict[str, object]) -> float:
    base = type_weight(ev.type) * relevance(ev.type, dispute_type)

    structured = facts.get("structured") or {}
    support = facts.get("support_keywords") or []
    condition = facts.get("condition_keywords") or []
    llm = facts.get("llm") or {}

    bonus = 0.0
    if support:
        bonus += settings.keyword_bonus
    if condition:
        bonus += 0.05
    times = structured.get("times") or []
    bonus += 0.05 * min(len(times), 2)
    if isinstance(llm, dict) and llm.get("favorable") is True:
        bonus += settings.keyword_bonus

    return round(min(base + bonus, 1.5), 4)
