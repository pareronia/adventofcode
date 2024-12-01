#! /usr/bin/env python3
#
# Advent of Code 2021 Day 10
#

from __future__ import annotations

import sys
from collections import deque
from functools import reduce
from typing import NamedTuple

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log

PAREN_OPEN = "("
PAREN_CLOSE = ")"
SQUARE_OPEN = "["
SQUARE_CLOSE = "]"
CURLY_OPEN = "{"
CURLY_CLOSE = "}"
ANGLE_OPEN = "<"
ANGLE_CLOSE = ">"
OPEN = (PAREN_OPEN, SQUARE_OPEN, CURLY_OPEN, ANGLE_OPEN)
MAP = {
    PAREN_OPEN: PAREN_CLOSE,
    SQUARE_OPEN: SQUARE_CLOSE,
    CURLY_OPEN: CURLY_CLOSE,
    ANGLE_OPEN: ANGLE_CLOSE,
    PAREN_CLOSE: PAREN_OPEN,
    SQUARE_CLOSE: SQUARE_OPEN,
    CURLY_CLOSE: CURLY_OPEN,
    ANGLE_CLOSE: ANGLE_OPEN,
}
CORRUPTION_SCORES = {
    PAREN_CLOSE: 3,
    SQUARE_CLOSE: 57,
    CURLY_CLOSE: 1_197,
    ANGLE_CLOSE: 25_137,
    None: 0,
}
INCOMPLETE_SCORES = {
    PAREN_OPEN: 1,
    SQUARE_OPEN: 2,
    CURLY_OPEN: 3,
    ANGLE_OPEN: 4,
}
TEST = """\
[({(<(())[]>[[{[]{<()<>>
[(()[<>])]({[<{<<[]>>(
{([(<{}[<>[]}>{[]{[(<()>
(((({<>}<{<{<>}{[]{[]{}
[[<[([]))<([[{}[[()]]]
[{[{({}]{}}([{[{{{}}([]
{<[[]]>}<{[{[{[]{()[[[]
[<(<(<(<{}))><([]([]()
<{([([[(<>()){}]>(<<{{
<{([{{}}[<[[[<>{}]]]>[]]
"""


class Result(NamedTuple):
    corrupt: str | None
    incomplete: list[str] | None

    @classmethod
    def for_corrupt(cls, c: str) -> Result:
        return Result(c, None)

    @classmethod
    def for_incomplete(cls, q: list[str]) -> Result:
        return Result(None, q)


Input = InputData
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def check(self, line: str) -> Result:
        stack = deque[str]()
        for c in line:
            if c in OPEN:
                stack.appendleft(c)
            elif MAP[c] != stack.popleft():
                return Result.for_corrupt(c)
        return Result.for_incomplete(list(stack))

    def part_1(self, inputs: Input) -> int:
        return sum(
            CORRUPTION_SCORES[corrupt]
            for corrupt in (self.check(line).corrupt for line in inputs)
            if corrupt is not None
        )

    def part_2(self, inputs: Input) -> int:
        scores = sorted(
            reduce(
                lambda a, b: 5 * a + b,
                (INCOMPLETE_SCORES[i] for i in incomplete),
            )
            for incomplete in (self.check(line).incomplete for line in inputs)
            if incomplete is not None
        )
        log(scores)
        return scores[len(scores) // 2]

    @aoc_samples(
        (
            ("part_1", TEST, 26_397),
            ("part_2", TEST, 288_957),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2021, 10)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
