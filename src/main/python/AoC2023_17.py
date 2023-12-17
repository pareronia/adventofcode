#! /usr/bin/env python3
#
# Advent of Code 2023 Day 17
#

import sys
from typing import Iterator
from typing import NamedTuple
from typing import TypeVar

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.geometry import Direction
from aoc.geometry import Turn
from aoc.graph import a_star
from aoc.grid import Cell
from aoc.grid import IntGrid

Input = IntGrid
Output1 = int
Output2 = int
T = TypeVar("T")


TEST = """\
2413432311323
3215453535623
3255245654254
3446585845452
4546657867536
1438598798454
4457876987766
3637877979653
4654967986887
4564679986453
1224686865563
2546548887735
4322674655533
"""


class Move(NamedTuple):
    cell: Cell
    dir: Direction | None
    cost: int

    def __eq__(self, other) -> bool:  # type:ignore[no-untyped-def]
        return True

    def __lt__(self, other) -> bool:  # type:ignore[no-untyped-def]
        return True


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return IntGrid.from_strings([line for line in input_data])

    def solve(self, grid: Input, min_moves: int, max_moves: int) -> int:
        def adjacent(move: Move) -> Iterator[Move]:
            for dir in Direction.capitals():
                if dir == move.dir:
                    continue
                if move.dir is not None and dir == move.dir.turn(Turn.AROUND):
                    continue
                it = grid.get_cells_dir(move.cell, dir)
                ns = [next(it, None) for _ in range(max_moves)]
                tot = 0
                for i, n in enumerate(ns, start=1):
                    if n is None:
                        break
                    tot += grid.get_value(n)
                    if i >= min_moves:
                        yield Move(n, dir, tot)

        start = Cell(0, 0)
        end = Cell(grid.get_max_row_index(), grid.get_max_col_index())
        cost, _, _ = a_star(
            Move(start, None, 0),
            lambda block: block.cell == end,
            adjacent,
            lambda block: block.cost,
        )
        return cost

    def part_1(self, grid: Input) -> Output1:
        return self.solve(grid, 1, 3)

    def part_2(self, grid: Input) -> Output2:
        return self.solve(grid, 4, 10)

    @aoc_samples(
        (
            ("part_1", TEST, 102),
            ("part_2", TEST, 94),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 17)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
