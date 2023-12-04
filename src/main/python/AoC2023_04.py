#! /usr/bin/env python3
#
# Advent of Code 2023 Day 4
#

import sys
from collections import defaultdict

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log

Input = list[tuple[set[int], set[int]]]
Output1 = int
Output2 = int


TEST = """\
Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        cards = []
        for line in input_data:
            w, h = line.split(": ")[1].split("|")
            winning = {int(_) for _ in w.split()}
            have = {int(_) for _ in h.split()}
            cards.append((winning, have))
        return cards

    def part_1(self, cards: Input) -> Output1:
        ans = 0
        for card in cards:
            w, h = card
            c = w & h
            if len(c) == 0:
                continue
            ans += 2 ** (len(c) - 1)
        return ans

    def part_2(self, cards: Input) -> Output2:
        count = defaultdict[int, int](lambda: 1)
        for i, card in enumerate(cards):
            log(f"Card {i + 1}, count {count[i]}")
            w, h = card
            c = w & h
            log(f"Wins: {len(c)}")
            if len(c) == 0:
                continue
            for j in range(i + 1, i + 1 + len(c)):
                log(f" -> Card {j + 1}: +{count[i]}")
                count[j] += count[i]
        ans = sum(count.values())
        return ans

    @aoc_samples(
        (
            ("part_1", TEST, 13),
            ("part_2", TEST, 30),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 4)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
