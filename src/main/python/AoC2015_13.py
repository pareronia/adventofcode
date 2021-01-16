#! /usr/bin/env python3
#
# Advent of Code 2015 Day 13
#

from typing import NamedTuple
from functools import lru_cache
import itertools
from aoc import my_aocd
from aoc.common import log


class Seating(NamedTuple):
    diner1: str
    diner2: str
    happiness_gain: int


class SeatingArrangement(NamedTuple):
    seatings: tuple[Seating]
    happiness_change: int


def _parse(inputs: tuple[str]) -> set[Seating]:
    def to_Seating(input_: str) -> Seating:
        splits = input_[:-1].split(" ")
        return Seating(splits[0], splits[10],
                       int(splits[3]) * (1 if splits[2] == "gain" else -1))

    seatings = {to_Seating(input_) for input_ in inputs}
    diners = {s.diner1 for s in seatings} | {s.diner2 for s in seatings}
    return diners, seatings


def _find_all_arrangements(diners: set[str],
                           seatings: set[Seating]) -> set[SeatingArrangement]:
    @lru_cache
    def find_happiness_gain(diner1: str, diner2: str) -> int:
        return sum(s.happiness_gain for s in seatings
                   if {s.diner1, s.diner2} == {diner1, diner2})

    log(diners)
    log(seatings)
    arrangements = {
        SeatingArrangement(c,
                           sum([find_happiness_gain(c[i], c[(i+1) % len(c)])
                                for i in range(len(c))]))
        for c in itertools.permutations(diners, len(diners))
    }
    log(find_happiness_gain.cache_info())
    return arrangements


def _log_arrangements(arrangements: set[SeatingArrangement]):
    log(f"SeatingArrangements: {len(arrangements)}")
    if len(arrangements) <= 24:
        [log(str(a)) for a in arrangements]


def part_1(inputs: tuple[str]) -> int:
    diners, seatings = _parse(inputs)
    arrangements = _find_all_arrangements(diners, seatings)
    _log_arrangements(arrangements)
    return max({a.happiness_change for a in arrangements})


def part_2(inputs: tuple[str]) -> int:
    diners, seatings = _parse(inputs)
    for d in diners:
        seatings.add(Seating("me", d, 0))
        seatings.add(Seating(d, "me", 0))
    diners.add("me")
    arrangements = _find_all_arrangements(diners, seatings)
    return max({a.happiness_change for a in arrangements})


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
    my_aocd.print_header(2015, 13)

    assert part_1(TEST) == 330

    inputs = my_aocd.get_input(2015, 13, 56)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
