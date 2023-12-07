#! /usr/bin/env python3
#
# Advent of Code 2023 Day 7
#

from __future__ import annotations

import sys
from collections import Counter
from functools import total_ordering
from typing import NamedTuple

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log

TEST = """\
32T3K 765
T55J5 684
KK677 28
KTJJT 220
QQQJA 483
"""

VALUES = {
    "2": 2,
    "3": 3,
    "4": 4,
    "5": 5,
    "6": 6,
    "7": 7,
    "8": 8,
    "9": 9,
    "T": 10,
    "J": 11,
    "Q": 12,
    "K": 13,
    "A": 14,
}


@total_ordering
class Hand(NamedTuple):
    cards: str
    bid: int

    @classmethod
    def from_input(cls, line: str) -> Hand:
        c, b = line.split()
        return Hand(c, int(b))

    def value(self) -> int:
        c = Counter(self.cards)
        if 5 in c.values():
            type = 1000
        elif 4 in c.values():
            type = 900
        elif sorted(c.values()) == [2, 3]:
            type = 800
        elif 3 in c.values():
            type = 700
        elif sorted(c.values()) == [1, 2, 2]:
            type = 600
        elif 2 in c.values():
            type = 500
        else:
            type = 400
        return type

    def strength(self) -> list[int]:
        return [VALUES[self.cards[i]] for i in range(5)]

    def __eq__(self, other: Hand) -> bool:
        if self.value() == other.value():
            return self.strength() == other.strength()
        else:
            return False

    def __lt__(self, other: Hand) -> bool:
        if self.value() < other.value():
            return True
        elif self.value() == other.value():
            return self.strength() < other.strength()
        return False


Input = list[Hand]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [Hand.from_input(line) for line in input_data]

    def part_1(self, hands: Input) -> Output1:
        hands = sorted(hands)
        log(hands)
        ans = sum((i + 1) * hands[i].bid for i in range(len(hands)))
        log(ans)
        return ans

    def part_2(self, input: Input) -> Output2:
        return 0

    @aoc_samples(
        (
            ("part_1", TEST, 6440),
            # ("part_2", TEST, "TODO"),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 7)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
