#! /usr/bin/env python3
#
# Advent of Code 2023 Day 1
#

import re
import sys
from typing import Callable

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

    def _solve(
        self, input: InputData, f: Callable[[str], tuple[int, int]]
    ) -> int:
        ans = 0
        for line in input:
            first, last = f(line)
            ans += first * 10 + last
        return ans

    def part_1(self, input: InputData) -> Output1:
        def get_first_and_last_digit(line: str) -> tuple[int, int]:
            digits = [c for c in line if c.isdigit()]
            return int(digits[0]), int(digits[-1])

        return self._solve(input, get_first_and_last_digit)

    def part_2(self, input: InputData) -> Output2:
        nums = {
            "one": "1",
            "two": "2",
            "three": "3",
            "four": "4",
            "five": "5",
            "six": "6",
            "seven": "7",
            "eight": "8",
            "nine": "9",
            "zero": "0",
        }

        def get_first_and_last_digit(line: str) -> tuple[int, int]:
            digits = []
            for x in nums:
                digits.extend(
                    [(m.start(), nums[x]) for m in re.finditer(x, line)]
                )
            for x in "0123456789":
                digits.extend([(m.start(), x) for m in re.finditer(x, line)])
            digits.sort()
            return int(digits[0][1]), int(digits[-1][1])

        return self._solve(input, get_first_and_last_digit)

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
