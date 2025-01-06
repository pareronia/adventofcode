#! /usr/bin/env python3
#
# Advent of Code 2020 Day 23
#

from __future__ import annotations

import sys
from dataclasses import dataclass

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = list[int]
Output1 = int
Output2 = int


TEST = """\
389125467
"""


@dataclass
class Cup:
    label: int
    next_: Cup | None


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [int(c) for c in list(input_data)[0]]

    def prepare_cups(
        self, labels: list[int]
    ) -> tuple[dict[int, Cup], int, int, int]:
        cups = dict[int, Cup]()
        first = labels[0]
        last = labels[-1]
        tail = Cup(last, None)
        prev = tail
        for label in reversed(labels[1:-1]):
            cup = Cup(label, prev)
            cups[label] = cup
            prev = cup
        head = Cup(first, prev)
        cups[first] = head
        tail.next_ = head
        cups[last] = tail
        return cups, len(labels), min(labels), max(labels)

    def do_move(
        self,
        move: int,
        cups: dict[int, Cup],
        current: Cup,
        size: int,
        min_val: int,
        max_val: int,
    ) -> tuple[dict[int, Cup], Cup]:
        c = current
        p1 = c.next_
        assert p1 is not None
        p2 = p1.next_
        assert p2 is not None
        p3 = p2.next_
        assert p3 is not None
        c.next_ = p3.next_
        pickup = (p1.label, p2.label, p3.label)
        d = c.label - 1
        if d < min_val:
            d = max_val
        while d in pickup:
            d -= 1
            if d < min_val:
                d = max_val
        destination = cups[d]
        p3.next_ = destination.next_
        destination.next_ = p1
        assert c.next_ is not None
        current = c.next_
        return cups, current

    def part_1(self, cups: Input) -> Output1:
        cd, size, min_val, max_val = self.prepare_cups(cups)
        current = cd[cups[0]]
        for move in range(100):
            cd, current = self.do_move(
                move, cd, current, size, min_val, max_val
            )
        cup = cd[1]
        result = ""
        while cup.next_:
            if cup.next_.label == 1:
                break
            result += str(cup.next_.label)
            cup = cup.next_
        return int(result)

    def part_2(self, cups: Input) -> Output2:
        cups.extend([i for i in range(max(cups) + 1, 1_000_001)])
        cd, size, min_val, max_val = self.prepare_cups(cups)
        current = cd[cups[0]]
        for move in range(10_000_000):
            cd, current = self.do_move(
                move, cd, current, size, min_val, max_val
            )
        one = cd[1]
        assert one.next_ is not None
        star1 = one.next_.label
        assert one.next_.next_ is not None
        star2 = one.next_.next_.label
        return star1 * star2

    @aoc_samples(
        (
            ("part_1", TEST, 67384529),
            ("part_2", TEST, 149245887792),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2020, 23)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
