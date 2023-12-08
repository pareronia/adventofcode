#! /usr/bin/env python3
#
# Advent of Code 2023 Day 8
#

import itertools
import sys
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


Input = Map
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Map:
        blocks = my_aocd.to_blocks(input_data)
        instructions = blocks[0][0]
        network = dict[str, tuple[str, str]]()
        for line in blocks[1]:
            key, vals = line.split(" = ")
            network[key] = (vals[1:4], vals[6:9])
        map = Map(instructions, network)
        return map

    def _steps(self, map: Map, start_key: str) -> int:
        inss = itertools.cycle(map.instructions)
        node = map.network[start_key]
        ans = 1
        while True:
            ins = next(inss)
            key = node[0 if ins == "L" else 1]
            if key[-1] == "Z":
                break
            node = map.network[key]
            ans += 1
        return ans

    def part_1(self, map: Map) -> Output1:
        return self._steps(map, "AAA")

    def part_2(self, map: Map) -> Output2:
        keys = [k for k in map.network if k[-1] == "A"]
        steps = [self._steps(map, key) for key in keys]
        ans = steps[0]
        for s in steps[1:]:
            ans = lcm(ans, s)
        return ans

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
