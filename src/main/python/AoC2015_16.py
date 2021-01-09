#! /usr/bin/env python3
#
# Advent of Code 2015 Day 16
#

import operator
import itertools
from dataclasses import dataclass
from collections import defaultdict
from aoc import my_aocd


@dataclass(frozen=True)
class AuntSue:
    nbr: int
    things: dict

    def get_thing(self, thing: str) -> int:
        return self.things[thing] if thing in self.things else None


@dataclass(frozen=True)
class Rule:
    operation: str
    operand: int

    def matches(self, value) -> bool:
        if self.operation == "eq":
            return value is not None and value == self.operand
        elif self.operation == "lt":
            return value is not None and value < self.operand
        elif self.operation == "gt":
            return value is not None and value > self.operand
        else:
            raise RuntimeError("Unsupported operation")


def _parse(inputs: tuple[str]) -> list[AuntSue]:
    aunt_sues = list[AuntSue]()
    for input_ in inputs:
        splits = input_.split(", ")
        nbr = splits[0].split(": ")[0].split(" ")[1]
        splits = input_[len("Sue " + nbr + ": "):].split(", ")
        things = dict()
        for split in splits:
            thing_splits = split.split(": ")
            things[thing_splits[0]] = int(thing_splits[1])
        aunt_sues.append(AuntSue(int(nbr), things))
    return aunt_sues


def _find_aunt_sue_with_best_score(aunt_sues: list[AuntSue],
                                   rules: dict) -> int:
    scores = defaultdict(int)
    for p in itertools.product(aunt_sues, rules.keys()):
        aunt_sue = p[0]
        thing = p[1]
        if rules[thing].matches(aunt_sue.get_thing(thing)):
            scores[aunt_sue.nbr] += 1
    return max(scores.items(), key=operator.itemgetter(1))[0]


def part_1(inputs: tuple[str]) -> int:
    aunt_sues = _parse(inputs)
    rules = {'children': Rule("eq", 3),
             'cats': Rule("eq", 7),
             'samoyeds': Rule("eq", 2),
             'pomeranians': Rule("eq", 3),
             'akitas': Rule("eq", 0),
             'vizslas': Rule("eq", 0),
             'goldfish': Rule("eq", 5),
             'trees': Rule("eq", 3),
             'cars': Rule("eq", 2),
             'perfumes': Rule("eq", 1),
             }
    return _find_aunt_sue_with_best_score(aunt_sues, rules)


def part_2(inputs: tuple[str]) -> int:
    aunt_sues = _parse(inputs)
    rules = {'children': Rule("eq", 3),
             'cats': Rule("gt", 7),
             'samoyeds': Rule("eq", 2),
             'pomeranians': Rule("lt", 3),
             'akitas': Rule("eq", 0),
             'vizslas': Rule("eq", 0),
             'goldfish': Rule("lt", 5),
             'trees': Rule("gt", 3),
             'cars': Rule("eq", 2),
             'perfumes': Rule("eq", 1),
             }
    return _find_aunt_sue_with_best_score(aunt_sues, rules)


def main() -> None:
    my_aocd.print_header(2015, 16)

    inputs = my_aocd.get_input(2015, 16, 500)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
