#! /usr/bin/env python3
#
# Advent of Code 2023 Day 10
#

import sys
from collections import deque
from typing import Iterator

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.geometry import Direction
from aoc.graph import flood_fill
from aoc.grid import Cell
from aoc.grid import CharGrid

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
TEST3 = """\
...........
.S-------7.
.|F-----7|.
.||.....||.
.||.....||.
.|L-7.F-J|.
.|..|.|..|.
.L--J.L--J.
...........
"""
TEST4 = """\
.F----7F7F7F7F-7....
.|F--7||||||||FJ....
.||.FJ||||||||L7....
FJL7L7LJLJ||LJ.L-7..
L--J.L7...LJS7F-7L7.
....F-J..F7FJ|L7L7L7
....L7.F7||L7|.L7L7|
.....|FJLJ|FJ|F7|.LJ
....FJL-7.||.||||...
....L---J.LJ.LJLJ...
"""
TEST5 = """\
FF7FSF7F7F7F7F7F---7
L|LJ||||||||||||F--J
FL-7LJLJ||||||LJL-77
F--JF--7||LJLJ7F7FJ-
L---JF-JLJ.||-FJLJJ7
|F|F-JF---7F7-L7L|7|
|FFJF7L7F-JF7|JL---7
7-L-JL7||F7|L7F-7F7|
L.L7LFJ|||||FJL7||LJ
L7JLJL-JLJLJL--JLJ.L
"""


class LoopFinder():
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

    @classmethod
    def find_loop(cls, grid: Input) -> list[Cell]:
        start = next(grid.get_all_equal_to("S"))
        q: deque[tuple[int, Cell, Direction]] = deque()
        seen = set[tuple[Cell, Direction]]()
        parent = dict[tuple[Cell, Direction], tuple[Cell, Direction]]()
        for dir in Direction.capitals():
            q.append((0, start, dir))
            seen.add((start, dir))
        while not len(q) == 0:
            distance, curr, dir = q.popleft()
            n = curr.at(dir)
            if n == start:
                path = [curr]
                c = (curr, dir)
                while c in parent:
                    c = parent[c]
                    path.append(c[0])
                return path
            if grid.is_in_bounds(n):
                val = grid.get_value(n)
                if val in cls.TILES[dir]:
                    new_dir = cls.TILES[dir][val]
                    _next = (n, new_dir)
                    if _next in seen:
                        continue
                    seen.add(_next)
                    parent[(n, new_dir)] = (curr, dir)
                    q.append((distance + 1, *_next))
        raise RuntimeError("unsolvable")


class EnlargeGridInsideFinder:
    XGRIDS = {
        "|": CharGrid.from_strings([".#.", ".#.", ".#."]),
        "-": CharGrid.from_strings(["...", "###", "..."]),
        "L": CharGrid.from_strings([".#.", ".##", "..."]),
        "J": CharGrid.from_strings([".#.", "##.", "..."]),
        "7": CharGrid.from_strings(["...", "##.", ".#."]),
        "F": CharGrid.from_strings(["...", ".##", ".#."]),
        ".": CharGrid.from_strings(["...", "...", "..."]),
        "S": CharGrid.from_strings([".S.", "SSS", ".S."]),
    }

    @classmethod
    def count_inside(cls, grid: CharGrid, loop: list[Cell]) -> int:
        grids = [
            [cls.XGRIDS["."] for _ in range(grid.get_width())]
            for _ in range(grid.get_height())
        ]
        for r, c in loop:
            grids[r][c] = cls.XGRIDS[grid.get_value(Cell(r, c))]
        xgrid = CharGrid.merge(grids)  # type:ignore[arg-type]
        new_loop = {
            cell
            for cell in xgrid.find_all_matching(
                lambda c: xgrid.get_value(c) in "#S"
            )
        }

        def adjacent(cell: Cell) -> Iterator[Cell]:
            return (
                n
                for n in xgrid.get_capital_neighbours(cell)
                if n not in new_loop
            )

        outside = {_ for _ in flood_fill(Cell(0, 0), adjacent)}
        inside = {cell for cell in xgrid.get_cells()} - outside - new_loop
        return sum(
            all(
                Cell(3 * r + rr, 3 * c + cc) in inside
                for rr in range(3)
                for cc in range(3)
            )
            for r, c in grid.get_cells()
        )


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return CharGrid.from_strings([line for line in input_data])

    def part_1(self, grid: Input) -> Output1:
        loop = LoopFinder.find_loop(grid)
        return len(loop) // 2

    def part_2(self, grid: Input) -> Output2:
        loop = LoopFinder.find_loop(grid)
        return EnlargeGridInsideFinder.count_inside(grid, loop)

    @aoc_samples(
        (
            ("part_1", TEST1, 4),
            ("part_1", TEST2, 8),
            ("part_2", TEST1, 1),
            ("part_2", TEST2, 1),
            ("part_2", TEST3, 4),
            ("part_2", TEST4, 8),
            ("part_2", TEST5, 10),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 10)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
