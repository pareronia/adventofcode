#! /usr/bin/env python3
#
# Advent of Code 2024 Day 7
#

from __future__ import annotations

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = InputData
Output1 = int
Output2 = int


TEST = """\
190: 10 19
3267: 81 40 27
83: 17 5
156: 15 6
7290: 6 8 6 15
161011: 16 10 13
192: 17 8 14
21037: 9 7 18 13
292: 11 6 16 20
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def part_1(self, input: Input) -> Output1:
        ans = 0
        for line in input:
            sol, right = line.split(": ")
            terms = list(right.split())
            eqs = [terms[0]]
            for i in range(1, len(terms)):
                neqs = []
                for eq in eqs:
                    neqs.append("(" + eq + " + " + terms[i] + ")")
                    neqs.append("(" + eq + " * " + terms[i] + ")")
                eqs = neqs
            for eq in eqs:
                if eval(eq) == int(sol):  # nosec
                    ans += int(sol)
                    break
        return ans

    def part_2(self, input: Input) -> Output2:
        class Term:
            def __init__(self, val: int):
                self.val = val

            def __add__(self, other: Term) -> int:
                return self.val + other.val

            def __mul__(self, other: Term) -> int:
                return self.val * other.val

            def __or__(self, other: Term) -> int:
                return self.val * int(10 ** len(str(other.val))) + other.val

        ans = 0
        for line in input:
            sol, right = line.split(": ")
            terms = list(map(int, right.split()))
            eqs = [terms[0]]
            for i in range(1, len(terms)):
                neqs = []
                for eq in eqs:
                    if eq > int(sol):
                        continue
                    neqs.append(Term(eq) + Term(terms[i]))
                    neqs.append(Term(eq) * Term(terms[i]))
                    neqs.append(Term(eq) | Term(terms[i]))
                eqs = neqs
            for eq in eqs:
                if eq == int(sol):
                    ans += int(sol)
                    break
        return ans

    @aoc_samples(
        (
            ("part_1", TEST, 3749),
            ("part_2", TEST, 11387),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2024, 7)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
