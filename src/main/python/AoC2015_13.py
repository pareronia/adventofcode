#! /usr/bin/env python3
#
# Advent of Code 2015 Day 13
#

from __future__ import annotations

import itertools
import sys
from collections import defaultdict
from dataclasses import dataclass

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST = """\
Alice would gain 54 happiness units by sitting next to Bob.
Alice would lose 79 happiness units by sitting next to Carol.
Alice would lose 2 happiness units by sitting next to David.
Bob would gain 83 happiness units by sitting next to Alice.
Bob would lose 7 happiness units by sitting next to Carol.
Bob would lose 63 happiness units by sitting next to David.
Carol would lose 62 happiness units by sitting next to Alice.
Carol would gain 60 happiness units by sitting next to Bob.
Carol would gain 55 happiness units by sitting next to David.
David would gain 46 happiness units by sitting next to Alice.
David would lose 7 happiness units by sitting next to Bob.
David would gain 41 happiness units by sitting next to Carol.
"""


@dataclass
class Happiness:
    matrix: list[list[int]]

    @classmethod
    def from_input(cls, inputs: InputData) -> Happiness:
        cntr = itertools.count()
        idxs = defaultdict[str, int](lambda: next(cntr))
        values = dict[tuple[int, int], int]()
        for line in inputs:
            splits = line[:-1].split()
            key = (idxs[splits[0]], idxs[splits[10]])
            value = int(splits[3]) * (1 if splits[2] == "gain" else -1)
            values[key] = value
        matrix = [[0] * (len(idxs) + 1) for _ in range(len(idxs) + 1)]
        for k, v in values.items():
            matrix[k[0]][k[1]] = v
        return Happiness(matrix)

    def solve(self, size: int) -> int:
        return max(
            sum(
                self.matrix[d1][d2] + self.matrix[d2][d1]
                for d1, d2 in itertools.zip_longest(p, p[1:], fillvalue=p[0])
            )
            for p in itertools.permutations(range(size), size)
        )

    def get_optimal_happiness_change_without_me(self) -> int:
        return self.solve(len(self.matrix) - 1)

    def get_optimal_happiness_change_with_me(self) -> int:
        return self.solve(len(self.matrix))


Input = Happiness
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return Happiness.from_input(input_data)

    def part_1(self, happiness: Happiness) -> Output1:
        return happiness.get_optimal_happiness_change_without_me()

    def part_2(self, happiness: Happiness) -> Output2:
        return happiness.get_optimal_happiness_change_with_me()

    @aoc_samples((("part_1", TEST, 330),))
    def samples(self) -> None:
        pass


solution = Solution(2015, 13)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
