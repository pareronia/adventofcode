#! /usr/bin/env python3
#
# Advent of Code 2015 Day 9
#

from typing import NamedTuple
from functools import lru_cache
import itertools
from aoc import my_aocd
from aoc.common import log


class Leg(NamedTuple):
    frôm: str
    to: str
    distance: int


class Route(NamedTuple):
    stops: tuple[str]
    total: int

    def __repr__(self):
        return " -> ".join(self.stops) + " = " + str(self.total)


def _parse(inputs: tuple[str]) -> set[Leg]:
    def to_Leg(input_: str) -> Leg:
        _, dist = input_.split(" = ")
        return Leg(*_.split(" to "), int(dist))

    return {to_Leg(input_) for input_ in inputs}


def _find_all_routes(legs: set[Leg]) -> set[Route]:
    @lru_cache
    def find_distance(frôm: str, to: str) -> int:
        return next(d.distance for d in legs
                    if {frôm, to} == {d.frôm, d.to})

    stops = {d.frôm for d in legs} | {d.to for d in legs}
    log("Stops:")
    log(stops)
    log("Legs:")
    log(legs)
    routes = {
        Route(
            c,
            sum([find_distance(c[i], c[i+1]) for i in range(len(c) - 1)]))
        for c in itertools.permutations(stops, len(stops))
    }
    log(find_distance.cache_info())
    return routes


def _log_routes(routes: set[Route]):
    if len(routes) <= 24:
        log(f"Routes: {len(routes)}")
        [log(str(r)) for r in routes]


def part_1(inputs: tuple[str]) -> int:
    routes = _find_all_routes(_parse(inputs))
    _log_routes(routes)
    return min({r.total for r in routes})


def part_2(inputs: tuple[str]) -> int:
    return max({r.total for r in _find_all_routes(_parse(inputs))})


TEST = """\
London to Dublin = 464
London to Belfast = 518
Dublin to Belfast = 141
""".splitlines()


def main() -> None:
    my_aocd.print_header(2015, 9)

    assert part_1(TEST) == 605
    assert part_2(TEST) == 982

    inputs = my_aocd.get_input(2015, 9, 28)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
