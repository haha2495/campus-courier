from typing import Dict, Optional

from .keywords import extract_structured, match_condition, match_support
from .llm import extract_facts
from .models import Evidence


def enrich(ev: Evidence, dispute_type: str, use_llm: bool = True) -> Dict[str, object]:
    text = ev.content or ""
    facts: Dict[str, object] = {
        "structured": extract_structured(text),
        "support_keywords": match_support(text, ev.submitted_by),
        "condition_keywords": match_condition(text, dispute_type),
        "llm": None,
    }
    if use_llm:
        result: Optional[dict] = extract_facts(text)
        if result:
            facts["llm"] = result
    return facts


def support_strength(facts: Dict[str, object]) -> float:
    support = facts.get("support_keywords") or []
    condition = facts.get("condition_keywords") or []
    return min(1.0, (len(support) + len(condition)) / 4.0)
