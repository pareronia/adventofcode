#! /usr/bin/env python3
#
# Advent of Code 2019 Day 13
#

import aocd

from aoc import my_aocd
from aoc.intcode import IntCode


def _parse(inputs: tuple[str]) -> list[int]:
    return [int(_) for _ in inputs[0].split(",")]


def _solve(ints: list[int], inp: int) -> int:
    IntCode(ints).run([inp], out := [])
    return out[-1]


def part_1(inputs: tuple[str]) -> int:
    IntCode(_parse(inputs)).run([], out := [])
    return sum(1 for o in [out[i] for i in range(2, len(out), 3)] if o == 2)


def part_2(inputs: tuple[str]) -> int:
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
        buf.clear()
        if x == -1 and y == 0:
            score = id
        elif id == 3:
            paddle = x
        elif id == 4:
            ball = x
        if ball and paddle:
            inp.append(-1 if ball < paddle else 1 if ball > paddle else 0)
            ball = None
    return score


def main() -> None:
    puzzle = aocd.models.Puzzle(2019, 13)
    my_aocd.print_header(puzzle.year, puzzle.day)

    inputs = my_aocd.get_input_data(puzzle, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()
