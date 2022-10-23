#! /usr/bin/env python3
#
# Advent of Code 2017 Day 10
#

from aoc import my_aocd
from aoc.knothash import KnotHash
import aocd


def _solve_1(inputs: tuple[str], elements: list[int]) -> int:
    lengths = [int(_) for _ in inputs[0].split(",")]
    ans = KnotHash.round(KnotHash.State(elements, lengths, 0, 0))
    return ans.elements[0] * ans.elements[1]


def part_1(inputs: tuple[str]) -> int:
    return _solve_1(inputs, [_ for _ in KnotHash.SEED])


def part_2(inputs: tuple[str]) -> int:
    return KnotHash.hex_string(inputs[0])


TEST1 = """3,4,1,5""".splitlines()
TEST2 = """AoC 2017""".splitlines()
TEST3 = """1,2,3""".splitlines()
TEST4 = """1,2,4""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2017, 10)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert _solve_1(TEST1, [0, 1, 2, 3, 4]) == 12
    assert part_2(TEST2) == "33efeb34ea91902bb2f59c9920caa6cd"
    assert part_2(TEST3) == "3efbe78a8d82f29979031a4aa0b16a9d"
    assert part_2(TEST4) == "63960835bcdc130f0b66d7ff4f6a5a8e"

    inputs = my_aocd.get_input(2017, 10, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()
