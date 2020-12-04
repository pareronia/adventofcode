#! /usr/bin/env python3
#
# Advent of Code 2020 Day 2
#
import aocd
import re


def _get_input() -> set:
    inputs = aocd.get_data(year=2020, day=2).splitlines()
    assert len(inputs) == 1000
    return inputs


def _parse(input_: str) -> (int, int, str, str, int):
    m = re.search(r'^(\d{1,2})-(\d{1,2}) ([a-z]{1}): ([a-z]+)$', input_)
    min_ = int(m.group(1))
    max_ = int(m.group(2))
    wanted = m.group(3)
    passw = m.group(4)
    m1 = re.findall(r'(' + wanted + '{1})', passw)
    return (min_, max_, wanted, passw, len(m1))


def _part_1(inputs: set) -> int:
    valid = 0
    for input_ in inputs:
        parsed = _parse(input_)
        check = parsed[4] in range(parsed[0], parsed[1] + 1)
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

    inputs = _get_input()
    print("Part 1: " + str(_part_1(inputs)))


if __name__ == '__main__':
    main()
