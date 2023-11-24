#! /usr/bin/env python3
#
# Advent of Code 2015 Day 9
#

from __future__ import annotations

import itertools
from collections import defaultdict
from typing import Generator, NamedTuple

import aocd

from aoc import my_aocd


class Distances(NamedTuple):
    matrix: list[list[int]]

    @classmethod
    def from_input(cls, inputs: tuple[str]) -> Distances:
        cnt = 0

        def get_and_increment():
            nonlocal cnt
            tmp = cnt
            cnt += 1
            return tmp

        idxs = defaultdict[str, int](get_and_increment)
        values = dict[tuple[int, int], int]()
        for line in inputs:
            splits = line.split(" ")
            values[(idxs[splits[0]], idxs[splits[2]])] = int(splits[4])
        matrix = [[0] * (len(idxs)) for _ in range(len(idxs))]
        for k, v in values.items():
            matrix[k[0]][k[1]] = v
            matrix[k[1]][k[0]] = v
        return Distances(matrix)

    def get_distances_of_complete_routes(self) -> Generator[int]:
        size = len(self.matrix)
        for p in itertools.permutations(range(size), size):
            yield sum(self.matrix[p[i - 1]][p[i]] for i in range(1, size))


def part_1(inputs: tuple[str]) -> int:
    return min(Distances.from_input(inputs).get_distances_of_complete_routes())


def part_2(inputs: tuple[str]) -> int:
    return max(Distances.from_input(inputs).get_distances_of_complete_routes())


TEST = """\
London to Dublin = 464
London to Belfast = 518
Dublin to Belfast = 141
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2015, 9)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 605
    assert part_2(TEST) == 982

    inputs = my_aocd.get_input(puzzle.year, puzzle.day, 28)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()
