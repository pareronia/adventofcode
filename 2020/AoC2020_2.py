#! /usr/bin/env python3
#
# Advent of Code 2020 Day 2
#
import re
from operator import xor
import my_aocd


def _parse(input_: str) -> (int, int, str, str, int):
    m = re.search(r'^(\d{1,2})-(\d{1,2}) ([a-z]{1}): ([a-z]+)$', input_)
    first = int(m.group(1))
    second = int(m.group(2))
    wanted = m.group(3)
    passw = m.group(4)
    m1 = re.findall(r'(' + wanted + '{1})', passw)
    return (first, second, wanted, passw, len(m1))


def _part_1(inputs: set) -> int:
    valid = 0
    for input_ in inputs:
        parsed = _parse(input_)
        check = parsed[4] in range(parsed[0], parsed[1] + 1)
        print(f"{input_} -> {parsed} : {check}")
        if check:
            valid += 1
    return valid


def _part_2(inputs: set) -> int:
    valid = 0
    for input_ in inputs:
        parsed = _parse(input_)
        first = parsed[0]
        second = parsed[1]
        wanted = parsed[2]
        passw = parsed[3]
        first_matched = passw[first - 1] == wanted
        second_matched = passw[second - 1] == wanted
        check = xor(first_matched, second_matched)
        print(f"{input_} -> {parsed} : {check}")
        if check:
            valid += 1
    return valid


test = {"1-3 a: abcde",
        "2-9 c: ccccccccc",
        "1-3 b: cdefg",
        }


def main() -> None:
    print("====================================================")
    print("AoC 2020 Day 2 - https://adventofcode.com/2020/day/2")
    print("====================================================")
    print("")

    assert _part_1(test) == 2
    assert _part_2(test) == 1

    inputs = my_aocd.get_input_as_set(2020, 2, 1000)
    result1 = _part_1(inputs)
    result2 = _part_2(inputs)
    print("Part 1: " + str(result1))
    print("Part 2: " + str(result2))


if __name__ == '__main__':
    main()
