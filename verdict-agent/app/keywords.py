import re
from typing import Dict, List

TIME_PATTERN = re.compile(r"\b(\d{1,2}[:：]\d{2})\b")
AMOUNT_PATTERN = re.compile(r"(\d+(?:\.\d+)?)\s*元")
ORDER_PATTERN = re.compile(r"(\d{5,})")

SUPPORT_KEYWORDS: Dict[str, List[str]] = {
    "runner": [
        "已送达", "已签收", "按时", "已拍照", "当面交付",
        "已通知", "已联系", "无法联系", "无人接听", "已放置",
        "已取件", "已转交",
    ],
    "requester": [
        "未送达", "没收到", "未收到", "损坏", "破损", "丢失",
        "超时", "迟到", "数目不符", "金额不符", "拒收",
        "没有送到", "少件", "错件",
    ],
}

CONDITION_KEYWORDS: Dict[str, List[str]] = {
    "未送达": ["未送达", "没收到", "未收到", "没有送到", "已送达", "已签收", "已放置"],
    "货损": ["损坏", "破损", "压坏", "洒出", "变形", "已拍照"],
    "超时": ["超时", "迟到", "超出", "按时", "延迟"],
    "金额争议": ["金额不符", "数目不符", "少付", "多付", "已支付", "已收款"],
}


def match_support(text: str, party: str) -> List[str]:
    text = text or ""
    return [kw for kw in SUPPORT_KEYWORDS.get(party, []) if kw in text]


def match_condition(text: str, dispute_type: str) -> List[str]:
    text = text or ""
    return [kw for kw in CONDITION_KEYWORDS.get(dispute_type, []) if kw in text]


def extract_structured(text: str) -> Dict[str, object]:
    text = text or ""
    times = TIME_PATTERN.findall(text)
    amounts = AMOUNT_PATTERN.findall(text)
    order_ids = ORDER_PATTERN.findall(text)
    return {
        "times": times,
        "amounts": amounts,
        "order_ids": order_ids,
    }
