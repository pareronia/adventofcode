#! /usr/bin/env python3
#
# Advent of Code 2025 Day 6
#

import sys
from math import prod

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.geometry import Direction
from aoc.grid import Cell
from aoc.grid import CharGrid

Input = InputData
Output1 = int
Output2 = int


TEST = "123 328  51 64 \n 45 64  387 23 \n  6 98  215 314\n*   +   *   +  "


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def part_1(self, inputs: Input) -> Output1:
        nums = list[list[int]]()
        ops = list[str]()
        for line in inputs:
            items = line.strip().split()
            if items[0].isnumeric():
                nums.append([int(n) for n in items])
            else:
                ops = items
        ans = 0
        for i in range(len(nums[0])):
            if ops[i] == "+":
                ans += sum(nums[j][i] for j in range(len(nums)))
            else:
                ans += prod(nums[j][i] for j in range(len(nums)))
        return ans

    def part_2(self, inputs: Input) -> Output2:
        lines = list(inputs)
        grid = CharGrid.from_strings(lines[:-1])
        ops = lines[-1].strip().split()
        pos = Cell(0, 0)
        nums = list[int]()
        ans = 0
        j = 0
        for i in range(grid.get_width() + 1):
            if i < grid.get_width():
                s = grid.get_value(pos) + "".join(
                    grid.get_value(c) for c in grid.get_cells_s(pos)
                )
            else:
                s = ""
            if s.strip() == "":
                if ops[j] == "+":
                    ans += sum(nums)
                else:
                    ans += prod(nums)
                nums = []
                j += 1
            else:
                nums.append(int(s.strip()))
            pos = pos.at(Direction.RIGHT)
        return ans

    @aoc_samples(
        (
            ("part_1", TEST, 4277556),
            ("part_2", TEST, 3263827),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2025, 6)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
