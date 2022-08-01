#! /usr/bin/env python3
#
# Advent of Code 2020 Day 5
#
from aoc import my_aocd
import aocd


def _parse(inputs: tuple[str]) -> list[str]:
    table = str.maketrans("FBLR", "0101")
    return [line.translate(table) for line in inputs]


def _as_int(binary: str) -> int:
    return int(binary, 2)


def part_1(inputs: tuple[str]) -> int:
    translated = _parse(inputs)
    return max(_as_int(_) for _ in translated)


def part_2(inputs: tuple[str]) -> int:
    translated = _parse(inputs)
    translated.sort()
    for i in range(len(translated)):
        if i+1 == len(translated):
            break
        if translated[i][-1] == translated[i+1][-1]:
            return _as_int(translated[i]) + 1
    raise ValueError("Unsolvable")


TEST1 = """\
FBFBBFFRLR
BFFFBBFRRR
FFFBBBFRRR
BBFFBBFRLL
""".splitlines()


TEST2 = """\
FFFFFFFLLL
FFFFFFFLLR
FFFFFFFLRL
FFFFFFFRLL
FFFFFFFRLR
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2020, 5)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST1) == 820
    assert part_2(TEST2) == 3

    inputs = my_aocd.get_input(2020, 5, 839)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()
