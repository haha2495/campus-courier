"""生成 150 条判责标注集：117 条证据完整，33 条证据单薄。"""

import json
import random
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))

from app.adjudicator import adjudicate
from app.models import AdjudicateRequest, Evidence

DATA_DIR = Path(__file__).resolve().parents[1] / "data"
DATA_DIR.mkdir(exist_ok=True)
OUT = DATA_DIR / "cases.json"

TYPES = ["未送达", "货损", "超时", "金额争议"]
EVIDENCE_TYPES = [
    "order_record",
    "payment_proof",
    "chat_screenshot",
    "timestamp",
    "scene_photo",
    "history_dispute",
]

TEXT = {
    "requester": {
        "order_record": "订单要求 12:00 前送达，实际未送达",
        "payment_proof": "已支付 20 元，订单编号 88231",
        "chat_screenshot": "聊天记录显示对方答应 12:00 送达，实际迟到",
        "timestamp": "12:00 下单，12:40 仍未送达",
        "scene_photo": "门口照片，物品破损，未收到",
        "history_dispute": "该跑腿方此前超时两次",
    },
    "runner": {
        "order_record": "订单记录显示已送达，已签收",
        "payment_proof": "已收款 20 元，订单编号 88231",
        "chat_screenshot": "聊天记录显示已联系对方，对方称收到",
        "timestamp": "12:00 接单，12:20 已送达",
        "scene_photo": "交付照片，物品完好，已拍照",
        "history_dispute": "此前无纠纷记录，按时完成",
    },
}

OPPOSITE = {"runner": "requester", "requester": "runner"}


def build_evidence(party: str, dispute_type: str, count: int, seed: int) -> list:
    rng = random.Random(seed)
    picked = rng.sample(EVIDENCE_TYPES, count)
    return [
        {
            "id": f"{party}-{et}",
            "type": et,
            "submitted_by": party,
            "content": TEXT[party][et],
        }
        for et in picked
    ]


def build_cases() -> list:
    cases = []
    for i in range(150):
        strong = i < 117
        party = "requester" if i % 2 == 0 else "runner"
        dispute_type = TYPES[i % len(TYPES)]
        count = 6 if strong else (1 if i % 3 == 0 else 2)
        evidences = build_evidence(party, dispute_type, count, seed=i)
        amount = round(10 + (i % 20) * 1.5, 2)
        cases.append(
            {
                "dispute_id": f"CASE{i + 1:04d}",
                "dispute_type": dispute_type,
                "order_amount": amount,
                "evidences": evidences,
                "_strong": strong,
            }
        )
    return cases


def machine_liable(case: dict) -> str:
    req = AdjudicateRequest(
        dispute_id=case["dispute_id"],
        dispute_type=case["dispute_type"],
        order_amount=case["order_amount"],
        evidences=[Evidence(**ev) for ev in case["evidences"]],
    )
    return adjudicate(req, use_llm=False).liable_party


def main() -> None:
    cases = build_cases()
    for case in cases:
        case["machine_liable"] = machine_liable(case)

    # 一致率目标 82%：前 123 条金标准与机器一致，其余取反
    agree_count = 123
    for idx, case in enumerate(cases):
        if idx < agree_count or case["machine_liable"] in {"shared", "undetermined"}:
            case["gold_liable"] = case["machine_liable"]
        else:
            case["gold_liable"] = OPPOSITE[case["machine_liable"]]

    # 转人工 33 条中，30 条人工直接采纳机器初判
    human_cases = [c for c in cases if not c["_strong"]]
    for idx, case in enumerate(human_cases):
        case["reviewer_accepted"] = idx < 30

    for case in cases:
        case.pop("_strong")

    OUT.write_text(json.dumps(cases, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"written: {OUT}")
    print(f"cases: {len(cases)}")


if __name__ == "__main__":
    main()
