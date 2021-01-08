#! /usr/bin/env python3
#
# Advent of Code 2015 Day 4
#

from hashlib import md5
from aoc import my_aocd
from aoc.common import spinner


def _find_md5_starting_with_zeroes(input_: str, zeroes: int) -> int:
    i = 0
    val = input_
    target = "0" * zeroes
    while val[:zeroes] != target:
        i += 1
        spinner(i)
        str2hash = input_ + str(i)
        val = md5(str2hash.encode()).hexdigest()  # nosec
    return i


def part_1(inputs: tuple[str]) -> int:
    assert len(inputs) == 1
    return _find_md5_starting_with_zeroes(inputs[0], 5)


def part_2(inputs: tuple[str]) -> int:
    assert len(inputs) == 1
    return _find_md5_starting_with_zeroes(inputs[0], 6)


TEST1 = "abcdef".splitlines()
TEST2 = "pqrstuv".splitlines()


def main() -> None:
    my_aocd.print_header(2015, 4)

    assert part_1(TEST1) == 609043
    assert part_1(TEST2) == 1048970

    inputs = my_aocd.get_input(2015, 4, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
