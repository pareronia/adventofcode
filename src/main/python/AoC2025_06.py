#! /usr/bin/env python3
#
# Advent of Code 2025 Day 6
#

import sys
from dataclasses import dataclass
from enum import Enum
from enum import auto
from enum import unique
from math import prod
from typing import Self

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.grid import CharGrid

TEST = "123 328  51 64 \n 45 64  387 23 \n  6 98  215 314\n*   +   *   +  "


@unique
class Mode(Enum):
    BY_ROWS = auto()
    BY_COLUMNS = auto()


@unique
class Operation(Enum):
    ADD = auto()
    MULTIPLY = auto()

    @classmethod
    def from_string(cls, s: str) -> Self:
        match s:
            case "+":
                return Operation.ADD
            case "*":
                return Operation.MULTIPLY
            case _:
                raise ValueError


@dataclass(frozen=True)
class Problem:
    nums: list[int]
    operation: Operation


@dataclass(frozen=True)
class Worksheet:
    strings: list[str]

    def get_problems(self, mode: Mode) -> list[Problem]:
        grid = CharGrid.from_strings([s + " " for s in self.strings[:-1]])
        ops = [
            Operation.from_string(s) for s in self.strings[-1].strip().split()
        ]
        problems, ns, j = list[Problem](), list[str](), 0
        for col in grid.get_cols_as_strings():
            if col.strip() == "":
                match mode:
                    case Mode.BY_ROWS:
                        rows = CharGrid.from_strings(ns).get_cols_as_strings()
                    case Mode.BY_COLUMNS:
                        rows = CharGrid.from_strings(ns).get_rows_as_strings()
                problems.append(
                    Problem([int(row.strip()) for row in rows], ops[j])
                )
                ns = []
                j += 1
            else:
                ns.append(col)
        return problems


Input = Worksheet
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return Worksheet(list(input_data))

    def solve(self, worksheet: Worksheet, mode: Mode) -> int:
        def solve_problem(problem: Problem) -> int:
            match problem.operation:
                case Operation.ADD:
                    return sum(problem.nums)
                case Operation.MULTIPLY:
                    return prod(problem.nums)

        return sum(solve_problem(p) for p in worksheet.get_problems(mode))

    def part_1(self, worksheet: Input) -> Output1:
        return self.solve(worksheet, Mode.BY_ROWS)

    def part_2(self, worksheet: Input) -> Output2:
        return self.solve(worksheet, Mode.BY_COLUMNS)

    @aoc_samples(
        (
            ("part_1", TEST, 4277556),
            ("part_2", TEST, 3263827),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2025, 6)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
