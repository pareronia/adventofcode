#! /usr/bin/env python3
#
# Advent of Code 2015 Day 8
#

import re
from aoc import my_aocd
from aoc.common import log


def _get_char_count(string: str) -> int:
    assert string[0] == '\"' and string[-1] == '\"'
    string = string[1:-1]
    log(string)
    cnt = 2
    while string.find('\\\\') != -1:
        string = string.replace('\\\\', '', 1)
        cnt += 1
    log("Backslashes: " + string)
    while string.find('\\\"') != -1:
        string = string.replace('\\\"', '', 1)
        cnt += 1
    log("Double quotes: " + string)
    while re.search(r'\\x[0-9a-f]{2}', string):
        string = re.sub(r'\\x[0-9a-f]{2}', '', string, count=1)
        cnt += 3
    log("Hex: " + string)
    log(cnt)
    return cnt


def part_1(inputs: tuple[str]) -> int:
    return sum([_get_char_count(s) for s in inputs])


def part_2(inputs: tuple[str]) -> int:
    return 0


TEST = """\
\"\"
\"abc\"
\"aaa\\"aaa\"
\"\\x27\"
""".splitlines()


def main() -> None:
    my_aocd.print_header(2015, 8)

    assert part_1(TEST) == 12

    inputs = my_aocd.get_input_as_tuple(2015, 8, 300)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
