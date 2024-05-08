#! /usr/bin/env python3
#
# Advent of Code 2016 Day 9
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST1 = "ADVENT"
TEST2 = "A(1x5)BC"
TEST3 = "(3x3)XYZ"
TEST4 = "A(2x2)BCD(2x2)EFG"
TEST5 = "(6x1)(1x3)A"
TEST6 = "X(8x2)(3x3)ABCY"
TEST7 = "(27x12)(20x12)(13x14)(7x10)(1x12)A"
TEST8 = "(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN"


Input = str
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return list(input_data)[0]

    def decompressed_length(self, input: str, recursive: bool) -> int:
        if "(" not in input:
            return len(input)

        cnt = 0
        in_marker = False
        marker = ""
        i = 0
        while i < len(input):
            c = input[i]
            if c == "(":
                in_marker = True
            elif c == ")":
                splits = marker.split("x")
                skip = int(splits[0])
                repeat = int(splits[1])
                if recursive:
                    skipped = input[i + 1 : i + 1 + skip]  # noqa E203
                    cnt += repeat * self.decompressed_length(skipped, True)
                else:
                    cnt += repeat * skip
                i += skip
                marker = ""
                in_marker = False
            else:
                if in_marker:
                    marker += c
                else:
                    cnt += 1
            i += 1
        return cnt

    def part_1(self, input: str) -> int:
        return self.decompressed_length(input, False)

    def part_2(self, input: str) -> int:
        return self.decompressed_length(input, True)

    @aoc_samples(
        (
            ("part_1", TEST1, 6),
            ("part_1", TEST2, 7),
            ("part_1", TEST3, 9),
            ("part_1", TEST4, 11),
            ("part_1", TEST5, 6),
            ("part_1", TEST6, 18),
            ("part_2", TEST3, 9),
            ("part_2", TEST6, 20),
            ("part_2", TEST7, 241920),
            ("part_2", TEST8, 445),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2016, 9)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
