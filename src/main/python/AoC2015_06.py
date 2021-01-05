#! /usr/bin/env python3
#
# Advent of Code 2015 Day 6
#


from enum import Enum
from dataclasses import dataclass
from aoc import my_aocd
from aoc.geometry import Position
from aoc.common import spinner


class Action(Enum):
    TURN_ON = 1
    TOGGLE = 2
    TURN_OFF = 3


@dataclass(frozen=True)
class Instruction:
    action: str
    start: Position
    end: Position

    def is_turn_on(self):
        return self.action == Action.TURN_ON

    def is_turn_off(self):
        return self.action == Action.TURN_OFF

    def is_toggle(self):
        return self.action == Action.TOGGLE


def _parse(inputs: tuple[str]) -> list[Instruction]:
    inss = list[Instruction]()
    for input_ in inputs:
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
        inss.append(Instruction(action, start, end))
    return inss


def _sum_of_all_values(lights: dict) -> int:
    return sum([light[1] for light in lights.items()])


def _process_instructions_1(lights: dict, inss: list[Instruction]):
    for ins in inss:
        for i in range(ins.start.x, ins.end.x + 1):
            for j in range(ins.start.y, ins.end.y + 1):
                if (i, j) in lights:
                    prev = lights[(i, j)]
                else:
                    prev = None
                if ins.is_turn_on():
                    if prev:
                        continue
                    else:
                        lights[(i, j)] = 1
                elif ins.is_turn_off():
                    if prev:
                        del lights[(i, j)]
                    else:
                        continue
                elif ins.is_toggle():
                    if prev:
                        del lights[(i, j)]
                    else:
                        lights[(i, j)] = 1
                spinner(i * j)


def part_1(inputs: tuple[str]) -> int:
    inss = _parse(inputs)
    lights = dict()
    _process_instructions_1(lights, inss)
    return _sum_of_all_values(lights)


def _process_instructions_2(lights: dict, inss: list[Instruction]):
    for ins in inss:
        for i in range(ins.start.x, ins.end.x + 1):
            for j in range(ins.start.y, ins.end.y + 1):
                if (i, j) in lights:
                    prev = lights[(i, j)]
                else:
                    prev = None
                if ins.is_turn_on():
                    if prev:
                        lights[(i, j)] = lights[(i, j)] + 1
                    else:
                        lights[(i, j)] = 1
                elif ins.is_turn_off():
                    if not prev:
                        continue
                    elif prev == 1:
                        del lights[(i, j)]
                    else:
                        lights[(i, j)] = lights[(i, j)] - 1
                elif ins.is_toggle():
                    if prev:
                        lights[(i, j)] = lights[(i, j)] + 2
                    else:
                        lights[(i, j)] = 2
                spinner(i * j)


def part_2(inputs: tuple[str]) -> int:
    inss = _parse(inputs)
    lights = dict()
    _process_instructions_2(lights, inss)
    return _sum_of_all_values(lights)


TEST1 = "turn on 0,0 through 999,999".splitlines()
TEST2 = "toggle 0,0 through 999,0".splitlines()
TEST3 = "turn off 499,499 through 500,500".splitlines()
TEST4 = "turn on 0,0 through 0,0".splitlines()
TEST5 = "toggle 0,0 through 999,999".splitlines()


def main() -> None:
    my_aocd.print_header(2015, 6)

    assert part_1(TEST1) == 1_000_000
    assert part_1(TEST2) == 1000
    assert part_1(TEST3) == 0
    assert part_2(TEST4) == 1
    assert part_2(TEST5) == 2_000_000

    inputs = my_aocd.get_input_as_tuple(2015, 6, 300)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
