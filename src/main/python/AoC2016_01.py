#! /usr/bin/env python3
#
# Advent of Code 2016 Day 1
#

import sys
from typing import Iterator

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.geometry import Position
from aoc.geometry import Turn
from aoc.navigation import Heading
from aoc.navigation import NavigationWithHeading

Input = list[str]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return list(input_data)[0].split(", ")

    def navigate(self, steps: list[str]) -> Iterator[Position]:
        navigation = NavigationWithHeading(Position(0, 0), Heading.NORTH)
        for step in steps:
            navigation.turn(Turn.from_str(step[0]))
            for _ in range(int(step[1:])):
                navigation.forward(1)
                yield navigation.position

    def part_1(self, steps: Input) -> Output1:
        *_, final = self.navigate(steps)
        return final.manhattan_distance_to_origin()

    def part_2(self, steps: Input) -> Output2:
        seen = set()
        for pos in self.navigate(steps):
            if pos in seen:
                return pos.manhattan_distance_to_origin()
            else:
                seen.add(pos)
        raise ValueError("Unsolvable")

    @aoc_samples(
        (
            ("part_1", "R2, L3", 5),
            ("part_1", "R2, R2, R2", 2),
            ("part_1", "R5, L5, R5, R3", 12),
            ("part_2", "R8, R4, R4, R8", 4),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2016, 1)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
