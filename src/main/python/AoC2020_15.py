#! /usr/bin/env python3
#
# Advent of Code 2020 Day 15
#

from aoc import my_aocd
from aoc.common import log


def _parse(inputs: tuple[str]) -> tuple[int]:
    assert len(inputs) == 1
    return tuple([int(n) for n in inputs[0].split(",")])


def _play(starting_numbers: tuple[int], number_of_turns: int) -> int:
    log(f"{starting_numbers} ({number_of_turns} turns)")
    turns = [-1] * number_of_turns
    for i, n in enumerate(starting_numbers[:-1], start=1):
        turns[n] = i
    prev = starting_numbers[-1]
    for i in range(len(starting_numbers), number_of_turns):
        prev_prev = turns[prev]
        turns[prev] = i
        prev = 0 if prev_prev == -1 else i - prev_prev
    return prev


def part_1(inputs: tuple[str]) -> int:
    return _play(starting_numbers=_parse(inputs), number_of_turns=2020)


def part_2(inputs: tuple[str]) -> int:
    return _play(starting_numbers=_parse(inputs), number_of_turns=30000000)


TEST1 = "0,3,6".splitlines()
TEST2 = "1,3,2".splitlines()
TEST3 = "2,1,3".splitlines()
TEST4 = "1,2,3".splitlines()
TEST5 = "2,3,1".splitlines()
TEST6 = "3,2,1".splitlines()
TEST7 = "3,1,2".splitlines()


def main() -> None:
    my_aocd.print_header(2020, 15)

    assert part_1(TEST1) == 436
    assert part_1(TEST2) == 1
    assert part_1(TEST3) == 10
    assert part_1(TEST4) == 27
    assert part_1(TEST5) == 78
    assert part_1(TEST6) == 438
    assert part_1(TEST7) == 1836
    assert part_2(TEST1) == 175594
    assert part_2(TEST2) == 2578
    assert part_2(TEST3) == 3544142
    assert part_2(TEST4) == 261214
    assert part_2(TEST5) == 6895259
    assert part_2(TEST6) == 18
    assert part_2(TEST7) == 362

    inputs = my_aocd.get_input(2020, 15, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
