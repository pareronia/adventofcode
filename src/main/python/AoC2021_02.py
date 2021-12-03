#! /usr/bin/env python3
#
# Advent of Code 2021 Day 2
#

from aoc import my_aocd
from typing import NamedTuple

FORWARD = "forward"
UP = "up"
DOWN = "down"


class Command(NamedTuple):
    dir: str
    amount: int

    @classmethod
    def create(cls, dir: str, amount: str):
        if dir not in {FORWARD, UP, DOWN}:
            raise ValueError
        return cls(dir, int(amount))


def _parse(inputs: tuple[str]) -> list[Command]:
    return [Command.create(*input.split()) for input in inputs]


def part_1(inputs: tuple[str]) -> int:
    hor = ver = 0
    for command in _parse(inputs):
        if command.dir == UP:
            ver -= command.amount
        elif command.dir == DOWN:
            ver += command.amount
        else:
            hor += command.amount
    return hor * ver


def part_2(inputs: tuple[str]) -> int:
    hor = ver = aim = 0
    for command in _parse(inputs):
        if command.dir == UP:
            aim -= command.amount
        elif command.dir == DOWN:
            aim += command.amount
        else:
            hor += command.amount
            ver += aim * command.amount
    return hor * ver


TEST = """\
forward 5
down 5
forward 8
up 3
down 8
forward 2
""".splitlines()


def main() -> None:
    my_aocd.print_header(2021, 2)

    assert part_1(TEST) == 150
    assert part_2(TEST) == 900

    inputs = my_aocd.get_input(2021, 2, 1000)
    print(f"Part 1: {part_1(inputs)}")
    print(f"Part 2: {part_2(inputs)}")


if __name__ == '__main__':
    main()
