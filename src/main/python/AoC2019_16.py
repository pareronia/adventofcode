#! /usr/bin/env python3
#
# Advent of Code 2019 Day 16
#


import itertools
from collections.abc import Iterable, Iterator

import aocd

from aoc import my_aocd

ELEMENTS = (0, 1, 0, -1)
PHASES = 100


class Pattern:
    repeater: Iterator[int]

    def __init__(self, repeat):
        self.repeat = repeat
        self.elements = itertools.cycle(list(ELEMENTS))
        self.repeater = itertools.repeat(next(self.elements), self.repeat)

    def __iter__(self):
        return self

    def __next__(self) -> int:
        try:
            return next(self.repeater)
        except StopIteration:
            self.repeater = itertools.repeat(next(self.elements), self.repeat)
            return next(self.repeater)


class Patterns:
    n: int = 1

    def __next__(self) -> Iterable[int]:
        pattern = Pattern(self.n)
        self.n += 1
        next(pattern)
        return pattern


def _parse(inputs: tuple[str]) -> list[int]:
    return [int(_) for _ in inputs[0]]


def part_1(inputs: tuple[str]) -> str:
    nums = _parse(inputs)
    digits = nums[:]
    for _ in range(PHASES):
        patterns = Patterns()
        for j in range(len(nums)):
            pattern = next(patterns)
            digits[j] = abs(sum(a * b for a, b in zip(nums, pattern))) % 10
        nums = digits[:]
    return "".join(map(str, nums[:8]))


def part_2(inputs: tuple[str]) -> str:
    nums = _parse(inputs)
    tailsize = 10_000 * len(nums) - int("".join(map(str, nums[:7])))
    nums = nums * (tailsize // len(nums) + 1)
    start = len(nums) - tailsize
    for _ in range(PHASES):
        for j in range(len(nums) - 2, start - 1, -1):
            nums[j] = (nums[j] + nums[j + 1]) % 10
    return "".join(map(str, nums[start:][:8]))


TEST1 = "80871224585914546619083218645595".splitlines()
TEST2 = "19617804207202209144916044189917".splitlines()
TEST3 = "69317163492948606335995924319873".splitlines()
TEST4 = "03036732577212944063491565474664".splitlines()
TEST5 = "02935109699940807407585447034323".splitlines()
TEST6 = "03081770884921959731165446850517".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2019, 16)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST1) == "24176176"
    assert part_1(TEST2) == "73745418"
    assert part_1(TEST3) == "52432133"
    assert part_2(TEST4) == "84462026"
    assert part_2(TEST5) == "78725270"
    assert part_2(TEST6) == "53553731"

    inputs = my_aocd.get_input_data(puzzle, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()
