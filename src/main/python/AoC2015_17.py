#! /usr/bin/env python3
#
# Advent of Code 2015 Day 17
#

import itertools
import sys

from aoc.common import InputData
from aoc.common import SolutionBase

TEST = """\
20
15
10
5
5
"""


Input = list[int]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [int(line) for line in input_data]

    def get_cocos(
        self, containers: list[int], eggnog_volume: int
    ) -> list[tuple[int, ...]]:
        containers.sort(reverse=True)
        minimal_containers = list[int]()
        for c in containers:
            if sum(minimal_containers) + c > eggnog_volume:
                minimal_containers.append(c)
        return [
            c
            for i in range(len(minimal_containers), len(containers) + 1)
            for c in itertools.combinations(containers, i)
            if sum(c) == eggnog_volume
        ]

    def solve_1(self, containers: list[int], eggnog_volume: int) -> int:
        return len(self.get_cocos(containers, eggnog_volume))

    def part_1(self, containers: Input) -> Output1:
        return self.solve_1(containers, 150)

    def solve_2(self, containers: list[int], eggnog_volume: int) -> int:
        cocos = self.get_cocos(containers, eggnog_volume)
        min_cont = min(len(coco) for coco in cocos)
        return sum(1 for coco in cocos if len(coco) == min_cont)

    def part_2(self, containers: Input) -> Output2:
        return self.solve_2(containers, 150)

    def samples(self) -> None:
        assert self.solve_1(self.parse_input(TEST.splitlines()), 25) == 4
        assert self.solve_2(self.parse_input(TEST.splitlines()), 25) == 3


solution = Solution(2015, 17)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
