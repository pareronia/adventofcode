#! /usr/bin/env python3
#
# Advent of Code 2023 Day 18
#

import sys
from typing import NamedTuple, Iterator

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log
from aoc.geometry import Direction
from aoc.geometry import Draw
from aoc.geometry import Position
from aoc.graph import flood_fill
from aoc.navigation import Heading
from aoc.navigation import NavigationWithHeading


class DigInstruction(NamedTuple):
    direction: Direction
    amount: int


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


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        ans = []
        for line in input_data:
            d, a, _ = line.split()
            ans.append(DigInstruction(Direction.from_str(d), int(a)))
        return ans

    def part_1(self, instructions: Input) -> Output1:
        log(instructions)
        nav = NavigationWithHeading(Position(0, 0), Heading.NORTH)
        for ins in instructions:
            for i in range(ins.amount):
                nav.drift(Heading.from_direction(ins.direction), 1)
        positions = {_ for _ in nav.get_visited_positions(True)}
        for line in Draw.draw(positions, "#", "."):
            log(line)
        min_x = min(p.x for p in positions)
        max_y = max(p.y for p in positions)
        pos = Position(min_x - 1, max_y + 1)
        while True:
            pos = Position(pos.x + 1, pos.y - 1)
            if pos in positions:
                break
        start = Position(pos.x + 1, pos.y - 1)
        log(start)

        def adjacent(pos: Position) -> Iterator[Position]:
            for d in Direction.octants():
                n = pos.translate(d.vector)
                if n not in positions:
                    yield n

        inside = flood_fill(start, adjacent)
        return len(inside) + len(positions)

    def part_2(self, input: Input) -> Output2:
        return 0

    @aoc_samples(
        (
            ("part_1", TEST, 62),
            # ("part_2", TEST, "TODO"),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 18)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
