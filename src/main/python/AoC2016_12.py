#! /usr/bin/env python3
#
# Advent of Code 2015 Day 12
#

from typing import NamedTuple
from aoc import my_aocd
from aoc.vm import Program, Instruction, VirtualMachine


class Instruction_(NamedTuple):
    operation: str
    operands: tuple[str]

    @classmethod
    def of(cls, in_: tuple[str]):
        return Instruction_(in_[0], (in_[1:]))

    def check_valid_operation(self) -> None:
        if self.operation not in ("cpy", "inc", "dec", "jnz"):
            raise ValueError("Invalid instruction operation")
        for operand in self.operands:
            if not (operand.strip('-').isnumeric() or operand in "abcd"):
                raise ValueError("Invalid instruction operand")


def _parse(inputs: tuple[str]) -> list[Instruction_]:
    return [Instruction_.of(input_.split()) for input_ in inputs]


def _build_program(lines: list[Instruction_]) -> Program:
    instructions = list[Instruction]()
    for line in lines:
        line.check_valid_operation()
        if line.operation == "cpy":
            value, register = line.operands
            if value.strip('-').isnumeric():
                instructions.append(Instruction.SET(register, int(value)))
            else:
                instructions.append(Instruction.CPY(value, register))
        elif line.operation == "inc":
            register = line.operands[0]
            instructions.append(Instruction.ADD(register, 1))
        elif line.operation == "dec":
            register = line.operands[0]
            instructions.append(Instruction.ADD(register, -1))
        elif line.operation == "jnz":
            register, value = line.operands
            if register in ("abcd"):
                instructions.append(Instruction.JN0(register, int(value)))
            elif register.strip('-').isnumeric():
                instructions.append(Instruction.SET("tmp", int(register)))
                instructions.append(Instruction.JN0("tmp", int(value)))
            else:
                raise ValueError("Invalid operands for jnz")
    return Program(instructions)


# from u/blockingthesky @reddit
def _solve(inp: tuple[str], reg: dict) -> int:
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


def _part_1_vm(inputs: tuple[str]) -> int:
    inss = _parse(inputs)
    program = _build_program(inss)
    VirtualMachine().run_program(program)
    return program.registers["a"]


def _part_1(inputs: tuple[str]) -> int:
    return _solve(inputs, {'a': 0, 'b': 0, 'c': 0, 'd': 0})


def part_1(inputs: tuple[str]) -> int:
    return _part_1(inputs)


def _part_2_vm(inputs: tuple[str]) -> int:
    program = _build_program(_parse(inputs))
    program.instructions.insert(0, Instruction.SET("c", 1))
    VirtualMachine().run_program(program)
    return program.registers["a"]


def _part_2(inputs: tuple[str]) -> int:
    return _solve(inputs, {'a': 0, 'b': 0, 'c': 1, 'd': 0})


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