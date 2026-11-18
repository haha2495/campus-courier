import json
from typing import List, Optional

import httpx

from .config import settings
from .prompts import build_messages


def available() -> bool:
    return bool(settings.deepseek_key)


def extract_facts(text: str, timeout: Optional[float] = None) -> Optional[dict]:
    if not available():
        return None
    payload = {
        "model": settings.deepseek_model,
        "messages": build_messages(text),
        "temperature": 0,
        "response_format": {"type": "json_object"},
    }
    try:
        resp = httpx.post(
            settings.deepseek_url,
            headers={
                "Authorization": f"Bearer {settings.deepseek_key}",
                "Content-Type": "application/json",
            },
            json=payload,
            timeout=timeout or settings.llm_timeout,
        )
        resp.raise_for_status()
        body = resp.json()
        content = body["choices"][0]["message"]["content"]
        return json.loads(content)
    except Exception:
        return None


def extract_batch(texts: List[str]) -> List[Optional[dict]]:
    return [extract_facts(text) for text in texts]
