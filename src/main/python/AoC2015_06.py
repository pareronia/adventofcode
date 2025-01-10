#! /usr/bin/env python3
#
# Advent of Code 2015 Day 6
#

from __future__ import annotations

import sys
from enum import Enum
from typing import Callable
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


class Action(Enum):
    TURN_ON = 1
    TOGGLE = 2
    TURN_OFF = 3


class Instruction(NamedTuple):
    action: Action
    start: Cell
    end: Cell

    @classmethod
    def from_input(cls, input_: str) -> Instruction:
        input_ = input_.replace("turn ", "turn_")
        splits = input_.split(" through ")
        action_and_start_splits = splits[0].split(" ")
        action_s = action_and_start_splits[0]
        if action_s == "turn_on":
            action = Action.TURN_ON
        elif action_s == "turn_off":
            action = Action.TURN_OFF
        elif action_s == "toggle":
            action = Action.TOGGLE
        else:
            raise ValueError("Invalid input")
        start_splits = action_and_start_splits[1].split(",")
        start = Cell(int(start_splits[0]), int(start_splits[1]))
        end_splits = splits[1].split(",")
        end = Cell(int(end_splits[0]), int(end_splits[1]))
        return Instruction(action, start, end)


class Grid:
    def __init__(
        self,
        turn_on: Callable[[list[list[int]], Cell, Cell], None],
        turn_off: Callable[[list[list[int]], Cell, Cell], None],
        toggle: Callable[[list[list[int]], Cell, Cell], None],
    ):
        self.lights = [[0 for _ in range(1000)] for _ in range(1000)]
        self.turn_on = turn_on
        self.turn_off = turn_off
        self.toggle = toggle

    def process_instructions(self, instructions: list[Instruction]) -> None:
        for instruction in instructions:
            action = (
                self.turn_on
                if instruction.action == Action.TURN_ON
                else (
                    self.turn_off
                    if instruction.action == Action.TURN_OFF
                    else self.toggle
                )
            )
            action(self.lights, instruction.start, instruction.end)

    def get_total_light_value(self) -> int:
        return sum(
            self.lights[r][c]
            for r in range(len(self.lights))
            for c in range(len(self.lights[0]))
        )


Input = list[Instruction]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return [Instruction.from_input(line) for line in input_data]

    def part_1(self, inputs: Input) -> Output1:
        def turn_on(lights: list[list[int]], start: Cell, end: Cell) -> None:
            for r in range(start.row, end.row + 1):
                for c in range(start.col, end.col + 1):
                    lights[r][c] = 1

        def turn_off(lights: list[list[int]], start: Cell, end: Cell) -> None:
            for r in range(start.row, end.row + 1):
                for c in range(start.col, end.col + 1):
                    lights[r][c] = 0

        def toggle(lights: list[list[int]], start: Cell, end: Cell) -> None:
            for r in range(start.row, end.row + 1):
                for c in range(start.col, end.col + 1):
                    lights[r][c] = 0 if lights[r][c] == 1 else 1

        lights = Grid(
            lambda lights, start, end: turn_on(lights, start, end),
            lambda lights, start, end: turn_off(lights, start, end),
            lambda lights, start, end: toggle(lights, start, end),
        )
        lights.process_instructions(inputs)
        return lights.get_total_light_value()

    def part_2(self, inputs: Input) -> Output2:
        def turn_on(lights: list[list[int]], start: Cell, end: Cell) -> None:
            for r in range(start.row, end.row + 1):
                for c in range(start.col, end.col + 1):
                    lights[r][c] += 1

        def turn_off(lights: list[list[int]], start: Cell, end: Cell) -> None:
            for r in range(start.row, end.row + 1):
                for c in range(start.col, end.col + 1):
                    lights[r][c] = max(lights[r][c] - 1, 0)

        def toggle(lights: list[list[int]], start: Cell, end: Cell) -> None:
            for r in range(start.row, end.row + 1):
                for c in range(start.col, end.col + 1):
                    lights[r][c] += 2

        lights = Grid(
            lambda lights, start, end: turn_on(lights, start, end),
            lambda lights, start, end: turn_off(lights, start, end),
            lambda lights, start, end: toggle(lights, start, end),
        )
        lights.process_instructions(inputs)
        return lights.get_total_light_value()

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
