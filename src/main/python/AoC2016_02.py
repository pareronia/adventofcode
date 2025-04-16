#! /usr/bin/env python3
#
# Advent of Code 2016 Day 2
#

import sys
from typing import NamedTuple

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.geometry import Position
from aoc.navigation import Heading
from aoc.navigation import NavigationWithHeading

TEST = """\
ULL
RRDDD
LURDL
UUUUD
"""

KEYPAD_1 = {
    (-1, 1): "1",
    (0, 1): "2",
    (1, 1): "3",
    (-1, 0): "4",
    (0, 0): "5",
    (1, 0): "6",
    (-1, -1): "7",
    (0, -1): "8",
    (1, -1): "9",
}
KEYPAD_2 = {
    (2, 2): "1",
    (1, 1): "2",
    (2, 1): "3",
    (3, 1): "4",
    (0, 0): "5",
    (1, 0): "6",
    (2, 0): "7",
    (3, 0): "8",
    (4, 0): "9",
    (1, -1): "A",
    (2, -1): "B",
    (3, -1): "C",
    (2, -2): "D",
}


Input = InputData
Output1 = str
Output2 = str


class Keypad(NamedTuple):
    keys: dict[tuple[int, int], str]

    def in_bounds(self, pos: Position) -> bool:
        return (pos.x, pos.y) in self.keys.keys()

    def get(self, pos: Position) -> str:
        return self.keys[(pos.x, pos.y)]


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def solve(self, inputs: Input, keypad: Keypad) -> str:
        navigation = NavigationWithHeading(
            Position.of(0, 0), Heading.NORTH, keypad.in_bounds
        )
        code = ""
        for line in inputs:
            for c in line:
                navigation.drift(Heading.from_str(c), 1)
            code += keypad.get(navigation.position)
        return code

    def part_1(self, inputs: Input) -> Output1:
        return self.solve(inputs, Keypad(KEYPAD_1))

    def part_2(self, inputs: Input) -> Output2:
        return self.solve(inputs, Keypad(KEYPAD_2))

    @aoc_samples(
        (
            ("part_1", TEST, "1985"),
            ("part_2", TEST, "5DB3"),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2016, 2)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
