import os
from dataclasses import dataclass


@dataclass(frozen=True)
class Settings:
    auto_threshold: float = 0.85
    confirm_threshold: float = 0.60
    keyword_bonus: float = 0.15
    shrink_total: float = 2.0
    base_confidence: float = 0.35
    coverage_weight: float = 0.25
    gap_weight: float = 0.40
    max_confidence: float = 0.99
    evidence_type_count: int = 6
    db_url: str = os.getenv("VERDICT_DB_URL", "sqlite:///./verdict.db")
    redis_url: str = os.getenv("REDIS_URL", "")
    idempotent_ttl: int = int(os.getenv("VERDICT_IDEMPOTENT_TTL", "3600"))
    deepseek_key: str = os.getenv("DEEPSEEK_API_KEY", "")
    deepseek_url: str = os.getenv("DEEPSEEK_URL", "https://api.deepseek.com/chat/completions")
    deepseek_model: str = os.getenv("DEEPSEEK_MODEL", "deepseek-chat")
    llm_timeout: float = float(os.getenv("DEEPSEEK_TIMEOUT", "20"))
    log_level: str = os.getenv("VERDICT_LOG_LEVEL", "INFO")


settings = Settings()
