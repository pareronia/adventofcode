#! /usr/bin/env python3
#
# Advent of Code 2016 Day 12
#

import aocd
from aoc import my_aocd
from aoc.vm import Program, VirtualMachine
from aoc.assembunny import Assembunny
from aoc.math import Fibonacci
from aoc.common import log


# from u/blockingthesky @reddit
def _solve_reddit(inp: tuple[str], init_c: int) -> int:
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


def _solve_vm(inputs: tuple[str], init_c: int) -> int:
    inss = Assembunny.parse(inputs)
    program = Program(Assembunny.translate(inss))
    program.set_register_value("c", init_c)
    VirtualMachine().run_program(program)
    log(program.registers["a"])
    return int(program.registers["a"])


def _solve(inputs: tuple[str, ...], init_c: int) -> int:
    values = list(map(lambda i: int(i),
                      filter(lambda i: i.isnumeric(),
                             map(lambda i: i.operands[0],
                                 filter(lambda i: i.operation == "cpy",
                                        Assembunny.parse(inputs))))))
    n = values[0] + values[1] + values[2]
    n += 0 if init_c == 0 else 7
    return Fibonacci.binet(n) + values[4] * values[5]


def part_1(inputs: tuple[str, ...]) -> int:
    return _solve(inputs, 0)


def part_2(inputs: tuple[str, ...]) -> int:
    return _solve(inputs, 1)


TEST1 = '''\
cpy 41 a
inc a
inc a
dec a
jnz a 2
dec a
'''.splitlines()
TEST2 = '''\
cpy 1 a
cpy 1 b
cpy 26 d
jnz c 2
jnz 1 5
cpy 7 c
inc d
dec c
jnz c -2
cpy a c
inc a
dec b
jnz b -2
cpy c b
dec d
jnz d -6
cpy 16 c
cpy 12 d
inc a
dec d
jnz d -2
dec c
jnz c -5
'''.splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2016, 12)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert _solve(TEST2, 0) == 318_003  # type:ignore[arg-type]
    assert _solve(TEST2, 1) == 9_227_657  # type:ignore[arg-type]
    assert _solve_reddit(TEST1, 0) == 42  # type:ignore[arg-type]
    assert _solve_reddit(TEST2, 0) == 318_003  # type:ignore[arg-type]
    assert _solve_reddit(TEST2, 1) == 9_227_657  # type:ignore[arg-type]

    inputs = my_aocd.get_input_data(puzzle, 23)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()
