#! /usr/bin/env python3
#
# Advent of Code 2015 Day 25
#

import sys
from dataclasses import dataclass
from typing import Self

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log

TEST1 = """\
To continue, please consult the code grid in the manual.\
  Enter the code at row 1, column 1.
"""
TEST2 = """\
To continue, please consult the code grid in the manual.\
  Enter the code at row 4, column 3.
"""
TEST3 = """\
To continue, please consult the code grid in the manual.\
  Enter the code at row 2, column 5.
"""


@dataclass(frozen=True)
class Code:
    row: int
    col: int

    @classmethod
    def from_input(cls, string: str) -> Self:
        splits = string.replace(".", "").replace(",", "").split()
        return cls(int(splits[15]), int(splits[17]))

    def number(self) -> int:
        r = self.row
        c = self.col
        if r == c == 1:
            return 1
        return (c * (c + 1)) // 2 + (r - 1) * c + ((r - 2) * (r - 1)) // 2


Input = Code
Output1 = int
Output2 = str


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return Code.from_input(next(iter(input_data)))

    def calculate_code(self, code: Code) -> int:
        start = 20151125
        number = code.number()
        if number == 1:
            return start
        val = start
        i = 2
        while i <= number:
            val = (val * 252533) % 33554393
            i += 1
        return val

    def part_1(self, code: Input) -> Output1:
        log(code)
        log(code.number())
        return self.calculate_code(code)

    def part_2(self, code: Input) -> Output2:  # noqa: ARG002
        return "🎄"

    @aoc_samples(
        (
            ("part_1", TEST1, 20151125),
            ("part_1", TEST2, 21345942),
            ("part_1", TEST3, 15514188),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2015, 25)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
