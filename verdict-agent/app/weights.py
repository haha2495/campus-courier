from typing import Dict, List

TYPE_CN: Dict[str, str] = {
    "order_record": "订单记录",
    "payment_proof": "支付凭证",
    "chat_screenshot": "聊天截图",
    "timestamp": "时间戳",
    "scene_photo": "现场照片",
    "history_dispute": "历史纠纷",
}

EVIDENCE_WEIGHTS: Dict[str, float] = {
    "order_record": 1.0,
    "payment_proof": 0.9,
    "chat_screenshot": 0.7,
    "timestamp": 0.65,
    "scene_photo": 0.6,
    "history_dispute": 0.4,
}

RELEVANCE: Dict[str, Dict[str, float]] = {
    "未送达": {
        "order_record": 0.9,
        "timestamp": 0.8,
        "chat_screenshot": 0.7,
        "scene_photo": 0.5,
        "payment_proof": 0.3,
        "history_dispute": 0.3,
    },
    "货损": {
        "scene_photo": 0.9,
        "chat_screenshot": 0.7,
        "order_record": 0.6,
        "payment_proof": 0.5,
        "timestamp": 0.5,
        "history_dispute": 0.3,
    },
    "超时": {
        "timestamp": 0.9,
        "order_record": 0.8,
        "chat_screenshot": 0.6,
        "payment_proof": 0.3,
        "scene_photo": 0.3,
        "history_dispute": 0.3,
    },
    "金额争议": {
        "payment_proof": 0.9,
        "chat_screenshot": 0.7,
        "order_record": 0.6,
        "timestamp": 0.4,
        "scene_photo": 0.3,
        "history_dispute": 0.4,
    },
}

FAVOR_KEYWORDS: Dict[str, List[str]] = {
    "runner": [
        "已送达", "已签收", "按时", "已拍照", "当面交付",
        "已通知", "已联系", "无法联系", "无人接听", "已放置",
    ],
    "requester": [
        "未送达", "没收到", "未收到", "损坏", "破损", "丢失",
        "超时", "迟到", "数目不符", "金额不符", "拒收",
    ],
}

PARTY_CN: Dict[str, str] = {
    "requester": "需求方",
    "runner": "跑腿方",
}
