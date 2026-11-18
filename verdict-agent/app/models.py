from typing import Dict, List, Literal, Optional

from pydantic import BaseModel, Field

EvidenceType = Literal[
    "order_record",
    "payment_proof",
    "chat_screenshot",
    "timestamp",
    "scene_photo",
    "history_dispute",
]
Party = Literal["requester", "runner"]
DisputeType = Literal["未送达", "货损", "超时", "金额争议"]
LiableParty = Literal["runner", "requester", "shared", "undetermined"]
Route = Literal["auto", "human_confirm", "human"]


class Evidence(BaseModel):
    id: str
    type: EvidenceType
    submitted_by: Party
    content: str = ""
    meta: Dict[str, str] = Field(default_factory=dict)


class AdjudicateRequest(BaseModel):
    dispute_id: str = Field(..., min_length=1, max_length=64)
    dispute_type: DisputeType
    order_amount: float = Field(default=0.0, ge=0)
    evidences: List[Evidence] = Field(default_factory=list)


class AdjudicateResponse(BaseModel):
    dispute_id: str
    liable_party: LiableParty
    ratio: Dict[str, float]
    confidence: float
    need_human: bool
    route: Route
    coverage: float
    gap: float
    conflicts: int
    basis: List[str]
    suggestion: str


class ReviewRequest(BaseModel):
    dispute_id: str = Field(..., min_length=1, max_length=64)
    reviewer: str = Field(..., min_length=1, max_length=64)
    accepted: bool
    final_liable_party: Optional[LiableParty] = None
    final_requester_ratio: Optional[float] = Field(default=None, ge=0, le=1)
    final_runner_ratio: Optional[float] = Field(default=None, ge=0, le=1)
    reason: str = ""


class ReviewResponse(BaseModel):
    dispute_id: str
    accepted: bool
    reviewer: str
    reviewed_at: str


class StatsResponse(BaseModel):
    total: int
    auto: int
    human: int
    auto_rate: float
    human_rate: float
    review_count: int
    accepted_count: int
    adoption_rate: float
