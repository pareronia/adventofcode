#! /usr/bin/env python3
#
# Advent of Code 2020 Day 9
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase

TEST = """\
35
20
15
25
47
40
62
55
65
95
102
117
150
182
127
219
299
277
309
576
"""

Input = list[int]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [int(_) for _ in input_data]

    def solve_1(self, numbers: list[int], window_size: int) -> int:
        return next(
            i
            for i in range(window_size, len(numbers))
            if not any(
                numbers[i] - n in numbers[i - window_size : i]  # noqa E203
                for n in numbers[i - window_size : i]  # noqa E203
            )
        )

    def part_1(self, numbers: list[int]) -> int:
        invalid_idx = self.solve_1(numbers, window_size=25)
        return numbers[invalid_idx]

    def solve_2(self, numbers: list[int], window_size: int) -> int:
        invalid_idx = self.solve_1(numbers, window_size)
        sublists = (
            numbers[i:j]
            for i in range(invalid_idx + 1)
            for j in range(i + 1, invalid_idx + 1)
            if j - i >= 2
        )
        return next(
            min(s) + max(s) for s in sublists if sum(s) == numbers[invalid_idx]
        )

    def part_2(self, numbers: list[int]) -> int:
        return self.solve_2(numbers, window_size=25)

    def samples(self) -> None:
        assert self.solve_1(self.parse_input(TEST.splitlines()), 5) == 14
        assert self.solve_2(self.parse_input(TEST.splitlines()), 5) == 62


solution = Solution(2020, 9)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
