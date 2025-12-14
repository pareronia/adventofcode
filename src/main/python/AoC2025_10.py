#! /usr/bin/env python3
#
# Advent of Code 2025 Day 10
#

import itertools
import sys
from collections.abc import Iterable
from dataclasses import dataclass
from functools import cache
from typing import Self

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST = """\
[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}
[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}
[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}
"""


def parity(nums: Iterable[int]) -> tuple[int, ...]:
    return tuple(n % 2 for n in nums)


@dataclass
class Machine:
    lights: tuple[int, ...]
    joltages: tuple[int, ...]
    patterns: dict[tuple[int, ...], dict[tuple[int, ...], int]]

    @classmethod
    def from_input(cls, line: str) -> Self:
        ll, *pp, jj = line.split()
        lights = tuple(1 if ch == "#" else 0 for ch in ll[1:-1])
        presses = tuple(tuple(int(x) for x in p[1:-1].split(",")) for p in pp)
        joltages = tuple(int(j) for j in jj[1:-1].split(","))
        return cls(
            lights, joltages, cls.create_patterns(lights, joltages, presses)
        )

    @classmethod
    def create_patterns(
        cls,
        lights: tuple[int, ...],
        joltages: tuple[int, ...],
        presses: tuple[tuple[int, ...], ...],
    ) -> dict[tuple[int, ...], dict[tuple[int, ...], int]]:
        num_buttons = len(presses)
        num_variables = len(joltages)
        assert num_variables == len(lights)
        patterns: dict[tuple[int, ...], dict[tuple[int, ...], int]] = {
            parity_pattern: {}
            for parity_pattern in itertools.product(
                range(2), repeat=num_variables
            )
        }
        for n in range(num_buttons + 1):
            for combo in itertools.combinations(range(num_buttons), n):
                pattern = [0] * num_variables
                for idx in combo:
                    for p in presses[idx]:
                        pattern[p] += 1
                key = tuple(pattern)
                parity_pattern = parity(key)
                if key not in patterns[parity_pattern]:
                    patterns[parity_pattern][key] = n
        return patterns

    def button_presses_for_lights(self) -> int:
        return min(self.patterns[self.lights].values())

    def button_presses_for_joltages(self) -> int:
        @cache
        def dfs(joltages: tuple[int, ...]) -> int:
            if all(j == 0 for j in joltages):
                return 0
            return min(
                (
                    dfs(
                        tuple(
                            (j - p) // 2
                            for p, j in zip(pattern, joltages, strict=True)
                        )
                    )
                    * 2
                    + n
                    for pattern, n in self.patterns[parity(joltages)].items()
                    if all(
                        p <= j for p, j in zip(pattern, joltages, strict=True)
                    )
                ),
                default=sys.maxsize,
            )

        return dfs(self.joltages)


Input = tuple[Machine, ...]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return tuple(Machine.from_input(line) for line in input_data)

    def part_1(self, machines: Input) -> Output1:
        return sum(machine.button_presses_for_lights() for machine in machines)

    def part_2(self, machines: Input) -> Output2:
        return sum(
            machine.button_presses_for_joltages() for machine in machines
        )

    @aoc_samples(
        (
            ("part_1", TEST, 7),
            ("part_2", TEST, 33),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2025, 10)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
