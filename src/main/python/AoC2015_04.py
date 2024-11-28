#! /usr/bin/env python3
#
# Advent of Code 2015 Day 4
#

import sys
from hashlib import md5

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import spinner

Input = str
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return list(input_data)[0]

    def find_md5_starting_with_zeroes(self, seed: str, zeroes: int) -> int:
        i = 0
        val = seed
        target = "0" * zeroes
        while val[:zeroes] != target:
            i += 1
            spinner(i)
            str2hash = seed + str(i)
            val = md5(str2hash.encode()).hexdigest()  # nosec
        return i

    def part_1(self, seed: Input) -> Output1:
        return self.find_md5_starting_with_zeroes(seed, 5)

    def part_2(self, seed: Input) -> Output2:
        return self.find_md5_starting_with_zeroes(seed, 6)

    @aoc_samples(
        (
            ("part_1", "abcdef", 609043),
            ("part_1", "pqrstuv", 1048970),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2015, 4)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
