#! /usr/bin/env python3
#
# Advent of Code 2019 Day 11
#

import os

import aocd
from advent_of_code_ocr import convert_6

from aoc import my_aocd
from aoc.common import log
from aoc.geometry import Position
from aoc.intcode import IntCode
from aoc.navigation import Headings, NavigationWithHeading

BLACK = 0
WHITE = 1
LEFT = 0
FILL = "▒"
EMPTY = " "


def _parse(inputs: tuple[str]) -> tuple[int]:
    return [int(_) for _ in inputs[0].split(",")]


def _paint(
    ints: tuple[int], start: int
) -> tuple[set[tuple[int, int]], set[tuple[int, int]]]:
    white = set[tuple[int, int]]()
    nav = NavigationWithHeading(Position.of(0, 0), Headings.N)
    int_code = IntCode(ints)
    input = [start]
    out = []
    int_code.run_till_input_required(input, out)
    while True:
        if int_code.halted:
            break
        if out.pop(0) == WHITE:
            white.add((nav.position.x, nav.position.y))
        else:
            white.discard((nav.position.x, nav.position.y))
        if out.pop(0) == LEFT:
            nav.left(90)
        else:
            nav.right(90)
        nav.forward(1)
        input.append(1 if (nav.position.x, nav.position.y) in white else 0)
        int_code.run_till_input_required(input, out)
    visited = {(p.x, p.y) for p in nav.get_visited_positions()}
    return white, visited


def part_1(inputs: tuple[str]) -> int:
    _, visited = _paint(_parse(inputs), BLACK)
    return len(visited)


def _draw(positions: set[tuple[int, int]], fill: str, empty: str) -> list[str]:
    min_x = min(x for x, _ in positions)
    max_x = max(x for x, _ in positions)
    min_y = min(y for _, y in positions)
    max_y = max(y for _, y in positions)
    width = max_x + 2
    strings = []
    for y in range(min_y, max_y + 1):
        line = "".join(
            fill if (x, y) in positions else empty for x in range(min_x, width)
        )
        strings.append(line)
    return list(reversed(strings))


def part_2(inputs: tuple[str]) -> str:
    white, _ = _paint(_parse(inputs), WHITE)
    drawing = _draw(white, FILL, EMPTY)
    [log(_) for _ in drawing]
    return convert_6(
        os.linesep.join(drawing), fill_pixel=FILL, empty_pixel=EMPTY
    )


def main() -> None:
    puzzle = aocd.models.Puzzle(2019, 11)
    my_aocd.print_header(puzzle.year, puzzle.day)

    inputs = my_aocd.get_input_data(puzzle, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()
