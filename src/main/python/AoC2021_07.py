#! /usr/bin/env python3
#
# Advent of Code 2021 Day 7
#

from aoc import my_aocd


def _solve(inputs: tuple[str], calc) -> int:
    assert len(inputs) == 1
    positions = [int(_) for _ in inputs[0].split(',')]
    return min(sum(calc(a, b) for b in positions)
               for a in range(max(positions) + 1))


def part_1(inputs: tuple[str]) -> int:
    return _solve(inputs, lambda a, b: abs(a - b))


def part_2(inputs: tuple[str]) -> int:
    return _solve(inputs, lambda a, b: abs(a - b) * (abs(a - b) + 1) // 2)


TEST = """\
16,1,2,0,4,2,7,1,2,14
""".splitlines()


def main() -> None:
    my_aocd.print_header(2021, 7)

    assert part_1(TEST) == 37
    assert part_2(TEST) == 168

    inputs = my_aocd.get_input(2021, 7, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
