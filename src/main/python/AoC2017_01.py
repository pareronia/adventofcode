#! /usr/bin/env python3
#
# Advent of Code 2017 Day 1
#
from aoc import my_aocd
import aocd


def _sum_same_chars_at(string: str, distance: int) -> int:
    test = string + string[:distance]
    return sum([int(test[i])
                for i in range(len(string))
                if test[i] == test[i + distance]])


def part_1(inputs: tuple[str]) -> int:
    return _sum_same_chars_at(inputs[0], 1)


def part_2(inputs: tuple[str]) -> int:
    return _sum_same_chars_at(inputs[0], len(inputs[0]) // 2)


TEST1 = "1122".splitlines()
TEST2 = "1111".splitlines()
TEST3 = "1234".splitlines()
TEST4 = "91212129".splitlines()
TEST5 = "1212".splitlines()
TEST6 = "1221".splitlines()
TEST7 = "123425".splitlines()
TEST8 = "123123".splitlines()
TEST9 = "12131415".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2017, 1)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST1) == 3
    assert part_1(TEST2) == 4
    assert part_1(TEST3) == 0
    assert part_1(TEST4) == 9
    assert part_2(TEST5) == 6
    assert part_2(TEST6) == 0
    assert part_2(TEST7) == 4
    assert part_2(TEST8) == 12
    assert part_2(TEST9) == 4

    inputs = my_aocd.get_input(2017, 1, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()
