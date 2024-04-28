#! /usr/bin/env python3
#
# Advent of Code 2016 Day 16
#

import sys
from typing import NamedTuple

from aoc.common import InputData
from aoc.common import SolutionBase


class DragonCurve(NamedTuple):
    initial_state: str
    table: dict[int, int] = str.maketrans("01", "10")

    def dragon_curve(self, input_: str) -> str:
        return input_ + "0" + input_[::-1].translate(self.table)

    def checksum(self, data: list[str]) -> str:
        pairs = [
            "1" if data[i] == data[i + 1] else "0"
            for i in range(0, len(data) - 1, 2)
        ]
        return "".join(pairs) if len(pairs) % 2 != 0 else self.checksum(pairs)

    def checksum_for_size(self, size: int) -> str:
        data = self.initial_state
        while len(data) < size:
            data = self.dragon_curve(data)
        return self.checksum(list(data[:size]))


Input = DragonCurve
Output1 = str
Output2 = str


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return DragonCurve(list(input_data)[0])

    def solve(self, dragon_curve: DragonCurve, size: int) -> str:
        return dragon_curve.checksum_for_size(size)

    def part_1(self, dragon_curve: DragonCurve) -> str:
        return self.solve(dragon_curve, 272)

    def part_2(self, dragon_curve: DragonCurve) -> str:
        return self.solve(dragon_curve, 35651584)

    def samples(self) -> None:
        assert self.solve(self.parse_input(["10000"]), 20) == "01100"


solution = Solution(2016, 16)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
