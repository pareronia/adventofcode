#! /usr/bin/env python3
#
# Advent of Code 2025 Day 2
#

import re
import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = tuple[tuple[int, int], ...]
Output1 = int
Output2 = int


TEST = (
    "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,"
    "1698522-1698528,446443-446449,38593856-38593862,565653-565659,"
    "824824821-824824827,2121212118-2121212124"
)


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        ans = list[tuple[int, int]]()
        for rng in next(iter(input_data)).split(","):
            lo, hi = map(int, rng.split("-"))
            ans.append((lo, hi))
        return tuple(ans)

    def solve(self, inputs: Input, pattern: str) -> int:
        regex = re.compile(pattern)
        ans = 0
        for lo, hi in inputs:
            for n in range(lo, hi + 1):
                if regex.match(str(n)):
                    ans += n
        return ans

    def part_1(self, inputs: Input) -> Output1:
        return self.solve(inputs, r"^(\d+)\1$")

    def part_2(self, inputs: Input) -> Output2:
        return self.solve(inputs, r"^(\d+)\1+$")

    @aoc_samples(
        (
            ("part_1", TEST, 1227775554),
            ("part_2", TEST, 4174379265),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2025, 2)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
