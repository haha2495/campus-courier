from fastapi.testclient import TestClient

from app.main import app

client = TestClient(app)


def _payload(dispute_id, dispute_type="超时", evidences=None, amount=20.0):
    return {
        "dispute_id": dispute_id,
        "dispute_type": dispute_type,
        "order_amount": amount,
        "evidences": evidences or [],
    }


def _full_evidence(target="requester"):
    return [
        {"id": "e1", "type": "order_record", "submitted_by": target, "content": "约定 12:00 前送达，实际未送达"},
        {"id": "e2", "type": "timestamp", "submitted_by": target, "content": "超出约定时间 40 分钟仍未送达"},
        {"id": "e3", "type": "chat_screenshot", "submitted_by": target, "content": "对方承认迟到"},
        {"id": "e4", "type": "payment_proof", "submitted_by": target, "content": "已支付 20 元"},
        {"id": "e5", "type": "scene_photo", "submitted_by": target, "content": "门口无物品，超时未送达"},
        {"id": "e6", "type": "history_dispute", "submitted_by": target, "content": "此前超时两次"},
    ]


def test_health():
    r = client.get("/health")
    assert r.status_code == 200
    assert r.json()["status"] == "ok"


def test_adjudicate_auto_route():
    r = client.post("/adjudicate", json=_payload("D2001", evidences=_full_evidence()))
    assert r.status_code == 200
    body = r.json()
    assert body["liable_party"] == "runner"
    assert body["route"] == "auto"
    assert body["need_human"] is False
    assert body["confidence"] > 0.85
    assert body["coverage"] == 1.0


def test_adjudicate_human_confirm_when_thin():
    r = client.post(
        "/adjudicate",
        json=_payload(
            "D2002",
            dispute_type="货损",
            evidences=[
                {"id": "e1", "type": "chat_screenshot", "submitted_by": "requester", "content": "东西坏了"},
            ],
        ),
    )
    body = r.json()
    assert body["need_human"] is True
    assert body["route"] in {"human", "human_confirm"}


def test_adjudicate_is_idempotent():
    payload = _payload("D2003", evidences=_full_evidence())
    first = client.post("/adjudicate", json=payload).json()
    second = client.post("/adjudicate", json=payload).json()
    assert first["confidence"] == second["confidence"]
    assert first["ratio"] == second["ratio"]


def test_get_dispute():
    client.post("/adjudicate", json=_payload("D2004", evidences=_full_evidence()))
    r = client.get("/disputes/D2004")
    assert r.status_code == 200
    assert r.json()["dispute_id"] == "D2004"


def test_get_dispute_not_found():
    r = client.get("/disputes/NOT_EXIST")
    assert r.status_code == 404


def test_review_and_stats():
    client.post("/adjudicate", json=_payload("D2005", dispute_type="货损", evidences=[
        {"id": "e1", "type": "chat_screenshot", "submitted_by": "requester", "content": "东西损坏了"},
    ]))

    r = client.post(
        "/review",
        json={
            "dispute_id": "D2005",
            "reviewer": "admin_01",
            "accepted": True,
            "reason": "证据清晰，直接采纳",
        },
    )
    assert r.status_code == 200
    assert r.json()["accepted"] is True

    s = client.get("/stats").json()
    assert s["total"] >= 1
    assert s["review_count"] >= 1
    assert 0 <= s["adoption_rate"] <= 1


def test_review_unknown_dispute():
    r = client.post(
        "/review",
        json={"dispute_id": "NOT_EXIST", "reviewer": "admin_01", "accepted": True},
    )
    assert r.status_code == 404


def test_empty_evidence():
    r = client.post("/adjudicate", json=_payload("D2006", dispute_type="金额争议"))
    body = r.json()
    assert body["liable_party"] == "undetermined"
    assert body["need_human"] is True
    assert body["confidence"] < 0.6
