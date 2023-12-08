#! /usr/bin/env python3
#
# Advent of Code 2023 Day 8
#

import sys
from typing import NamedTuple
import itertools

from aoc import my_aocd
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
# from aoc.common import log


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

    def part_1(self, map: Map) -> Output1:
        inss = itertools.cycle(map.instructions)
        node = map.network['AAA']
        ans = 1
        while True:
            ins = next(inss)
            key = node[0 if ins == 'L' else 1]
            if key == 'ZZZ':
                break
            node = map.network[key]
            ans += 1
        return ans

    def part_2(self, input: Map) -> Output2:
        return 0

    @aoc_samples(
        (
            ("part_1", TEST1, 2),
            ("part_1", TEST2, 6),
            # ("part_2", TEST, "TODO"),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 8)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
