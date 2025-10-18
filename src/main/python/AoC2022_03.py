#! /usr/bin/env python3
#
# Advent of Code 2022 Day 3
#


import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST = """\
vJrwpWtwJgWrhcsFMMfFFhFp
jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
PmmdzqPrVvPwwTWBwg
wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
ttgJtRGJQctTZtZT
CrZsJsPPZsGzwwsLwLmpwMDw
"""


Input = tuple[str, ...]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return tuple(input_data)

    def priority(self, ch: str) -> int:
        if "a" <= ch <= "z":
            return ord(ch) - ord("a") + 1
        return ord(ch) - ord("A") + 27

    def part_1(self, inputs: Input) -> Output1:
        ans = 0
        for input_ in inputs:
            ln = len(input_) // 2
            s1 = set(input_[:ln])
            s2 = set(input_[ln:])
            ch = (s1 & s2).pop()
            ans += self.priority(ch)
        return ans

    def part_2(self, inputs: Input) -> Output2:
        ans = 0
        for i in range(0, len(inputs), 3):
            s1 = set(inputs[i])
            s2 = set(inputs[i + 1])
            s3 = set(inputs[i + 2])
            ch = (s1 & s2 & s3).pop()
            ans += self.priority(ch)
        return ans

    @aoc_samples(
        (
            ("part_1", TEST, 157),
            ("part_2", TEST, 70),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2022, 3)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
