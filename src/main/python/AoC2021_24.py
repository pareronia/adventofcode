#! /usr/bin/env python3
#
# Advent of Code 2021 Day 24
#

from aoc import my_aocd
import aocd
from aoc.common import log


DIGITS = 14


def _parse(inputs: tuple[str]) -> tuple[list[int], list[int], list[int]]:
    return ([int(s.split()[2])
            for i, s in enumerate(inputs)
            if i % 18 == offset]
            for offset in [4, 15, 5])


def _exec(a: int, b: int, c: int, w: int, z: int) -> int:
    if z % 26 + b == w:
        return z // a
    else:
        return (z // a) * 26 + w + c


def _solve(inputs: tuple[str], highest: bool) -> int:
    divz, addy, addx = _parse(inputs)
    wzz = dict[int, set[int]]()
    wzz[DIGITS] = {0}
    for i in range(DIGITS - 1, 0, -1):
        log("Looking for possible z input values, digit " + str(i + 1))
        zz = set[int]()
        for w in range(9, 0, -1):
            for z in range(0, 400_001):
                z_ = _exec(divz[i], addx[i], addy[i], w, z)
                if z_ in wzz[i + 1]:
                    zz.add(z)
        wzz[i] = zz
    ans = [0] * DIGITS
    if highest:
        range_ = [9, 8, 7, 6, 5, 4, 3, 2, 1]
    else:
        range_ = [1, 2, 3, 4, 5, 6, 7, 8, 9]
    z = 0
    for j in range(DIGITS):
        log("Finding digit " + str(j + 1) + " satisfying known valid z values")
        for w in range_:
            z_ = _exec(divz[j], addx[j], addy[j], w, z)
            if z_ in wzz[j + 1]:
                z = z_
                ans[j] = w
                break
    return int(''.join(str(_) for _ in ans))


def part_1(inputs: tuple[str]) -> int:
    return _solve(inputs, True)


def part_2(inputs: tuple[str]) -> int:
    return _solve(inputs, False)


def main() -> None:
    puzzle = aocd.models.Puzzle(2021, 24)
    my_aocd.print_header(puzzle.year, puzzle.day)

    inputs = my_aocd.get_input(puzzle.year, puzzle.day, 252)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()
