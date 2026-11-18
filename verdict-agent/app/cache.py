import time
from typing import Optional

from .config import settings

_memory: dict = {}


class Cache:
    def __init__(self) -> None:
        self._redis = None
        if settings.redis_url:
            try:
                import redis

                client = redis.from_url(settings.redis_url, decode_responses=True)
                client.ping()
                self._redis = client
            except Exception:
                self._redis = None

    @property
    def backend(self) -> str:
        return "redis" if self._redis else "memory"

    def get(self, key: str) -> Optional[str]:
        if self._redis:
            try:
                return self._redis.get(key)
            except Exception:
                return None
        item = _memory.get(key)
        if not item:
            return None
        value, expire_at = item
        if expire_at and time.time() > expire_at:
            _memory.pop(key, None)
            return None
        return value

    def set(self, key: str, value: str, ttl: Optional[int] = None) -> None:
        ttl = ttl if ttl is not None else settings.idempotent_ttl
        if self._redis:
            try:
                self._redis.set(key, value, ex=ttl)
                return
            except Exception:
                pass
        _memory[key] = (value, time.time() + ttl)

    def setnx(self, key: str, value: str, ttl: Optional[int] = None) -> bool:
        ttl = ttl if ttl is not None else settings.idempotent_ttl
        if self._redis:
            try:
                return bool(self._redis.set(key, value, nx=True, ex=ttl))
            except Exception:
                pass
        if key in _memory and (not _memory[key][1] or _memory[key][1] > time.time()):
            return False
        _memory[key] = (value, time.time() + ttl)
        return True

    def delete(self, key: str) -> None:
        if self._redis:
            try:
                self._redis.delete(key)
                return
            except Exception:
                pass
        _memory.pop(key, None)

    def clear(self) -> None:
        if self._redis:
            try:
                self._redis.flushdb()
                return
            except Exception:
                pass
        _memory.clear()


cache = Cache()
