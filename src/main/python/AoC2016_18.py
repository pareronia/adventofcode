#! /usr/bin/env python3
#
# Advent of Code 2016 Day 18
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase

SAFE = "."
TRAP = "^"
TRAPS = {"^^.", ".^^", "^..", "..^"}


Input = str
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return list(input_data)[0]

    def solve(self, first_row: str, rows: int) -> int:
        width = len(first_row)
        safe_count = first_row.count(SAFE)
        prev_row = [_ for _ in first_row]
        for _ in range(1, rows):
            new_row = [" "] * width
            for j in range(width):
                first = SAFE if j - 1 < 0 else prev_row[j - 1]
                second = prev_row[j]
                third = SAFE if j + 1 == width else prev_row[j + 1]
                prev = first + second + third
                if prev in TRAPS:
                    new_row[j] = TRAP
                else:
                    new_row[j] = SAFE
                    safe_count += 1
            prev_row = new_row
        return safe_count

    def part_1(self, input: str) -> int:
        return self.solve(input, 40)

    def part_2(self, input: str) -> int:
        return self.solve(input, 400_000)

    def samples(self) -> None:
        assert self.solve("..^^.", 3) == 6
        assert self.solve(".^^.^.^^^^", 10) == 38


solution = Solution(2016, 18)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
