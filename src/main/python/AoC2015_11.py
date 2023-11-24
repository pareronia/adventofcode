#! /usr/bin/env python3
#
# Advent of Code 2015 Day 11
#

import re

import aocd

from aoc import my_aocd

ALPH = "abcdefghijklmnopqrstuvwxyz"
NEXT = "bcdefghjjkmmnppqrstuvwxyza"
CONFUSING_LETTERS = {"i", "o", "l"}
RE = re.compile(r"([a-z])\1")


def _is_ok(password: str) -> bool:
    if len({p for p in RE.findall(password)}) < 2:
        return False
    trio = False
    for i in range(len(password)):
        if password[i] in CONFUSING_LETTERS:
            return False
        trio = (
            trio
            or i < len(password) - 3
            and ord(password[i]) == ord(password[i + 1]) - 1
            and ord(password[i + 1]) == ord(password[i + 2]) - 1
        )
    return trio


def _get_next(password: str) -> str:
    def _increment(password: str, i: int) -> str:
        password = (
            password[:i]
            + NEXT[ALPH.index(password[i])]
            + password[i + 1 :]  # noqa E203
        )
        if password[i] == "a":
            password = _increment(password, i - 1)
        return password

    while True:
        password = _increment(password, len(password) - 1)
        if _is_ok(password):
            break
    return password


def part_1(inputs: tuple[str]) -> str:
    assert len(inputs) == 1
    return _get_next(inputs[0])


def part_2(inputs: tuple[str]) -> str:
    assert len(inputs) == 1
    return _get_next(_get_next(inputs[0]))


def main() -> None:
    puzzle = aocd.models.Puzzle(2015, 11)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert not _is_ok("abci")
    assert not _is_ok("hijklmmn")
    assert not _is_ok("abbceffg")
    assert not _is_ok("abbcegjk")
    assert _get_next("abcdefgh") == "abcdffaa"
    assert _get_next("ghijklmn") == "ghjaabcc"

    inputs = my_aocd.get_input_data(puzzle, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()
