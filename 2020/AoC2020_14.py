#! /usr/bin/env python3
#
# Advent of Code 2020 Day 14
#

from dataclasses import dataclass
import my_aocd
from common import log


@dataclass
class Instruction():
    command: str
    args: tuple[str]


def _parse(inputs: tuple[str]) -> list[Instruction]:
    inss = list[Instruction]()
    for line in inputs:
        if line.startswith("mask"):
            splits = line.split(" = ")
            inss.append(Instruction("mask", (splits[1], )))
        elif line.startswith("mem"):
            splits = line.split(" = ")
            add = splits[0][4:][:-1]
            inss.append(Instruction("mem", (add, splits[1])))
        else:
            raise ValueError("Invalid input")
    log(inss)
    return inss


def _apply_mask(mem: str, mask: str) -> str:
    if len(mem) < 36:
        pre = "0" * (36 - len(mem))
        mem = pre + mem
    new_mem = ""
    for i, m in enumerate(mask):
        if m == "X":
            new_mem += mem[i]
        else:
            new_mem += m
    return new_mem


def part_1(inputs: tuple[int]) -> int:
    inss = _parse(inputs)
    mem = dict()
    mask: str
    for ins in inss:
        if ins.command == "mask":
            mask = ins.args[0]
        elif ins.command == "mem":
            idx = int(ins.args[0])
            value = "{0:b}".format(int(ins.args[1]))
            log(value)
            log(mask)
            mem[idx] = _apply_mask(value, mask)
        log(mem)
    return sum([int(m, 2) for m in mem.values()])


def part_2(inputs: tuple[int]) -> int:
    return 0


test = """\
mask = XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X
mem[8] = 11
mem[7] = 101
mem[8] = 0
"""


def main() -> None:
    my_aocd.print_header(2020, 14)

    assert part_1(test.splitlines()) == 165
    assert part_2(test.splitlines()) == 0

    inputs = my_aocd.get_input_as_tuple(2020, 14, 556)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    # result2 = part_2(inputs)
    # print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
