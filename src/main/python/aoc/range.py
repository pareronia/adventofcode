from collections.abc import Iterable
from collections.abc import Iterator
from typing import NamedTuple
from typing import Self


class Range:
    @classmethod
    def left_join(
        cls, r1: tuple[int, int], r2: tuple[int, int]
    ) -> tuple[tuple[int, int] | None, set[tuple[int, int]]]:
        overlap = None
        others = set()
        o0 = max(r1[0], r2[0])
        o1 = min(r1[1], r2[1])
        if o0 < o1:
            overlap = (o0, o1)
            if o0 > r1[0]:
                others.add((r1[0], o0))
            if o1 < r1[1]:
                others.add((o1, r1[1]))
        else:
            others.add(r1)
        return overlap, others


class RangeInclusive(NamedTuple):
    minimum: int
    maximum: int

    @classmethod
    def between(cls, minimum: int, maximum: int) -> Self:
        return cls(minimum, maximum)

    @classmethod
    def merge(cls, ranges: Iterable[Self]) -> list[Self]:
        merged = list[Self]()
        for rng in sorted(ranges):
            if len(merged) == 0:
                merged.append(rng)
                continue
            last = merged[-1]
            if last.is_overlapped_by(rng):
                merged[-1] = cls.between(
                    last.minimum, max(last.maximum, rng.maximum)
                )
            else:
                merged.append(rng)
        return merged

    @property
    def len(self) -> int:
        return self.maximum - self.minimum + 1

    def contains(self, element: int) -> bool:
        return self.minimum <= element <= self.maximum

    def is_overlapped_by(self, other: Self) -> bool:
        return (
            other.contains(self.minimum)
            or other.contains(self.maximum)
            or self.contains(other.minimum)
        )

    def iterator(self) -> Iterator[int]:
        return (_ for _ in range(self.minimum, self.maximum + 1))
