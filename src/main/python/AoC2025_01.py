#! /usr/bin/env python3
#
# Advent of Code 2025 Day 1
#

import sys
from enum import Enum
from enum import auto
from enum import unique

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST = """\
L68
L30
R48
L5
R60
L55
L1
L99
R14
L82
"""

START = 50
TOTAL = 100


@unique
class Count(Enum):
    LANDED_ON_ZERO = auto()
    PASSED_BY_ZERO = auto()

    def count(self, dial: int, rotation: int) -> int:
        match self:
            case Count.LANDED_ON_ZERO:
                return 1 if (dial + rotation) % TOTAL == 0 else 0
            case Count.PASSED_BY_ZERO:
                if rotation >= 0:
                    return (dial + rotation) // TOTAL
                return ((TOTAL - dial) % TOTAL - rotation) // TOTAL


Input = list[int]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [
            (1 if line[0] == "R" else -1) * int(line[1:])
            for line in input_data
        ]

    def solve(self, rotations: Input, count: Count) -> int:
        dial = START
        ans = 0
        for rotation in rotations:
            ans += count.count(dial, rotation)
            dial = (dial + rotation) % TOTAL
        return ans

    def part_1(self, rotations: Input) -> Output1:
        return self.solve(rotations, Count.LANDED_ON_ZERO)

    def part_2(self, rotations: Input) -> Output2:
        return self.solve(rotations, Count.PASSED_BY_ZERO)

    @aoc_samples(
        (
            ("part_1", TEST, 3),
            ("part_2", TEST, 6),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2025, 1)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
