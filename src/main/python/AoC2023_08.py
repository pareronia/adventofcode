#! /usr/bin/env python3
#
# Advent of Code 2023 Day 8
#

from __future__ import annotations

import itertools
import sys
from functools import reduce
from math import lcm
from typing import NamedTuple

from aoc import my_aocd
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST1 = """\
RL

AAA = (BBB, CCC)
BBB = (DDD, EEE)
CCC = (ZZZ, GGG)
DDD = (DDD, DDD)
EEE = (EEE, EEE)
GGG = (GGG, GGG)
ZZZ = (ZZZ, ZZZ)
"""
TEST2 = """\
LLR

AAA = (BBB, BBB)
BBB = (AAA, ZZZ)
ZZZ = (ZZZ, ZZZ)
"""
TEST3 = """\
LR

11A = (11B, XXX)
11B = (XXX, 11Z)
11Z = (11B, XXX)
22A = (22B, XXX)
22B = (22C, 22C)
22C = (22Z, 22Z)
22Z = (22B, 22B)
XXX = (XXX, XXX)
"""


class Map(NamedTuple):
    instructions: str
    network: dict[str, tuple[str, str]]

    @classmethod
    def from_input(cls, input_data: InputData) -> Map:
        blocks = my_aocd.to_blocks(input_data)
        instructions = blocks[0][0]
        network = dict[str, tuple[str, str]]()
        for line in blocks[1]:
            key, vals = line.split(" = ")
            network[key] = (vals[1:4], vals[6:9])
        return cls(instructions, network)

    def steps(self, key: str) -> int:
        inss = itertools.cycle(self.instructions)
        node = self.network[key]
        ans = 0
        while key[-1] != "Z":
            key = node[0 if next(inss) == "L" else 1]
            node = self.network[key]
            ans += 1
        return ans


Input = Map
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Map:
        return Map.from_input(input_data)

    def part_1(self, the_map: Map) -> Output1:
        return the_map.steps("AAA")

    def part_2(self, the_map: Map) -> Output2:
        return reduce(
            lcm,
            (the_map.steps(key) for key in the_map.network if key[-1] == "A"),
        )

    @aoc_samples(
        (
            ("part_1", TEST1, 2),
            ("part_1", TEST2, 6),
            ("part_2", TEST3, 6),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 8)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
