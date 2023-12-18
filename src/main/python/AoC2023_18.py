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


Input = list[DigInstruction]
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


class Polygon(NamedTuple):
    vertices: list[Position]

    def shoelace(self) -> int:
        ans = 0
        size = len(self.vertices)
        for i in range(size):
            ans += self.vertices[i].x * (
                self.vertices[(i + 1) % size].y - self.vertices[i - 1].y
            )
        return abs(ans) // 2

    def circumference(self) -> int:
        ans = 0
        for i in range(1, len(self.vertices)):
            ans += self.vertices[i].manhattan_distance(self.vertices[i - 1])
        return ans

    def picks(self) -> int:
        a = self.shoelace()
        b = self.circumference()
        return a - b // 2 + 1

    def area(self) -> int:
        return self.picks() + self.circumference()


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        ans = []
        for line in input_data:
            d, a, hx = line.split()
            bd = [
                Direction.RIGHT,
                Direction.DOWN,
                Direction.LEFT,
                Direction.UP,
            ][int(hx[7])]
            ba = int(hx[2:7], 16)
            ans.append(
                DigInstruction(
                    Direction.from_str(d),
                    int(a),
                    bd,
                    ba,
                )
            )
        return ans

    def to_positions(
        self, instructions: list[DigInstruction]
    ) -> list[Position]:
        pos = Position(0, 0)
        ans = [pos]
        for instruction in instructions:
            pos = pos.translate(
                instruction.direction.vector, instruction.amount
            )
            ans.append(pos)
        return ans

    def to_positions_big(
        self, instructions: list[DigInstruction]
    ) -> list[Position]:
        pos = Position(0, 0)
        ans = [pos]
        for instruction in instructions:
            pos = pos.translate(
                instruction.big_direction.vector, instruction.big_amount
            )
            ans.append(pos)
        return ans

    def part_1(self, instructions: Input) -> Output1:
        positions = self.to_positions(instructions)
        polygon = Polygon(positions)
        return polygon.area()

    def part_2(self, instructions: Input) -> Output2:
        positions = self.to_positions_big(instructions)
        polygon = Polygon(positions)
        return polygon.area()

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
