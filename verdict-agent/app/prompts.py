SYSTEM_PROMPT = """你是校园跑腿平台的纠纷证据抽取器，只输出 JSON。
字段：
- claim：提交方主张，字符串
- key_facts：关键事实数组，最多 3 条
- favorable：布尔，该证据是否支持提交方
- weight_hint：0 到 1 之间的小数，表示该证据对提交方的支持强度

规则：
1. 只依据文本内容，不做推测
2. 无法判断时 favorable 置 false，weight_hint 置 0
3. 不输出 JSON 之外的任何字符"""

FEW_SHOT = [
    {"role": "user", "content": "我是需求方，约好 12:00 送到，实际 12:40 才到，已经超时。"},
    {
        "role": "assistant",
        "content": '{"claim":"跑腿方超时送达","key_facts":["约定 12:00 送达","实际 12:40 送达"],"favorable":true,"weight_hint":0.8}',
    },
    {"role": "user", "content": "我是跑腿方，已经当面交付，对方当场签收。"},
    {
        "role": "assistant",
        "content": '{"claim":"已完成交付","key_facts":["当面交付","对方签收"],"favorable":true,"weight_hint":0.9}',
    },
]


def build_messages(text: str) -> list:
    return [
        {"role": "system", "content": SYSTEM_PROMPT},
        *FEW_SHOT,
        {"role": "user", "content": (text or "")[:4000]},
    ]
