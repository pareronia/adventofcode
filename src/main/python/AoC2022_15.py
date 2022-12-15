#! /usr/bin/env python3
#
# Advent of Code 2022 Day 15
#


import re
from collections import defaultdict

import aocd
from aoc import my_aocd

Sensors = list[tuple[int, int, int]]
Beacons = dict[int, set[int]]
Range = tuple[int, int]


def _parse(inputs: tuple[str]) -> tuple[Sensors, Beacons]:
    sensors = Sensors()
    beacons = defaultdict[int, set[int]](set)
    for line in inputs:
        sx, sy, bx, by = map(int, re.findall(r"-?[0-9]+", line))
        sensors.append((sx, sy, abs(sx - bx) + abs(sy - by)))
        beacons[by].add(bx)
    return sensors, beacons


def _get_ranges(sensors: Sensors, y: int) -> list[Range]:
    ranges = list[Range]()
    for sx, sy, d in sensors:
        dy = abs(sy - y)
        if dy > d:
            continue
        ranges.append((sx - d + dy, sx + d - dy))
    merged = list[Range]()
    for s_min, s_max in sorted(ranges):
        if len(merged) == 0:
            merged.append((s_min, s_max))
            continue
        last_min, last_max = merged[-1]
        if s_min <= last_max:
            merged[-1] = (last_min, max(last_max, s_max))
            continue
        merged.append((s_min, s_max))
    return merged


def solve_1(inputs: tuple[str], y: int) -> int:
    sensors, beacons = _parse(inputs)
    return sum(
        (
            r_max
            - r_min
            + 1
            - sum(1 for bx in beacons[y] if r_min <= bx <= r_max)
        )
        for r_min, r_max in _get_ranges(sensors, y)
    )


def solve_2(inputs: tuple[str], the_max: int) -> int:
    sensors, _ = _parse(inputs)
    for y in range(the_max, 0, -1):
        ranges = _get_ranges(sensors, y)
        x = 0
        while x <= the_max:
            for r_min, r_max in ranges:
                if x < r_min:
                    return x * 4_000_000 + y
                x = r_max + 1
    raise RuntimeError()


def part_1(inputs: tuple[str]) -> int:
    return solve_1(inputs, 2_000_000)


def part_2(inputs: tuple[str]) -> int:
    return solve_2(inputs, 4_000_000)


TEST = tuple(
    """\
Sensor at x=2, y=18: closest beacon is at x=-2, y=15
Sensor at x=9, y=16: closest beacon is at x=10, y=16
Sensor at x=13, y=2: closest beacon is at x=15, y=3
Sensor at x=12, y=14: closest beacon is at x=10, y=16
Sensor at x=10, y=20: closest beacon is at x=10, y=16
Sensor at x=14, y=17: closest beacon is at x=10, y=16
Sensor at x=8, y=7: closest beacon is at x=2, y=10
Sensor at x=2, y=0: closest beacon is at x=2, y=10
Sensor at x=0, y=11: closest beacon is at x=2, y=10
Sensor at x=20, y=14: closest beacon is at x=25, y=17
Sensor at x=17, y=20: closest beacon is at x=21, y=22
Sensor at x=16, y=7: closest beacon is at x=15, y=3
Sensor at x=14, y=3: closest beacon is at x=15, y=3
Sensor at x=20, y=1: closest beacon is at x=15, y=3
""".splitlines()
)


def main() -> None:
    puzzle = aocd.models.Puzzle(2022, 15)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert solve_1(TEST, 10) == 26
    assert solve_2(TEST, 20) == 56_000_011

    inputs = my_aocd.get_input_data(puzzle, 36)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()
