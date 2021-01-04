#! /usr/bin/env python3
#
# Advent of Code 2015 Day 5
#

import re
from aoc import my_aocd


def _count_matches(regexp: str, string: str) -> int:
    return len(re.findall(regexp, string))


def part_1(inputs: tuple[str]) -> int:
    return sum(1 for input_ in inputs
               if _count_matches(r"(a|e|i|o|u)", input_) >= 3
               and _count_matches(r"([a-z])\1", input_) >= 1
               and _count_matches(r"(ab|cd|pq|xy)", input_) == 0
               )


def part_2(inputs: tuple[str]) -> int:
    return sum(1 for input_ in inputs
               if _count_matches(r"([a-z]{2})[a-z]*\1", input_) >= 1
               and _count_matches(r"([a-z])[a-z]\1", input_) >= 1
               )


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
