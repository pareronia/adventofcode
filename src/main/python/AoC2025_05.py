#! /usr/bin/env python3
#
# Advent of Code 2025 Day 5
#

import sys
from dataclasses import dataclass
from typing import Self

from aoc import my_aocd
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.range import RangeInclusive

TEST = """\
3-5
10-14
16-20
12-18

1
5
8
11
17
32
"""


@dataclass(frozen=True)
class Database:
    id_ranges: set[RangeInclusive]
    available_ids: list[int]

    @classmethod
    def from_input(cls, input_data: InputData) -> Self:
        blocks = my_aocd.to_blocks(input_data)
        ranges = set[RangeInclusive]()
        for line in blocks[0]:
            lo, hi = map(int, line.split("-"))
            ranges.add(RangeInclusive.between(lo, hi))
        pids = [int(line) for line in blocks[1]]
        return cls(ranges, pids)


Input = Database
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return Database.from_input(input_data)

    def part_1(self, database: Input) -> Output1:
        return sum(
            any(rng.contains(pid) for rng in database.id_ranges)
            for pid in database.available_ids
        )

    def part_2(self, database: Input) -> Output2:
        return sum(rng.len for rng in RangeInclusive.merge(database.id_ranges))

    @aoc_samples(
        (
            ("part_1", TEST, 3),
            ("part_2", TEST, 14),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2025, 5)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
