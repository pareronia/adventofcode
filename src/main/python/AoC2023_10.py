#! /usr/bin/env python3
#
# Advent of Code 2023 Day 10
#

import sys
from collections import deque

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log
from aoc.geometry import Direction
from aoc.grid import CharGrid, Cell

Input = CharGrid
Output1 = int
Output2 = int


TEST1 = """\
.....
.S-7.
.|.|.
.L-J.
.....
"""
TEST2 = """\
..F7.
.FJ|.
SJ.L7
|F--J
LJ...
"""

TILES = {
    Direction.UP: {
        "|": Direction.UP,
        "7": Direction.LEFT,
        "F": Direction.RIGHT,
    },
    Direction.RIGHT: {
        "-": Direction.RIGHT,
        "J": Direction.UP,
        "7": Direction.DOWN,
    },
    Direction.DOWN: {
        "|": Direction.DOWN,
        "J": Direction.LEFT,
        "L": Direction.RIGHT,
    },
    Direction.LEFT: {
        "-": Direction.LEFT,
        "L": Direction.UP,
        "F": Direction.DOWN,
    },
}


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return CharGrid([[ch for ch in line] for line in input_data])

    def part_1(self, grid: Input) -> Output1:
        start = next(grid.get_all_equal_to("S"))
        log(start)
        q: deque[tuple[int, Cell, Direction]] = deque()
        seen = set[tuple[Cell, Direction]]()
        for dir in Direction.capitals():
            q.append((0, start, dir))
            seen.add((start, dir))
        while not len(q) == 0:
            distance, curr, dir = q.popleft()
            n = curr.at(dir)
            if grid.is_in_bounds(n):
                if n.row == start.row and n.col == start.col:
                    return (distance + 1) // 2
                val = grid.get_value(n)
                if val in TILES[dir]:
                    _next = (n, TILES[dir][val])
                    if _next in seen:
                        continue
                    seen.add(_next)
                    q.append((distance + 1, *_next))
        raise RuntimeError("unsolvable")

    def part_2(self, grid: Input) -> Output2:
        return 0

    @aoc_samples(
        (
            ("part_1", TEST1, 4),
            ("part_1", TEST2, 8),
            # ("part_2", TEST, "TODO"),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 10)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
