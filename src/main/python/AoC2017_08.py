#! /usr/bin/env python3
#
# Advent of Code 2017 Day 8
#

from collections import defaultdict
from aoc import my_aocd
from aoc.common import log


def _parse(inputs: tuple[str]) -> tuple[str]:
    pass


def _solve(inputs: tuple[str]) -> int:
    regs = defaultdict(int)
    max_value = 0
    for input_ in inputs:
        sp = input_.split()
        c_reg = sp[4]  # noqa F841
        test = sp[5]
        c_value = sp[6]
        if not eval("regs[c_reg] " + test + c_value):  # nosec
            continue
        if sp[1] == "inc":
            regs[sp[0]] += int(sp[2])
            log(str(regs[c_reg]) + test + str(c_value)
                + ":" + sp[0] + "->" + str(regs[sp[0]]))
        elif sp[1] == "dec":
            regs[sp[0]] -= int(sp[2])
            log(str(regs[c_reg]) + test + str(c_value) + ":"
                + sp[0] + "->" + str(regs[sp[0]]))
        else:
            raise ValueError
        max_value = max(max_value, max(regs.values()))
    log(regs)
    return max(regs.values()), max_value


def part_1(inputs: tuple[str]) -> int:
    return _solve(inputs)[0]


def part_2(inputs: tuple[str]) -> int:
    return _solve(inputs)[1]


TEST = '''\
b inc 5 if a > 1
a inc 1 if b < 5
c dec -10 if a >= 1
c inc -20 if c == 10
'''.splitlines()


def main() -> None:
    my_aocd.print_header(2017, 8)

    assert part_1(TEST) == 1

    inputs = my_aocd.get_input(2017, 8, 1000)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
