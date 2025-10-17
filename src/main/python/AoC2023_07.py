#! /usr/bin/env python3
#
# Advent of Code 2023 Day 7
#

from __future__ import annotations

import sys
from collections import Counter
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from collections.abc import Callable

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

JOKER = "J"
BEST = "AAAAA"


class Hand(NamedTuple):
    cards: str
    bid: int
    value: int
    strength: str
    value_with_jokers: int
    strength_with_jokers: str

    @classmethod
    def from_input(cls, line: str) -> Hand:
        def get_value(cards: str) -> int:
            mc = Counter(cards).most_common()
            return 2 * mc[0][1] + (mc[1][1] if len(mc) > 1 else 0)

        def with_jokers(cards: str) -> str:
            c = Counter(card for card in cards if card != JOKER)
            if not c:
                return BEST
            best = c.most_common()[0][0]
            return "".join((card if card != JOKER else best) for card in cards)

        cards, bid = line.split()
        value = get_value(cards)
        strength = cards.translate(str.maketrans("TJQKA", "BCDEF"))
        value_with_jokers = get_value(with_jokers(cards))
        strength_with_jokers = cards.translate(str.maketrans("TJQKA", "B0DEF"))
        return Hand(
            cards,
            int(bid),
            value,
            strength,
            value_with_jokers,
            strength_with_jokers,
        )


Input = list[Hand]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [Hand.from_input(line) for line in input_data]

    def _solve(
        self,
        hands: Input,
        order_func: Callable[[Hand], tuple[int, str]],
    ) -> int:
        return sum(
            i * hand.bid
            for i, hand in enumerate(sorted(hands, key=order_func), start=1)
        )

    def part_1(self, hands: Input) -> Output1:
        return self._solve(hands, lambda hand: (hand.value, hand.strength))

    def part_2(self, hands: Input) -> Output2:
        return self._solve(
            hands,
            lambda hand: (hand.value_with_jokers, hand.strength_with_jokers),
        )

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
