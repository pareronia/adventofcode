#! /usr/bin/env python3
#
# Advent of Code 2015 Day 1
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = str
Output1 = int
Output2 = int

TEST1 = "(())"
TEST2 = "()()"
TEST3 = "((("
TEST4 = "(()(()("
TEST5 = "))((((("
TEST6 = "())"
TEST7 = "))("
TEST8 = ")))"
TEST9 = ")())())"
TEST10 = ")"
TEST11 = "()())"


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return next(iter(input_data))

    def part_1(self, string: Input) -> Output1:
        return len(string) - 2 * string.count(")")

    def part_2(self, string: Input) -> Output2:
        sum_ = 0
        for i, c in enumerate(string):
            sum_ += 1 if c == "(" else -1
            if sum_ == -1:
                return i + 1
        msg = "Unreachable"
        raise RuntimeError(msg)

    @aoc_samples(
        (
            ("part_1", TEST1, 0),
            ("part_1", TEST2, 0),
            ("part_1", TEST3, 3),
            ("part_1", TEST4, 3),
            ("part_1", TEST5, 3),
            ("part_1", TEST6, -1),
            ("part_1", TEST7, -1),
            ("part_1", TEST8, -3),
            ("part_1", TEST9, -3),
            ("part_2", TEST10, 1),
            ("part_2", TEST11, 5),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2015, 1)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
