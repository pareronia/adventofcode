#! /usr/bin/env python3
#
# Advent of Code 2024 Day 14
#

import math
import sys
from typing import Iterator

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import log
from aoc.geometry import Position
from aoc.geometry import Vector
from aoc.graph import flood_fill

Input = list[tuple[Position, Vector]]
Output1 = int
Output2 = int


TEST = """\
p=0,4 v=3,-3
p=6,3 v=-1,-3
p=10,3 v=-1,2
p=2,0 v=2,-1
p=0,0 v=1,3
p=3,0 v=-2,-2
p=7,6 v=-1,-3
p=3,0 v=-1,-2
p=9,3 v=2,3
p=7,3 v=-1,2
p=2,4 v=2,-3
p=9,5 v=-3,-3
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        robots = list[tuple[Position, Vector]]()
        for line in input_data:
            p, v = line.split()
            pos = Position(*map(int, p.split("=")[1].split(",")))
            vec = Vector(*map(int, v.split("=")[1].split(",")))
            robots.append((pos, vec))
        return robots

    def solve_1(
        self, robots: list[tuple[Position, Vector]], w: int, h: int
    ) -> int:
        for _ in range(100):
            new_robots = []
            for pos, vec in robots:
                new_pos = pos.translate(vec)
                new_pos = Position(new_pos.x % w, new_pos.y % h)
                new_robots.append((new_pos, vec))
            robots = new_robots
        q1, q2, q3, q4 = 0, 0, 0, 0
        for pos, _ in robots:
            if 0 <= pos.x < w // 2:
                if 0 <= pos.y < h // 2:
                    q1 += 1
                elif h // 2 + 1 <= pos.y < h:
                    q2 += 1
            elif w // 2 + 1 <= pos.x < w:
                if 0 <= pos.y < h // 2:
                    q3 += 1
                elif h // 2 + 1 <= pos.y < h:
                    q4 += 1
        return math.prod((q1, q2, q3, q4))

    def part_1(self, robots: Input) -> Output1:
        return self.solve_1(robots, 101, 103)

    def part_2(self, robots: Input) -> Output2:
        def print_robots(robots: set[tuple[int, int]]) -> None:
            if not __debug__:
                return
            lines = []
            for y in range(h):
                line = []
                for x in range(w):
                    line.append("*" if (x, y) in robots else ".")
                lines.append("".join(line))
            for ln in lines:
                print(ln)

        def find_ccs(robots: set[tuple[int, int]]) -> list[int]:
            def adjacent(
                r: tuple[int, int], R: set[tuple[int, int]]
            ) -> Iterator[tuple[int, int]]:
                for dx, dy in ((0, 1), (0, -1), (1, 0), (-1, 0)):
                    nx, ny = r[0] + dx, r[1] + dy
                    if (nx, ny) in R:
                        yield (nx, ny)

            ans = list[int]()
            while len(robots):
                cc = flood_fill(
                    next(iter(robots)), lambda r: adjacent(r, robots)
                )
                ans.append(len(cc))
                robots -= cc
            return ans

        w, h = 101, 103
        round = 1
        while True:
            new_robots = []
            for pos, vec in robots:
                new_pos = pos.translate(vec)
                new_pos = Position(new_pos.x % w, new_pos.y % h)
                new_robots.append((new_pos, vec))
                robots = new_robots
            log(f"{round=}")
            R = set((r.x, r.y) for r, _ in robots)
            ccs = find_ccs(set(R))
            if any(cc for cc in ccs if cc >= 100):
                print_robots(R)
                break
            round += 1
        return round

    def samples(self) -> None:
        assert self.solve_1(self.parse_input(TEST.splitlines()), 11, 7) == 12


solution = Solution(2024, 14)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
