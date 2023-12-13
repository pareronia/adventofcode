#! /usr/bin/env python3
#
# Advent of Code 2023 Day 3
#

import re
import sys
from collections import defaultdict
from math import prod
from typing import NamedTuple

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
    pos: Cell


Input = list[EnginePart]
Output1 = int
Output2 = int


class Solution(SolutionBase[list[EnginePart], Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        grid = CharGrid([[c for c in line] for line in input_data])

        def find_engine_part(
            row: int, col_span: tuple[int, int]
        ) -> EnginePart | None:
            for col in range(*col_span):
                for n in grid.get_all_neighbours(Cell(row, col)):
                    val = grid.get_value(n)
                    if not val.isdigit() and val != ".":
                        s = grid.get_row_as_string(row)[
                            col_span[0] : col_span[1]  # noqa E203
                        ]
                        return EnginePart(val, int(s), n)
            return None

        return [
            ep
            for ep in (
                find_engine_part(r, m.span())
                for r, row in enumerate(grid.get_rows_as_strings())
                for m in re.finditer(r"[0-9]+", row)
            )
            if ep is not None
        ]

    def part_1(self, engine_parts: list[EnginePart]) -> Output1:
        return sum(ep.number for ep in engine_parts)

    def part_2(self, engine_parts: list[EnginePart]) -> Output2:
        d = defaultdict(list)
        for gear in (ep for ep in engine_parts if ep.part == "*"):
            d[gear.pos].append(gear.number)
        return sum(prod(v) for v in d.values() if len(v) == 2)

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
