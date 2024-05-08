#! /usr/bin/env python3
#
# Advent of Code 2020 Day 2
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST = """\
1-3 a: abcde
2-9 c: ccccccccc
1-3 b: cdefg
"""


Input = list[tuple[int, int, str, str]]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        def parse(line: str) -> tuple[int, int, str, str]:
            splits = line.split(": ")
            left_and_right = splits[0].split(" ")
            first, second = left_and_right[0].split("-")
            wanted = left_and_right[1][0]
            password = splits[1]
            return (int(first), int(second), wanted, password)

        return [parse(line) for line in input_data]

    def part_1(self, inputs: Input) -> int:
        def check_valid(
            first: int, second: int, wanted: str, passw: str
        ) -> bool:
            return first <= passw.count(wanted) <= second

        return sum(check_valid(*line) for line in inputs)

    def part_2(self, inputs: Input) -> int:
        def check_valid(
            first: int, second: int, wanted: str, passw: str
        ) -> bool:
            return (passw[first - 1] == wanted) ^ (passw[second - 1] == wanted)

        return sum(check_valid(*line) for line in inputs)

    @aoc_samples(
        (
            ("part_1", TEST, 2),
            ("part_2", TEST, 1),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2020, 2)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
