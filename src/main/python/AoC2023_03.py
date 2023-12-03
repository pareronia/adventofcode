#! /usr/bin/env python3
#
# Advent of Code 2023 Day 3
#

import re
import sys
from typing import NamedTuple
from collections import defaultdict
from math import prod

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

from aoc.grid import Cell
from aoc.grid import CharGrid

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


class EnginePart(NamedTuple):
    part: str
    number: int
    start: int
    end: int


Input = list[EnginePart]
Output1 = int
Output2 = int


class Solution(SolutionBase[list[EnginePart], Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        g = [[c for c in line] for line in input_data]
        grid = CharGrid(g)
        engine_parts = []
        for r, row in enumerate(grid.values):
            s = "".join(row)
            mm = re.finditer(r"[0-9]+", "".join(row))
            matches = [_ for _ in mm]
            for m in matches:
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
                        number = int(s[start:end])
                        engine_parts.append(
                            EnginePart(v, number, n.row, n.col)
                        )
                        break
        return engine_parts

    def part_1(self, engine_parts: list[EnginePart]) -> Output1:
        return sum(ep.number for ep in engine_parts)

    def part_2(self, engine_parts: list[EnginePart]) -> Output2:
        gears = [ep for ep in engine_parts if ep.part == "*"]
        d = defaultdict(list)
        for g in gears:
            d[(g.start, g.end)].append(g.number)
        ans = 0
        for x in d:
            if len(d[x]) == 2:
                ans += prod(d[x])
        return ans

    @aoc_samples(
        (
            ("part_1", TEST, 4361),
            ("part_2", TEST, 467835),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 3)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
