from dataclasses import dataclass
from typing import Dict, List

from .config import settings


@dataclass
class ConfidenceResult:
    value: float
    coverage: float
    gap: float
    route: str
    need_human: bool


def route_of(value: float) -> str:
    if value > settings.auto_threshold:
        return "auto"
    if value >= settings.confirm_threshold:
        return "human_confirm"
    return "human"


def compute(
    contributions: Dict[str, float],
    types_seen: List[str],
) -> ConfidenceResult:
    requester = contributions.get("requester", 0.0)
    runner = contributions.get("runner", 0.0)
    total = requester + runner

    coverage = min(len(types_seen) / settings.evidence_type_count, 1.0)
    gap = abs(requester - runner) / total if total > 0 else 0.0

    value = settings.base_confidence + settings.coverage_weight * coverage + settings.gap_weight * gap
    value = round(min(value, settings.max_confidence), 3)

    route = route_of(value)
    return ConfidenceResult(
        value=value,
        coverage=round(coverage, 4),
        gap=round(gap, 4),
        route=route,
        need_human=route != "auto",
    )
