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


REGEXP = r'([A-Za-z]+): capacity ([-0-9]+), durability ([-0-9]+), flavor ([-0-9]+), texture ([-0-9]+), calories ([-0-9]+)'  # noqa


@dataclass(frozen=True)
class Ingredient:
    name: str
    capacity: int
    durability: int
    flavor: int
    texture: int
    calories: int

    @classmethod
    def of(cls, name: str, capacity: str, durability: str, flavor: str,
           texture: str, calories: str) -> Ingredient:
        return Ingredient(name, int(capacity), int(durability), int(flavor),
                          int(texture), int(calories))


def _parse(inputs: tuple[str]) -> list[Ingredient]:
    return [Ingredient.of(*re.search(REGEXP, input_).groups())
            for input_ in inputs]


def _generate_measures(size: int):
    for x in itertools.product(range(101), repeat=size):
        if sum(x) == 100:
            for p in itertools.permutations(x, size):
                yield p


def _caclulate_score(ingredients: list[Ingredient],
                     measure: tuple[int],
                     calories_target: int = None) -> int:
    def calculate_total(attribute: str) -> int:
        return sum([measure[i] * getattr(ingredients[i], attribute)
                    for i in range(size)])

    assert len(measure) == len(ingredients)
    size = len(measure)
    if calories_target is not None:
        if calculate_total('calories') != calories_target:
            return 0
    totals = dict()
    for attribute in ['capacity', 'durability', 'flavor', 'texture']:
        totals[attribute] = calculate_total(attribute)
        if totals[attribute] < 0:
            return 0
    return math.prod(totals.values())


def _find_max_score(ingredients: list[Ingredient],
                    calories_target: int = None) -> int:
    max_score = 0
    for measure in _generate_measures(len(ingredients)):
        score = _caclulate_score(ingredients, measure, calories_target)
        if score > max_score:
            max_score = score
            print(max_score, end="\r", flush=True)
    return max_score


def part_1(inputs: tuple[str]) -> int:
    return _find_max_score(_parse(inputs))


def part_2(inputs: tuple[str]) -> int:
    return _find_max_score(_parse(inputs), calories_target=500)


TEST = """\
Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3
""".splitlines()


def main() -> None:
    my_aocd.print_header(2015, 15)

    assert part_1(TEST) == 62842880
    assert part_2(TEST) == 57600000

    inputs = my_aocd.get_input(2015, 15, 4)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
