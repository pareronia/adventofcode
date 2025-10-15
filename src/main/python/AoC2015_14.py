#! /usr/bin/env python3
#
# Advent of Code 2015 Day 14
#

from __future__ import annotations

import sys
from collections import defaultdict
from dataclasses import dataclass
from typing import Self

from aoc.common import InputData
from aoc.common import SolutionBase

TEST = """\
Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.
"""


@dataclass(frozen=True)
class Reindeer:
    name: str
    speed: int
    go: int
    stop: int

    @classmethod
    def from_input(cls, string: str) -> Self:
        splits = string.split()
        return cls(splits[0], int(splits[3]), int(splits[6]), int(splits[13]))

    def distance_reached(self, time: int) -> int:
        period_distance = self.speed * self.go
        period_time = self.go + self.stop
        periods = time // period_time
        left = time % period_time
        if left >= self.go:
            return periods * period_distance + period_distance
        return periods * period_distance + self.speed * left


Input = list[Reindeer]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [Reindeer.from_input(line) for line in input_data]

    def solve_1(self, reindeer: list[Reindeer], time: int) -> int:
        return max(r.distance_reached(time) for r in reindeer)

    def part_1(self, reindeer: Input) -> Output1:
        return self.solve_1(reindeer, 2503)

    def solve_2(self, reindeer: list[Reindeer], time: int) -> int:
        points = defaultdict[str, int](int)
        for i in range(time):
            distances = {r.name: r.distance_reached(i + 1) for r in reindeer}
            lead = max(distances.values())
            for r, d in distances.items():
                if d == lead:
                    points[r] += 1
        return max(points.values())

    def part_2(self, reindeer: Input) -> Output2:
        return self.solve_2(reindeer, 2503)

    def samples(self) -> None:
        reindeer = self.parse_input(TEST.splitlines())
        assert self.solve_1(reindeer, 1000) == 1120
        assert self.solve_2(reindeer, 1000) == 689


solution = Solution(2015, 14)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
