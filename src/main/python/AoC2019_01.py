#! /usr/bin/env python3
#
# Advent of Code 2019 Day 1
#

from aoc import my_aocd


def _parse(inputs: tuple[str]) -> tuple[int]:
    return (int(_) for _ in inputs)


def _fuel_for_mass(m: int) -> int:
    return m // 3 - 2


def part_1(inputs: tuple[str]) -> int:
    return sum(_fuel_for_mass(m) for m in _parse(inputs))


def _rocket_equation(m: int) -> int:
    total_fuel = 0
    while (fuel := _fuel_for_mass(m)) > 0:
        total_fuel += fuel
        m = fuel
    return total_fuel


def part_2(inputs: tuple[str]) -> int:
    return sum(_rocket_equation(m) for m in _parse(inputs))


TEST1 = "12".splitlines()
TEST2 = "14".splitlines()
TEST3 = "1969".splitlines()
TEST4 = "100756".splitlines()


def main() -> None:
    my_aocd.print_header(2019, 1)

    assert part_1(TEST1) == 2
    assert part_1(TEST2) == 2
    assert part_1(TEST3) == 654
    assert part_1(TEST4) == 33583
    assert part_2(TEST1) == 2
    assert part_2(TEST3) == 966
    assert part_2(TEST4) == 50346

    inputs = my_aocd.get_input(2019, 1, 100)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
