#! /usr/bin/env python3
#
# Advent of Code 2017 Day 6
#
from aoc import my_aocd
import aocd


def _parse(inputs: tuple[str]) -> list[int]:
    return [int(_) for _ in inputs[0].split()]


def _solve(inputs: tuple[str]):
    banks = tuple(_parse(inputs))
    cnt = 0
    seen = dict()
    while banks not in seen:
        seen[banks] = cnt
        idx = banks.index(max(banks))
        blocks = banks[idx]
        _banks = [_ for _ in banks]
        _banks[idx] = 0
        for i in range(1, blocks + 1, 1):
            j = (idx + i) % len(_banks)
            _banks[j] += 1
        banks = tuple(_banks)
        cnt += 1
    return seen, banks


def part_1(inputs: tuple[str]) -> int:
    seen, _ = _solve(inputs)
    return len(seen)


def part_2(inputs: tuple[str]) -> int:
    seen, last = _solve(inputs)
    return len(seen) - seen[last]


TEST = """0 2 7 0""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2017, 6)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 5
    assert part_2(TEST) == 4

    inputs = my_aocd.get_input(2017, 6, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()
