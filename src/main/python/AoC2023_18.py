#! /usr/bin/env python3
#
# Advent of Code 2023 Day 18
#

import sys
from typing import NamedTuple

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.geometry import Direction
from aoc.geometry import Position


class DigInstruction(NamedTuple):
    direction: Direction
    amount: int
    big_direction: Direction
    big_amount: int


Input = InputData
Output1 = int
Output2 = int


TEST = """\
R 6 (#70c710)
D 5 (#0dc571)
L 2 (#5713f0)
D 2 (#d2c081)
R 2 (#59c680)
D 2 (#411b91)
L 5 (#8ceee2)
U 2 (#caa173)
L 1 (#1b58a2)
U 2 (#caa171)
R 2 (#7807d2)
U 3 (#a77fa3)
L 2 (#015232)
U 2 (#7a21e3)
"""

DIRS = [
    Direction.RIGHT,
    Direction.DOWN,
    Direction.LEFT,
    Direction.UP,
]


class Polygon(NamedTuple):
    vertices: list[Position]

    def shoelace(self) -> int:
        size = len(self.vertices)
        s = sum(
            self.vertices[i].x
            * (self.vertices[(i + 1) % size].y - self.vertices[i - 1].y)
            for i in range(size)
        )
        return abs(s) // 2

    def circumference(self) -> int:
        return sum(
            self.vertices[i].manhattan_distance(self.vertices[i - 1])
            for i in range(1, len(self.vertices))
        )

    def inside_area(self) -> int:
        return self.shoelace() + self.circumference() // 2 + 1


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def solve(self, instructions: list[tuple[Direction, int]]) -> int:
        vertices = [Position(0, 0)]
        for instruction in instructions:
            vertices.append(
                vertices[-1].translate(instruction[0].vector, instruction[1])
            )
        return Polygon(vertices).inside_area()

    def part_1(self, dig_plan: Input) -> Output1:
        instructions = [
            (Direction.from_str(d), int(a))
            for d, a, _ in (line.split() for line in dig_plan)
        ]
        return self.solve(instructions)

    def part_2(self, dig_plan: Input) -> Output2:
        instructions = [
            (DIRS[int(hx[7])], int(hx[2:7], 16))
            for _, _, hx in (line.split() for line in dig_plan)
        ]
        return self.solve(instructions)

    @aoc_samples(
        (
            ("part_1", TEST, 62),
            ("part_2", TEST, 952408144115),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 18)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
