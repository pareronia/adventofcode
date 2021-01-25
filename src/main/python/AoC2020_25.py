#! /usr/bin/env python3
#
# Advent of Code 2020 Day 25
#

from aoc import my_aocd


def _find_loop_size(key: int) -> int:
    loop_size = 0
    val = 1
    while val != key:
        loop_size += 1
        val = val * 7 % 20201227
    return loop_size


def _find_encryption_key(pub_key: int, loop_size: int):
    return pow(pub_key, loop_size, 20201227)


def part_1(inputs: tuple[str]) -> int:
    pub_key1 = int(inputs[0])
    pub_key2 = int(inputs[1])
    loop_size2 = _find_loop_size(pub_key2)
    return _find_encryption_key(pub_key1, loop_size2)


def part_2(inputs: tuple[str]) -> int:
    return


TEST = """\
5764801
17807724
""".splitlines()


def main() -> None:
    my_aocd.print_header(2020, 25)

    assert part_1(TEST) == 14897079

    inputs = my_aocd.get_input(2020, 25, 2)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
