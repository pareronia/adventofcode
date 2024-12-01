#! /usr/bin/env python3
#
# Advent of Code 2015 Day 2
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Present = tuple[int, int, int]
Input = list[Present]
Output1 = int
Output2 = int


TEST1 = "2x3x4"
TEST2 = "1x1x10"


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        def parse_present(line: str) -> Present:
            lg, w, h = map(int, line.split("x"))
            return (lg, w, h)

        return [parse_present(line) for line in input_data]

    def part_1(self, presents: Input) -> Output1:
        def calculate_required_area(present: Present) -> int:
            lg, w, h = present
            sides = (2 * lg * w, 2 * w * h, 2 * h * lg)
            return sum(sides) + min(sides) // 2

        return sum(calculate_required_area(present) for present in presents)

    def part_2(self, presents: Input) -> Output2:
        def calculate_required_length(present: Present) -> int:
            lg, w, h = present
            circumferences = (2 * (lg + w), 2 * (w + h), 2 * (h + lg))
            return min(circumferences) + lg * w * h

        return sum(calculate_required_length(present) for present in presents)

    @aoc_samples(
        (
            ("part_1", TEST1, 58),
            ("part_1", TEST2, 43),
            ("part_2", TEST1, 34),
            ("part_2", TEST2, 14),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2015, 2)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
