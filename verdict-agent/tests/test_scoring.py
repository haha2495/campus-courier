from app import store
from app.extractor import enrich
from app.models import Evidence
from app.scoring import relevance, score_evidence, type_weight


def _ev(ev_type="order_record", party="requester", content="未送达"):
    return Evidence(id="e1", type=ev_type, submitted_by=party, content=content)


def test_type_weight_known():
    assert type_weight("order_record") == 1.0
    assert type_weight("payment_proof") == 0.9


def test_type_weight_unknown():
    assert type_weight("not_exist") == 0.3


def test_relevance_known():
    assert relevance("order_record", "超时") == 0.8
    assert relevance("scene_photo", "货损") == 0.9


def test_relevance_unknown():
    assert relevance("order_record", "不存在") == 0.3


def test_score_with_support_keyword():
    ev = _ev(party="runner", content="已送达，已签收")
    facts = enrich(ev, "未送达", use_llm=False)
    score = score_evidence(ev, "未送达", facts)
    assert score > 1.0


def test_score_without_keyword():
    ev = _ev(content="随便写点东西")
    facts = enrich(ev, "未送达", use_llm=False)
    score = score_evidence(ev, "未送达", facts)
    assert score == 0.9


def test_score_with_timestamps():
    ev = _ev(ev_type="timestamp", content="12:00 接单，12:40 送达")
    facts = enrich(ev, "超时", use_llm=False)
    score = score_evidence(ev, "超时", facts)
    assert score > 0.5
