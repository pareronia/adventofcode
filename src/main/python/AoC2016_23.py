#! /usr/bin/env python3
#
# Advent of Code 2015 Day 23
#

import math
import aocd
from aoc import my_aocd
from aoc.vm import Program, VirtualMachine
from aoc.assembunny import Assembunny
from aoc.common import log


def _solve_vm(inputs: tuple[str, ...], init_a: int) -> int:
    inss = Assembunny.parse(inputs)
    program = Program(Assembunny.translate(inss))
    log(program.instructions)
    program.set_register_value("a", init_a)
    VirtualMachine().run_program(program)
    log(program.registers["a"])
    return int(program.registers["a"])


def _solve(inputs: tuple[str, ...], init_a: int) -> int:
    ops = {"cpy", "jnz"}
    values = list(map(lambda i: int(i),
                      filter(lambda i: i.isnumeric(),
                             map(lambda i: i.operands[0],
                                 filter(lambda i: i.operation in ops,
                                        Assembunny.parse(inputs))))))
    return values[2] * values[3] + math.factorial(init_a)


def part_1(inputs: tuple[str, ...]) -> int:
    return _solve(inputs, 7)


def part_2(inputs: tuple[str, ...]) -> int:
    return _solve(inputs, 12)


TEST1 = '''\
cpy 2 a
tgl a
tgl a
tgl a
cpy 1 a
dec a
dec a
'''.splitlines()
TEST2 = '''\
cpy a b
dec b
cpy a d
cpy 0 a
cpy b c
inc a
dec c
jnz c -2
dec d
jnz d -5
dec b
cpy b c
cpy c d
dec d
inc c
jnz d -2
tgl c
cpy -16 c
jnz 1 c
cpy 90 c
jnz 73 d
inc a
inc d
jnz d -2
inc c
jnz c -5
'''.splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2016, 23)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert _solve(TEST2, 7) == 11_610  # type:ignore[arg-type]
    assert _solve(TEST2, 12) == 479_008_170  # type:ignore[arg-type]
    assert _solve_vm(TEST1, 7) == 3  # type:ignore[arg-type]

    inputs = my_aocd.get_input_data(puzzle, 26)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()
