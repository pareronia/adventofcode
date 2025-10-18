#! /usr/bin/env python3
#
# Advent of Code 2022 Day 15
#

import itertools
import re
import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.geometry import Position
from aoc.range import RangeInclusive

Sensors = dict[Position, int]
Beacons = set[Position]
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
        sensors, beacons = Sensors(), Beacons()
        for line in input_data:
            sx, sy, bx, by = map(int, re.findall(r"-?[0-9]+", line))
            s, b = Position.of(sx, sy), Position.of(bx, by)
            sensors[s] = s.manhattan_distance(b)
            beacons.add(b)
        return sensors, beacons

    def solve_1(self, inputs: Input, y: int) -> Output1:
        sensors, beacons = inputs

        def get_ranges(y: int) -> list[RangeInclusive]:
            ranges = list[RangeInclusive]()
            for s, md in sensors.items():
                dy = abs(s.y - y)
                if dy > md:
                    continue
                ranges.append(
                    RangeInclusive.between(s.x - md + dy, s.x + md - dy)
                )
            return RangeInclusive.merge(ranges)

        return sum(
            r.len - len({b for b in beacons if b.y == y and r.contains(b.x)})
            for r in get_ranges(y)
        )

    def solve_2(self, inputs: Input, the_max: int) -> Output2:
        """https://old.reddit.com/r/adventofcode/comments/zmcn64/2022_day_15_solutions/j0b90nr/."""
        sensors, _ = inputs
        a_coeffs, b_coeffs = set(), set()
        for s, md in sensors.items():
            a_coeffs.add(s.y - s.x + md + 1)
            a_coeffs.add(s.y - s.x - md - 1)
            b_coeffs.add(s.x + s.y + md + 1)
            b_coeffs.add(s.x + s.y - md - 1)
        return next(
            4_000_000 * p.x + p.y
            for p in (
                Position.of((ab[1] - ab[0]) // 2, (ab[0] + ab[1]) // 2)
                for ab in itertools.product(a_coeffs, b_coeffs)
                if ab[0] < ab[1] and (ab[1] - ab[0]) % 2 == 0
            )
            if 0 < p.x < the_max
            and 0 < p.y < the_max
            and all(p.manhattan_distance(s) > sensors[s] for s in sensors)
        )

    def part_1(self, inputs: Input) -> Output1:
        return self.solve_1(inputs, 2_000_000)

    def part_2(self, inputs: Input) -> Output2:
        return self.solve_2(inputs, 4_000_000)

    def samples(self) -> None:
        inputs = self.parse_input(TEST.splitlines())
        assert self.solve_1(inputs, 10) == 26
        assert self.solve_2(inputs, 20) == 56_000_011


solution = Solution(2022, 15)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
