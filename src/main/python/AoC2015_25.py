#! /usr/bin/env python3
#
# Advent of Code 2015 Day 25
#

import re
from typing import NamedTuple
from aoc import my_aocd
from aoc.common import log


class Code(NamedTuple):
    row: int
    col: int

    def number(self) -> int:
        r = self.row
        c = self.col
        if r == c == 1:
            return 1
        return (c * (c+1)) // 2 + (r-1) * c + ((r-2) * (r-1)) // 2


def _parse(inputs: tuple[str]):
    assert len(inputs) == 1
    return Code(*tuple([int(d) for d in re.findall(r'[0-9]+', inputs[0])]))


def _calculate_code(code: Code) -> int:
    start = 20151125
    number = code.number()
    if number == 1:
        return start
    val = start
    i = 2
    while i <= number:
        val = (val * 252533) % 33554393
        i += 1
    return val


def part_1(inputs: tuple[str]) -> int:
    code = _parse(inputs)
    log(code)
    log(code.number())
    return _calculate_code(code)


TEST1 = """\
To continue, please consult the code grid in the manual.\
  Enter the code at row 1, column 1.
""".splitlines()
TEST2 = """\
To continue, please consult the code grid in the manual.\
  Enter the code at row 4, column 3.
""".splitlines()
TEST3 = """\
To continue, please consult the code grid in the manual.\
  Enter the code at row 2, column 5.
""".splitlines()


def main() -> None:
    my_aocd.print_header(2015, 25)

    assert part_1(TEST1) == 20151125
    assert part_1(TEST2) == 21345942
    assert part_1(TEST3) == 15514188

    inputs = my_aocd.get_input(2015, 25, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    print("Part 2: None")


if __name__ == '__main__':
    main()
