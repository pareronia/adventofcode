from __future__ import annotations
from enum import Enum, auto
from typing import NamedTuple


class ResultType(Enum):
    OK = auto()
    MISSING = auto()
    SKIPPED = auto()


class Result(NamedTuple):
    type: ResultType
    answer: str

    @classmethod
    def missing(cls) -> Result:
        return Result(ResultType.MISSING, None)

    @classmethod
    def ok(cls, answer: str) -> Result:
        return Result(ResultType.OK, str(answer))

    @classmethod
    def skipped(cls) -> Result:
        return Result(ResultType.SKIPPED, None)

    @property
    def is_ok(self) -> bool:
        return self.type == ResultType.OK

    @property
    def is_missing(self) -> bool:
        return self.type == ResultType.MISSING

    @property
    def is_skipped(self) -> bool:
        return self.type == ResultType.SKIPPED
