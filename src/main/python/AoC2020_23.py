#! /usr/bin/env python3
#
# Advent of Code 2020 Day 23
#

from __future__ import annotations

import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = list[int]
Output1 = str
Output2 = int


TEST = """\
389125467
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [int(c) for c in list(input_data)[0]]

    def prepare_cups(self, labels: list[int]) -> list[int]:
        cups = [0] * (len(labels) + 1)
        for i in range(len(labels)):
            cups[labels[i - 1]] = labels[i]
        return cups

    def do_move(
        self,
        cups: list[int],
        current: int,
        min_val: int,
        max_val: int,
    ) -> int:
        c = current
        p1 = cups[c]
        p2 = cups[p1]
        p3 = cups[p2]
        cups[c] = cups[p3]
        pickup = (p1, p2, p3)
        d = c - 1
        if d < min_val:
            d = max_val
        while d in pickup:
            d -= 1
            if d < min_val:
                d = max_val
        cups[p3] = cups[d]
        cups[d] = p1
        current = cups[c]
        return current

    def part_1(self, labels: Input) -> Output1:
        cups = self.prepare_cups(labels)
        current = labels[0]
        for _ in range(100):
            current = self.do_move(cups, current, 1, 9)
        ans = ""
        cup = cups[1]
        while True:
            if cup == 1:
                break
            ans += str(cup)
            cup = cups[cup]
        return ans

    def part_2(self, labels: Input) -> Output2:
        labels.extend([i for i in range(max(labels) + 1, 1_000_001)])
        cups = self.prepare_cups(labels)
        current = labels[0]
        for _ in range(10_000_000):
            current = self.do_move(cups, current, 1, 1_000_000)
        return cups[1] * cups[cups[1]]

    @aoc_samples(
        (
            ("part_1", TEST, "67384529"),
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
