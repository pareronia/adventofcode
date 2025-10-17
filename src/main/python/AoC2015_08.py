#! /usr/bin/env python3
#
# Advent of Code 2015 Day 8
#

import re
import sys
from enum import Enum
from enum import auto
from enum import unique

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST = r"""""
"abc"
"aaa\"aaa"
"\x27"
"""
RE = re.compile(r"\\x[0-9a-f]{2}")

Input = InputData
Output1 = int
Output2 = int


@unique
class Mode(Enum):
    DECODE = auto()
    ENCODE = auto()

    def overhead(self, string: str) -> int:
        match self:
            case Mode.DECODE:
                assert string[0] == '"' and string[-1] == '"'
                cnt = 2
                while string.find(r"\\") != -1:
                    string = string.replace(r"\\", "", 1)
                    cnt += 1
                return cnt + string.count(r"\"") + 3 * len(RE.findall(string))
            case Mode.ENCODE:
                return 2 + string.count("\\") + string.count('"')


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def solve(self, inputs: Input, mode: Mode) -> int:
        return sum(mode.overhead(s) for s in inputs)

    def part_1(self, inputs: Input) -> Output1:
        return self.solve(inputs, Mode.DECODE)

    def part_2(self, inputs: Input) -> Output2:
        return self.solve(inputs, Mode.ENCODE)

    @aoc_samples(
        (
            ("part_1", TEST, 12),
            ("part_2", TEST, 19),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2015, 8)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
