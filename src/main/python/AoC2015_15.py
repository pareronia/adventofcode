#! /usr/bin/env python3
#
# Advent of Code 2020 Day 2
#

from __future__ import annotations
import re
import math
import itertools
from dataclasses import dataclass
from aoc import my_aocd
from aoc.common import log


REGEXP = r'([A-Za-z]+): capacity ([-0-9]+), durability ([-0-9]+), flavor ([-0-9]+), texture ([-0-9]+), calories [-0-9]+'  # noqa


@dataclass(frozen=True)
class Ingredient:
    name: str
    capacity: int
    durability: int
    flavor: int
    texture: int

    @classmethod
    def of(cls, name: str, capacity: str, durability: str, flavor: str,
           texture: str) -> Ingredient:
        return Ingredient(name, int(capacity), int(durability), int(flavor),
                          int(texture))


def _parse(inputs: tuple[str]) -> list[Ingredient]:
    return [Ingredient.of(*re.search(REGEXP, input_).groups())
            for input_ in inputs]


def _generate_measures(size: int):
    for x in itertools.product(range(101), repeat=size):
        if sum(x) == 100:
            for p in itertools.permutations(x, size):
                yield p


def _caclulate_score(ingredients: list[Ingredient],
                     measure: tuple[int]) -> int:
    assert len(measure) == len(ingredients)
    size = len(measure)
    total_capacity = sum([measure[i] * ingredients[i].capacity
                          for i in range(size)])
    if total_capacity < 0:
        return 0
    total_durability = sum([measure[i] * ingredients[i].durability
                            for i in range(size)])
    if total_durability < 0:
        return 0
    total_flavor = sum([measure[i] * ingredients[i].flavor
                        for i in range(size)])
    if total_flavor < 0:
        return 0
    total_texture = sum([measure[i] * ingredients[i].texture
                         for i in range(size)])
    if total_texture < 0:
        return 0
    return math.prod((total_capacity, total_durability,
                      total_flavor, total_texture))


def part_1(inputs: tuple[str]) -> int:
    ingredients = _parse(inputs)
    log(ingredients)
    max_score = 0
    for measure in _generate_measures(len(ingredients)):
        score = _caclulate_score(ingredients, measure)
        if score > max_score:
            max_score = score
            print(max_score, end="\r", flush=True)
    log(max_score)
    return max_score


def part_2(inputs: tuple[str]) -> int:
    return 0


TEST = """\
Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3
""".splitlines()


def main() -> None:
    my_aocd.print_header(2015, 15)

    assert part_1(TEST) == 62842880
    assert part_2(TEST) == 0

    inputs = my_aocd.get_input(2015, 15, 4)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
