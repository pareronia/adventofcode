#! /usr/bin/env python3
#
# Advent of Code 2021 Day 2
#

from __future__ import annotations

import sys
from enum import Enum
from typing import NamedTuple

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST = """\
forward 5
down 5
forward 8
up 3
down 8
forward 2
"""


class Direction(Enum):
    FORWARD = "forward"
    UP = "up"
    DOWN = "down"

    @classmethod
    def from_str(cls, s: str) -> Direction:
        for v in Direction:
            if v.value == s:
                return v
        raise ValueError


class Command(NamedTuple):
    dir: Direction
    amount: int

    @classmethod
    def create(cls, input: str) -> Command:
        dir, amount = input.split()
        return cls(Direction.from_str(dir), int(amount))


Input = list[Command]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [Command.create(line) for line in input_data]

    def part_1(self, commands: Input) -> Output1:
        hor = ver = 0
        for command in commands:
            match command.dir:
                case Direction.UP:
                    ver -= command.amount
                case Direction.DOWN:
                    ver += command.amount
                case _:
                    hor += command.amount
        return hor * ver

    def part_2(self, commands: Input) -> Output2:
        hor = ver = aim = 0
        for command in commands:
            match command.dir:
                case Direction.UP:
                    aim -= command.amount
                case Direction.DOWN:
                    aim += command.amount
                case _:
                    hor += command.amount
                    ver += aim * command.amount
        return hor * ver

    @aoc_samples(
        (
            ("part_1", TEST, 150),
            ("part_2", TEST, 900),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2021, 2)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
