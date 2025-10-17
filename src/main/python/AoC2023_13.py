#! /usr/bin/env python3
#
# Advent of Code 2023 Day 13
#

import sys

from aoc import my_aocd
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
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
            CharGrid.from_strings(block)
            for block in my_aocd.to_blocks(input_data)
        ]

    def solve(self, grids: list[CharGrid], smudge: int) -> int:
        def find_symmetry(grid: CharGrid, smudge: int) -> tuple[int, int]:
            h = grid.get_height()
            w = grid.get_width()
            for c in range(w - 1):
                err = 0
                for dc in range(w // 2):
                    left = c - dc
                    right = c + dc + 1
                    if not 0 <= left < right < w:
                        continue
                    err += sum(
                        1
                        for r in range(h)
                        if grid.values[r][left] != grid.values[r][right]
                    )
                if err == smudge:
                    return (0, c + 1)
            for r in range(h - 1):
                err = 0
                for dr in range(h // 2):
                    up = r - dr
                    down = r + dr + 1
                    if not 0 <= up < down < h:
                        continue
                    err += sum(
                        1
                        for c in range(w)
                        if grid.values[up][c] != grid.values[down][c]
                    )
                if err == smudge:
                    return (r + 1, 0)
            raise AssertionError

        return sum(
            (100 * r) + c
            for r, c in (find_symmetry(grid, smudge) for grid in grids)
        )

    def part_1(self, grids: Input) -> Output1:
        return self.solve(grids, smudge=0)

    def part_2(self, grids: Input) -> Output2:
        return self.solve(grids, smudge=1)

    @aoc_samples(
        (
            ("part_1", TEST, 405),
            ("part_2", TEST, 400),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 13)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
