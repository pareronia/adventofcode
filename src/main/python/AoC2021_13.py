#! /usr/bin/env python3
#
# Advent of Code 2021 Day 13
#

from __future__ import annotations
import os
from typing import NamedTuple
from advent_of_code_ocr import convert_6
from aoc import my_aocd
from aoc.common import log
from aoc.geometry import Vector
from aoc.navigation import Headings
import aocd


FILL = '▒'
EMPTY = ' '


class Position(NamedTuple):
    x: int
    y: int

    def translate(self, vector: Vector, amplitude: int = 1) -> Position:
        return Position.of(
            self.x + vector.x * amplitude,
            self.y + vector.y * amplitude
        )

    @classmethod
    def of(cls, x: int, y: int) -> Position:
        return Position(x, y)


class Fold(NamedTuple):
    x_axis: bool
    value: int

    def apply_to(self, positions: set[Position]) -> set[Position]:
        if self.x_axis:
            vector = Headings.W.value
            return {p.translate(vector, self._amplitude_x(p))
                    for p in positions}
        else:
            vector = Headings.S.value
            return {p.translate(vector, self._amplitude_y(p))
                    for p in positions}

    def _amplitude_x(self, position: Position) -> int:
        return 2 * (position.x - self.value) \
                if position.x > self.value\
                else 0

    def _amplitude_y(self, position: Position) -> int:
        return 2 * (position.y - self.value) \
                if position.y > self.value \
                else 0


def _parse(inputs: tuple[str]) -> set[Position, tuple[Fold]]:
    blocks = my_aocd.to_blocks(inputs)
    positions = {Position.of(*[int(_) for _ in line.split(',')])
                 for line in blocks[0]}
    folds = list[Fold]()
    for line in blocks[1]:
        axis, value = line[len("fold along "):].split('=')
        folds.append(Fold(axis == 'x', int(value)))
    return positions, tuple(folds)


def _draw(positions: set[Position], fill: str, empty: str) -> list[str]:
    max_x = max(p.x for p in positions)
    max_y = max(p.y for p in positions)
    return ["".join(fill if Position.of(x, y) in positions else empty
                    for x in range(max_x + 2))
            for y in range(max_y + 1)]


def _solve_2(inputs: tuple[str]) -> list[str]:
    positions_, folds = _parse(inputs)
    result = positions_
    for fold in folds:
        result = fold.apply_to(result)
    ans = _draw(result, FILL, EMPTY)
    [log(_) for _ in ans]
    return ans


def part_1(inputs: tuple[str]) -> int:
    positions, folds = _parse(inputs)
    return len(folds[0].apply_to(positions))


def part_2(inputs: tuple[str]) -> int:
    to_ocr = os.linesep.join(_solve_2(inputs))
    return convert_6(to_ocr, fill_pixel=FILL, empty_pixel=EMPTY)


TEST = """\
6,10
0,14
9,10
0,3
10,4
4,11
6,0
6,12
4,1
0,13
10,12
3,4
3,0
8,4
1,10
2,14
8,10
9,0

fold along y=7
fold along x=5
""".splitlines()
RESULT = ["▒▒▒▒▒ ",
          "▒   ▒ ",
          "▒   ▒ ",
          "▒   ▒ ",
          "▒▒▒▒▒ "]


def main() -> None:
    puzzle = aocd.models.Puzzle(2021, 13)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 17
    assert _solve_2(TEST) == RESULT

    inputs = my_aocd.get_input(puzzle.year, puzzle.day, 748)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()
