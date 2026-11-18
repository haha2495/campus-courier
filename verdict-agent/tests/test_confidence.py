from app.confidence import compute, route_of


def test_route_thresholds():
    assert route_of(0.9) == "auto"
    assert route_of(0.86) == "auto"
    assert route_of(0.85) == "human_confirm"
    assert route_of(0.6) == "human_confirm"
    assert route_of(0.59) == "human"


def test_full_coverage_full_gap():
    types = [
        "order_record",
        "payment_proof",
        "chat_screenshot",
        "timestamp",
        "scene_photo",
        "history_dispute",
    ]
    r = compute({"requester": 5.0, "runner": 0.0}, types)
    assert r.value == 0.99
    assert r.coverage == 1.0
    assert r.gap == 1.0
    assert r.route == "auto"
    assert r.need_human is False


def test_no_evidence():
    r = compute({"requester": 0.0, "runner": 0.0}, [])
    assert r.value == 0.35
    assert r.route == "human"
    assert r.need_human is True


def test_partial_coverage():
    r = compute({"requester": 1.0, "runner": 0.0}, ["chat_screenshot"])
    assert r.coverage == round(1 / 6, 4)
    assert r.route in {"human", "human_confirm"}
