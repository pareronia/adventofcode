#! /usr/bin/env python3
#
# Advent of Code 2023 Day 6
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log

Input = list[tuple[int, int]]
Output1 = int
Output2 = int


TEST = """\
Time:      7  15   30
Distance:  9  40  200
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        lines = [_ for _ in input_data]
        times = list(
            map(
                int,
                (
                    x
                    for x in lines[0].split(":")[1].strip().split(" ")
                    if len(x) > 0
                ),
            )
        )
        distances = list(
            map(
                int,
                (
                    x
                    for x in lines[1].split(":")[1].strip().split(" ")
                    if len(x) > 0
                ),
            )
        )
        return list(zip(times, distances))

    def part_1(self, races: Input) -> Output1:
        log(races)
        ans = 1
        for time, distance in races:
            win = 0
            for t in range(time + 1):
                d = (time - t) * t
                if d > distance:
                    win += 1
            ans *= win
        return ans

    def part_2(self, races: Input) -> Output2:
        return 0

    @aoc_samples(
        (
            ("part_1", TEST, 288),
            # ("part_2", TEST, "TODO"),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 6)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
