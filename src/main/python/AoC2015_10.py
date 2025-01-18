#! /usr/bin/env python3
#
# Advent of Code 2015 Day 10
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import spinner

Input = str
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return list(input_data)[0]

    def look_and_say(self, string: str) -> str:
        result = ""
        i = 0
        while i < len(string):
            digit = string[i]
            j = 0
            while i + j < len(string) and string[i + j] == digit:
                j += 1
            result += str(j) + digit
            i += j
        return result

    def solve(self, string: str, iterations: int) -> str:
        for i in range(iterations):
            string = self.look_and_say(string)
            spinner(i, iterations // 4)
        return string

    def part_1(self, input: Input) -> Output1:
        return len(self.solve(input, iterations=40))

    def part_2(self, input: Input) -> Output2:
        return len(self.solve(input, iterations=50))

    def samples(self) -> None:
        assert self.solve("1", iterations=5) == "312211"


solution = Solution(2015, 10)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
