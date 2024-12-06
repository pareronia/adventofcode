#! /usr/bin/env python3
#
# Advent of Code 2024 Day 6
#

import sys
from collections import defaultdict

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.geometry import Direction
from aoc.geometry import Turn
from aoc.grid import Cell
from aoc.grid import CharGrid

Input = InputData
Output1 = int
Output2 = int


TEST = """\
....#.....
.........#
..........
..#.......
.......#..
..........
.#..^.....
........#.
#.........
......#...
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def part_1(self, input: Input) -> Output1:
        grid = CharGrid.from_strings(list(input))
        pos = next(grid.get_all_equal_to("^"))
        seen = {pos}
        d = Direction.UP
        while True:
            nxt = pos.at(d)
            if not grid.is_in_bounds(nxt):
                break
            if grid.get_value(nxt) == "#":
                d = d.turn(Turn.RIGHT)
            else:
                pos = nxt
            seen.add(pos)
        return len(seen)

    def part_2(self, input: Input) -> Output2:
        grid = CharGrid.from_strings(list(input))

        def route(pos: Cell, d: Direction) -> tuple[bool, set[Cell]]:
            seen = defaultdict[Cell, set[Direction]](set)
            seen[pos].add(d)
            while True:
                nxt = pos.at(d)
                if not grid.is_in_bounds(nxt):
                    return False, {_ for _ in seen.keys()}
                if grid.get_value(nxt) == "#":
                    d = d.turn(Turn.RIGHT)
                else:
                    pos = nxt
                if d in seen[pos]:
                    return True, {_ for _ in seen.keys()}
                seen[pos].add(d)

        pos = next(grid.get_all_equal_to("^"))
        d = Direction.UP
        _, cells = route(pos, d)
        ans = 0
        for cell in cells:
            grid.set_value(cell, "#")
            loop, seen = route(pos, d)
            if loop:
                ans += 1
            grid.set_value(cell, ".")
        return ans

    @aoc_samples(
        (
            ("part_1", TEST, 41),
            ("part_2", TEST, 6),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2024, 6)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
