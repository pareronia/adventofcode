#! /usr/bin/env python3
#
# Advent of Code 2015 Day 6
#

from __future__ import annotations

from enum import Enum
from typing import Callable, NamedTuple

import aocd
import numpy as np
import numpy.typing as npt

from aoc import my_aocd
from aoc.geometry import Position


class Action(Enum):
    TURN_ON = 1
    TOGGLE = 2
    TURN_OFF = 3


class Instruction(NamedTuple):
    action: Action
    start: Position
    end: Position

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
        start = Position.of(int(start_splits[0]), int(start_splits[1]))
        end_splits = splits[1].split(",")
        end = Position.of(int(end_splits[0]), int(end_splits[1]))
        return Instruction(action, start, end)


class Grid:
    def __init__(
        self,
        turn_on: Callable[[npt.NDArray[np.int_], Position, Position], None],
        turn_off: Callable[[npt.NDArray[np.int_], Position, Position], None],
        toggle: Callable[[npt.NDArray[np.int_], Position, Position], None],
    ):
        self.lights = np.zeros((1000, 1000), np.byte)
        self.turn_on = turn_on
        self.turn_off = turn_off
        self.toggle = toggle

    def process_instructions(self, instructions: list[Instruction]) -> None:
        for instruction in instructions:
            action = (
                self.turn_on
                if instruction.action == Action.TURN_ON
                else self.turn_off
                if instruction.action == Action.TURN_OFF
                else self.toggle
            )
            action(self.lights, instruction.start, instruction.end)

    def get_total_light_value(self) -> int:
        return int(np.sum(self.lights))


def _parse(inputs: tuple[str, ...]) -> list[Instruction]:
    return [Instruction.from_input(line) for line in inputs]


def part_1(inputs: tuple[str, ...]) -> int:
    def turn_on(
        lights: npt.NDArray[np.int_], start: Position, end: Position
    ) -> None:
        lights[start.x : end.x + 1, start.y : end.y + 1] = 1  # noqa E203

    def turn_off(
        lights: npt.NDArray[np.int_], start: Position, end: Position
    ) -> None:
        lights[start.x : end.x + 1, start.y : end.y + 1] = 0  # noqa E203

    def toggle(
        lights: npt.NDArray[np.int_], start: Position, end: Position
    ) -> None:
        lights[
            start.x : end.x + 1, start.y : end.y + 1  # noqa E203
        ] = np.logical_not(
            lights[start.x : end.x + 1, start.y : end.y + 1]  # noqa E203
        )

    lights = Grid(
        lambda lights, start, end: turn_on(lights, start, end),
        lambda lights, start, end: turn_off(lights, start, end),
        lambda lights, start, end: toggle(lights, start, end),
    )
    lights.process_instructions(_parse(inputs))
    return lights.get_total_light_value()


def part_2(inputs: tuple[str, ...]) -> int:
    def turn_on(
        lights: npt.NDArray[np.int_], start: Position, end: Position
    ) -> None:
        lights[start.x : end.x + 1, start.y : end.y + 1] += 1  # noqa E203

    def turn_off(
        lights: npt.NDArray[np.int_], start: Position, end: Position
    ) -> None:
        lights[start.x : end.x + 1, start.y : end.y + 1] -= 1  # noqa E203
        lights[lights < 0] = 0

    def toggle(
        lights: npt.NDArray[np.int_], start: Position, end: Position
    ) -> None:
        lights[start.x : end.x + 1, start.y : end.y + 1] += 2  # noqa E203

    lights = Grid(
        lambda lights, start, end: turn_on(lights, start, end),
        lambda lights, start, end: turn_off(lights, start, end),
        lambda lights, start, end: toggle(lights, start, end),
    )
    lights.process_instructions(_parse(inputs))
    return lights.get_total_light_value()


TEST1 = "turn on 0,0 through 999,999".splitlines()
TEST2 = "toggle 0,0 through 999,0".splitlines()
TEST3 = "turn off 499,499 through 500,500".splitlines()
TEST4 = "turn on 0,0 through 0,0".splitlines()
TEST5 = "toggle 0,0 through 999,999".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2015, 6)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST1) == 1_000_000  # type:ignore[arg-type]
    assert part_1(TEST2) == 1000  # type:ignore[arg-type]
    assert part_1(TEST3) == 0  # type:ignore[arg-type]
    assert part_2(TEST4) == 1  # type:ignore[arg-type]
    assert part_2(TEST5) == 2_000_000  # type:ignore[arg-type]

    inputs = my_aocd.get_input(puzzle.year, puzzle.day, 300)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()
