#! /usr/bin/env python3
#
# Advent of Code 2015 Day 5
#

from hashlib import md5
from aoc import my_aocd
from aoc.common import spinner, log


def _find_md5_starting_with_5_zeroes(input_: str, index: int) -> (int, int):
    val = input_
    while val[:5] != "00000":
        index += 1
        spinner(index)
        str2hash = input_ + str(index)
        val = md5(str2hash.encode()).hexdigest()  # nosec
    return val, index


def part_1(inputs: tuple[str]) -> str:
    assert len(inputs) == 1
    index = 0
    result = ""
    for i in range(8):
        log(i)
        val, index = _find_md5_starting_with_5_zeroes(inputs[0], index)
        result += val[5]
        log(result)
    return result


def part_2(inputs: tuple[str]) -> str:
    assert len(inputs) == 1
    index = 0
    result = list(" " * 8)
    seen = set()
    while True:
        val, index = _find_md5_starting_with_5_zeroes(inputs[0], index)
        temp = val[5]
        if temp not in "01234567" or temp in seen:
            continue
        seen.add(temp)
        result[int(temp)] = val[6]
        log(result)
        if result.count(' ') == 0:
            break
    return "".join(result)


TEST = "abc".splitlines()


def main() -> None:
    my_aocd.print_header(2016, 5)

    assert part_1(TEST) == "18f47a30"
    assert part_2(TEST) == "05ace8e3"

    inputs = my_aocd.get_input(2016, 5, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
