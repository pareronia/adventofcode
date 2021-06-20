#! /usr/bin/env python3
#
# Advent of Code 2016 Day 18
#
# TODO : try w/ numpy

from aoc import my_aocd

SAFE = '.'
TRAP = '^'
TRAPS = ('^^.', '.^^', '^..', '..^')


def _parse(inputs: tuple[str]) -> str:
    assert len(inputs) == 1
    return inputs[0]


def _get_previous(s: str, i: int) -> str:
    first = SAFE if i - 1 < 0 else s[i-1]
    second = s[i]
    third = SAFE if i + 1 == len(s) else s[i+1]
    return first + second + third


def _solve(first_row: str, rows: int) -> int:
    width = len(first_row)
    safe_count = first_row.count(SAFE)
    prev_row = first_row
    for _ in range(1, rows):
        new_row = str()
        for j in range(width):
            prev = _get_previous(prev_row, j)
            if prev in TRAPS:
                new_row += TRAP
            else:
                new_row += SAFE
                safe_count += 1
        prev_row = new_row
    return safe_count


def part_1(inputs: tuple[str]) -> int:
    return _solve(_parse(inputs), 40)


def part_2(inputs: tuple[str]) -> int:
    return _solve(_parse(inputs), 400_000)


def main() -> None:
    my_aocd.print_header(2016, 18)

    assert _solve("..^^.", 3) == 6
    assert _solve(".^^.^.^^^^", 10) == 38

    inputs = my_aocd.get_input(2016, 18, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
