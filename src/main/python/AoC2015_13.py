#! /usr/bin/env python3
#
# Advent of Code 2015 Day 13
#

from __future__ import annotations

import itertools
from collections import defaultdict
from typing import NamedTuple

import aocd

from aoc import my_aocd


class Happiness(NamedTuple):
    matrix: list[list[int]]

    @classmethod
    def from_input(cls, inputs: tuple[str]) -> Happiness:
        cnt = 0

        def get_and_increment():
            nonlocal cnt
            tmp = cnt
            cnt += 1
            return tmp

        idxs = defaultdict[str, int](get_and_increment)
        values = dict[tuple[int, int], int]()
        for line in inputs:
            splits = line[:-1].split(" ")
            values[(idxs[splits[0]], idxs[splits[10]])] = int(splits[3]) * (
                1 if splits[2] == "gain" else -1
            )
        matrix = [[0] * (len(idxs) + 1) for _ in range(len(idxs) + 1)]
        for k, v in values.items():
            matrix[k[0]][k[1]] = v
        return Happiness(matrix)

    def _solve(self, size: int) -> int:
        the_max = -1e9
        for p in itertools.permutations(range(size), size):
            the_sum = 0
            for i in range(size):
                d1, d2 = p[i], p[(i + 1) % size]
                the_sum += self.matrix[d1][d2] + self.matrix[d2][d1]
            the_max = max(the_max, the_sum)
        return the_max

    def get_optimal_happiness_change_without_me(self) -> int:
        return self._solve(len(self.matrix) - 1)

    def get_optimal_happiness_change_with_me(self) -> int:
        return self._solve(len(self.matrix))


def part_1(inputs: tuple[str]) -> int:
    return Happiness.from_input(
        inputs
    ).get_optimal_happiness_change_without_me()


def part_2(inputs: tuple[str]) -> int:
    return Happiness.from_input(inputs).get_optimal_happiness_change_with_me()


TEST = """\
Alice would gain 54 happiness units by sitting next to Bob.
Alice would lose 79 happiness units by sitting next to Carol.
Alice would lose 2 happiness units by sitting next to David.
Bob would gain 83 happiness units by sitting next to Alice.
Bob would lose 7 happiness units by sitting next to Carol.
Bob would lose 63 happiness units by sitting next to David.
Carol would lose 62 happiness units by sitting next to Alice.
Carol would gain 60 happiness units by sitting next to Bob.
Carol would gain 55 happiness units by sitting next to David.
David would gain 46 happiness units by sitting next to Alice.
David would lose 7 happiness units by sitting next to Bob.
David would gain 41 happiness units by sitting next to Carol.
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2015, 13)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 330

    inputs = my_aocd.get_input(puzzle.year, puzzle.day, 56)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()
