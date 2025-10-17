#! /usr/bin/env python3
#
# Advent of Code 2023 Day 6
#

import sys
from math import ceil
from math import floor
from math import prod
from math import sqrt

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
        times, distances = [
            list(map(int, line.split(":")[1].split())) for line in input_data
        ]
        return list(zip(times, distances, strict=True))

    def _brute_force(self, time: int, distance: int) -> int:
        ans = sum(t * (time - t) > distance for t in range(time))
        log(ans)
        return ans

    def _equation(self, time: int, distance: int) -> int:
        r = sqrt(time**2 - 4 * distance)
        fx1 = (time - r) / 2
        fx2 = (time + r) / 2
        x1 = (int(fx1) + 1) if fx1.is_integer() else ceil(fx1)
        x2 = (int(fx2) - 1) if fx2.is_integer() else floor(fx2)
        return x2 - x1 + 1

    def _solve(self, races: Input) -> int:
        return prod(self._equation(time, distance) for time, distance in races)

    def part_1(self, races: Input) -> Output1:
        return self._solve(races)

    def part_2(self, races: Input) -> Output2:
        time, distance = (
            int("".join(str(race[i]) for race in races)) for i in [0, 1]
        )
        return self._solve([(time, distance)])

    @aoc_samples(
        (
            ("part_1", TEST, 288),
            ("part_2", TEST, 71503),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 6)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
