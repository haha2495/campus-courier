from app.responsibility import compute


def test_no_evidence():
    r = compute({"requester": 0.0, "runner": 0.0}, 0.0, 20.0)
    assert r.liable_party == "undetermined"
    assert r.ratio == {"requester": 0.5, "runner": 0.5}


def test_runner_liable_smoothed_when_weak():
    r = compute({"requester": 1.0, "runner": 0.0}, 1.0, 20.0)
    assert r.liable_party == "runner"
    assert 0.5 < r.ratio["runner"] < 1.0


def test_runner_liable_when_strong():
    r = compute({"requester": 6.0, "runner": 0.5}, 6.5, 20.0)
    assert r.liable_party == "runner"
    assert r.ratio["runner"] > 0.9
    assert "赔付" in r.suggestion


def test_requester_liable():
    r = compute({"requester": 0.2, "runner": 3.0}, 3.2, 20.0)
    assert r.liable_party == "requester"
    assert r.ratio["requester"] > 0.5


def test_shared_when_equal():
    r = compute({"requester": 1.0, "runner": 1.0}, 2.0, 20.0)
    assert r.liable_party == "shared"
    assert r.ratio["requester"] == 0.5
    assert r.ratio["runner"] == 0.5
