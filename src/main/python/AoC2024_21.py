#! /usr/bin/env python3
#
# Advent of Code 2024 Day 21
#

import sys
from dataclasses import dataclass
from functools import cache
from typing import Iterator

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = InputData
Output1 = int
Output2 = int


TEST = """\
029A
980A
179A
456A
379A
"""


@dataclass(frozen=True)
class Keypad:
    layout: list[str]

    def get_position(self, k: str) -> tuple[int, int]:
        return next(
            (x, y)
            for y, r in enumerate(self.layout)
            for x, c in enumerate(r)
            if c == k
        )

    def __getitem__(self, idx: int) -> str:
        return self.layout[idx]


NUMERIC = Keypad(layout=["789", "456", "123", " 0A"])
DIRECTIONAL = Keypad(layout=[" ^A", "<v>"])


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def solve(self, input: Input, levels: int) -> int:
        def path(keypad: Keypad, from_: str, to: str) -> str:
            from_x, from_y = keypad.get_position(from_)
            to_x, to_y = keypad.get_position(to)

            def paths(x: int, y: int, s: str) -> Iterator[str]:
                if (x, y) == (to_x, to_y):
                    yield s + "A"
                if to_x < x and keypad[y][x - 1] != " ":
                    yield from paths(x - 1, y, s + "<")
                if to_y < y and keypad[y - 1][x] != " ":
                    yield from paths(x, y - 1, s + "^")
                if to_y > y and keypad[y + 1][x] != " ":
                    yield from paths(x, y + 1, s + "v")
                if to_x > x and keypad[y][x + 1] != " ":
                    yield from paths(x + 1, y, s + ">")

            return min(
                paths(from_x, from_y, ""),
                key=lambda p: sum(a != b for a, b in zip(p, p[1:])),
            )

        @cache
        def count(s: str, level: int, max_level: int) -> int:
            if level > max_level:
                return len(s)
            keypad = DIRECTIONAL if level else NUMERIC
            return sum(
                count(path(keypad, from_, to), level + 1, max_level)
                for from_, to in zip("A" + s, s)
            )

        return sum(
            int(combo[:-1]) * count(combo, 0, levels) for combo in input
        )

    def part_1(self, input: Input) -> Output1:
        return self.solve(input, 2)

    def part_2(self, input: Input) -> Output2:
        return self.solve(input, 25)

    @aoc_samples((("part_1", TEST, 126384),))
    def samples(self) -> None:
        pass


solution = Solution(2024, 21)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
