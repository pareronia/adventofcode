#! /usr/bin/env python3
#
# Advent of Code 2023 Day 13
#

import sys

from aoc import my_aocd
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log
from aoc.grid import Cell
from aoc.grid import CharGrid

Input = list[CharGrid]
Output1 = int
Output2 = int


TEST = """\
#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.

#...##..#
#....#..#
..##..###
#####.##.
#####.##.
..##..###
#....#..#
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [
            CharGrid.from_strings([line for line in block])
            for block in my_aocd.to_blocks(input_data)
        ]

    def part_1(self, grids: Input) -> Output1:
        ans = 0
        for grid in grids:
            h = grid.get_height()
            w = grid.get_width()
            for c in range(w - 1):
                for dc in range(w - c):
                    left = c - dc
                    right = c + dc + 1
                    if 0 <= left < right < w:
                        for r in range(h):
                            if grid.get_value(Cell(r, left)) != grid.get_value(
                                Cell(r, right)
                            ):
                                break
                        else:
                            continue
                        break
                else:
                    ans += c + 1
                    break
            for r in range(h - 1):
                for dr in range(h - r):
                    up = r - dr
                    down = r + dr + 1
                    if 0 <= up < down < h:
                        for c in range(w):
                            if grid.get_value(Cell(up, c)) != grid.get_value(
                                Cell(down, c)
                            ):
                                break
                        else:
                            continue
                        break
                else:
                    ans += 100 * (r + 1)
                    break
        log(ans)
        return ans

    def part_2(self, input: Input) -> Output2:
        return 0

    @aoc_samples(
        (
            ("part_1", TEST, 405),
            # ("part_2", TEST, "TODO"),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 13)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
