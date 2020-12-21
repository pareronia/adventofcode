#! /usr/bin/env python3
#
# Advent of Code 2020 Day 21
#

from dataclasses import dataclass
from collections import defaultdict
from typing import Iterator
import my_aocd
from common import log


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


def part_1(inputs: tuple[str]) -> int:
    _foods, ingredients, allergens, rules = _parse(inputs)
    log(_foods)
    log(ingredients)
    log(allergens)
    log(rules)
    foods = Foods(_foods)
    fba = list(foods)
    fba.sort(key=lambda f: len(f.allergens), reverse=True)
    # log(fba)
    # nai = set[Allergen]()
    # for i, food in enumerate(fba):
    #     for ingredient in food.ingredients:
    #         foods.with_allergens(
    all_ingredients = [i for i in foods.all_ingredients()]
    inltoa = [i for i in all_ingredients if all_ingredients.count(i) == 1]
    log(inltoa)
    fields = list[tuple[Ingredient, Allergen]]()
    ftt = [f for f in foods if f not in foods.with_single_allergen()]
    itt = set(all_ingredients).difference(inltoa)
    _find(fields, ftt, itt,  rules)
    log(fields)
    f_i = [f[0] for f in fields]
    fields = list[tuple[Ingredient, Allergen]]()
    inltoa.extend([i
                   for i in set(all_ingredients).difference(inltoa)
                   if i not in f_i])
    log(inltoa)
    log(len(inltoa))
    for i in inltoa:
        for rule in rules:
            ii = rule.ingredients.difference({i})
            if len(ii) == 1:
                iii = list(ii)[0]
                if iii in f_i:
                    inltoa.remove(i)
    log(inltoa)
    log(len(inltoa))
    return sum([all_ingredients.count(i) for i in inltoa])


def part_2(inputs: tuple[str]) -> int:
    return 0


test = """\
mxmxvkd kfcds sqjhc nhms (contains dairy, fish)
trh fvjkl sbzzf mxmxvkd (contains dairy)
sqjhc fvjkl (contains soy)
sqjhc mxmxvkd sbzzf (contains fish)
""".splitlines()


def main() -> None:
    my_aocd.print_header(2020, 21)

    assert part_1(test) == 5
    assert part_2(test) == 0

    inputs = my_aocd.get_input_as_tuple(2020, 21, 40)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    # result2 = part_2(inputs)
    # print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
