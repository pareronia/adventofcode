#! /usr/bin/env python3
#
# Advent of Code 2019 Day 13
#

import os
from enum import Enum
from typing import Callable

import aocd

from aoc import my_aocd
from aoc.intcode import IntCode

Tile = tuple[int, int, int]


class TileId(bytes, Enum):

    def __new__(cls, id, char):
        obj = bytes.__new__(cls, [id])
        obj._value_ = id
        obj.char = char
        return obj

    EMPTY = (0, " ")
    WALL = (1, "█")
    BLOCK = (2, "▦")
    PADDLE = (3, "▀")
    BALL = (4, "◯")


def _parse(inputs: tuple[str]) -> list[int]:
    return [int(_) for _ in inputs[0].split(",")]


def _solve(ints: list[int], inp: int) -> int:
    IntCode(ints).run([inp], out := [])
    return out[-1]


def part_1(inputs: tuple[str]) -> int:
    IntCode(_parse(inputs)).run([], out := [])
    return sum(1 for o in [out[i] for i in range(2, len(out), 3)] if o == 2)


def _play(inputs: tuple[str], on_step: Callable[[Tile], None] = None) -> int:
    prog = _parse(inputs)
    prog[0] = 2
    int_code = IntCode(prog)
    inp = out = buf = []
    ball = paddle = score = None
    while True:
        int_code.run_till_has_output(inp, out)
        if int_code.halted:
            break
        buf.append(out.pop())
        if len(buf) < 3:
            continue
        x, y, id = buf
        if on_step is not None:
            on_step((x, y, id))
        buf.clear()
        if x == -1 and y == 0:
            score = id
        elif id == TileId.PADDLE.value:
            paddle = x
        elif id == TileId.BALL.value:
            ball = x
        if ball and paddle:
            inp.append(-1 if ball < paddle else 1 if ball > paddle else 0)
            ball = None
    return score


def part_2(inputs: tuple[str]) -> int:
    return _play(inputs)


def visualize_part_2(inputs: tuple[str]) -> None:
    tiles = dict[tuple[int, int], TileId]()
    score = 0

    def on_step(tile: Tile) -> None:
        nonlocal score
        os.system("cls" if os.name in ("nt", "dos") else "clear")  # nosec
        if tile[0] == -1 and tile[1] == 0:
            score = tile[2]
        else:
            try:
                tiles[(tile[0], tile[1])] = TileId(tile[2])
            except ValueError:
                pass
        for y in range(20):
            line = ""
            for x in range(35):
                if (x, y) not in tiles:
                    line += TileId.EMPTY.char
                    continue
                line += tiles[(x, y)].char
            print(line)
        print(f"Score: {score}")

    _play(inputs, on_step)


def main() -> None:
    puzzle = aocd.models.Puzzle(2019, 13)
    my_aocd.print_header(puzzle.year, puzzle.day)

    inputs = my_aocd.get_input_data(puzzle, 1)
    assert visualize_part_2(inputs) is None
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()
