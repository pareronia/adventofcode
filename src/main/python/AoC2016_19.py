#! /usr/bin/env python3
#
# Advent of Code 2016 Day 19
#

from __future__ import annotations

import sys
from dataclasses import dataclass

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples


class Node:
    value: int
    prev: Node
    next: Node

    def __init__(self, value: int):
        self.value = value
        self.prev = None  # type:ignore[assignment]
        self.next = None  # type:ignore[assignment]


@dataclass
class Elves:
    head: Node = None  # type:ignore[assignment]
    size: int = 0

    @classmethod
    def from_input(cls, input: str) -> Elves:
        elves = Elves()
        prev: Node = None  # type:ignore[assignment]
        node: Node = None  # type:ignore[assignment]
        for i in range(int(input)):
            node = Node(i + 1)
            if elves.size == 0:
                elves.head = node
            else:
                node.prev = prev
                prev.next = node
            prev = node
            elves.size += 1
        elves.head.prev = node
        node.next = elves.head
        return elves

    def remove(self, node: Node) -> None:
        node.prev.next = node.next
        node.next.prev = node.prev
        self.size -= 1


Input = str
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return list(input_data)[0]

    def part_1(self, input: str) -> int:
        elves = Elves.from_input(input)
        curr = elves.head
        while elves.size > 1:
            loser = curr.next
            elves.remove(loser)
            curr = curr.next
        return curr.value

    def part_2(self, input: str) -> int:
        elves = Elves.from_input(input)
        curr = elves.head
        opposite = elves.head
        for i in range(elves.size // 2):
            opposite = opposite.next
        while elves.size > 1:
            loser = opposite
            elves.remove(loser)
            if elves.size % 2:
                opposite = opposite.next
            else:
                opposite = opposite.next.next
            curr = curr.next
        return curr.value

    @aoc_samples(
        (
            ("part_1", "5", 3),
            ("part_2", "5", 2),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2016, 19)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
