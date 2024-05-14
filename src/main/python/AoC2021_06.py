#! /usr/bin/env python3
#
# Advent of Code 2021 Day 6
#

import sys
from collections import deque

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST = "3,4,3,1,2"


Input = list[int]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [int(_) for _ in list(input_data)[0].split(",")]

    def solve(self, inputs: list[int], days: int) -> int:
        fishies = deque([0] * 9)
        for n in inputs:
            fishies[n] += 1
        for i in range(days):
            zeroes = fishies[0]
            fishies.rotate(-1)
            fishies[6] += zeroes
        return sum(fishies)

    def part_1(self, inputs: Input) -> Output1:
        return self.solve(inputs, days=80)

    def part_2(self, inputs: Input) -> Output2:
        return self.solve(inputs, days=256)

    @aoc_samples(
        (
            ("part_1", TEST, 5_934),
            ("part_2", TEST, 26_984_457_539),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2021, 6)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
