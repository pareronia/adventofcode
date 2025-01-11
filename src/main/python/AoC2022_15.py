#! /usr/bin/env python3
#
# Advent of Code 2022 Day 15
#

import re
import sys
from collections import defaultdict

from aoc.common import InputData
from aoc.common import SolutionBase

Sensors = list[tuple[int, int, int]]
Beacons = dict[int, set[int]]
Range = tuple[int, int]
Input = tuple[Sensors, Beacons]
Output1 = int
Output2 = int

TEST = """\
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
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        sensors = Sensors()
        beacons = defaultdict[int, set[int]](set)
        for line in input_data:
            sx, sy, bx, by = map(int, re.findall(r"-?[0-9]+", line))
            sensors.append((sx, sy, abs(sx - bx) + abs(sy - by)))
            beacons[by].add(bx)
        return sensors, beacons

    def _get_ranges(self, sensors: Sensors, y: int) -> list[Range]:
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

    def solve_1(self, input: Input, y: int) -> int:
        sensors, beacons = input
        return sum(
            (
                r_max
                - r_min
                + 1
                - sum(1 for bx in beacons[y] if r_min <= bx <= r_max)
            )
            for r_min, r_max in self._get_ranges(sensors, y)
        )

    def solve_2(self, input: Input, the_max: int) -> int:
        sensors, _ = input
        for y in range(the_max, 0, -1):
            ranges = self._get_ranges(sensors, y)
            x = 0
            while x <= the_max:
                for r_min, r_max in ranges:
                    if x < r_min:
                        return x * 4_000_000 + y
                    x = r_max + 1
        raise RuntimeError()

    def part_1(self, input: Input) -> int:
        return self.solve_1(input, 2_000_000)

    def part_2(self, input: Input) -> int:
        return self.solve_2(input, 4_000_000)

    def samples(self) -> None:
        input = self.parse_input(TEST.splitlines())
        assert self.solve_1(input, 10) == 26
        assert self.solve_2(input, 20) == 56_000_011


solution = Solution(2022, 15)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
