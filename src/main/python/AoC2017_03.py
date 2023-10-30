#! /usr/bin/env python3
#
# Advent of Code 2017 Day 3
#
from collections.abc import Generator
from aoc import my_aocd
from aoc.geometry import Position, Direction
import aocd


class DirectionsAndPeriods:
    def __init__(self):
        self.periods = [1, 1, 2, 2]
        self.directions = [
            Direction.RIGHT,
            Direction.UP,
            Direction.LEFT,
            Direction.DOWN,
        ]

    def apply(self, t: int) -> tuple[Direction, int]:
        idx = t % 4
        period = self.periods[idx]
        self.periods[idx] = period + 2
        direction = self.directions[idx]
        return direction, period


def _parse(inputs: tuple[str]) -> int:
    assert len(inputs) == 1
    return int(inputs[0])


def _coordinates() -> Generator[tuple[int, int]]:
    x = y = j = k = 0
    directions_and_periods = DirectionsAndPeriods()
    direction, period = directions_and_periods.apply(k)

    while True:
        if j == period:
            k += 1
            direction, period = directions_and_periods.apply(k)
            j = 0
        x += direction.x
        y += direction.y
        j += 1
        yield (x, y)


def part_1(inputs: tuple[str]) -> int:
    square = _parse(inputs)
    if square == 1:
        return 0
    coords = _coordinates()
    i = 1
    while i < square:
        i += 1
        x, y = next(coords)
        if i == square:
            return Position.of(x, y).manhattan_distance_to_origin()
    raise ValueError


def part_2(inputs: tuple[str]) -> int:
    square = _parse(inputs)
    if square == 1:
        return 1
    squares = dict()
    squares[(0, 0)] = 1
    coords = _coordinates()
    while True:
        x, y = next(coords)
        value = 0
        for d in Direction.octants():
            neighbour = (x + d.x, y + d.y)
            value += squares[neighbour] if neighbour in squares else 0
        squares[(x, y)] = value
        if value > square:
            return value


def main() -> None:
    puzzle = aocd.models.Puzzle(2017, 3)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(("1",)) == 0
    assert part_1(("12",)) == 3
    assert part_1(("23",)) == 2
    assert part_1(("1024",)) == 31
    assert part_2(("1",)) == 1
    assert part_2(("2",)) == 4
    assert part_2(("3",)) == 4
    assert part_2(("4",)) == 5
    assert part_2(("5",)) == 10

    inputs = my_aocd.get_input(2017, 3, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()
