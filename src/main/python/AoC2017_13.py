#! /usr/bin/env python3
#
# Advent of Code 2017 Day 13
#

from aoc import my_aocd
from typing import NamedTuple
import aocd


class Layer(NamedTuple):
    depth: int
    range: int


def _parse(inputs: tuple[str]) -> list[Layer]:
    return [Layer(*map(int, layer.split(": "))) for layer in inputs]


def _caught(layer: Layer, delay: int) -> bool:
    return (layer.depth + delay) % ((layer.range - 1) * 2) == 0


def part_1(inputs: tuple[str]) -> int:
    layers = _parse(inputs)
    return sum(map(lambda layer: layer.depth * layer.range,
                   (layer for layer in layers if _caught(layer, 0))))


def part_2(inputs: tuple[str]) -> int:
    layers = _parse(inputs)
    delay = 0
    while any(_caught(layer, delay) for layer in layers):
        delay += 1
    return delay


TEST = '''\
0: 3
1: 2
4: 4
6: 4
'''.splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2017, 13)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 24
    assert part_2(TEST) == 10

    inputs = my_aocd.get_input(2017, 13, 43)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()
