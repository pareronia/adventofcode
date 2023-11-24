#! /usr/bin/env python3
#
# Advent of Code 2019 Day 11
#

import aocd
from advent_of_code_ocr import convert_6

from aoc import my_aocd
from aoc.common import log
from aoc.geometry import Draw, Position, Turn
from aoc.intcode import IntCode
from aoc.navigation import Heading, NavigationWithHeading

BLACK = 0
WHITE = 1
LEFT = 0
FILL = "▒"
EMPTY = " "


def _parse(inputs: tuple[str, ...]) -> list[int]:
    return [int(_) for _ in inputs[0].split(",")]


def _paint(ints: list[int], start: int) -> tuple[set[Position], set[Position]]:
    white = set[Position]()
    nav = NavigationWithHeading(Position.of(0, 0), Heading.NORTH)
    int_code = IntCode(ints)
    input = [start]
    out: list[int] = []
    int_code.run_till_input_required(input, out)
    while True:
        if int_code.halted:
            break
        if out.pop(0) == WHITE:
            white.add(nav.position)
        else:
            white.discard(nav.position)
        nav.turn(Turn.LEFT if out.pop(0) == LEFT else Turn.RIGHT)
        nav.forward(1)
        input.append(1 if nav.position in white else 0)
        int_code.run_till_input_required(input, out)
    visited = {p for p in nav.get_visited_positions()}
    return white, visited


def part_1(inputs: tuple[str, ...]) -> int:
    _, visited = _paint(_parse(inputs), BLACK)
    return len(visited)


def part_2(inputs: tuple[str, ...]) -> str:
    white, _ = _paint(_parse(inputs), WHITE)
    drawing = Draw.draw(white, FILL, EMPTY)
    for _ in drawing:
        log(_)
    return convert_6(  # type:ignore[no-any-return]
        "\n".join(drawing), fill_pixel=FILL, empty_pixel=EMPTY
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
