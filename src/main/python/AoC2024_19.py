#! /usr/bin/env python3
#
# Advent of Code 2024 Day 19
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = tuple[set[str], list[str]]
Output1 = int
Output2 = int


TEST = """\
r, wr, b, g, bwu, rb, gb, br

brwrr
bggr
gbbr
rrbgbr
ubwu
bwurrg
brgr
bbrgwb
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        lines = list(input_data)
        return set(lines[0].split(", ")), lines[2:]

    def part_1(self, input: Input) -> Output1:
        towels, designs = input
        possible = set[str](towels)

        def find(w: str, pos: int) -> bool:
            if pos == len(w):
                return True
            pp = [p for p in possible if w[pos:].startswith(p)]
            for ppp in pp:
                possible.add(w[:pos + len(ppp)])
            return any(find(w, pos + len(ppp)) for ppp in pp)

        ans = 0
        for design in designs:
            if find(design, 0):
                ans += 1
        return ans

    def part_2(self, input: Input) -> Output2:
        return 0

    @aoc_samples(
        (
            ("part_1", TEST, 6),
            # ("part_2", TEST, "TODO"),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2024, 19)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
