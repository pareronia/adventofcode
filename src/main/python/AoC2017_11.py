#! /usr/bin/env python3
#
# Advent of Code 2017 Day 20
#
from __future__ import annotations
from aoc import my_aocd
from aoc.geometry3d import Vector3D, Position3D
import aocd


ORIGIN = Position3D.of(0, 0, 0)
HEADING = {
    "n": Vector3D.of(0, -1, 1),
    "ne": Vector3D.of(1, -1, 0),
    "se": Vector3D.of(1, 0, -1),
    "s": Vector3D.of(0, 1, -1),
    "sw": Vector3D.of(-1, 1, 0),
    "nw": Vector3D.of(-1, 0, 1),
}


def _parse(inputs: tuple[str]) -> list[Vector3D]:
    assert len(inputs) == 1
    return [HEADING[p] for p in inputs[0].split(",")]


def _positions(path: list[Vector3D]) -> list[Position3D]:
    positions = [ORIGIN]
    [positions.append(positions[-1].translate(p)) for p in path]
    return positions


def _steps(p: Position3D) -> int:
    return p.manhattan_distance_to_origin() // 2


def part_1(inputs: tuple[str]) -> int:
    path = _parse(inputs)
    return _steps(_positions(path)[-1])


def part_2(inputs: tuple[str]) -> int:
    path = _parse(inputs)
    return max(_steps(p) for p in _positions(path))


TEST1 = """ne,ne,ne""".splitlines()
TEST2 = """ne,ne,sw,sw""".splitlines()
TEST3 = """ne,ne,s,s""".splitlines()
TEST4 = """se,sw,se,sw,sw""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2017, 11)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST1) == 3
    assert part_1(TEST2) == 0
    assert part_1(TEST3) == 2
    assert part_1(TEST4) == 3

    inputs = my_aocd.get_input(2017, 11, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()
