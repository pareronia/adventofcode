#! /usr/bin/env python3
#
# Advent of Code 2015 Day 12
#

import json
from aoc import my_aocd


def _add_all_numbers(string: str) -> int:
    numbers = ""
    for c in string:
        if c in {',', '-'} or c.isnumeric():
            numbers += c
    return sum([int(_) for _ in numbers.split(',') if len(_) > 0])


def part_1(inputs: tuple[str]) -> int:
    assert len(inputs) == 1
    return _add_all_numbers(inputs[0])


def part_2(inputs: tuple[str]) -> int:
    def object_hook(obj: dict) -> dict:
        if "red" in obj.values():
            return None
        return obj

    assert len(inputs) == 1
    string = json.dumps(
        json.loads(inputs[0], object_hook=object_hook))
    return _add_all_numbers(string)


TEST1 = "[1,2,3]".splitlines()
TEST2 = "{\"a\":2,\"b\":4}".splitlines()
TEST3 = "[[[3]]]".splitlines()
TEST4 = "{\"a\":{\"b\":4},\"c\":-1}".splitlines()
TEST5 = "{\"a\":[-1,1]}".splitlines()
TEST6 = "[-1,{\"a\":1}]".splitlines()
TEST7 = "[]".splitlines()
TEST8 = "{}".splitlines()
TEST9 = "[1,{\"c\":\"red\",\"b\":2},3]".splitlines()
TEST10 = "{\"d\":\"red\",\"e\":[1,2,3,4],\"f\":5}".splitlines()
TEST11 = "[1,\"red\",5]".splitlines()


def main() -> None:
    my_aocd.print_header(2015, 12)

    assert part_1(TEST1) == 6
    assert part_1(TEST2) == 6
    assert part_1(TEST3) == 3
    assert part_1(TEST4) == 3
    assert part_1(TEST5) == 0
    assert part_1(TEST6) == 0
    assert part_1(TEST7) == 0
    assert part_1(TEST8) == 0
    assert part_2(TEST1) == 6
    assert part_2(TEST9) == 4
    assert part_2(TEST10) == 0
    assert part_2(TEST11) == 6

    inputs = my_aocd.get_input_as_tuple(2015, 12, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
