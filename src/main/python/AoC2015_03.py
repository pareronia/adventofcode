#! /usr/bin/env python3
#
# Advent of Code 2015 Day 3
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.geometry import Position
from aoc.navigation import Heading
from aoc.navigation import NavigationWithHeading

START = Position(0, 0)
Navigation = NavigationWithHeading
Input = list[Heading]
Output1 = int
Output2 = int

TEST1 = ">"
TEST2 = "^>v<"
TEST3 = "^v^v^v^v^v"
TEST4 = "^v"


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [Heading.from_str(ch) for ch in next(iter(input_data))]

    def count_unique_positions(self, positions: list[Position]) -> int:
        return len(set(positions))

    def add_navigation_instruction(
        self, navigation: Navigation, c: Heading
    ) -> None:
        navigation.drift(c, 1)

    def part_1(self, inputs: Input) -> Output1:
        navigation = Navigation(START, Heading.NORTH)
        for c in inputs:
            self.add_navigation_instruction(navigation, c)
        return self.count_unique_positions(
            navigation.get_visited_positions(include_start_position=True)
        )

    def part_2(self, inputs: Input) -> Output2:
        santa_nav = Navigation(START, Heading.NORTH)
        robot_nav = Navigation(START, Heading.NORTH)
        for i, c in enumerate(inputs):
            self.add_navigation_instruction(
                robot_nav if i % 2 else santa_nav, c
            )
        return self.count_unique_positions(
            santa_nav.get_visited_positions(include_start_position=True)
            + robot_nav.get_visited_positions(include_start_position=True)
        )

    @aoc_samples(
        (
            ("part_1", TEST1, 2),
            ("part_1", TEST2, 4),
            ("part_1", TEST3, 2),
            ("part_2", TEST4, 3),
            ("part_2", TEST2, 3),
            ("part_2", TEST3, 11),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2015, 3)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
