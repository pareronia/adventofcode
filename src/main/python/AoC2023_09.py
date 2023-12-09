#! /usr/bin/env python3
#
# Advent of Code 2023 Day 9
#

import sys
from collections import deque

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log

Input = list[list[int]]
Output1 = int
Output2 = int


TEST = """\
0 3 6 9 12 15
1 3 6 10 15 21
10 13 16 21 30 45
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [[int(_) for _ in line.split()] for line in input_data]

    def part_1(self, input: Input) -> Output1:
        ans = 0
        for line in input:
            log(line)
            lasts = deque[int]()
            lasts.append(line[-1])
            while not all(_ == lasts[-1] for _ in line):
                line = [line[i] - line[i - 1] for i in range(1, len(line), 1)]
                lasts.append(line[-1])
            log(line)
            log(lasts)
            tmp = lasts.pop()
            while len(lasts) > 0:
                tmp += lasts.pop()
            log(tmp)
            ans += tmp
        return ans

    def part_2(self, input: Input) -> Output2:
        return 0

    @aoc_samples(
        (
            ("part_1", TEST, 114),
            # ("part_2", TEST, "TODO"),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 9)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
