#! /usr/bin/env python3
#
# Advent of Code 2022 Day 6
#


import aocd
from aoc import my_aocd


def solve(buffer: str, size: int) -> int:
    for i in range(size, len(buffer)):
        test = buffer[i - size : i]  # noqa
        if len(set(test)) == size:
            return i


def part_1(inputs: tuple[str]) -> int:
    return solve(inputs[0], 4)


def part_2(inputs: tuple[str]) -> int:
    return solve(inputs[0], 14)


TEST1 = "mjqjpqmgbljsphdztnvjfqwrcgsmlb".splitlines()
TEST2 = "bvwbjplbgvbhsrlpgdmjqwftvncz".splitlines()
TEST3 = "nppdvjthqldpwncqszvftbrmjlhg".splitlines()
TEST4 = "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg".splitlines()
TEST5 = "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2022, 6)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST1) == 7
    assert part_1(TEST2) == 5
    assert part_1(TEST3) == 6
    assert part_1(TEST4) == 10
    assert part_1(TEST5) == 11
    assert part_2(TEST1) == 19
    assert part_2(TEST2) == 23
    assert part_2(TEST3) == 23
    assert part_2(TEST4) == 29
    assert part_2(TEST5) == 26

    inputs = my_aocd.get_input_data(puzzle, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()
