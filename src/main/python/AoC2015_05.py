#! /usr/bin/env python3
#
# Advent of Code 2015 Day 5
#

import re
import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = list[str]
Output1 = int
Output2 = int


TEST1 = "ugknbfddgicrmopn"
TEST2 = "aaa"
TEST3 = "jchzalrnumimnmhp"
TEST4 = "haegwjzuvuyypxyu"
TEST5 = "dvszwmarrgswjxmb"
TEST6 = "qjhvhtzxzqqjkmpb"
TEST7 = "xxyxx"
TEST8 = "uurcxstgmygtbstg"
TEST9 = "ieodomkazucvgmuy"
TEST10 = "xyxy"


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return list(input_data)

    def count_matches(self, regexp: str, string: str) -> int:
        return len(re.findall(regexp, string))

    def part_1(self, inputs: Input) -> Output1:
        return sum(
            1
            for line in inputs
            if self.count_matches(r"(a|e|i|o|u)", line) >= 3
            and self.count_matches(r"([a-z])\1", line) >= 1
            and self.count_matches(r"(ab|cd|pq|xy)", line) == 0
        )

    def part_2(self, inputs: Input) -> Output2:
        return sum(
            1
            for line in inputs
            if self.count_matches(r"([a-z]{2})[a-z]*\1", line) >= 1
            and self.count_matches(r"([a-z])[a-z]\1", line) >= 1
        )

    @aoc_samples(
        (
            ("part_1", TEST1, 1),
            ("part_1", TEST2, 1),
            ("part_1", TEST3, 0),
            ("part_1", TEST4, 0),
            ("part_1", TEST5, 0),
            ("part_2", TEST6, 1),
            ("part_2", TEST7, 1),
            ("part_2", TEST8, 0),
            ("part_2", TEST9, 0),
            ("part_2", TEST10, 1),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2015, 5)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
