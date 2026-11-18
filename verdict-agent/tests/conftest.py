import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))

import pytest

from app import store


@pytest.fixture(autouse=True)
def temp_db(tmp_path):
    store.init_db(f"sqlite:///{tmp_path / 'test.db'}")
    yield
