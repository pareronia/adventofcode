#! /usr/bin/env python3
#
# Advent of Code 2023 Day 1
#

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

    def _solve(self, input: InputData, f: Callable[[str], list[int]]) -> int:
        ans = 0
        for line in input:
            digits = f(line)
            ans += digits[0] * 10 + digits[-1]
        return ans

    def part_1(self, input: InputData) -> Output1:
        def get_digits(line: str) -> list[int]:
            return [int(c) for c in line if c.isdigit()]

        return self._solve(input, get_digits)

    def part_2(self, input: InputData) -> Output2:
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

        def get_digits(line: str) -> list[int]:
            digits = []
            for i, c in enumerate(line):
                if c.isdigit():
                    digits.append(int(c))
                else:
                    for j, num in enumerate(nums):
                        if line[i:].startswith(num):
                            digits.append(j + 1)
                            break
            return digits

        return self._solve(input, get_digits)

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
