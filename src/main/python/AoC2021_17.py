#! /usr/bin/env python3
#
# Advent of Code 2021 Day 17
#

import re
from typing import Callable, NamedTuple
from collections import defaultdict
from aoc import my_aocd
import aocd
from aoc.geometry import Position, Vector


class TargetArea(NamedTuple):
    bottom_left: Position
    upper_right: Position


def _parse(inputs: tuple[str]) -> TargetArea:
    assert len(inputs) == 1
    m = re.fullmatch(r'^target area: x=(-?[0-9]*)\.{2}(-?[0-9]*), '
                     r'y=(-?[0-9]*)\.{2}(-?[0-9]*)$',
                     inputs[0])
    assert m is not None
    x1, x2, y1, y2 = list(map(int, m.groups()))
    return TargetArea(Position.of(x1, y1), Position.of(x2, y2))


def _next(position: Position, velocity: Vector) -> Position:
    next_position = Position.copy(position)
    next_position.translate(velocity)
    return next_position


def _update_velocity(velocity: Vector) -> Vector:
    assert velocity.x >= 0
    return Vector.of(max(0, velocity.x - 1), velocity.y - 1)


def _in_target_area(position: Position,
                    target_area: TargetArea) -> bool:
    return position.x >= target_area.bottom_left.x \
            and position.x <= target_area.upper_right.x \
            and position.y <= target_area.upper_right.y \
            and position.y >= target_area.bottom_left.y


def _overshot(position: Position, target_area: TargetArea) -> bool:
    return position.x > target_area.upper_right.x \
            or position.y < target_area.bottom_left.y


def _shoot(target_area: TargetArea, initial_velocity: Vector,
           trajectory_consumer: Callable) -> bool:
    position = Position.of(0, 0)
    trajectory_consumer(position)
    velocity = initial_velocity
    while True:
        position = _next(position, velocity)
        trajectory_consumer(position)
        velocity = _update_velocity(velocity)
        if _overshot(position, target_area):
            return False
        if _in_target_area(position, target_area):
            return True


def _find_hits(target_area: TargetArea, shoot_upwards_only: bool) \
        -> dict[tuple[int, int], list[Position]]:
    hits = defaultdict[tuple[int, int], list[Position]](list)
    min_y = 0 if shoot_upwards_only else target_area.bottom_left.y
    for y in range(300, min_y - 1, -1):
        for x in range(1, target_area.upper_right.x + 1):
            trajectory = list[Position]()
            velocity = Vector.of(x, y)
            if _shoot(target_area, velocity, trajectory.append):
                hits[(velocity.x, velocity.y)] = trajectory
    return hits


def part_1(inputs: tuple[str]) -> int:
    target_area = _parse(inputs)
    hits = _find_hits(target_area, True)
    return max(p.y
               for trajectory in hits.values()
               for p in trajectory)


def part_2(inputs: tuple[str]) -> int:
    target_area = _parse(inputs)
    hits = _find_hits(target_area, False)
    return len(hits)


TEST = """\
target area: x=20..30, y=-10..-5
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2021, 17)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 45
    assert part_2(TEST) == 112

    inputs = my_aocd.get_input(puzzle.year, puzzle.day, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()
