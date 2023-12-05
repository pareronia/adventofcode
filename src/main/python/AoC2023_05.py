#! /usr/bin/env python3
#
# Advent of Code 2023 Day 5
#

from __future__ import annotations

import sys
from typing import NamedTuple

import aoc.range
from aoc import my_aocd
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST = """\
seeds: 79 14 55 13

seed-to-soil map:
50 98 2
52 50 48

soil-to-fertilizer map:
0 15 37
37 52 2
39 0 15

fertilizer-to-water map:
49 53 8
0 11 42
42 0 7
57 7 4

water-to-light map:
88 18 7
18 25 70

light-to-temperature map:
45 77 23
81 45 19
68 64 13

temperature-to-humidity map:
0 69 1
1 0 69

humidity-to-location map:
60 56 37
56 93 4
"""

Range = tuple[int, int]


class MappingParams(NamedTuple):
    range: Range
    diff: int


class Mapping(NamedTuple):
    ranges: list[MappingParams]

    def map(self, inps: list[Range]) -> list[Range]:
        ans = list[Range]()
        while inps:
            inp = inps.pop()
            for r in self.ranges:
                new, other = aoc.range.Range.left_join(inp, r.range)
                if new:
                    ans.append((new[0] + r.diff, new[1] + r.diff))
                    for o in other:
                        inps.append(o)
                    break
            else:
                ans.append(inp)
        return ans


class Almanac(NamedTuple):
    seeds: list[int]
    mappings: list[Mapping]

    @classmethod
    def from_input(cls, input_data: InputData) -> Almanac:
        def get_range(s: str) -> MappingParams:
            dest, src, size = map(int, s.split())
            return MappingParams((src, src + size), dest - src)

        blocks = my_aocd.to_blocks(input_data)
        seeds = list(map(int, blocks[0][0].split(": ")[1].split()))
        mappings = [
            Mapping([get_range(line) for line in blocks[i][1:]])
            for i in range(1, len(blocks))
        ]
        return Almanac(seeds, mappings)

    def get_location(self, seed_ranges: list[Range]) -> list[Range]:
        ans = seed_ranges
        for m in self.mappings:
            ans = m.map(ans)
        return ans


Input = Almanac
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return Almanac.from_input(input_data)

    def _solve(self, almanac: Input, seed_ranges: list[Range]) -> int:
        location = almanac.get_location(seed_ranges)
        return sorted(location)[0][0]

    def part_1(self, almanac: Input) -> Output1:
        seed_ranges = [(seed, seed + 1) for seed in almanac.seeds]
        return self._solve(almanac, seed_ranges)

    def part_2(self, almanac: Input) -> Output2:
        seed_ranges = [
            (almanac.seeds[i], almanac.seeds[i] + almanac.seeds[i + 1])
            for i in range(0, len(almanac.seeds), 2)
        ]
        return self._solve(almanac, seed_ranges)

    @aoc_samples(
        (
            ("part_1", TEST, 35),
            ("part_2", TEST, 46),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 5)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
