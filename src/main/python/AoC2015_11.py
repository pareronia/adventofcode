#! /usr/bin/env python3
#
# Advent of Code 2015 Day 11
#

import re
from aoc import my_aocd

ALPH = "abcdefghijklmnopqrstuvwxyz"
NEXT = "bcdefghjjkmmnppqrstuvwxyza"


def _next_letter(letter: str) -> str:
    return NEXT[ALPH.index(letter)]


def _increment(password: str) -> str:
    i = len(password) - 1
    password = password[:i] + _next_letter(password[i])
    while password[i] == 'a' and i > 0:
        i -= 1
        password = password[:i] + _next_letter(password[i]) + password[i+1:]
    return password


def _is_ok(password: str) -> bool:
    if len({p for p in re.findall(r"([a-z])\1", password)}) < 2:
        return False
    for i in range(len(password) - 3):
        if ord(password[i]) == ord(password[i+1]) - 1 \
           and ord(password[i+1]) == ord(password[i+2]) - 1:
            return True
    return False


def _get_next(password: str) -> str:
    h = (password.find('i'), password.find('o'), password.find('l'))
    if h != (-1, -1, -1):
        h = min(_ for _ in h if _ != -1)
        if h < len(password) - 1:
            password = password[:h] + _next_letter(password[h]) \
                    + 'a' * len(password[h+1:])
    else:
        password = _increment(password)
    while not _is_ok(password):
        password = _increment(password)
    return password


def part_1(inputs: tuple[str]) -> str:
    assert len(inputs) == 1
    return _get_next(inputs[0])


def part_2(inputs: tuple[str]) -> str:
    assert len(inputs) == 1
    return _get_next(_get_next(inputs[0]))


TEST1 = "abcdefgh"
TEST2 = "ghijklmn"


def main() -> None:
    my_aocd.print_header(2015, 11)

    assert _get_next(TEST1) == "abcdffaa"
    assert _get_next(TEST2) == "ghjaabcc"

    inputs = my_aocd.get_input(2015, 11, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
