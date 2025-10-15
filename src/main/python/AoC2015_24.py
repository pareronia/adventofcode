#! /usr/bin/env python3
#
# Advent of Code 2015 Day 24
#

import itertools
import math
import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST = """\
1
2
3
4
5
7
8
9
10
11
"""

Input = list[int]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [int(line) for line in input_data]

    def solve(self, weights: list[int], groups: int) -> int:
        target = sum(weights) // groups
        for i in range(len(weights)):
            cands = {
                tuple(sorted(c))
                for c in itertools.combinations(weights, i + 1)
                if sum(c) == target
            }
            if len(cands) > 0:
                return min(math.prod(cand) for cand in cands)
        msg = "Unsolvable"
        raise RuntimeError(msg)

    def part_1(self, weights: Input) -> Output1:
        return self.solve(weights, 3)

    def part_2(self, weights: Input) -> Output2:
        return self.solve(weights, 4)

    @aoc_samples(
        (
            ("part_1", TEST, 99),
            ("part_2", TEST, 44),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2015, 24)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
