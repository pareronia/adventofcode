#! /usr/bin/env python3
#
# Advent of Code 2020 Day 14
#

from dataclasses import dataclass
from aoc import my_aocd
from aoc.common import log
from aoc.vm import Program, Instruction, VirtualMachine


@dataclass(frozen=True)
class Instruction_():
    command: str
    args: tuple[str]


def _parse(inputs: tuple[str]) -> list[Instruction_]:
    inss = list[Instruction_]()
    for line in inputs:
        if line.startswith("mask"):
            splits = line.split(" = ")
            inss.append(Instruction_("mask", (splits[1], )))
        elif line.startswith("mem"):
            splits = line.split(" = ")
            add = splits[0][4:][:-1]
            inss.append(Instruction_("mem", (add, splits[1])))
        else:
            raise ValueError("Invalid input")
    log(inss)
    return inss


def _apply_mask_1(bits: str, mask: str) -> str:
    if len(bits) < 36:
        pre = "0" * (36 - len(bits))
        bits = pre + bits
    new_bits = ""
    for i, m in enumerate(mask):
        if m == "X":
            new_bits += bits[i]
        else:
            new_bits += m
    return new_bits


def _as_binary_string(num: int) -> str:
    return "{0:b}".format(num)


def _to_program_1(lines: list[Instruction_]) -> Program:
    mask: str
    instructions = list[Instruction]()
    for line in lines:
        if line.command == "mask":
            mask = line.args[0]
        elif line.command == "mem":
            address = int(line.args[0])
            value = _as_binary_string(int(line.args[1]))
            value = _apply_mask_1(value, mask)
            instructions.append(Instruction.MEM(address, value))
    return Program(instructions)


def part_1(inputs: tuple[int]) -> int:
    vm = VirtualMachine()
    program = _to_program_1(_parse(inputs))
    vm.run_program(program)
    return sum([int(m, 2) for m in program.memory.values()])


def _apply_mask_2(bits: str, mask: str) -> str:
    if len(bits) < 36:
        pre = "0" * (36 - len(bits))
        bits = pre + bits
    log(f"in  bits: {bits}")
    log(f"mask    : {mask}")
    new_bits = ""
    for i, m in enumerate(mask):
        if m == "0":
            new_bits += bits[i]
        elif m == "1":
            new_bits += "1"
        else:
            new_bits += "X"
    log(f"new_bits: {new_bits}")
    return new_bits


def _double_address(addresses: list[str], pos: int):
    if pos == -1:
        return addresses
    else:
        new_addresses = []
        for address in addresses:
            a1 = address[:pos] + "0" + address[pos+1:]
            new_addresses.append(a1)
            a2 = address[:pos] + "1" + address[pos+1:]
            new_addresses.append(a2)
        x = new_addresses[0].find("X")
        return _double_address(new_addresses, x)


def _perm_address(address: str) -> list[str]:
    x = address.find("X")
    return _double_address([address], x)


def _to_program_2(lines: list[Instruction_]) -> Program:
    mask: str
    instructions = list[Instruction]()
    for line in lines:
        if line.command == "mask":
            mask = line.args[0]
        elif line.command == "mem":
            value = _as_binary_string(int(line.args[1]))
            address = int(line.args[0])
            address = _as_binary_string(address)
            log(f"add in: {address}")
            address = _apply_mask_2(address, mask)
            log(f"add masked:{address}")
            new_adds = _perm_address(address)
            log(f"new adds: {new_adds}")
            for add in new_adds:
                add = int(add, 2)
                instructions.append(Instruction.MEM(add, value))
    return Program(instructions)


def part_2(inputs: tuple[int]) -> int:
    vm = VirtualMachine()
    program = _to_program_2(_parse(inputs))
    vm.run_program(program)
    return sum([int(m, 2) for m in program.memory.values()])


test_1 = """\
mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
mem[8] = 11
mem[7] = 101
mem[8] = 0
"""
test_2 = """\
mask = 000000000000000000000000000000X1001X
mem[42] = 100
mask = 00000000000000000000000000000000X0XX
mem[26] = 1
"""


def main() -> None:
    my_aocd.print_header(2020, 14)

    assert part_1(test_1.splitlines()) == 165
    assert part_2(test_2.splitlines()) == 208

    inputs = my_aocd.get_input_as_tuple(2020, 14, 556)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
