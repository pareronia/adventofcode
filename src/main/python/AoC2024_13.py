#! /usr/bin/env python3
#
# Advent of Code 2024 Day 13
#

import sys

from aoc import my_aocd
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Input = list[tuple[tuple[int, int], tuple[int, int], tuple[int, int]]]
Output1 = int
Output2 = int


TEST = """\
Button A: X+94, Y+34
Button B: X+22, Y+67
Prize: X=8400, Y=5400

Button A: X+26, Y+66
Button B: X+67, Y+21
Prize: X=12748, Y=12176

Button A: X+17, Y+86
Button B: X+84, Y+37
Prize: X=7870, Y=6450

Button A: X+69, Y+23
Button B: X+27, Y+71
Prize: X=18641, Y=10279
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        machines = []
        for block in my_aocd.to_blocks(input_data):
            a = (int(block[0][12:14]), int(block[0][18:20]))
            b = (int(block[1][12:14]), int(block[1][18:20]))
            sp = block[2].split(", ")
            px = int(sp[0].split("=")[1])
            py = int(sp[1][2:])
            machines.append((a, b, (px, py)))
        return machines

    def guess(
        self, ax: int, bx: int, ay: int, by: int, px: int, py: int
    ) -> int | None:
        # best = sys.maxsize
        div = bx * ay - ax * by
        ans_a = (py * bx - px * by) / div
        ans_b = (px * ay - py * ax) / div
        if int(ans_a) == ans_a:
            return int(ans_a) * 3 + int(ans_b)
        # for ans_a in range(100, 0, -1):
        #     for ans_b in range(1, 101):
        #         if (
        #             ans_a * ax + ans_b * bx == px
        #             and ans_a * ay + ans_b * by == py
        #         ):
        #             best = min(best, ans_a * 3 + ans_b)
        # if best < sys.maxsize:
        #     return best
        else:
            return None

    def part_1(self, machines: Input) -> Output1:
        ans = 0
        for a, b, p in machines:
            ax, ay = a
            bx, by = b
            px, py = p
            g = self.guess(ax, bx, ay, by, px, py)
            if g is not None:
                ans += g
        return ans

    def part_2(self, machines: Input) -> Output2:
        MOD = 10_000_000_000_000
        ans = 0
        for a, b, p in machines:
            ax, ay = a
            bx, by = b
            px, py = p
            g = self.guess(ax, bx, ay, by, MOD + px, MOD + py)
            if g is not None:
                ans += g
        return ans

    @aoc_samples(
        (
            ("part_1", TEST, 480),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2024, 13)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
