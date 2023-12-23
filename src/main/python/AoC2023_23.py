#! /usr/bin/env python3
#
# Advent of Code 2023 Day 23
#

import sys
from typing import Iterator

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
#.#####################
#.......#########...###
#######.#########.#.###
###.....#.>.>.###.#.###
###v#####.#v#.###.#.###
###.>...#.#.#.....#...#
###v###.#.#.#########.#
###...#.#.#.......#...#
#####.#.#.#######.#.###
#.....#.#.#.......#...#
#.#####.#.#.#########v#
#.#...#...#...###...>.#
#.#.#v#######v###.###v#
#...#.>.#...>.>.#.###.#
#####v#.#.###v#.#.###.#
#.....#...#...#.#.#...#
#.#########.###.#.#.###
#...###...#...#...#.###
###.###.#.###v#####v###
#...#...#.#.>.>.#.>.###
#.###.###.#.###.#.#v###
#.....###...###...#...#
#####################.#
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return CharGrid.from_strings([line for line in input_data])

    def part_1(self, grid: Input) -> Output1:
        def adjacent(cell: Cell) -> Iterator[Cell]:
            for d in Direction.capitals():
                n = cell.at(d)
                val = grid.get_value(n)
                if val == "#":
                    continue
                if (
                    val in {"^", ">", "v", "<"}
                    and Direction.from_str(val) != d
                ):
                    continue
                yield n

        def find_longest(cell: Cell, size: int) -> int:
            if cell == end:
                return size
            if cell in seen:
                return int(-1e9)
            max_size = size
            a = [_ for _ in adjacent(cell)]
            for n in a:
                seen.add(cell)
                max_size = max(max_size, find_longest(n, size + 1))
                seen.remove(cell)
            return max_size

        sys.setrecursionlimit(10_000)
        start = Cell(0, 1)
        end = (grid.get_height() - 1, grid.get_width() - 2)
        seen = set[Cell]()
        ans = find_longest(start, 0)
        log(ans)
        return ans

    def part_2(self, grid: Input) -> Output2:
        return 0

    @aoc_samples(
        (
            ("part_1", TEST, 94),
            # ("part_2", TEST, "TODO"),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 23)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
