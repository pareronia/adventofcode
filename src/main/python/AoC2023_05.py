#! /usr/bin/env python3
#
# Advent of Code 2023 Day 5
#

from __future__ import annotations

import sys
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
    range: tuple[int, int]
    diff: int


class Map(NamedTuple):
    ranges: list[Range]

    def map(self, inp: list[tuple[int, int]]) -> list[tuple[int, int]]:
        ans = list[tuple[int, int]]()
        while inp:
            i = inp.pop()
            for r in self.ranges:
                new, other = intervals(i, r.range, r.diff)
                if new:
                    ans.append(new)
                    for o in other:
                        inp.append(o)
                    break
            else:
                ans.append(i)
        return ans


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
            return Range((src, src + size), dest - src)

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

    def get_location(
        self, seed: list[tuple[int, int]]
    ) -> list[tuple[int, int]]:
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
        return location


Input = Almanac
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return Almanac.from_input(input_data)

    def part_1(self, almanac: Input) -> Output1:
        s = []
        for seed in almanac.seeds:
            s.append((seed, seed + 1))
        location = almanac.get_location(s)
        return sorted(location)[0][0]

    def part_2(self, almanac: Input) -> Output2:
        s = []
        for i in range(0, len(almanac.seeds), 2):
            s.append(
                (almanac.seeds[i], almanac.seeds[i] + almanac.seeds[i + 1])
            )
        location = almanac.get_location(s)
        return sorted(location)[0][0]

    @aoc_samples(
        (
            ("part_1", TEST, 35),
            ("part_2", TEST, 46),
        )
    )
    def samples(self) -> None:
        pass


def intervals(
    i1: tuple[int, int], i2: tuple[int, int], diff: int
) -> tuple[tuple[int, int] | None, set[tuple[int, int]]]:
    ans = None
    others = set()
    o0 = max(i1[0], i2[0])
    o1 = min(i1[1], i2[1])
    if o0 < o1:
        ans = (o0 + diff, o1 + diff)
        if o0 > i1[0]:
            others.add((i1[0], o0))
        if o1 < i1[1]:
            others.add((o1, i1[1]))
    else:
        others.add(i1)
    return ans, others


solution = Solution(2023, 5)


def main() -> None:
    assert intervals((0, 5), (5, 10), 100) == (None, {(0, 5)})
    assert intervals((0, 5), (3, 10), 100) == ((103, 105), {(0, 3)})
    assert intervals((3, 6), (4, 10), 100) == ((104, 106), {(3, 4)})
    assert intervals((5, 7), (3, 10), 100) == ((105, 107), set())
    assert intervals((3, 10), (5, 7), 100) == (
        (105, 107),
        {(3, 5), (7, 10)},
    )
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
