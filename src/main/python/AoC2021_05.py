#! /usr/bin/env python3
#
# Advent of Code 2021 Day 5
#

from __future__ import annotations

import re
import sys
from collections import Counter
from typing import Iterator
from typing import NamedTuple

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST = """\
0,9 -> 5,9
8,0 -> 0,8
9,4 -> 3,4
2,2 -> 2,1
7,0 -> 7,4
6,4 -> 2,0
0,9 -> 2,9
3,4 -> 1,4
0,0 -> 8,8
5,5 -> 8,2
"""


class LineSegment(NamedTuple):
    x1: int
    y1: int
    x2: int
    y2: int
    mx: int
    my: int

    @classmethod
    def from_input(cls, s: str) -> LineSegment:
        x1, y1, x2, y2 = map(int, re.findall(r"\d+", s))
        mx = 0 if x1 == x2 else 1 if x1 < x2 else -1
        my = 0 if y1 == y2 else 1 if y1 < y2 else -1
        return LineSegment(x1, y1, x2, y2, mx, my)

    def positions(self) -> Iterator[tuple[int, int]]:
        length = max(abs(self.x1 - self.x2), abs(self.y1 - self.y2))
        for i in range(0, length + 1):
            yield (self.x1 + self.mx * i, self.y1 + self.my * i)


Input = list[LineSegment]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [LineSegment.from_input(s) for s in input_data]

    def count_intersections(self, lines: Input, diagonals: bool) -> int:
        counter = Counter(
            p
            for line in lines
            for p in line.positions()
            if diagonals or line.mx == 0 or line.my == 0
        )
        return sum(v > 1 for v in counter.values())

    def part_1(self, lines: Input) -> Output1:
        return self.count_intersections(lines, diagonals=False)

    def part_2(self, lines: Input) -> Output2:
        return self.count_intersections(lines, diagonals=True)

    @aoc_samples(
        (
            ("part_1", TEST, 5),
            ("part_2", TEST, 12),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2021, 5)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
