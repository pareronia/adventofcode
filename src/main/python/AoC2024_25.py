#! /usr/bin/env python3
#
# Advent of Code 2024 Day 25
#

import sys

from aoc import my_aocd
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Heights = tuple[int, ...]
Input = tuple[set[Heights], set[Heights]]
Output1 = int
Output2 = str


TEST = """\
#####
.####
.####
.####
.#.#.
.#...
.....

#####
##.##
.#.##
...##
...#.
...#.
.....

.....
#....
#....
#...#
#.#.#
#.###
#####

.....
.....
#.#..
###..
###.#
###.#
#####

.....
.....
.....
#....
#.#..
#.#.#
#####
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        keys, locks = set(), set()
        blocks = my_aocd.to_blocks(input_data)
        for block in blocks:
            rows = ["".join(x) for x in zip(*block, strict=True)]
            nums: Heights = tuple(
                sum(1 for ch in row if ch == "#") - 1 for row in rows
            )
            if rows[0][0] == "#":
                locks.add(nums)
            else:
                keys.add(nums)
        return keys, locks

    def part_1(self, schematic: Input) -> Output1:
        keys, locks = schematic
        return sum(
            all(a + b <= 5 for a, b in zip(lock, key, strict=True))
            for lock in locks
            for key in keys
        )

    def part_2(self, _schematic: Input) -> Output2:
        return "🎄"

    @aoc_samples((("part_1", TEST, 3),))
    def samples(self) -> None:
        pass


solution = Solution(2024, 25)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
