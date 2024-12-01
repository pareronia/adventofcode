#! /usr/bin/env python3
#
# Advent of Code 2017 Day 15
#

from __future__ import annotations

import sys
from typing import Callable
from typing import NamedTuple

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

FACTOR_A = 16807
FACTOR_B = 48271
MOD = 2147483647

TEST = """\
Generator A starts with 65
Generator B starts with 8921
"""


class Generator:
    def __init__(
        self, seed: int, factor: int, condition: Callable[[int], bool]
    ) -> None:
        self.prev = seed
        self.factor = factor
        self.condition = condition

    def __iter__(self) -> Generator:
        return self

    def __next__(self) -> int:
        val = self.prev
        while True:
            val = (val * self.factor) % MOD
            if self.condition(val):
                self.prev = val
                return val


class Generators(NamedTuple):
    a: int
    b: int

    @classmethod
    def from_input(cls, input: list[str]) -> Generators:
        return Generators(*(int(input[i].split()[-1]) for i in (0, 1)))

    def generator_a(self, condition: Callable[[int], bool]) -> Generator:
        return Generator(self.a, FACTOR_A, condition)

    def generator_b(self, condition: Callable[[int], bool]) -> Generator:
        return Generator(self.b, FACTOR_B, condition)


Input = Generators
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return Generators.from_input(list(input_data))

    def solve(
        self,
        generators: Generators,
        reps: int,
        condition_a: Callable[[int], bool],
        condition_b: Callable[[int], bool],
    ) -> int:
        generator_a = generators.generator_a(condition_a)
        generator_b = generators.generator_b(condition_b)
        return sum(
            next(generator_a) & 0xFFFF == next(generator_b) & 0xFFFF
            for _ in range(reps)
        )

    def part_1(self, generators: Generators) -> int:
        return self.solve(
            generators, 40_000_000, lambda x: True, lambda x: True
        )

    def part_2(self, generators: Generators) -> int:
        return self.solve(
            generators, 5_000_000, lambda x: x % 4 == 0, lambda x: x % 8 == 0
        )

    @aoc_samples(
        (
            ("part_1", TEST, 588),
            ("part_2", TEST, 309),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2017, 15)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
