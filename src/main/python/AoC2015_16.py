#! /usr/bin/env python3
#
# Advent of Code 2015 Day 16
#

import itertools
import operator
import sys
from collections import defaultdict
from dataclasses import dataclass
from typing import Self
from typing import cast

from aoc.common import InputData
from aoc.common import SolutionBase

EQ, LT, GT = "eq", "lt", "gt"
OPS = {EQ: operator.eq, LT: operator.lt, GT: operator.gt}


@dataclass(frozen=True)
class AuntSue:
    nbr: int
    things: dict[str, int]

    @classmethod
    def from_input(cls, line: str) -> Self:
        splits = line.split(", ")
        nbr = splits[0].split(": ")[0].split(" ")[1]
        splits = line[len("Sue " + nbr + ": ") :].split(", ")
        things = {
            thing: int(amount)
            for thing, amount in (split.split(": ") for split in splits)
        }
        return cls(int(nbr), things)

    def get_thing(self, thing: str) -> int | None:
        return self.things.get(thing)


@dataclass(frozen=True)
class Rule:
    operation: str
    operand: int

    def matches(self, value: int) -> bool:
        return cast("bool", OPS[self.operation](value, self.operand))


Input = list[AuntSue]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [AuntSue.from_input(line) for line in input_data]

    def find_aunt_sue_with_best_score(
        self, aunt_sues: list[AuntSue], rules: dict[str, Rule]
    ) -> int:
        scores = defaultdict[int, int](int)
        for aunt_sue, thing in itertools.product(aunt_sues, rules.keys()):
            value = aunt_sue.get_thing(thing)
            if value is not None and rules[thing].matches(value):
                scores[aunt_sue.nbr] += 1
        return max(scores.items(), key=operator.itemgetter(1))[0]

    def part_1(self, aunt_sues: Input) -> Output1:
        rules = {
            "children": Rule(EQ, 3),
            "cats": Rule(EQ, 7),
            "samoyeds": Rule(EQ, 2),
            "pomeranians": Rule(EQ, 3),
            "akitas": Rule(EQ, 0),
            "vizslas": Rule(EQ, 0),
            "goldfish": Rule(EQ, 5),
            "trees": Rule(EQ, 3),
            "cars": Rule(EQ, 2),
            "perfumes": Rule(EQ, 1),
        }
        return self.find_aunt_sue_with_best_score(aunt_sues, rules)

    def part_2(self, aunt_sues: Input) -> Output2:
        rules = {
            "children": Rule(EQ, 3),
            "cats": Rule("gt", 7),
            "samoyeds": Rule(EQ, 2),
            "pomeranians": Rule(LT, 3),
            "akitas": Rule(EQ, 0),
            "vizslas": Rule(EQ, 0),
            "goldfish": Rule(LT, 5),
            "trees": Rule(GT, 3),
            "cars": Rule(EQ, 2),
            "perfumes": Rule(EQ, 1),
        }
        return self.find_aunt_sue_with_best_score(aunt_sues, rules)

    def samples(self) -> None:
        pass


solution = Solution(2015, 16)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
