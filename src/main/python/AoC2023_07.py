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

TEST = """\
32T3K 765
T55J5 684
KK677 28
KTJJT 220
QQQJA 483
"""

VALUES = {
    "5_KIND": 1000,
    "4_KIND": 900,
    "FULL_HOUSE": 800,
    "3_KIND": 700,
    "2_PAIR": 600,
    "1_PAIR": 500,
    "HIGH_CARD": 400,
}

STRENGTH = {
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


def _value(s: str) -> str:
    c = Counter(s)
    if 5 in c.values():
        type = "5_KIND"
    elif 4 in c.values():
        type = "4_KIND"
    elif sorted(c.values()) == [2, 3]:
        type = "FULL_HOUSE"
    elif 3 in c.values():
        type = "3_KIND"
    elif sorted(c.values()) == [1, 2, 2]:
        type = "2_PAIR"
    elif 2 in c.values():
        type = "1_PAIR"
    else:
        type = "HIGH_CARD"
    return type


def _strength(s: str) -> list[int]:
    return [STRENGTH[s[i]] if s[i] != "J" else 1 for i in range(5)]


@total_ordering
class Hand(NamedTuple):
    cards: str
    bid: int

    @classmethod
    def from_input(cls, line: str) -> Hand:
        c, b = line.split()
        return Hand(c, int(b))

    def value(self) -> str:
        return _value(self.cards)

    # def value(self) -> str:
    #     c = Counter(self.cards)
    #     if 5 in c.values():
    #         type = "5_KIND"
    #     elif 4 in c.values():
    #         if c.get("J") == 1:
    #             type = "5_KIND"
    #         else:
    #             type = "4_KIND"
    #     elif sorted(c.values()) == [2, 3]:
    #         if c.get("J") == 2 or c.get("J") == 3:
    #             type = "5_KIND"
    #         else:
    #             type = "FULL_HOUSE"
    #     elif 3 in c.values():
    #         if c.get("J") == 1:
    #             type = "4_KIND"
    #         elif c.get("J") == 2:
    #             type = "5_KIND"
    #         else:
    #             type = "3_KIND"
    #     elif sorted(c.values()) == [1, 2, 2]:
    #         if c.get("J") == 1:
    #             type = "FULL_HOUSE"
    #         elif c.get("J") == 2:
    #             type = "4_KIND"
    #         else:
    #             type = "2_PAIR"
    #     elif 2 in c.values():
    #         if c.get("J") == 1:
    #             type = "3_KIND"
    #         elif c.get("J") == 2:
    #             type = "4_KIND"
    #         else:
    #             type = "1_PAIR"
    #     else:
    #         if c.get("J") == 1:
    #             type = "1_PAIR"
    #         else:
    #             type = "HIGH_CARD"
    #     return type

    def strength(self) -> list[int]:
        return [STRENGTH[self.cards[i]] for i in range(5)]

    def __eq__(self, other: Hand) -> bool:
        if VALUES[self.value()] == VALUES[other.value()]:
            return self.strength() == other.strength()
        else:
            return False

    def __lt__(self, other: Hand) -> bool:
        if VALUES[self.value()] < VALUES[other.value()]:
            return True
        elif VALUES[self.value()] == VALUES[other.value()]:
            return self.strength() < other.strength()
        return False

    def all_possible(self) -> list[str]:
        if "J" not in self.cards:
            return [self.cards]
        ans = []
        for i, c in enumerate(self.cards):
            if c == "J":
                for x in "23456789TQKA":
                    ans.append(
                        "".join(x if _ == "J" else _ for _ in self.cards)
                    )
        return ans


Input = list[Hand]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [Hand.from_input(line) for line in input_data]

    def part_1(self, hands: Input) -> Output1:
        hands = sorted(hands)
        ans = sum((i + 1) * hands[i].bid for i in range(len(hands)))
        return ans

    def part_2(self, hands: Input) -> Output2:
        # for hand in hands:
        #   assert not (hand.value() == "HIGH_CARD" and "J" in hand.cards)
        #   assert not (hand.value() == "1_PAIR" and hand.cards.count("J") > 1)
        #   assert not (hand.value() == "2_PAIR" and "J" in hand.cards)
        new_hands = []
        for h in hands:
            p = h.all_possible()
            new_hands.append(
                (
                    max(p, key=lambda x: (VALUES[_value(x)])),
                    h.cards,
                    h.bid,
                )
            )
        new_hands.sort(
            key=lambda hand: (VALUES[_value(hand[0])], _strength(hand[1]))
        )
        ans = sum((i + 1) * new_hands[i][2] for i in range(len(new_hands)))
        return ans

    @aoc_samples(
        (
            ("part_1", TEST, 6440),
            ("part_2", TEST, 5905),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 7)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
