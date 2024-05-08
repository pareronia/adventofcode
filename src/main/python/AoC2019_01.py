#! /usr/bin/env python3
#
# Advent of Code 2019 Day 1
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST1 = "12"
TEST2 = "14"
TEST3 = "1969"
TEST4 = "100756"

Input = list[int]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [int(_) for _ in input_data]

    def fuel_for_mass(self, m: int) -> int:
        return m // 3 - 2

    def part_1(self, inputs: Input) -> int:
        return sum(self.fuel_for_mass(m) for m in inputs)

    def rocket_equation(self, m: int) -> int:
        total_fuel = 0
        while (fuel := self.fuel_for_mass(m)) > 0:
            total_fuel += fuel
            m = fuel
        return total_fuel

    def part_2(self, inputs: Input) -> int:
        return sum(self.rocket_equation(m) for m in inputs)

    @aoc_samples(
        (
            ("part_1", TEST1, 2),
            ("part_1", TEST2, 2),
            ("part_1", TEST3, 654),
            ("part_1", TEST4, 33583),
            ("part_2", TEST1, 2),
            ("part_2", TEST3, 966),
            ("part_2", TEST4, 50346),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2019, 1)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
