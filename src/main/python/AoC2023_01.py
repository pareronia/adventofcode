#! /usr/bin/env python3
#
# Advent of Code 2023 Day 1
#

import re
import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = InputData
Output1 = int
Output2 = int


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

    def part_1(self, input: InputData) -> Output1:
        ans = 0
        for line in input:
            digits = [c for c in line if c.isdigit()]
            ans += int(digits[0] + digits[-1])
        return ans

    def part_2(self, input: InputData) -> Output2:
        nums = {
            "one": 1,
            "two": 2,
            "three": 3,
            "four": 4,
            "five": 5,
            "six": 6,
            "seven": 7,
            "eight": 8,
            "nine": 9,
            "zero": 0,
        }
        ans = 0
        for line in input:
            print(line)
            tmp = []
            for x in nums:
                tmp.extend(
                    [(m.start(), nums[x]) for m in re.finditer(x, line)]
                )
            for x in "0123456789":
                tmp.extend(
                    [(m.start(), int(x)) for m in re.finditer(x, line)]
                )
            # tmp.extend([(line.find(c), int(c)) for c in line if c.isdigit()])
            tmp.sort()
            print(tmp)
            if len(tmp) == 0:
                continue
            d = tmp[0][1] * 10 + tmp[-1][1]
            print(d)
            ans += d
        return ans

    @aoc_samples(
        (
            # ("part_1", TEST, "TODO"),
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
