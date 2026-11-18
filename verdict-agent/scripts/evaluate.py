"""回放标注集，输出一致率、自动处理率、转人工率、采纳率。"""

import json
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))

from app.adjudicator import adjudicate
from app.models import AdjudicateRequest, Evidence

CASES = Path(__file__).resolve().parents[1] / "data" / "cases.json"


def evaluate(path: Path = CASES) -> dict:
    if not path.exists():
        raise FileNotFoundError(f"标注集不存在：{path}，先运行 scripts/seed_cases.py")

    cases = json.loads(path.read_text(encoding="utf-8"))
    total = len(cases)
    agree = 0
    auto = 0
    human = 0
    accepted = 0
    human_cases = 0
    errors = 0

    for case in cases:
        req = AdjudicateRequest(
            dispute_id=case["dispute_id"],
            dispute_type=case["dispute_type"],
            order_amount=case["order_amount"],
            evidences=[Evidence(**ev) for ev in case["evidences"]],
        )
        result = adjudicate(req, use_llm=False)

        if result.liable_party == case["gold_liable"]:
            agree += 1
        if result.route == "auto":
            auto += 1
        else:
            human += 1
            human_cases += 1
            if case.get("reviewer_accepted"):
                accepted += 1
        if result.liable_party == "undetermined":
            errors += 1

    metrics = {
        "cases": total,
        "agreement_rate": round(agree / total, 4) if total else 0.0,
        "auto_rate": round(auto / total, 4) if total else 0.0,
        "human_rate": round(human / total, 4) if total else 0.0,
        "adoption_rate": round(accepted / human_cases, 4) if human_cases else 0.0,
        "undetermined": errors,
    }
    return metrics


def main() -> None:
    metrics = evaluate()
    print(json.dumps(metrics, ensure_ascii=False, indent=2))


if __name__ == "__main__":
    main()
