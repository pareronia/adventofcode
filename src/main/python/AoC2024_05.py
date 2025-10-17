#! /usr/bin/env python3
#
# Advent of Code 2024 Day 5
#

import sys
from collections import defaultdict
from enum import Enum
from enum import auto
from enum import unique
from functools import cmp_to_key

from aoc import my_aocd
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = tuple[dict[int, list[int]], list[list[int]]]
Output1 = int
Output2 = int


TEST = """\
47|53
97|13
97|61
97|47
75|29
61|13
75|53
29|13
97|29
53|29
61|53
97|53
61|29
47|13
75|47
97|75
47|61
75|61
47|29
75|13
53|13

75,47,61,53,29
97,61,53,29,13
75,29,13
75,97,47,61,53
61,13,29
97,13,75,29,47
"""


@unique
class Mode(Enum):
    USE_CORRECT = auto()
    USE_INCORRECT = auto()


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        blocks = my_aocd.to_blocks(input_data)
        order = defaultdict[int, list[int]](list)
        for line in blocks[0]:
            a, b = map(int, line.split("|"))
            order[a].append(b)
        updates = [list(map(int, line.split(","))) for line in blocks[1]]
        return order, updates

    def solve(self, inputs: Input, mode: Mode) -> int:
        order, updates = inputs

        def cmp(a: int, b: int) -> int:
            return -1 if b in order[a] else 1

        ans = 0
        for update in updates:
            correct = update[:]
            correct.sort(key=cmp_to_key(cmp))
            if not ((mode == Mode.USE_CORRECT) ^ (update == correct)):
                ans += correct[len(correct) // 2]
        return ans

    def part_1(self, inputs: Input) -> Output1:
        return self.solve(inputs, Mode.USE_CORRECT)

    def part_2(self, inputs: Input) -> Output2:
        return self.solve(inputs, Mode.USE_INCORRECT)

    @aoc_samples(
        (
            ("part_1", TEST, 143),
            ("part_2", TEST, 123),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2024, 5)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
