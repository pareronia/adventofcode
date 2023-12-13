#! /usr/bin/env python3
#
# Advent of Code 2023 Day 13
#

import sys

from aoc import my_aocd
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
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

    def solve(self, grid: CharGrid) -> list[tuple[int, int]]:
        ans = []
        h = grid.get_height()
        w = grid.get_width()
        for c in range(w - 1):
            for dc in range(w):
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
                ans.append((0, c + 1))
        for r in range(h - 1):
            for dr in range(h):
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
                ans.append((r + 1, 0))
        return ans

    def part_1(self, grids: Input) -> Output1:
        ans = 0
        for grid in grids:
            syms = self.solve(grid)
            assert len(syms) <= 1
            ans += (100 * syms[0][0]) + syms[0][1]
        return ans

    def part_2(self, grids: Input) -> Output2:
        ans = 0
        for i, grid in enumerate(grids):
            osyms = self.solve(grid)
            g = CharGrid([[c for c in row] for row in grid.values])
            for cell in g.get_cells():
                tmp = g.get_value(cell)
                g.set_value(cell, "." if tmp == "#" else "#")
                syms = self.solve(g)
                for s in syms:
                    if not osyms or s != osyms[0]:
                        ans += (100 * s[0]) + s[1]
                        break
                else:
                    g.set_value(cell, tmp)
                    continue
                g.set_value(cell, tmp)
                break
        return ans

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
