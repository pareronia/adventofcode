#! /usr/bin/env python3
#
# Advent of Code 2017 Day 22
#
from __future__ import annotations
from typing import NamedTuple, Callable
from enum import Enum, unique
from aoc import my_aocd
from aoc.common import log
from aoc.geometry import Vector
from aoc.navigation import Headings, Heading
import aocd


INFECTED = "#"


@unique
class State(Enum):
    CLEAN = "."
    INFECTED = "#"
    WEAKENED = "W"
    FLAGGED = "F"


class Position(NamedTuple):
    x: int
    y: int

    def translate(self, vector: Vector, amplitude: int = 1) -> Position:
        return Position.of(
            self.x + vector.x * amplitude, self.y + vector.y * amplitude
        )

    @classmethod
    def of(cls, x: int, y: int) -> Position:
        return Position(x, y)


class Carrier(NamedTuple):
    position: Position
    heading: Heading
    count: int


def _parse(inputs: tuple[str]) -> tuple[set[Position, Carrier]]:
    size = len(inputs)
    nodes = {
        Position.of(x, y)
        for x, y in ((x, y) for x in range(size) for y in range(size))
        if inputs[size - 1 - y][x] == INFECTED
    }
    carrier = Carrier(Position.of(size // 2, size // 2), Headings.N.value, 0)
    return carrier, nodes


def _solve(
    carrier: Carrier,
    bursts: int,
    get_current_state: Callable[[Position], State],
    calc_new_state: Callable[[State], State],
    calc_new_heading: Callable[[Heading, State], State],
    set_new_state: Callable[[Position, State], None],
) -> int:
    for _ in range(bursts):
        current_state = get_current_state(carrier.position)
        heading = calc_new_heading(carrier.heading, current_state)
        new_state = calc_new_state(current_state)
        set_new_state(carrier.position, new_state)
        carrier = Carrier(
            carrier.position.translate(heading),
            heading,
            carrier.count + 1
            if new_state == State.INFECTED
            else carrier.count,
        )
    return carrier.count


def part_1(inputs: tuple[str]) -> int:
    def set_new_state(nodes: set[Position], p: Position, s: State) -> None:
        if s == State.INFECTED:
            nodes.add(p)
        elif p in nodes:
            nodes.remove(p)

    carrier, nodes = _parse(inputs)
    return _solve(
        carrier,
        10_000,
        lambda p: State.INFECTED if p in nodes else State.CLEAN,
        lambda s: State.CLEAN if s == State.INFECTED else State.INFECTED,
        lambda h, s: Heading.rotate(h, -90 if s == State.CLEAN else 90),
        lambda p, s: set_new_state(nodes, p, s),
    )


def part_2(inputs: tuple[str]) -> int:
    def set_new_state(
        nodes: dict[Position, State], p: Position, s: State
    ) -> None:
        if s == State.CLEAN:
            del nodes[p]
        else:
            nodes[p] = s

    carrier, _ = _parse(inputs)
    nodes = {p: State.INFECTED for p in _}
    log(nodes)
    return _solve(
        carrier,
        10_000_000,
        lambda p: nodes.get(p, State.CLEAN),
        lambda s: State.CLEAN
        if s == State.FLAGGED
        else State.FLAGGED
        if s == State.INFECTED
        else State.WEAKENED
        if s == State.CLEAN
        else State.INFECTED,
        lambda h, s: Heading.rotate(
            h,
            -90
            if s == State.CLEAN
            else 90
            if s == State.INFECTED
            else 180
            if s == State.FLAGGED
            else 0,
        ),
        lambda p, s: set_new_state(nodes, p, s),
    )


TEST = """\
..#
#..
...
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2017, 22)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 5587
    assert part_2(TEST) == 2511944

    inputs = my_aocd.get_input(2017, 22, 25)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()
