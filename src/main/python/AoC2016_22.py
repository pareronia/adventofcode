#! /usr/bin/env python3
#
# Advent of Code 2016 Day 22
#

from __future__ import annotations
from typing import NamedTuple
import aocd
import re
from aoc import my_aocd


REGEX = r'^\/dev\/grid\/node-x([0-9]+)-y([0-9]+)'  \
        + r'\s+[0-9]+T\s+([0-9]+)T\s+([0-9]+)T\s+[0-9]+%$'


class Node(NamedTuple):
    x: int
    y: int
    used: int
    free: int

    @classmethod
    def of(cls, x: str, y: str, used: str, free: str) -> Node:
        return Node(int(x), int(y), int(used), int(free))

    def __eq__(self, other) -> bool:
        if other is None:
            return False
        return self.x == other.x and self.y == other.y

    def is_not_empty(self) -> bool:
        return self.used > 0


def _parse(inputs: tuple[str]) -> list[Node]:
    return [Node.of(*re.search(REGEX, i).groups()) for i in inputs[2:]]


def part_1(inputs: tuple[str]) -> str:
    nodes = _parse(inputs)
    return sum(1
               for b in nodes
               for a in nodes if a.is_not_empty()
               if a != b and a.used <= b.free)


def part_2(inputs: tuple[str]) -> str:
    return 0


TEST = '''\
root@ebhq-gridcenter# df -h
Filesystem            Size  Used  Avail  Use%
/dev/grid/node-x0-y0   10T    8T     2T   80%
/dev/grid/node-x0-y1   11T    6T     5T   54%
/dev/grid/node-x0-y2   32T   28T     4T   87%
/dev/grid/node-x1-y0    9T    7T     2T   77%
/dev/grid/node-x1-y1    8T    0T     8T    0%
/dev/grid/node-x1-y2   11T    7T     4T   63%
/dev/grid/node-x2-y0   10T    6T     4T   60%
/dev/grid/node-x2-y1    9T    8T     1T   88%
/dev/grid/node-x2-y2    9T    6T     3T   66%
'''.splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2016, 22)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 7
    assert part_2(TEST) == 0

    inputs = my_aocd.get_input_data(puzzle, 1052)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()
