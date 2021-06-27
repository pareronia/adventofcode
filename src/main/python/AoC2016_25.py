#! /usr/bin/env python3
#
# Advent of Code 2015 Day 25
#

from aoc import my_aocd
from aoc.vm import Program, VirtualMachine
from aoc.assembunny import Assembunny
from aoc.common import log


def _run_program(inputs: tuple[str], init_a) -> int:
    inss = Assembunny.parse(inputs)
    output = list()
    program = Program(Assembunny.translate(inss),
                      inf_loop_treshold=6_000,
                      output_consumer=lambda s: output.append(s))
    log(program.instructions)
    program.set_register_value("a", init_a)
    try:
        VirtualMachine().run_program(program)
    except RuntimeError:
        return output


def _solve_vm(inputs: tuple[str]) -> int:
    n = 0
    while True:
        output = _run_program(inputs, n)
        ok = True
        for i, v in enumerate(output):
            ok = ok and (i % 2 == v)
        if ok:
            return n
        n += 1


def _solve(inputs: tuple[str]) -> int:
    values = list(map(lambda i: int(i),
                      filter(lambda i: i.isnumeric(),
                             map(lambda i: i.operands[0],
                                 filter(lambda i: i.operation == "cpy",
                                        Assembunny.parse(inputs))))))
    return 2730 - values[0] * 182


def part_1(inputs: tuple[str]) -> int:
    return _solve_vm(inputs)


def part_2(inputs: tuple[str]) -> int:
    return


TEST = '''\
cpy a d
cpy 14 c
cpy 182 b
inc d
dec b
jnz b -2
dec c
jnz c -5
cpy d a
jnz 0 0
cpy a b
cpy 0 a
cpy 2 c
jnz b 2
jnz 1 6
dec b
dec c
jnz c -4
inc a
jnz 1 -7
cpy 2 b
jnz c 2
jnz 1 4
dec b
dec c
jnz 1 -4
jnz 0 0
out b
jnz a -19
jnz 1 -21
'''.splitlines()


def main() -> None:
    my_aocd.print_header(2016, 25)

    assert _solve(TEST) == 182

    inputs = my_aocd.get_input(2016, 25, 30)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
