#! /usr/bin/env python3
#
# Advent of Code 2016 Day 15
#

from __future__ import annotations

import itertools
import sys
from typing import NamedTuple

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST1 = """\
Disc #1 has 5 positions; at time=0, it is at position 4.
Disc #2 has 2 positions; at time=0, it is at position 1.
"""
TEST2 = """\
Disc #1 has 5 positions; at time=0, it is at position 3.
Disc #2 has 2 positions; at time=0, it is at position 1.
Disc #3 has 3 positions; at time=0, it is at position 2.
"""


class Disc(NamedTuple):
    period: int
    offset: int
    delay: int

    @classmethod
    def from_input(cls, line: str) -> Disc:
        sp = line.split()
        delay, period, position = map(int, (sp[1][1:], sp[3], sp[11][:-1]))
        return cls(period, (period - position) % period, delay)

    def aligned_at(self, time: int) -> bool:
        return (time + self.delay) % self.period == self.offset


Input = list[Disc]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [Disc.from_input(line) for line in input_data]

    def solve(self, discs: list[Disc]) -> int:
        return next(
            i for i in itertools.count() if all(d.aligned_at(i) for d in discs)
        )

    def part_1(self, discs: Input) -> Output1:
        return self.solve(discs)

    def part_2(self, discs: Input) -> Output2:
        discs.append(Disc(11, 0, len(discs) + 1))
        return self.solve(discs)

    @aoc_samples(
        (
            ("part_1", TEST1, 5),
            ("part_1", TEST2, 1),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2016, 15)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
