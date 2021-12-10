#! /usr/bin/env python3
#
# Advent of Code 2021 Day 10
#

from __future__ import annotations
from typing import NamedTuple
from collections import deque
from functools import reduce
from aoc import my_aocd
from aoc.common import log


PAREN_OPEN = '('
PAREN_CLOSE = ')'
SQUARE_OPEN = '['
SQUARE_CLOSE = ']'
CURLY_OPEN = '{'
CURLY_CLOSE = '}'
ANGLE_OPEN = '<'
ANGLE_CLOSE = '>'
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


class Result(NamedTuple):
    corrupt: str
    incomplete: list[str]

    @classmethod
    def corrupt(cls, c: str) -> Result:
        return Result(c, None)

    @classmethod
    def incomplete(cls, q: list[str]) -> Result:
        return Result(None, q)


def _check(line: str) -> Result:
    stack = deque[str]()
    for c in list(line):
        if c in OPEN:
            stack.appendleft(c)
        else:
            if MAP[c] != stack.popleft():
                return Result(c, None)
    return Result(None, list(stack))


def part_1(inputs: tuple[str]) -> int:
    return sum(CORRUPTION_SCORES[result.corrupt]
               for result in filter(lambda x: x.corrupt is not None,
                                    (_check(line) for line in inputs)))


def part_2(inputs: tuple[str]) -> int:
    scores = sorted(
        reduce(lambda a, b: 5 * a + b,
               (INCOMPLETE_SCORES[x] for x in result.incomplete))
        for result in filter(lambda x: x.incomplete is not None,
                             (_check(line) for line in inputs))
    )
    log(scores)
    assert len(scores) % 2 == 1
    return scores[len(scores) // 2]


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
""".splitlines()


def main() -> None:
    my_aocd.print_header(2021, 10)

    assert part_1(TEST) == 26_397
    assert part_2(TEST) == 288_957

    inputs = my_aocd.get_input(2021, 10, 94)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
