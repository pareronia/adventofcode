#! /usr/bin/env python3
#
# Advent of Code 2015 Day 14
#

from __future__ import annotations
import re
from dataclasses import dataclass
from collections import defaultdict
from aoc import my_aocd
import aocd


REGEXP = r'([A-Za-z]+) can fly ([0-9]+) km/s for ([0-9]+) seconds, but then must rest for ([0-9]+) seconds\.'  # noqa


@dataclass(frozen=True)
class Reindeer:
    name: str
    speed: int
    go: int
    stop: int

    @classmethod
    def of(cls, name: str, speed: str, go: str, stop: str) -> Reindeer:
        return Reindeer(name, int(speed), int(go), int(stop))


def _parse(inputs: tuple[str]) -> list[Reindeer]:
    return [Reindeer.of(*re.search(REGEXP, input_).groups())
            for input_ in inputs]


def _distance_reached(reindeer: Reindeer, time: int) -> int:
    period_distance = reindeer.speed * reindeer.go
    period_time = reindeer.go + reindeer.stop
    periods = time // period_time
    left = time % period_time
    if left >= reindeer.go:
        return periods * period_distance + period_distance
    else:
        return periods * period_distance + reindeer.speed * left


def _do_part_1(inputs: tuple[str], time: int) -> int:
    return max([_distance_reached(r, time) for r in _parse(inputs)])


def part_1(inputs: tuple[str]) -> int:
    return _do_part_1(inputs, 2503)


def _do_part_2(inputs: tuple[str], time: int) -> int:
    reindeer = _parse(inputs)
    points = defaultdict(int)
    for i in range(time):
        distances = {r.name: _distance_reached(r, i + 1)
                     for r in reindeer}
        lead = max(distances.values())
        for r, d in distances.items():
            if d == lead:
                points[r] += 1
    return max(points.values())


def part_2(inputs: tuple[str]) -> int:
    return _do_part_2(inputs, 2503)


TEST = """\
Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2015, 14)

    assert _do_part_1(TEST, 1000) == 1120
    assert _do_part_2(TEST, 1000) == 689

    inputs = my_aocd.get_input(puzzle.year, puzzle.day, 9)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()
