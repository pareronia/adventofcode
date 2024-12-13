#! /usr/bin/env python3
#
# Advent of Code 2024 Day 13
#

from __future__ import annotations

import sys
from typing import NamedTuple

from aoc import my_aocd
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

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


class Machine(NamedTuple):
    ax: int
    bx: int
    ay: int
    by: int
    px: int
    py: int

    @classmethod
    def from_input(cls, block: list[str]) -> Machine:
        a, b = ((int(block[i][12:14]), int(block[i][18:20])) for i in range(2))
        sp = block[2].split(", ")
        px, py = int(sp[0].split("=")[1]), int(sp[1][2:])
        return Machine(a[0], b[0], a[1], b[1], px, py)


Input = list[Machine]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [
            Machine.from_input(block)
            for block in my_aocd.to_blocks(input_data)
        ]

    def solve(self, machines: list[Machine], offset: int = 0) -> int:
        def calc_tokens(machine: Machine, offset: int) -> int | None:
            px, py = machine.px + offset, machine.py + offset
            div = machine.bx * machine.ay - machine.ax * machine.by
            ans_a = (py * machine.bx - px * machine.by) / div
            ans_b = (px * machine.ay - py * machine.ax) / div
            if int(ans_a) == ans_a and int(ans_b) == ans_b:
                return int(ans_a) * 3 + int(ans_b)
            else:
                return None

        return sum(
            tokens
            for tokens in (calc_tokens(m, offset) for m in machines)
            if tokens is not None
        )

    def part_1(self, machines: Input) -> Output1:
        return self.solve(machines)

    def part_2(self, machines: Input) -> Output2:
        return self.solve(machines, offset=10_000_000_000_000)

    @aoc_samples((("part_1", TEST, 480),))
    def samples(self) -> None:
        pass


solution = Solution(2024, 13)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
