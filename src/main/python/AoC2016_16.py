#! /usr/bin/env python3
#
# Advent of Code 2016 Day 16
#

from aoc import my_aocd


TABLE = str.maketrans('01', '10')


def _parse(inputs: tuple[str]) -> str:
    assert len(inputs) == 1
    return inputs[0]


def _dragon_curve(input_: str) -> str:
    return input_ + "0" + input_[::-1].translate(TABLE)


def _checksum(data: list[str]) -> str:
    pairs = ['1' if data[i] == data[i+1] else '0'
             for i in range(0, len(data) - 1, 2)]
    return pairs if len(pairs) % 2 != 0 else _checksum(pairs)


def _solve(data: str, size: int) -> str:
    while len(data) < size:
        data = _dragon_curve(data)
    return "".join(_checksum(list(data[:size])))


def part_1(inputs: tuple[str]) -> str:
    return _solve(_parse(inputs), 272)


def part_2(inputs: tuple[str]) -> str:
    return _solve(_parse(inputs), 35651584)


def main() -> None:
    my_aocd.print_header(2016, 16)

    assert _solve("10000", 20) == "01100"

    inputs = my_aocd.get_input(2016, 16, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
