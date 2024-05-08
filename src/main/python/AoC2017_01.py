#! /usr/bin/env python3
#
# Advent of Code 2017 Day 1
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = str
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return list(input_data)[0]

    def sum_same_chars_at(self, string: str, distance: int) -> int:
        test = string + string[:distance]
        return sum(
            int(test[i])
            for i in range(len(string))
            if test[i] == test[i + distance]
        )

    def part_1(self, input: str) -> int:
        return self.sum_same_chars_at(input, 1)

    def part_2(self, input: str) -> int:
        return self.sum_same_chars_at(input, len(input) // 2)

    @aoc_samples(
        (
            ("part_1", "1122", 3),
            ("part_1", "1111", 4),
            ("part_1", "1234", 0),
            ("part_1", "91212129", 9),
            ("part_2", "1212", 6),
            ("part_2", "1221", 0),
            ("part_2", "123425", 4),
            ("part_2", "123123", 12),
            ("part_2", "12131415", 4),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2017, 1)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
