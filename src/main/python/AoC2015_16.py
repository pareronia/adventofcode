#! /usr/bin/env python3
#
# Advent of Code 2015 Day 16
#

from dataclasses import dataclass
from collections import defaultdict
from aoc import my_aocd
from aoc.common import log


@dataclass(frozen=True)
class AuntSue:
    nbr: int
    things: dict

    def match_thing(self, thing: str, amount: int) -> bool:
        return thing in self.things and self.things[thing] == amount

    def get_thing(self, thing: str) -> int:
        return self.things[thing] if thing in self.things else None

    def has_thing(self, thing: str) -> bool:
        return thing in self.things


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


def part_1(inputs: tuple[str]) -> int:
    aunt_sues = _parse(inputs)
    log(aunt_sues)
    scores = defaultdict(int)
    for aunt_sue in aunt_sues:
        if aunt_sue.has_thing('children'):
            if aunt_sue.get_thing('children') == 3:
                scores[aunt_sue.nbr] += 1
            else:
                continue
        if aunt_sue.has_thing('cats'):
            if aunt_sue.get_thing('cats') == 7:
                scores[aunt_sue.nbr] += 1
            else:
                continue
        if aunt_sue.has_thing('samoyeds'):
            if aunt_sue.get_thing('samoyeds') == 2:
                scores[aunt_sue.nbr] += 1
            else:
                continue
        if aunt_sue.has_thing('pomeranians'):
            if aunt_sue.get_thing('pomeranians') == 3:
                scores[aunt_sue.nbr] += 1
            else:
                continue
        if aunt_sue.has_thing('akitas'):
            if aunt_sue.get_thing('akitas') == 0:
                scores[aunt_sue.nbr] += 1
            else:
                continue
        if aunt_sue.has_thing('vizslas'):
            if aunt_sue.get_thing('vizslas') == 0:
                scores[aunt_sue.nbr] += 1
            else:
                continue
        if aunt_sue.has_thing('goldfish'):
            if aunt_sue.get_thing('goldfish') == 5:
                scores[aunt_sue.nbr] += 1
            else:
                continue
        if aunt_sue.has_thing('trees'):
            if aunt_sue.get_thing('trees') == 3:
                scores[aunt_sue.nbr] += 1
            else:
                continue
        if aunt_sue.has_thing('cars'):
            if aunt_sue.get_thing('cars') == 2:
                scores[aunt_sue.nbr] += 1
            else:
                continue
        if aunt_sue.has_thing('perfumes'):
            if aunt_sue.get_thing('perfumes') == 1:
                scores[aunt_sue.nbr] += 1
            else:
                continue
    best_score = None
    for score in scores.items():
        if best_score is None or score[1] > best_score[1]:
            best_score = score
    return best_score[0]


def part_2(inputs: tuple[str]) -> int:
    aunt_sues = _parse(inputs)
    log(aunt_sues)
    scores = defaultdict(int)
    for aunt_sue in aunt_sues:
        if aunt_sue.has_thing('children'):
            if aunt_sue.get_thing('children') == 3:
                scores[aunt_sue.nbr] += 1
            else:
                continue
        if aunt_sue.has_thing('cats'):
            if aunt_sue.get_thing('cats') > 7:
                scores[aunt_sue.nbr] += 1
            else:
                continue
        if aunt_sue.has_thing('samoyeds'):
            if aunt_sue.get_thing('samoyeds') == 2:
                scores[aunt_sue.nbr] += 1
            else:
                continue
        if aunt_sue.has_thing('pomeranians'):
            if aunt_sue.get_thing('pomeranians') < 3:
                scores[aunt_sue.nbr] += 1
            else:
                continue
        if aunt_sue.has_thing('akitas'):
            if aunt_sue.get_thing('akitas') == 0:
                scores[aunt_sue.nbr] += 1
            else:
                continue
        if aunt_sue.has_thing('vizslas'):
            if aunt_sue.get_thing('vizslas') == 0:
                scores[aunt_sue.nbr] += 1
            else:
                continue
        if aunt_sue.has_thing('goldfish'):
            if aunt_sue.get_thing('goldfish') < 5:
                scores[aunt_sue.nbr] += 1
            else:
                continue
        if aunt_sue.has_thing('trees'):
            if aunt_sue.get_thing('trees') > 3:
                scores[aunt_sue.nbr] += 1
            else:
                continue
        if aunt_sue.has_thing('cars'):
            if aunt_sue.get_thing('cars') == 2:
                scores[aunt_sue.nbr] += 1
            else:
                continue
        if aunt_sue.has_thing('perfumes'):
            if aunt_sue.get_thing('perfumes') == 1:
                scores[aunt_sue.nbr] += 1
            else:
                continue
    best_score = None
    for score in scores.items():
        if best_score is None or score[1] > best_score[1]:
            best_score = score
    return best_score[0]


def main() -> None:
    my_aocd.print_header(2015, 16)

    inputs = my_aocd.get_input(2015, 16, 500)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
