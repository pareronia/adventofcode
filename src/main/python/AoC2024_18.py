#! /usr/bin/env python3
#
# Advent of Code 2024 Day 18
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import log
from aoc.graph import bfs, flood_fill
from aoc.grid import Cell

Input = list[Cell]
Output1 = int
Output2 = str


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
        cells = list[Cell]()
        for line in input_data:
            c, r = line.split(",")
            cells.append(Cell(int(r), int(c)))
        return cells

    def solve_1(self, cells: Input, size: int, time: int) -> int:
        distance, _ = bfs(
            Cell(0, 0),
            lambda cell: cell == Cell(size - 1, size - 1),
            lambda cell: (
                n
                for n in cell.get_capital_neighbours()
                if 0 <= n.row < size
                and 0 <= n.col < size
                and n not in cells[:time]
            ),
        )
        return distance

    def part_1(self, cells: Input) -> Output1:
        return self.solve_1(cells, 71, 1024)

    def solve_2(self, cells: Input, size: int) -> str:
        end = Cell(size - 1, size - 1)
        for time in range(264, len(cells)):
            log(time)
            occupied = set(cells[:time])
            open = flood_fill(
                Cell(0, 0),
                lambda cell: (
                    n
                    for n in cell.get_capital_neighbours()
                    if 0 <= n.row < size
                    and 0 <= n.col < size
                    and n not in occupied
                ),
            )
            if end not in open:
                log(cells[time - 1])
                return f"{cells[time - 1].col},{cells[time - 1].row}"
        raise RuntimeError("unsolvable")

    def part_2(self, cells: Input) -> Output2:
        return self.solve_2(cells, 71)

    def samples(self) -> None:
        assert self.solve_1(self.parse_input(TEST.splitlines()), 7, 12) == 22
        # assert self.solve_2(self.parse_input(TEST.splitlines()), 7) == "6,1"


solution = Solution(2024, 18)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
