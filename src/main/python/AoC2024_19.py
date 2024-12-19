#! /usr/bin/env python3
#
# Advent of Code 2024 Day 19
#

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log

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
        towels, designs = input
        possible = set[str](towels)

        def find(w: str, pos: tuple[int], poss: set[tuple[str, ...]]) -> None:
            if sum(pos) == len(w):
                ii = 0
                lst = list[str]()
                for i in range(1, len(pos)):
                    lst.append(w[ii:ii+pos[i]])
                    ii += pos[i]
                if all(_ in towels for _ in lst):
                    poss.add(tuple(_ for _ in lst))
                return
            pp = [p for p in possible if w[sum(pos):].startswith(p)]
            for ppp in pp:
                possible.add(w[:sum(pos) + len(ppp)])
                tmp = list(pos[:]) + [len(ppp)]
                new_pos = tuple(_ for _ in tmp)
                find(w, new_pos, poss)

        ans = 0
        for design in designs:
            log(f"{design=}")
            poss = set[tuple[str, ...]]()
            find(design, (0, ), poss)
            log(f"{design=}: {len(poss)}: {poss}")
            ans += len(poss)
        return ans

    @aoc_samples(
        (
            ("part_1", TEST, 6),
            ("part_2", TEST, 16),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2024, 19)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
