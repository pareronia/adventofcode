#! /usr/bin/env python3
#
# Advent of Code 2023 Day 4
#

from __future__ import annotations

import sys
from typing import NamedTuple

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST = """\
Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
"""


class ScratchCard(NamedTuple):
    matching: int

    @classmethod
    def from_input(cls, line: str) -> ScratchCard:
        w, h = line.split(": ")[1].split("|")
        winning = {int(_) for _ in w.split()}
        have = {int(_) for _ in h.split()}
        return ScratchCard(len(winning & have))


Input = list[ScratchCard]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [ScratchCard.from_input(line) for line in input_data]

    def part_1(self, cards: Input) -> Output1:
        return sum(
            2 ** (card.matching - 1) for card in cards if card.matching > 0
        )

    def part_2(self, cards: Input) -> Output2:
        count = [1] * len(cards)
        for i, card in enumerate(cards):
            for j in range(card.matching):
                count[i + 1 + j] += count[i]
        return sum(count)

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
