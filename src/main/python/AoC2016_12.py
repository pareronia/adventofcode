#! /usr/bin/env python3
#
# Advent of Code 2016 Day 12
#

from aoc import my_aocd
from aoc.vm import Program, VirtualMachine
from aoc.assembunny import Assembunny
from aoc.common import log


# from u/blockingthesky @reddit
def _solve(inp: tuple[str], init_c: int) -> int:
    reg = {'a': 0, 'b': 0, 'c': init_c, 'd': 0}
    ind = 0
    while ind < len(inp):
        ins = inp[ind].split(' ')
        if ins[0] == 'cpy':
            if ins[1][0] in 'abcd':
                reg[ins[2]] = reg[ins[1]]
            else:
                j = int(ins[1])
                reg[ins[2]] = j
        elif ins[0] == 'inc':
            reg[ins[1]] += 1
        elif ins[0] == 'dec':
            reg[ins[1]] -= 1
        if ins[0] == 'jnz':
            if ins[1] in 'abcd':
                if reg[ins[1]] != 0:
                    ind += int(ins[2])
                else:
                    ind += 1
            else:
                if int(ins[1]) != 0:
                    ind += int(ins[2])
                else:
                    ind += 1
        else:
            ind += 1
    return reg['a']


def _solve_vm(inputs: tuple[str], init_c) -> int:
    inss = Assembunny.parse(inputs)
    program = Program(Assembunny.translate(inss))
    program.set_register_value("c", init_c)
    VirtualMachine().run_program(program)
    log(program.registers["a"])
    return program.registers["a"]


def _part_1_vm(inputs: tuple[str]) -> int:
    return _solve_vm(inputs, 0)


def _part_1(inputs: tuple[str]) -> int:
    return _solve(inputs, 0)


def part_1(inputs: tuple[str]) -> int:
    return _part_1_vm(inputs)


def _part_2_vm(inputs: tuple[str]) -> int:
    return _solve_vm(inputs, 1)


def _part_2(inputs: tuple[str]) -> int:
    return _solve(inputs, 1)


def part_2(inputs: tuple[str]) -> int:
    return _part_2(inputs)


TEST = '''\
cpy 41 a
inc a
inc a
dec a
jnz a 2
dec a
'''.splitlines()


def main() -> None:
    my_aocd.print_header(2016, 12)

    assert part_1(TEST) == 42

    inputs = my_aocd.get_input(2016, 12, 23)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
