#! /usr/bin/env python3
#
# Advent of Code 2015 Day 23
#

from aoc import my_aocd
from aoc.vm import Program, VirtualMachine
from aoc.assembunny import Assembunny
from aoc.common import log


def _solve(inputs: tuple[str], init_a) -> int:
    inss = Assembunny.parse(inputs)
    program = Program(Assembunny.translate(inss))
    log(program.instructions)
    program.set_register_value("a", init_a)
    VirtualMachine().run_program(program)
    log(program.registers["a"])
    return program.registers["a"]


def part_1(inputs: tuple[str]) -> int:
    return _solve(inputs, 7)


def part_2(inputs: tuple[str]) -> int:
    return _solve(inputs, 12)


TEST = '''\
cpy 2 a
tgl a
tgl a
tgl a
cpy 1 a
dec a
dec a
'''.splitlines()


def main() -> None:
    my_aocd.print_header(2016, 23)

    assert part_1(TEST) == 3

    inputs = my_aocd.get_input(2016, 23, 26)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
