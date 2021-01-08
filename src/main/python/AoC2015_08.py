#! /usr/bin/env python3
#
# Advent of Code 2015 Day 8
#

import re
from aoc import my_aocd


def _count_decoding_overhead(string: str) -> int:
    assert string[0] == '\"' and string[-1] == '\"'
    cnt = 2
    while string.find('\\\\') != -1:
        string = string.replace('\\\\', '', 1)
        cnt += 1
    return cnt + string.count('\\\"') \
        + 3 * len(re.findall(r'\\x[0-9a-f]{2}', string))


def part_1(inputs: tuple[str]) -> int:
    return sum([_count_decoding_overhead(s) for s in inputs])


def _count_encoding_overhead(string: str) -> int:
    return 2 + string.count('\\') + string.count('\"')


def part_2(inputs: tuple[str]) -> int:
    return sum([_count_encoding_overhead(s) for s in inputs])


TEST = """\
\"\"
\"abc\"
\"aaa\\"aaa\"
\"\\x27\"
""".splitlines()


def main() -> None:
    my_aocd.print_header(2015, 8)

    assert part_1(TEST) == 12
    assert part_2(TEST) == 19

    inputs = my_aocd.get_input(2015, 8, 300)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
