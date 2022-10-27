#! /usr/bin/env python3
#
# Advent of Code 2017 Day 22
#

from aoc import my_aocd
from aoc.navigation import Headings
import aocd


CLEAN = "."
INFECTED = "#"
WEAKENED = "W"
FLAGGED = "F"
LEFT = "L"
RIGHT = "R"
AROUND = "A"
HEADING_UP = (Headings.N.value.x, Headings.N.value.y)
HEADING_DOWN = (Headings.S.value.x, Headings.S.value.y)
HEADING_LEFT = (Headings.W.value.x, Headings.W.value.y)
HEADING_RIGHT = (Headings.E.value.x, Headings.E.value.y)
TURNS = {
    HEADING_UP: {
        LEFT: HEADING_LEFT,
        RIGHT: HEADING_RIGHT,
        AROUND: HEADING_DOWN,
    },
    HEADING_DOWN: {
        LEFT: HEADING_RIGHT,
        RIGHT: HEADING_LEFT,
        AROUND: HEADING_UP,
    },
    HEADING_LEFT: {
        LEFT: HEADING_DOWN,
        RIGHT: HEADING_UP,
        AROUND: HEADING_RIGHT,
    },
    HEADING_RIGHT: {
        LEFT: HEADING_UP,
        RIGHT: HEADING_DOWN,
        AROUND: HEADING_LEFT,
    },
}


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
    states: dict[str, str],
    turns: dict[str, str],
) -> int:
    heading = HEADING_UP
    count = 0
    for _ in range(bursts):
        current_state = nodes.get(position, CLEAN)
        new_state = states[current_state]
        nodes[position] = new_state
        if new_state == INFECTED:
            count += 1
        turn = turns[current_state]
        if turn is not None:
            heading = TURNS[heading][turn]
        position = (position[0] + heading[0], position[1] + heading[1])
    return count


def part_1(inputs: tuple[str]) -> int:
    nodes, start = _parse(inputs)
    states = {CLEAN: INFECTED, INFECTED: CLEAN}
    turns = {CLEAN: LEFT, INFECTED: RIGHT}
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
        CLEAN: LEFT,
        WEAKENED: None,
        INFECTED: RIGHT,
        FLAGGED: AROUND,
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
