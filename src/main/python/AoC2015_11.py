#! /usr/bin/env python3
#
# Advent of Code 2015 Day 11
#

import re
from aoc import my_aocd
# from aoc.common import log


def _increment(password: str) -> str:
    def next_letter(letter: str) -> str:
        return chr(ord('a') + (ord(letter) + 1 - ord('a')) % 26)

    i = len(password) - 1
    password = password[:i] + next_letter(password[i])
    while password[i] == 'a' and i > 0:
        i -= 1
        password = password[:i] + next_letter(password[i]) + password[i+1:]
    return password


def _is_ok(password: str) -> bool:
    def get_matches(regexp: str, string: str):
        return re.findall(regexp, string)

    if 'i' in password or 'o' in password or 'l' in password:
        return False
    if len({p for p in get_matches(r"([a-z])\1", password)}) < 2:
        return False
    for i in range(len(password) - 3):
        if ord(password[i]) == ord(password[i+1]) - 1 \
           and ord(password[i+1]) == ord(password[i+2]) - 1:
            return True
    return False


def part_1(inputs: tuple[str]) -> str:
    assert len(inputs) == 1
    password = inputs[0]
    while not _is_ok(password):
        password = _increment(password)
    return password


def part_2(inputs: tuple[str]) -> int:
    password = _increment(part_1(inputs))
    while not _is_ok(password):
        password = _increment(password)
    return password


TEST1 = """\
abcdefgh
""".splitlines()
TEST2 = """\
ghijklmn
""".splitlines()


def main() -> None:
    my_aocd.print_header(2015, 11)

    assert part_1(TEST1) == "abcdffaa"
    assert part_1(TEST2) == "ghjaabcc"
    assert part_2(TEST1) == 0
    assert part_2(TEST2) == 0

    inputs = my_aocd.get_input(2015, 11, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
