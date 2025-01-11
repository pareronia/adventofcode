#! /usr/bin/env python3
#
# Advent of Code 2015 Day 6
#

from __future__ import annotations

import sys
from enum import Enum
from enum import auto
from enum import unique
from typing import NamedTuple

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.grid import Cell

TEST1 = "turn on 0,0 through 999,999"
TEST2 = "toggle 0,0 through 999,0"
TEST3 = "turn off 499,499 through 500,500"
TEST4 = "turn on 0,0 through 0,0"
TEST5 = "toggle 0,0 through 999,999"


@unique
class Mode(Enum):
    MODE_1 = auto()
    MODE_2 = auto()


@unique
class Action(Enum):
    TURN_ON = auto()
    TOGGLE = auto()
    TURN_OFF = auto()

    @classmethod
    def from_string(cls, action: str) -> Action:
        match action:
            case "turn_on":
                return Action.TURN_ON
            case "turn_off":
                return Action.TURN_OFF
            case _:
                return Action.TOGGLE

    def apply(
        self, lights: list[list[int]], start: Cell, end: Cell, mode: Mode
    ) -> None:
        match self:
            case Action.TURN_ON:
                match mode:
                    case Mode.MODE_1:
                        for r in range(start.row, end.row + 1):
                            for c in range(start.col, end.col + 1):
                                lights[r][c] = 1
                    case Mode.MODE_2:
                        for r in range(start.row, end.row + 1):
                            for c in range(start.col, end.col + 1):
                                lights[r][c] += 1
            case Action.TURN_OFF:
                match mode:
                    case Mode.MODE_1:
                        for r in range(start.row, end.row + 1):
                            for c in range(start.col, end.col + 1):
                                lights[r][c] = 0
                    case Mode.MODE_2:
                        for r in range(start.row, end.row + 1):
                            for c in range(start.col, end.col + 1):
                                lights[r][c] = max(lights[r][c] - 1, 0)
            case Action.TOGGLE:
                match mode:
                    case Mode.MODE_1:
                        for r in range(start.row, end.row + 1):
                            for c in range(start.col, end.col + 1):
                                lights[r][c] = 0 if lights[r][c] == 1 else 1
                    case Mode.MODE_2:
                        for r in range(start.row, end.row + 1):
                            for c in range(start.col, end.col + 1):
                                lights[r][c] += 2


class Instruction(NamedTuple):
    action: Action
    start: Cell
    end: Cell

    @classmethod
    def from_input(cls, input_: str) -> Instruction:
        splits = input_.replace("turn ", "turn_").split(" through ")
        action_s, start_s = splits[0].split(" ")
        start = Cell(*list(map(int, start_s.split(",")))[:2])
        end = Cell(*list(map(int, splits[1].split(",")))[:2])
        return Instruction(Action.from_string(action_s), start, end)


Input = list[Instruction]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [Instruction.from_input(line) for line in input_data]

    def solve(self, instructions: Input, mode: Mode) -> int:
        lights = [[0 for _ in range(1000)] for _ in range(1000)]
        for instruction in instructions:
            instruction.action.apply(
                lights, instruction.start, instruction.end, mode
            )
        return sum(lights[r][c] for r in range(1000) for c in range(1000))

    def part_1(self, instructions: Input) -> Output1:
        return self.solve(instructions, Mode.MODE_1)

    def part_2(self, instructions: Input) -> Output2:
        return self.solve(instructions, Mode.MODE_2)

    @aoc_samples(
        (
            ("part_1", TEST1, 1_000_000),
            ("part_1", TEST2, 1000),
            ("part_1", TEST3, 0),
            ("part_2", TEST4, 1),
            ("part_2", TEST5, 2_000_000),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2015, 6)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
