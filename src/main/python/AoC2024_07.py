#! /usr/bin/env python3
#
# Advent of Code 2024 Day 7
#

from __future__ import annotations

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = list[tuple[int, list[int]]]
Output1 = int
Output2 = int

ADD = 1
MULTIPLY = 2
CONCATENATE = 4


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
        ans = []
        for line in input_data:
            left, right = line.split(": ")
            sol = int(left)
            terms = list(map(int, right.split()))
            ans.append((sol, terms))
        return ans

    def solve(self, input: Input, ops: int) -> int:
        def is_true(sol: int, terms: list[int], ops: int) -> bool:
            if terms[0] > sol:
                return False
            if len(terms) == 1:
                return sol == terms[0]
            if ADD & ops and is_true(
                sol, [terms[0] + terms[1]] + terms[2:], ops
            ):
                return True
            if MULTIPLY & ops and is_true(
                sol, [terms[0] * terms[1]] + terms[2:], ops
            ):
                return True
            if CONCATENATE & ops:
                term = terms[0]
                tmp = terms[1]
                while tmp > 0:
                    tmp //= 10
                    term *= 10
                term = term + terms[1]
                if is_true(sol, [term] + terms[2:], ops):
                    return True
            return False

        return sum(sol for sol, terms in input if is_true(sol, terms, ops))

    def part_1(self, input: Input) -> Output1:
        return self.solve(input, ADD | MULTIPLY)

    def part_2(self, input: Input) -> Output2:
        return self.solve(input, ADD | MULTIPLY | CONCATENATE)

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
