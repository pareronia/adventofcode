#! /usr/bin/env python3
#
# Advent of Code 2020 Day 2
#
import re
from operator import xor
from aoc import my_aocd
from aoc.common import log


def _parse(input_: str) -> (int, int, str, str):
    m = re.search(r'^(\d{1,2})-(\d{1,2}) ([a-z]{1}): ([a-z]+)$', input_)
    return (int(m.group(1)), int(m.group(2)), m.group(3), m.group(4))


def part_1(inputs: tuple[str]) -> int:
    def _check_valid_1(input_: str) -> bool:
        first, second, wanted, passw = _parse(input_)
        cnt = passw.count(wanted)
        check = cnt in range(first, second + 1)
        log(f"{input_}, {(first, second, wanted, passw, cnt)}, {check}")
        return check
    return sum(1 for i in inputs if _check_valid_1(i))


def part_2(inputs: tuple[str]) -> int:
    def _check_valid_2(input_: str) -> bool:
        first, second, wanted, passw = _parse(input_)
        first_matched = passw[first - 1] == wanted
        second_matched = passw[second - 1] == wanted
        check = xor(first_matched, second_matched)
        log(f"{input_}, {(first, second, wanted, passw)}, {check}")
        return check
    return sum(1 for i in inputs if _check_valid_2(i))


TEST = """\
1-3 a: abcde
2-9 c: ccccccccc
1-3 b: cdefg
""".splitlines()


def main() -> None:
    my_aocd.print_header(2020, 2)

    assert part_1(TEST) == 2
    assert part_2(TEST) == 1

    inputs = my_aocd.get_input(2020, 2, 1000)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
