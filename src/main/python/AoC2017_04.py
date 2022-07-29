#! /usr/bin/env python3
#
# Advent of Code 2017 Day 4
#
from collections import Counter
from typing import Callable
from aoc import my_aocd
import aocd


def _parse(inputs: tuple[str]) -> int:
    assert len(inputs) == 1
    return int(inputs[0])


def _has_no_duplicate_words(string: str) -> bool:
    return Counter(string.split()).most_common(1)[0][1] == 1


def _has_no_anagrams(string: str) -> bool:
    words = string.split()
    letter_counts = {tuple(sorted(dict(Counter(word)).items()))
                     for word in words}
    return len(words) == len(letter_counts)


def _solve(inputs: tuple[str], strategy: Callable) -> int:
    return sum(1 for _ in inputs if strategy(_))


def part_1(inputs: tuple[str]) -> int:
    return _solve(inputs, _has_no_duplicate_words)


def part_2(inputs: tuple[str]) -> int:
    return _solve(inputs, _has_no_anagrams)


def main() -> None:
    puzzle = aocd.models.Puzzle(2017, 4)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(("aa bb cc dd ee",)) == 1
    assert part_1(("aa bb cc dd aa",)) == 0
    assert part_1(("aa bb cc dd aaa",)) == 1
    assert part_2(("abcde fghij",)) == 1
    assert part_2(("abcde xyz ecdab",)) == 0
    assert part_2(("a ab abc abd abf abj",)) == 1
    assert part_2(("iiii oiii ooii oooi oooo",)) == 1
    assert part_2(("oiii ioii iioi iiio",)) == 0

    inputs = my_aocd.get_input(2017, 4, 512)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()
