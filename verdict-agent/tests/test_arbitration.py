from app.arbitration import resolve
from app.models import Evidence


def _ev(ev_type, party, content=""):
    return Evidence(id=f"{party}-{ev_type}", type=ev_type, submitted_by=party, content=content)


def test_single_party_counts():
    r = resolve([(_ev("order_record", "requester"), 1.0)])
    assert r.contributions["requester"] == 1.0
    assert r.contributions["runner"] == 0.0
    assert r.conflicts == 0
    assert r.total == 1.0


def test_conflict_takes_higher_score():
    r = resolve(
        [
            (_ev("order_record", "requester"), 0.9),
            (_ev("order_record", "runner"), 1.1),
        ]
    )
    assert r.contributions["runner"] == 1.1
    assert r.contributions["requester"] == 0.0
    assert r.conflicts == 1
    assert any("冲突" in note for note in r.notes)


def test_multiple_types_aggregate():
    r = resolve(
        [
            (_ev("order_record", "requester"), 1.0),
            (_ev("timestamp", "requester"), 0.7),
            (_ev("chat_screenshot", "runner"), 0.5),
        ]
    )
    assert r.contributions["requester"] == 1.7
    assert r.contributions["runner"] == 0.5
    assert len(r.types_seen) == 3


def test_notes_contain_type_name():
    r = resolve([(_ev("order_record", "requester"), 1.0)])
    assert "订单记录" in r.notes[0]
