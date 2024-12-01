#! /usr/bin/env python3
#
# Advent of Code 2021 Day 3
#

from __future__ import annotations

import sys
from typing import Callable
from typing import NamedTuple

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST = """\
00100
11110
10110
10111
10101
01111
00111
11100
10000
11001
00010
01010
"""


class BitCount(NamedTuple):
    ones: int
    zeroes: int

    @classmethod
    def at_pos(cls, strings: list[str], pos: int) -> BitCount:
        zeroes = sum(s[pos] == "0" for s in strings)
        return BitCount(len(strings) - zeroes, zeroes)

    def most_common(self) -> str:
        return "1" if self.ones >= self.zeroes else "0"

    def least_common(self) -> str:
        return "1" if self.ones < self.zeroes else "0"


Input = list[str]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return list(input_data)

    def ans(self, value1: str, value2: str) -> int:
        return int(value1, 2) * int(value2, 2)

    def part_1(self, inputs: Input) -> Output1:
        gamma = ""
        epsilon = ""
        for i in range(len(inputs[0])):
            bit_count = BitCount.at_pos(inputs, i)
            gamma += bit_count.most_common()
            epsilon += bit_count.least_common()
        return self.ans(gamma, epsilon)

    def reduce(
        self, strings: list[str], keep: Callable[[BitCount], str]
    ) -> str:
        pos = 0
        while len(strings) > 1:
            to_keep = keep(BitCount.at_pos(strings, pos))
            strings = [s for s in strings if s[pos] == to_keep]
            pos += 1
        return strings[0]

    def part_2(self, inputs: Input) -> Output2:
        o2 = self.reduce(inputs, lambda x: x.most_common())
        co2 = self.reduce(inputs, lambda x: x.least_common())
        return self.ans(o2, co2)

    @aoc_samples(
        (
            ("part_1", TEST, 198),
            ("part_2", TEST, 230),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2021, 3)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
