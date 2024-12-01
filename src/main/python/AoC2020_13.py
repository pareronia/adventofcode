#! /usr/bin/env python3
#
# Advent of Code 2020 Day 13
#

import itertools
import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST1 = """\
939
7,13,x,x,59,x,31,19
"""
TEST2 = """\
0
17,x,13,19
"""
TEST3 = """\
0
67,7,59,61
"""
TEST4 = """\
0
67,x,7,59,61
"""
TEST5 = """\
0
67,7,x,59,61
"""
TEST6 = """\
0
1789,37,47,1889
"""

Input = tuple[int, list[tuple[int, int]]]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        inputs = list(input_data)
        buses = [
            (int(period), offset)
            for offset, period in enumerate(inputs[1].split(","))
            if period != "x"
        ]
        return int(inputs[0]), buses

    def part_1(self, input: Input) -> int:
        target, buses = input
        return next(
            period * cnt
            for cnt in itertools.count(0)
            for period, _ in buses
            if (target + cnt) % period == 0
        )

    def part_2(self, input: Input) -> int:
        _, buses = input
        r = 0
        lcm = buses[0][0]
        for period, offset in buses[1:]:
            while (r + offset) % period != 0:
                r += lcm
            lcm *= period
        return r

    @aoc_samples(
        (
            ("part_1", TEST1, 295),
            ("part_2", TEST1, 1068781),
            ("part_2", TEST2, 3417),
            ("part_2", TEST3, 754018),
            ("part_2", TEST4, 779210),
            ("part_2", TEST5, 1261476),
            ("part_2", TEST6, 1202161486),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2020, 13)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
