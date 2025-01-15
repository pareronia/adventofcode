#! /usr/bin/env python3
#
# Advent of Code 2015 Day 9
#

from __future__ import annotations

import itertools
import sys
from collections import defaultdict
from typing import Iterator
from typing import NamedTuple

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST = """\
London to Dublin = 464
London to Belfast = 518
Dublin to Belfast = 141
"""


class Distances(NamedTuple):
    matrix: list[list[int]]

    @classmethod
    def from_input(cls, inputs: InputData) -> Distances:
        cnt = 0

        def get_and_increment() -> int:
            nonlocal cnt
            tmp = cnt
            cnt += 1
            return tmp

        idxs = defaultdict[str, int](get_and_increment)
        values = dict[tuple[int, int], int]()
        for line in inputs:
            splits = line.split(" ")
            values[(idxs[splits[0]], idxs[splits[2]])] = int(splits[4])
        matrix = [[0] * (len(idxs)) for _ in range(len(idxs))]
        for k, v in values.items():
            matrix[k[0]][k[1]] = v
            matrix[k[1]][k[0]] = v
        return Distances(matrix)

    def get_distances_of_complete_routes(self) -> Iterator[int]:
        size = len(self.matrix)
        for p in itertools.permutations(range(size), size):
            yield sum(self.matrix[p[i - 1]][p[i]] for i in range(1, size))


Input = Distances
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return Distances.from_input(input_data)

    def part_1(self, distances: Input) -> Output1:
        return min(distances.get_distances_of_complete_routes())

    def part_2(self, distances: Input) -> Output2:
        return max(distances.get_distances_of_complete_routes())

    @aoc_samples(
        (
            ("part_1", TEST, 605),
            ("part_2", TEST, 982),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2015, 9)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
