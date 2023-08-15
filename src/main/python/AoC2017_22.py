#! /usr/bin/env python3
#
# Advent of Code 2017 Day 22
#

from aoc import my_aocd
import aocd
from aoc.geometry import Direction, Turn


CLEAN = "."
INFECTED = "#"
WEAKENED = "W"
FLAGGED = "F"
LEFT = "L"
RIGHT = "R"
AROUND = "A"


def _parse(
    inputs: tuple[str],
) -> tuple[tuple[int, int], dict[tuple[int, int], str]]:
    size = len(inputs)
    nodes = {
        (x, y): inputs[size - 1 - y][x]
        for x, y in ((x, y) for x in range(size) for y in range(size))
    }
    start = (size // 2, size // 2)
    return nodes, start


def _solve(
    position: tuple[int, int],
    bursts: int,
    nodes: dict[tuple[int, int], str],
    states: dict[str, Turn],
    turns: dict[str, Turn],
) -> int:
    direction = Direction.UP
    count = 0
    for _ in range(bursts):
        current_state = nodes.get(position, CLEAN)
        new_state = states[current_state]
        nodes[position] = new_state
        if new_state == INFECTED:
            count += 1
        turn = turns.get(current_state)
        if turn is not None:
            direction = direction.turn(turn)
        position = (position[0] + direction.x, position[1] + direction.y)
    return count


def part_1(inputs: tuple[str]) -> int:
    nodes, start = _parse(inputs)
    states = {CLEAN: INFECTED, INFECTED: CLEAN}
    turns = {CLEAN: Turn.LEFT, INFECTED: Turn.RIGHT}
    return _solve(
        start,
        10_000,
        nodes,
        states,
        turns,
    )


def part_2(inputs: tuple[str]) -> int:
    nodes, start = _parse(inputs)
    states = {
        CLEAN: WEAKENED,
        WEAKENED: INFECTED,
        INFECTED: FLAGGED,
        FLAGGED: CLEAN,
    }
    turns = {
        CLEAN: Turn.LEFT,
        INFECTED: Turn.RIGHT,
        FLAGGED: Turn.AROUND,
    }
    return _solve(
        start,
        10_000_000,
        nodes,
        states,
        turns,
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
