#! /usr/bin/env python3
#
# Advent of Code 2022 Day 25
#


import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST = """\
1=-0-2
12111
2=0=
21
2=01
111
20012
112
1=-1=
1-12
12
1=
122
"""
TEST1 = "1=11-2"
TEST2 = "1-0---0"
TEST3 = "1121-1110-1=0"

DECODE = {"0": 0, "1": 1, "2": 2, "-": -1, "=": -2}
ENCODE = {
    0: ("0", 0),
    1: ("1", 0),
    2: ("2", 0),
    3: ("=", 1),
    4: ("-", 1),
    5: ("0", 1),
}

Input = InputData
Output1 = str
Output2 = str


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def part_1(self, inputs: Input) -> Output1:
        total = sum(
            sum(DECODE[digit] * 5**i for i, digit in enumerate(line[::-1]))
            for line in inputs
        )
        ans = ""
        while total:
            digit, carry = ENCODE[total % 5]
            ans += digit
            total = total // 5 + carry
        return ans[::-1]

    def part_2(self, _inputs: Input) -> Output2:
        return "🎄"

    @aoc_samples(
        (
            ("part_1", TEST, "2=-1=0"),
            ("part_1", TEST1, "1=11-2"),
            ("part_1", TEST2, "1-0---0"),
            ("part_1", TEST3, "1121-1110-1=0"),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2022, 25)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
