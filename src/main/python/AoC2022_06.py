#! /usr/bin/env python3
#
# Advent of Code 2022 Day 6
#


import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST1 = "mjqjpqmgbljsphdztnvjfqwrcgsmlb"
TEST2 = "bvwbjplbgvbhsrlpgdmjqwftvncz"
TEST3 = "nppdvjthqldpwncqszvftbrmjlhg"
TEST4 = "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"
TEST5 = "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"


Input = str
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return next(iter(input_data))

    def solve(self, buffer: str, size: int) -> int:
        for i in range(size, len(buffer)):
            test = buffer[i - size : i]
            if len(set(test)) == size:
                return i
        raise AssertionError

    def part_1(self, buffer: Input) -> Output1:
        return self.solve(buffer, 4)

    def part_2(self, buffer: Input) -> Output2:
        return self.solve(buffer, 14)

    @aoc_samples(
        (
            ("part_1", TEST1, 7),
            ("part_1", TEST2, 5),
            ("part_1", TEST3, 6),
            ("part_1", TEST4, 10),
            ("part_1", TEST5, 11),
            ("part_2", TEST1, 19),
            ("part_2", TEST2, 23),
            ("part_2", TEST3, 23),
            ("part_2", TEST4, 29),
            ("part_2", TEST5, 26),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2022, 6)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
