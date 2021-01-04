#! /usr/bin/env python3
#
# Advent of Code 2015 Day 5
#

import re
from aoc import my_aocd
from aoc.common import log


def _is_nice_1(string: str) -> bool:
    log(string)
    m1 = re.findall(r"(a|e|i|o|u)", string)
    log(m1)
    m2 = re.findall(r"([a-z])\1", string)
    log(m2)
    m3 = re.findall(r"(ab|cd|pq|xy)", string)
    log(m3)
    result = len(m1) >= 3 and len(m2) >= 1 and len(m3) == 0
    log(result)
    return result


def part_1(inputs: tuple[str]) -> int:
    return sum(1 for input_ in inputs if _is_nice_1(input_))


def _is_nice_2(string: str) -> bool:
    log(string)
    m1 = re.findall(r"([a-z]{2})[a-z]*\1", string)
    log(m1)
    m2 = re.findall(r"([a-z])[a-z]\1", string)
    log(m2)
    result = len(m1) >= 1 and len(m2) >= 1
    return result


def part_2(inputs: tuple[str]) -> int:
    return sum(1 for input_ in inputs if _is_nice_2(input_))


TEST1 = "ugknbfddgicrmopn".splitlines()
TEST2 = "aaa".splitlines()
TEST3 = "jchzalrnumimnmhp".splitlines()
TEST4 = "haegwjzuvuyypxyu".splitlines()
TEST5 = "dvszwmarrgswjxmb".splitlines()
TEST6 = "qjhvhtzxzqqjkmpb".splitlines()
TEST7 = "xxyxx".splitlines()
TEST8 = "uurcxstgmygtbstg".splitlines()
TEST9 = "ieodomkazucvgmuy".splitlines()
TEST10 = "xyxy".splitlines()


def main() -> None:
    my_aocd.print_header(2015, 5)

    assert part_1(TEST1) == 1
    assert part_1(TEST2) == 1
    assert part_1(TEST3) == 0
    assert part_1(TEST4) == 0
    assert part_1(TEST5) == 0
    assert part_2(TEST6) == 1
    assert part_2(TEST7) == 1
    assert part_2(TEST8) == 0
    assert part_2(TEST9) == 0
    assert part_2(TEST10) == 1

    inputs = my_aocd.get_input_as_tuple(2015, 5, 1000)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
