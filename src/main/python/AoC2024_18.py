#! /usr/bin/env python3
#
# Advent of Code 2024 Day 18
#

import sys
from collections.abc import Iterator

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.graph import bfs
from aoc.graph import flood_fill
from aoc.grid import Cell

Input = list[Cell]
Output1 = int
Output2 = str

SIZE = 71
TIME = 1024
START = Cell(0, 0)

TEST = """\
5,4
4,2
4,5
3,0
2,1
6,3
2,4
1,5
0,6
3,3
2,6
5,1
1,2
5,5
2,5
6,5
1,4
0,4
6,4
1,1
6,1
1,0
0,5
1,6
2,0
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [
            Cell(int(r), int(c))
            for c, r in (line.split(",") for line in input_data)
        ]

    def adjacent(
        self, cell: Cell, size: int, occupied: set[Cell]
    ) -> Iterator[Cell]:
        return (
            n
            for n in cell.get_capital_neighbours()
            if 0 <= n.row < size and 0 <= n.col < size and n not in occupied
        )

    def solve_1(self, cells: Input, size: int = SIZE, time: int = TIME) -> int:
        end = Cell(size - 1, size - 1)
        occupied = set(cells[:time])
        distance, _ = bfs(
            START,
            lambda cell: cell == end,
            lambda cell: self.adjacent(cell, size, occupied),
        )
        return distance

    def solve_2(self, cells: Input, size: int = SIZE, time: int = TIME) -> str:
        end = Cell(size - 1, size - 1)

        def free(time: int) -> set[Cell]:
            occupied = set(cells[:time])
            return flood_fill(
                START,
                lambda cell: self.adjacent(cell, size, occupied),
            )

        lo, hi = time, len(cells)
        while lo < hi:
            mid = (lo + hi) // 2
            if end in free(mid):
                lo = mid + 1
            else:
                hi = mid
        return f"{cells[lo - 1].col},{cells[lo - 1].row}"

    def part_1(self, cells: Input) -> Output1:
        return self.solve_1(cells)

    def part_2(self, cells: Input) -> Output2:
        return self.solve_2(cells)

    def samples(self) -> None:
        inputs = self.parse_input(TEST.splitlines())
        assert self.solve_1(inputs, 7, 12) == 22
        assert self.solve_2(inputs, 7, 12) == "6,1"


solution = Solution(2024, 18)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
