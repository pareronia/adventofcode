#! /usr/bin/env python3
#
# Advent of Code 2021 Day 7
#

import sys
from typing import Callable

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = list[int]
Output1 = int
Output2 = int


TEST = "16,1,2,0,4,2,7,1,2,14"


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [int(_) for _ in list(input_data)[0].split(",")]

    def solve(
        self, positions: list[int], calc: Callable[[int, int], int]
    ) -> int:
        return min(
            sum(calc(a, b) for b in positions)
            for a in range(min(positions), max(positions) + 1)
        )

    def part_1(self, positions: Input) -> Output1:
        return self.solve(positions, lambda a, b: abs(a - b))

    def part_2(self, positions: Input) -> Output2:
        return self.solve(
            positions, lambda a, b: abs(a - b) * (abs(a - b) + 1) // 2
        )

    @aoc_samples(
        (
            ("part_1", TEST, 37),
            ("part_2", TEST, 168),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2021, 7)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
