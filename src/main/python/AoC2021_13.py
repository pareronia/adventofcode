#! /usr/bin/env python3
#
# Advent of Code 2021 Day 13
#

from __future__ import annotations

from typing import Callable, NamedTuple

import aocd
from advent_of_code_ocr import convert_6

from aoc import my_aocd
from aoc.common import log
from aoc.geometry import Direction, Draw, Position

FILL = "▒"
EMPTY = " "


class Fold(NamedTuple):
    x_axis: bool
    value: int

    def apply_to(self, positions: set[Position]) -> set[Position]:
        if self.x_axis:
            return {
                p.translate(
                    Direction.LEFT.vector,
                    self._amplitude(p, lambda position: position.x),
                )
                for p in positions
            }
        else:
            return {
                p.translate(
                    Direction.DOWN.vector,
                    self._amplitude(p, lambda position: position.y),
                )
                for p in positions
            }

    def _amplitude(
        self, position: Position, dim: Callable[[Position], int]
    ) -> int:
        return (
            2 * (dim(position) - self.value)
            if dim(position) > self.value
            else 0
        )


def _parse(inputs: tuple[str, ...]) -> tuple[set[Position], tuple[Fold, ...]]:
    blocks = my_aocd.to_blocks(inputs)
    positions = {
        Position.of(*[int(_) for _ in line.split(",")]) for line in blocks[0]
    }
    folds = list[Fold]()
    for line in blocks[1]:
        axis, value = line[len("fold along ") :].split("=")  # noqa E203
        folds.append(Fold(axis == "x", int(value)))
    return positions, tuple(folds)


def _solve_2(inputs: tuple[str, ...]) -> list[str]:
    positions_, folds = _parse(inputs)
    result = positions_
    for fold in folds:
        result = fold.apply_to(result)
    ans = list(
        reversed(
            Draw.draw(
                {Position.of(p.x, p.y) for p in result},
                FILL,
                EMPTY,
            )
        )
    )
    for _ in ans:
        log(_)
    return ans


def part_1(inputs: tuple[str, ...]) -> int:
    positions, folds = _parse(inputs)
    return len(folds[0].apply_to(positions))


def part_2(inputs: tuple[str, ...]) -> int:
    to_ocr = "\n".join(_solve_2(inputs))
    return convert_6(  # type:ignore[no-any-return]
        to_ocr, fill_pixel=FILL, empty_pixel=EMPTY
    )


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
# fmt: off
RESULT = ["▒▒▒▒▒ ",
          "▒   ▒ ",
          "▒   ▒ ",
          "▒   ▒ ",
          "▒▒▒▒▒ "]
# fmt: on


def main() -> None:
    puzzle = aocd.models.Puzzle(2021, 13)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 17  # type:ignore[arg-type]
    assert _solve_2(TEST) == RESULT  # type:ignore[arg-type]

    inputs = my_aocd.get_input(puzzle.year, puzzle.day, 748)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()
