#! /usr/bin/env python3
#
# Advent of Code 2024 Day 14
#

import math
import sys

from aoc.common import InputData
from aoc.common import SolutionBase

Position = tuple[int, int]
Vector = tuple[int, int]
Input = list[tuple[Position, Vector]]
Output1 = int
Output2 = int

W, H = 101, 103

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
            px, py = map(int, p.split("=")[1].split(","))
            vx, vy = map(int, v.split("=")[1].split(","))
            robots.append(((px, py), (vx, vy)))
        return robots

    def safety_factor(
        self,
        robots: list[tuple[Position, Vector]],
        w: int,
        h: int,
        rounds: int,
    ) -> int:
        mid_w, mid_h = w // 2, h // 2
        q1, q2, q3, q4 = 0, 0, 0, 0
        for pos, vec in robots:
            px, py = pos[0] + rounds * vec[0], pos[1] + rounds * vec[1]
            px, py = px % w, py % h
            if px < mid_w:
                if py < mid_h:
                    q1 += 1
                elif mid_h < py:
                    q2 += 1
            elif mid_w < px:
                if py < mid_h:
                    q3 += 1
                elif mid_h < py:
                    q4 += 1
        return math.prod((q1, q2, q3, q4))

    def solve_1(
        self, robots: list[tuple[Position, Vector]], w: int, h: int
    ) -> int:
        return self.safety_factor(robots, w, h, 100)

    def part_1(self, robots: Input) -> Output1:
        return self.solve_1(robots, W, H)

    def part_2(self, robots: Input) -> Output2:
        def print_robots(
            robots: list[tuple[Position, Vector]], w: int, h: int, round: int
        ) -> None:
            if not __debug__:
                return
            new_robots = [
                ((pos[0] + round * vec[0]) % w, (pos[1] + round * vec[1]) % h)
                for pos, vec in robots
            ]
            lines = [
                "".join("*" if (x, y) in new_robots else "." for x in range(w))
                for y in range(h)
            ]
            for line in lines:
                print(line)

        ans, best, round = 0, sys.maxsize, 1
        sfs = list[int]()
        while round < W + H:
            safety_factor = self.safety_factor(robots, W, H, round)
            sfs.append(safety_factor)
            if safety_factor < best:
                best = safety_factor
                ans = round
            round += 1
        mins = list[int]()
        for i in range(2, len(sfs) - 2):
            avg = sum(sfs[i + j] for j in (-2, -1, 1, 2)) // 4
            if sfs[i] < avg * 9 // 10:
                mins.append(i + 1)
        period = mins[2] - mins[0]
        round = mins[2] + period
        while round <= W * H:
            safety_factor = self.safety_factor(robots, W, H, round)
            if safety_factor < best:
                best = safety_factor
                ans = round
            round += period
        print_robots(robots, W, H, ans)
        return ans

    def samples(self) -> None:
        assert self.solve_1(self.parse_input(TEST.splitlines()), 11, 7) == 12


solution = Solution(2024, 14)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
