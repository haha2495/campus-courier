from dataclasses import dataclass, field
from typing import Dict, List, Tuple

from .models import Evidence
from .weights import EVIDENCE_WEIGHTS, PARTY_CN, TYPE_CN


@dataclass
class ArbitrationResult:
    contributions: Dict[str, float] = field(
        default_factory=lambda: {"requester": 0.0, "runner": 0.0}
    )
    types_seen: List[str] = field(default_factory=list)
    notes: List[str] = field(default_factory=list)
    conflicts: int = 0

    @property
    def total(self) -> float:
        return self.contributions["requester"] + self.contributions["runner"]


def resolve(items: List[Tuple[Evidence, float]]) -> ArbitrationResult:
    result = ArbitrationResult()
    by_type: Dict[str, List[Tuple[Evidence, float]]] = {}
    for ev, score in items:
        by_type.setdefault(ev.type, []).append((ev, score))

    for ev_type, group in by_type.items():
        result.types_seen.append(ev_type)
        winner_ev, winner_score = max(group, key=lambda pair: pair[1])
        party = winner_ev.submitted_by
        result.contributions[party] += winner_score
        result.notes.append(
            f"{TYPE_CN.get(ev_type, ev_type)}（{PARTY_CN[party]}提交，"
            f"类型权重 {EVIDENCE_WEIGHTS.get(ev_type, 0.3)}，得分 {winner_score}）"
            f"支持{PARTY_CN[party]}主张"
        )

        parties = {item_ev.submitted_by for item_ev, _ in group}
        if len(parties) > 1:
            result.conflicts += 1
            loser = "runner" if party == "requester" else "requester"
            result.notes.append(
                f"{TYPE_CN.get(ev_type, ev_type)}存在双方冲突证据，按权重取"
                f"{PARTY_CN[party]}，{PARTY_CN[loser]}该条不计分"
            )

    return result
