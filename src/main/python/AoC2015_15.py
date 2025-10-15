#! /usr/bin/env python3
#
# Advent of Code 2015 Day 15
#

from __future__ import annotations

import re
from typing import TYPE_CHECKING
from typing import Self

if TYPE_CHECKING:
    from collections.abc import Iterator

import sys
from enum import IntEnum
from enum import unique
from math import prod
from typing import NamedTuple

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST = """\
Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3
"""

REGEXP = re.compile(r"([-0-9]+)")
Measures = tuple[int, ...]


@unique
class Property(IntEnum):
    CAPACITY = 0
    DURABILITY = 1
    FLAVOR = 2
    TEXTURE = 3
    CALORIES = 4


class Ingredients(NamedTuple):
    ingredients: list[list[int]]

    @classmethod
    def from_input(cls, inputs: InputData) -> Self:
        return cls([list(map(int, REGEXP.findall(line))) for line in inputs])

    def get_highest_score(self, calorie_limit: int | None) -> int:
        def generate_measures() -> Iterator[Measures]:
            for i in range(101):
                if len(self.ingredients) == 2:
                    yield (i, 100 - i)
                    continue
                for j in range(101 - i):
                    for k in range(101 - i - j):
                        yield (i, j, k, 100 - i - j - k)

        def score(measures: Measures, calories_limit: int | None) -> int:
            def get_product_score(p: Property) -> int:
                return sum(
                    ingredient[p.value] * measures[i]
                    for i, ingredient in enumerate(self.ingredients)
                )

            if (
                calories_limit is not None
                and get_product_score(Property.CALORIES) != calories_limit
            ):
                return 0
            return prod(
                max(0, get_product_score(p))
                for p in Property
                if p != Property.CALORIES
            )

        return max(score(m, calorie_limit) for m in generate_measures())


Input = Ingredients
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return Ingredients.from_input(input_data)

    def part_1(self, ingredients: Input) -> Output1:
        return ingredients.get_highest_score(calorie_limit=None)

    def part_2(self, ingredients: Input) -> Output2:
        return ingredients.get_highest_score(calorie_limit=500)

    @aoc_samples(
        (
            ("part_1", TEST, 62_842_880),
            ("part_2", TEST, 57_600_000),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2015, 15)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
