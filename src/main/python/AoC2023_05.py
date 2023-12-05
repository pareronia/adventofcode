#! /usr/bin/env python3
#
# Advent of Code 2023 Day 5
#

from __future__ import annotations

import sys
from typing import Iterable
from typing import NamedTuple

from aoc import my_aocd
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log

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


class Range(NamedTuple):
    range: Iterable[int]
    diff: int


class Map(NamedTuple):
    ranges: list[Range]

    def map(self, n: int) -> int:
        for r in self.ranges:
            if n in r.range:
                return n + r.diff
        return n


class Almanac(NamedTuple):
    seeds: list[int]
    soil: Map
    fertilizer: Map
    water: Map
    light: Map
    temp: Map
    humidity: Map
    location: Map

    @classmethod
    def from_input(cls, input_data: InputData) -> Almanac:
        def get_range(s: str) -> Range:
            dest, src, size = map(int, s.split())
            return Range(range(src, src + size), dest - src)

        blocks = my_aocd.to_blocks(input_data)
        seeds = list(map(int, blocks[0][0].split(": ")[1].split()))
        soil = Map([get_range(line) for line in blocks[1][1:]])
        fertilizer = Map([get_range(line) for line in blocks[2][1:]])
        water = Map([get_range(line) for line in blocks[3][1:]])
        light = Map([get_range(line) for line in blocks[4][1:]])
        temp = Map([get_range(line) for line in blocks[5][1:]])
        humidity = Map([get_range(line) for line in blocks[6][1:]])
        location = Map([get_range(line) for line in blocks[7][1:]])
        return Almanac(
            seeds, soil, fertilizer, water, light, temp, humidity, location
        )

    def get_locations(self) -> list[int]:
        ans = []
        for seed in self.seeds:
            soil = self.soil.map(seed)
            fertilizer = self.fertilizer.map(soil)
            water = self.water.map(fertilizer)
            light = self.light.map(water)
            temp = self.temp.map(light)
            humidity = self.humidity.map(temp)
            location = self.location.map(humidity)
            log(
                f"{seed=}, {soil=}, {fertilizer=}, {water=}, {light=}, "
                + f"{temp=}, {humidity=}, {location=}"
            )
            ans.append(location)
        return ans


Input = Almanac
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return Almanac.from_input(input_data)

    def part_1(self, almanac: Input) -> Output1:
        log(almanac)
        return min(almanac.get_locations())

    def part_2(self, almanac: Input) -> Output2:
        return 0

    @aoc_samples(
        (
            ("part_1", TEST, 35),
            # ("part_2", TEST, "TODO"),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 5)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
