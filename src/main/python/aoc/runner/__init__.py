from __future__ import annotations
from enum import Enum, auto
from typing import NamedTuple
from typing import Optional


class ResultType(Enum):
    OK = auto()
    MISSING = auto()
    SKIPPED = auto()


class Result(NamedTuple):
    type: ResultType
    answer: str
    duration: int

    @classmethod
    def missing(cls) -> Result:
        return Result(ResultType.MISSING, None, None)

    @classmethod
    def ok(cls, answer: str, duration: Optional[int] = None) -> Result:
        return Result(ResultType.OK, str(answer), duration)

    @classmethod
    def skipped(cls) -> Result:
        return Result(ResultType.SKIPPED, None, None)

    @property
    def is_ok(self) -> bool:
        return self.type == ResultType.OK

    @property
    def is_missing(self) -> bool:
        return self.type == ResultType.MISSING

    @property
    def is_skipped(self) -> bool:
        return self.type == ResultType.SKIPPED
