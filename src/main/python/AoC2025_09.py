#! /usr/bin/env python3
#
# Advent of Code 2025 Day 9
#

import itertools
import sys
from collections.abc import Iterator
from dataclasses import dataclass
from typing import Self

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.grid import Cell

TEST = """\
7,1
11,1
11,7
9,7
9,5
2,5
2,3
7,3
"""


@dataclass(frozen=True)
class Rectangle:
    min_row: int
    min_col: int
    max_row: int
    max_col: int

    @classmethod
    def from_cells(cls, first: Cell, second: Cell) -> Self:
        min_row = min(first.row, second.row)
        min_col = min(first.col, second.col)
        max_row = max(first.row, second.row)
        max_col = max(first.col, second.col)
        return cls(min_row, min_col, max_row, max_col)

    @property
    def area(self) -> int:
        return (self.max_row - self.min_row + 1) * (
            self.max_col - self.min_col + 1
        )

    def intersects(self, other: Self) -> bool:
        return not (
            self.max_col <= other.min_col
            or self.min_col >= other.max_col
            or self.max_row <= other.min_row
            or self.min_row >= other.max_row
        )


Input = list[Cell]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [
            Cell(int(y), int(x))
            for x, y in (line.split(",") for line in input_data)
        ]

    def rectangles(self, reds: list[Cell]) -> Iterator[Rectangle]:
        for first, second in itertools.combinations(reds, 2):
            yield Rectangle.from_cells(first, second)

    def part_1(self, reds: Input) -> Output1:
        return max(rect.area for rect in self.rectangles(reds))

    def part_2(self, reds: Input) -> Output2:
        border_segments = [
            Rectangle.from_cells(first, second)
            for first, second in itertools.pairwise([*reds, reds[0]])
        ]
        return max(
            rect.area
            for rect in self.rectangles(reds)
            if not any(rect.intersects(bs) for bs in border_segments)
        )

    @aoc_samples(
        (
            ("part_1", TEST, 50),
            ("part_2", TEST, 24),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2025, 9)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
