#! /usr/bin/env python3
#
# Advent of Code 2023 Day 1
#

import sys
from collections.abc import Callable

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = InputData
Output1 = int
Output2 = int

TEST1 = """\
1abc2
pqr3stu8vwx
a1b2c3d4e5f
treb7uchet
"""
TEST2 = """\
two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def _solve(self, lines: InputData, f: Callable[[str], list[int]]) -> int:
        return sum(digits[0] * 10 + digits[-1] for digits in map(f, lines))

    def part_1(self, lines: InputData) -> Output1:
        def get_digits(line: str) -> list[int]:
            return [int(c) for c in line if c.isdigit()]

        return self._solve(lines, get_digits)

    def part_2(self, lines: InputData) -> Output2:
        def get_digits(line: str) -> list[int]:
            nums = [
                "one",
                "two",
                "three",
                "four",
                "five",
                "six",
                "seven",
                "eight",
                "nine",
            ]

            def find_digit(s: str) -> int | None:
                if s[0].isdigit():
                    return int(s[0])
                for j, num in enumerate(nums):
                    if s.startswith(num):
                        return j + 1
                return None

            return [
                x
                for x in (find_digit(line[i:]) for i in range(len(line)))
                if x is not None
            ]

        return self._solve(lines, get_digits)

    @aoc_samples(
        (
            ("part_1", TEST1, 142),
            ("part_2", TEST2, 281),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 1)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
