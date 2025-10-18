#! /usr/bin/env python3
#
# Advent of Code 2022 Day 18
#


import itertools
import sys
from collections import defaultdict
from collections import deque
from collections.abc import Iterable

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST1 = """\
1,1,1
2,1,1
"""
TEST2 = """\
2,2,2
1,2,2
3,2,2
2,1,2
2,3,2
2,2,1
2,2,3
2,2,4
2,2,6
1,2,5
3,2,5
2,1,5
2,3,5
"""

DIRS = [
    (-1, 0, 0),
    (1, 0, 0),
    (0, -1, 0),
    (0, 1, 0),
    (0, 0, -1),
    (0, 0, 1),
]

Cube = tuple[int, int, int]
Input = tuple[Cube, ...]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        ans = []
        for line in input_data:
            x, y, z = map(int, line.split(","))
            ans.append((x, y, z))
        return tuple(ans)

    def surface_area(self, cubes: Iterable[Cube]) -> int:
        def count_gaps(nums: list[int]) -> int:
            return sum(b != a + 1 for a, b in itertools.pairwise(nums))

        dx, dy, dz = defaultdict(list), defaultdict(list), defaultdict(list)
        for x, y, z in cubes:
            dx[(y, z)].append(x)
            dy[(x, z)].append(y)
            dz[(x, y)].append(z)
        return sum(
            sum(2 * count_gaps(sorted(v)) + 2 for v in values)
            for values in (dx.values(), dy.values(), dz.values())
        )

    def part_1(self, cubes: Input) -> Output1:
        return self.surface_area(cubes)

    def part_2(self, cubes: Input) -> Output2:
        min_x = min_y = min_z = sys.maxsize
        max_x = max_y = max_z = -sys.maxsize
        for x, y, z in cubes:
            min_x, min_y, min_z = min(x, min_x), min(y, min_y), min(z, min_z)
            max_x, max_y, max_z = max(x, max_x), max(y, max_y), max(z, max_z)
        min_x, min_y, min_z = (m - 1 for m in (min_x, min_y, min_z))
        max_x, max_y, max_z = (m + 1 for m in (max_x, max_y, max_z))

        air = set[Cube]()
        q = deque[Cube]()
        q.append((min_x, min_y, min_z))
        while q:
            x, y, z = q.popleft()
            if (x, y, z) in cubes:
                continue
            air.add((x, y, z))
            for dx, dy, dz in DIRS:
                xx, yy, zz = x + dx, y + dy, z + dz
                if (
                    min_x <= xx <= max_x
                    and min_y <= yy <= max_y
                    and min_z <= zz <= max_z
                    and (xx, yy, zz) not in air
                ):
                    q.append((xx, yy, zz))
                    air.add((xx, yy, zz))
        trapped = (
            (x, y, z)
            for x, y, z in itertools.product(
                range(min_x + 1, max_x),
                range(min_y + 1, max_y),
                range(min_z + 1, max_z),
            )
            if (x, y, z) not in air and (x, y, z) not in cubes
        )
        return self.surface_area(cubes) - self.surface_area(trapped)

    @aoc_samples(
        (
            ("part_1", TEST1, 10),
            ("part_1", TEST2, 64),
            ("part_2", TEST2, 58),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2022, 18)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
