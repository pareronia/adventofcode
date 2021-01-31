#! /usr/bin/env python3
#
# Advent of Code 2019 Day 2
#

import itertools
from copy import deepcopy
from aoc import my_aocd


def _parse(inputs: tuple[str]) -> tuple[int]:
    assert len(inputs) == 1
    return [int(_) for _ in inputs[0].split(",")]


def _run_intcode(prog: list[int]) -> list[int]:
    for i in range(0, len(prog), 4):
        op = prog[i]
        if op == 1:
            prog[prog[i+3]] = prog[prog[i+1]] + prog[prog[i+2]]
        elif op == 2:
            prog[prog[i+3]] = prog[prog[i+1]] * prog[prog[i+2]]
        elif op == 99:
            return prog
        else:
            raise ValueError("Invalid op")


def _run_prog(prog: list[int], noun: int, verb: int) -> int:
    prog[1] = noun
    prog[2] = verb
    return _run_intcode(prog)


def part_1(inputs: tuple[str]) -> int:
    return _run_prog(_parse(inputs), 12, 2)[0]


def part_2(inputs: tuple[str]) -> int:
    prog = _parse(inputs)
    for noun, verb in itertools.product(range(100), repeat=2):
        if _run_prog(deepcopy(prog), noun, verb)[0] == 19690720:
            return 100 * noun + verb
    raise RuntimeError("Unsolved")


TEST1 = "1,9,10,3,2,3,11,0,99,30,40,50".splitlines()
TEST2 = "1,0,0,0,99".splitlines()
TEST3 = "2,3,0,3,99".splitlines()
TEST4 = "2,4,4,5,99,0".splitlines()
TEST5 = "1,1,1,4,99,5,6,0,99".splitlines()


def main() -> None:
    my_aocd.print_header(2019, 2)

    assert _run_intcode(_parse(TEST1)) == [3500, 9, 10, 70,
                                           2, 3, 11, 0,
                                           99,
                                           30, 40, 50]
    assert _run_intcode(_parse(TEST2)) == [2, 0, 0, 0, 99]
    assert _run_intcode(_parse(TEST3)) == [2, 3, 0, 6, 99]
    assert _run_intcode(_parse(TEST4)) == [2, 4, 4, 5, 99, 9801]
    assert _run_intcode(_parse(TEST5)) == [30, 1, 1, 4, 2, 5, 6, 0, 99]

    inputs = my_aocd.get_input(2019, 2, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
