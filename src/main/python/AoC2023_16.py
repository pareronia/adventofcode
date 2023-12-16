#! /usr/bin/env python3
#
# Advent of Code 2023 Day 16
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log
from aoc.geometry import Direction
from aoc.grid import Cell
from aoc.grid import CharGrid

Input = CharGrid
Output1 = int
Output2 = int


TEST = """\
.|...\\....
|.-.\\.....
.....|-...
........|.
..........
.........\\
..../.\\\\..
.-.-/..|..
.|....-|.\\
..//.|....
"""


def log_grid(grid: CharGrid) -> None:
    if not __debug__:
        return
    for row in grid.get_rows_as_strings():
        print(row)


def draw(cells: set[Cell], fill: str, empty: str) -> list[str]:
    min_x = min(p.col for p in cells)
    max_x = max(p.col for p in cells)
    min_y = min(p.row for p in cells)
    max_y = max(p.row for p in cells)
    return list(
        [
            "".join(
                fill if Cell(y, x) in cells else empty
                for x in range(min_x, max_x + 1)
            )
            for y in range(min_y, max_y + 1)
        ]
    )


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return CharGrid.from_strings([line for line in input_data])

    def get_energised(
        self,
        grid: CharGrid,
        start: Cell,
        dir: Direction,
        seen: set[tuple[Cell, Direction]],
    ) -> set[Cell]:
        if not grid.is_in_bounds(start):
            return set()
        if ((start, dir)) in seen:
            return set()
        seen.add((start, dir))
        ans = set[Cell]()
        ans.add(start)
        val = grid.get_value(start)
        if val == ".":
            ans |= self.get_energised(grid, start.at(dir), dir, seen)
        elif val == "/" and dir == Direction.UP:
            ans |= self.get_energised(
                grid, start.at(Direction.RIGHT), Direction.RIGHT, seen
            )
        elif val == "/" and dir == Direction.RIGHT:
            ans |= self.get_energised(
                grid, start.at(Direction.UP), Direction.UP, seen
            )
        elif val == "/" and dir == Direction.DOWN:
            ans |= self.get_energised(
                grid, start.at(Direction.LEFT), Direction.LEFT, seen
            )
        elif val == "/" and dir == Direction.LEFT:
            ans |= self.get_energised(
                grid, start.at(Direction.DOWN), Direction.DOWN, seen
            )
        elif val == "\\" and dir == Direction.UP:
            ans |= self.get_energised(
                grid, start.at(Direction.LEFT), Direction.LEFT, seen
            )
        elif val == "\\" and dir == Direction.RIGHT:
            ans |= self.get_energised(
                grid, start.at(Direction.DOWN), Direction.DOWN, seen
            )
        elif val == "\\" and dir == Direction.DOWN:
            ans |= self.get_energised(
                grid, start.at(Direction.RIGHT), Direction.RIGHT, seen
            )
        elif val == "\\" and dir == Direction.LEFT:
            ans |= self.get_energised(
                grid, start.at(Direction.UP), Direction.UP, seen
            )
        elif val == "-" and dir in {Direction.RIGHT, Direction.LEFT}:
            ans |= self.get_energised(grid, start.at(dir), dir, seen)
        elif val == "-" and dir in {Direction.UP, Direction.DOWN}:
            ans |= self.get_energised(
                grid, start.at(Direction.RIGHT), Direction.RIGHT, seen
            ) | self.get_energised(
                grid, start.at(Direction.LEFT), Direction.LEFT, seen
            )
        elif val == "|" and dir in {Direction.UP, Direction.DOWN}:
            ans |= self.get_energised(grid, start.at(dir), dir, seen)
        elif val == "|" and dir in {Direction.LEFT, Direction.RIGHT}:
            ans |= self.get_energised(
                grid, start.at(Direction.UP), Direction.UP, seen
            ) | self.get_energised(
                grid, start.at(Direction.DOWN), Direction.DOWN, seen
            )
        else:
            assert False
        return ans

    def part_1(self, grid: Input) -> Output1:
        log_grid(grid)
        energised = self.get_energised(
            grid, Cell(0, 0), Direction.RIGHT, set()
        )
        log(draw(energised, "#", "."))
        return len(energised)

    def part_2(self, input: Input) -> Output2:
        return 0

    @aoc_samples(
        (
            ("part_1", TEST, 46),
            # ("part_2", TEST, "TODO"),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 16)


def main() -> None:
    sys.setrecursionlimit(10_000)
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
