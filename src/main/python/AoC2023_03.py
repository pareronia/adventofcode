#! /usr/bin/env python3
#
# Advent of Code 2023 Day 3
#

import sys
import re

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log
from aoc.grid import CharGrid, Cell

Input = CharGrid
Output1 = int
Output2 = int


TEST = """\
467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...$.*....
.664.598..
"""


class Solution(SolutionBase[CharGrid, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        g = [[c for c in line] for line in input_data]
        return CharGrid(g)

    def part_1(self, grid: CharGrid) -> Output1:
        ans = 0
        for r, row in enumerate(grid.values):
            s = "".join(row)
            mm = re.finditer(r"[0-9]+", "".join(row))
            matches = [_ for _ in mm]
            # if len(matches) == 0:
            #     continue
            log(matches)
            for m in matches:
                log(m.span())
                found = False
                start = m.span()[0]
                end = m.span()[1]
                for c in range(start, end):
                    for n in grid.get_all_neighbours(Cell(r, c)):
                        v = grid.get_value(n)
                        if not v.isnumeric() and v != ".":
                            found = True
                            break
                    if found:
                        ans += int(s[start:end])
                        break
        return ans

    def part_2(self, input: CharGrid) -> Output2:
        return 0

    @aoc_samples(
        (
            ("part_1", TEST, 4361),
            # ("part_2", TEST, "TODO"),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 3)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
