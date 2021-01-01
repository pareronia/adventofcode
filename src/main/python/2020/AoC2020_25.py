#! /usr/bin/env python3
#
# Advent of Code 2020 Day 25
#

import my_aocd


def _find_loop_size(key: int) -> int:
    loop_size = 0
    val = 1
    while val != key:
        loop_size += 1
        if loop_size % 10000 == 0:
            print(f"loop_size: {loop_size}", end="\r", flush=True)
        val = val * 7 % 20201227
    print("")
    return loop_size


def _find_encryption_key(pub_key: int, loop_size: int):
    return pow(pub_key, loop_size, 20201227)


def part_1(inputs: tuple[str]) -> int:
    pub_key1 = int(inputs[0])
    pub_key2 = int(inputs[1])
    loop_size2 = _find_loop_size(pub_key2)
    return _find_encryption_key(pub_key1, loop_size2)


test = """\
5764801
17807724
""".splitlines()


def main() -> None:
    my_aocd.print_header(2020, 25)

    assert part_1(test) == 14897079

    inputs = my_aocd.get_input_as_tuple(2020, 25, 2)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")


if __name__ == '__main__':
    main()
