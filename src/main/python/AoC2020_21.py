#! /usr/bin/env python3
#
# Advent of Code 2020 Day 21
#

from dataclasses import dataclass
from collections import defaultdict
from typing import Iterator
from aoc import my_aocd
from aoc.common import log


@dataclass(frozen=True)
class Ingredient(str):
    pass


@dataclass(frozen=True)
class Allergen(str):
    pass


@dataclass(frozen=True)
class Food:
    ingredients: set[Ingredient]
    allergens: set[Allergen]


@dataclass(frozen=True)
class Foods(list[Food]):
    def __init__(self, foods: list[Food]):
        super().__init__(foods)

    def with_allergens(self, allergens: set[Allergen]) -> Iterator[Food]:
        for f in self:
            if f.allergens == allergens:
                yield f

    def with_single_allergen(self) -> Iterator[Food]:
        for f in self:
            if len(f.allergens) == 1:
                yield f

    def all_ingredients(self) -> list[Ingredient]:
        for f in self:
            for i in f.ingredients:
                yield i


@dataclass(frozen=True)
class Rule:
    allergen: Allergen
    ingredients: frozenset[Ingredient]

    def __hash__(self):
        return hash(self.allergen)

    def validate(self, a: Allergen, i: Ingredient) -> bool:
        return a == self.allergen and i in self.ingredients


def _parse(inputs: tuple[str]) -> list[Food]:
    allergens = set[Allergen]()
    ingredients = set[Ingredient]()
    foods = list[Food]()
    rules_dict = defaultdict(set)
    for input_ in inputs:
        splits = input_[:-1].split(" (contains")
        f_ingredients = set(splits[0].split())
        ingredients |= f_ingredients
        f_allergens = set([s.strip() for s in splits[1].split(",")])
        allergens |= f_allergens
        if len(f_allergens) == 1:
            rules_dict[list(f_allergens)[0]] |= f_ingredients
        foods.append(Food(f_ingredients, f_allergens))
    rules = [Rule(k, v) for k, v in rules_dict.items()]
    return foods, ingredients, allergens, rules


def _do_match(foods, ingredients: set[Ingredient],
              rules: set[Rule]) -> dict[int, list[Rule]]:
    matches = defaultdict(set)
    for i in ingredients:
        for f in foods:
            itt = ingredients & f.ingredients
            for a in f.allergens:
                [matches[i].add(rule) for i in itt
                 for rule in rules
                 if rule.validate(a, i)]
    return matches


def _find(fields, foods, ingredients: set[Ingredient],
          rules: set[Rule]) -> None:
    if len(ingredients) == 0 or len(rules) == 0:
        return
    log("Fields:")
    log(fields)
    matches = _do_match(foods, ingredients, rules)
    log("matches:")
    if len(matches) == 0:
        return
    [log(i) for i in matches.items()]
    temp = [i for i in matches.items()]
    temp.sort(key=lambda m: len(m[1]))
    if not len(temp[0][1]) == 1:
        raise RuntimeError("Expected single match")
    i_t = temp[0][0]
    r_t = list(temp[0][1])[0]
    fields.append((i_t, r_t.allergen))
    new_ingredients = set([i for i in ingredients if i != i_t])
    new_rules = set([r for r in rules if r != r_t])
    _find(fields, foods, new_ingredients, new_rules)


def _find_i_for_a(foods: Foods, allergens: set[Allergen]):
    possible = defaultdict(set)
    for f in foods:
        for a in f.allergens:
            possible[a] = possible[a] | f.ingredients
    log(possible)
    m = set[tuple[Allergen, Ingredient]]()
    while len(m) < len(allergens):
        for p in possible:
            for f in foods:
                for a in f.allergens:
                    if a in possible.keys():
                        possible[a] = possible[a] & f.ingredients
        log(possible)
        for k in possible.keys():
            if len(possible[k]) == 1:
                i = list(possible[k])[0]
                m.add((k, i))
                for v in possible.values():
                    if i in v:
                        v.remove(i)
        log(m)
    return m


def part_1(inputs: tuple[str]) -> int:
    _foods, ingredients, allergens, rules = _parse(inputs)
    foods = Foods(_foods)
    all_ingredients = [i for i in foods.all_ingredients()]
    i_with_a = _find_i_for_a(foods, allergens)
    log(i_with_a)
    ing = {_[1] for _ in i_with_a}
    return sum([1 for i in all_ingredients if i not in ing])


def part_2(inputs: tuple[str]) -> str:
    _foods, ingredients, allergens, rules = _parse(inputs)
    foods = Foods(_foods)
    i_with_a = list(_find_i_for_a(foods, allergens))
    log(i_with_a)
    i_with_a.sort(key=lambda x: x[0])
    log(i_with_a)
    ings = [_[1] for _ in i_with_a]
    return ",".join(ings)


test = """\
mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
trh fvjkl sbzzf mxmxvkd (contains dairy)
sqjhc fvjkl (contains soy)
sqjhc mxmxvkd sbzzf (contains fish)
""".splitlines()


def main() -> None:
    my_aocd.print_header(2020, 21)

    assert part_1(test) == 5
    assert part_2(test) == "mxmxvkd,sqjhc,fvjkl"

    inputs = my_aocd.get_input_as_tuple(2020, 21, 40)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
