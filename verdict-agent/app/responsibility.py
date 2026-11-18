from dataclasses import dataclass
from typing import Dict

from .config import settings


@dataclass
class ResponsibilityResult:
    liable_party: str
    ratio: Dict[str, float]
    suggestion: str


def _liable(contributions: Dict[str, float], total: float) -> str:
    if total <= 0:
        return "undetermined"
    if contributions["requester"] > contributions["runner"]:
        return "runner"
    if contributions["runner"] > contributions["requester"]:
        return "requester"
    return "shared"


def compute(contributions: Dict[str, float], total: float, amount: float) -> ResponsibilityResult:
    liable = _liable(contributions, total)

    if total <= 0:
        ratio = {"requester": 0.5, "runner": 0.5}
        return ResponsibilityResult(
            liable_party="undetermined",
            ratio=ratio,
            suggestion="证据不足，建议补充订单记录、支付凭证或聊天记录后重新判责",
        )

    support = {
        "requester": round(contributions["requester"] / total, 3),
        "runner": round(contributions["runner"] / total, 3),
    }
    shrink = min(1.0, total / settings.shrink_total)

    if liable == "runner":
        runner_ratio = 0.5 + (support["requester"] - 0.5) * shrink
        ratio = {"runner": round(runner_ratio, 3), "requester": round(1 - runner_ratio, 3)}
        suggestion = (
            f"建议跑腿方承担 {ratio['runner'] * 100:.1f}% 责任，"
            f"按订单金额 {amount:.2f} 元赔付 {amount * ratio['runner']:.2f} 元"
        )
    elif liable == "requester":
        requester_ratio = 0.5 + (support["runner"] - 0.5) * shrink
        ratio = {"requester": round(requester_ratio, 3), "runner": round(1 - requester_ratio, 3)}
        suggestion = (
            f"建议需求方承担 {ratio['requester'] * 100:.1f}% 责任，"
            f"赔付 {amount * ratio['requester']:.2f} 元"
        )
    else:
        ratio = {"requester": 0.5, "runner": 0.5}
        suggestion = "双方责任相当，建议各承担 50%，或由平台协商处理"

    return ResponsibilityResult(liable_party=liable, ratio=ratio, suggestion=suggestion)
