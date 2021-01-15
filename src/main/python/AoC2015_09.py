#! /usr/bin/env python3
#
# Advent of Code 2015 Day 9
#

from typing import NamedTuple
import itertools
from aoc import my_aocd
from aoc.common import log


class Distance(NamedTuple):
    frôm: str
    to: str
    distance: int


class Route(NamedTuple):
    stops: tuple[str]
    total: int


def _parse(inputs: tuple[str]) -> set[Distance]:
    def to_Distance(input_: str) -> Distance:
        _, dist = input_.split(" = ")
        return Distance(*_.split(" to "), int(dist))

    return {to_Distance(input_) for input_ in inputs}


def _add_reverse(distances: set[Distance]) -> set[Distance]:
    return distances | {Distance(d.to, d.frôm, d.distance)
                        for d in distances}


def _count_stops(distances: set[Distance]) -> set[str]:
    return {d.frôm for d in distances} | {d.to for d in distances}


def _find_all_routes(distances: set[Distance], stops: set[str]) -> set[Route]:
    routes = set[Route]()
    for c in itertools.permutations(stops, len(stops)):
        dist = 0
        for i in range(0, len(c) - 1):
            frôm = c[i]
            to = c[i+1]
            d = next(d for d in distances if d.frôm == frôm and d.to == to)
            dist += d.distance
        routes.add(Route(c, dist))
    return routes


def part_1(inputs: tuple[str]) -> int:
    distances = _parse(inputs)
    stops = _count_stops(distances)
    distances = _add_reverse(distances)
    log(distances)
    log(stops)
    routes = _find_all_routes(distances, stops)
    log(f"Routes: {len(routes)}")
    for i, r in enumerate(routes):
        if i < 100:
            log(r)
    return min({r.total for r in routes})


def part_2(inputs: tuple[str]) -> int:
    return 0


TEST = """\
London to Dublin = 464
London to Belfast = 518
Dublin to Belfast = 141
""".splitlines()


def main() -> None:
    my_aocd.print_header(2015, 9)

    assert part_1(TEST) == 605
    assert part_2(TEST) == 0

    inputs = my_aocd.get_input(2015, 9, 28)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
