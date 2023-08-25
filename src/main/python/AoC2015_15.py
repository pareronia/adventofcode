#! /usr/bin/env python3
#
# Advent of Code 2015 Day 15
#

from __future__ import annotations

import re
from enum import Enum
from math import prod
from typing import Generator, NamedTuple

import aocd

from aoc import my_aocd

REGEXP = re.compile(r"([-0-9]+)")


class Property(Enum):
    CAPACITY = 0
    DURABILITY = 1
    FLAVOR = 2
    TEXTURE = 3
    CALORIES = 4


class Ingredients(NamedTuple):
    ingredients: list[list[int]]

    @classmethod
    def from_input(cls, inputs: tuple[str]) -> Ingredients:
        return Ingredients(
            [list(map(int, REGEXP.findall(line))) for line in inputs]
        )

    def _generate_measures(self) -> Generator[tuple[int, ...]]:
        for i in range(101):
            if len(self.ingredients) == 2:
                yield (i, 100 - i)
                continue
            for j in range(101 - i):
                for k in range(101 - i - j):
                    yield (i, j, k, 100 - i - j - k)

    def _get_property_score(
        self, measures: tuple[int, ...], p: Property
    ) -> int:
        return sum(
            self.ingredients[i][p.value] * measures[i]
            for i in range(len(self.ingredients))
        )

    def _calculate_score(
        self, measures: tuple[int, ...], calories_target: int | None
    ) -> int:
        if (
            calories_target is not None
            and self._get_property_score(measures, Property.CALORIES)
            != calories_target
        ):
            return 0
        return prod(
            max(0, self._get_property_score(measures, p))
            for p in Property
            if p != Property.CALORIES
        )

    def _get_maximum_score(self, limit: int | None) -> int:
        return max(
            self._calculate_score(m, limit) for m in self._generate_measures()
        )

    def get_highest_score(self):
        return self._get_maximum_score(None)

    def get_highest_score_with_calorie_limit(self, limit: int):
        return self._get_maximum_score(limit)


def part_1(inputs: tuple[str]) -> int:
    return Ingredients.from_input(inputs).get_highest_score()


def part_2(inputs: tuple[str]) -> int:
    ingredients = Ingredients.from_input(inputs)
    return ingredients.get_highest_score_with_calorie_limit(500)


TEST = """\
Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2015, 15)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 62_842_880
    assert part_2(TEST) == 57_600_000

    inputs = my_aocd.get_input(puzzle.year, puzzle.day, 4)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()
