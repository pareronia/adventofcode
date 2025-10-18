#! /usr/bin/env python3
#
# Advent of Code 2022 Day 4
#


from __future__ import annotations

import re
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from collections.abc import Callable

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST = """\
2-4,6-8
2-3,4-5
5-7,7-9
2-8,3-7
6-6,4-6
2-6,4-8
"""


Input = tuple[str, ...]
Output1 = int
Output2 = int
RE = re.compile(r"[0-9]+")


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return tuple(input_data)

    def solve(
        self, inputs: tuple[str, ...], f: Callable[[set[int], set[int]], bool]
    ) -> int:
        return sum(
            f(
                set(range(n1, n2 + 1)),
                set(range(n3, n4 + 1)),
            )
            for n1, n2, n3, n4 in (
                (int(n) for n in RE.findall(line)) for line in inputs
            )
        )

    def part_1(self, inputs: Input) -> Output1:
        return self.solve(
            inputs, lambda s1, s2: s1.issubset(s2) or s2.issubset(s1)
        )

    def part_2(self, inputs: Input) -> Output2:
        return self.solve(inputs, lambda s1, s2: not s1.isdisjoint(s2))

    @aoc_samples(
        (
            ("part_1", TEST, 2),
            ("part_2", TEST, 4),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2022, 4)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
