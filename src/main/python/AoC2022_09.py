#! /usr/bin/env python3
#
# Advent of Code 2022 Day 9
#


from itertools import product

import aocd
from aoc import my_aocd

Position = tuple[int, int]
Move = tuple[int, int]

MOVES = {"U": (0, 1), "D": (0, -1), "L": (-1, 0), "R": (1, 0)}


def _parse(inputs: tuple[str]) -> list[Move]:
    return [
        MOVES[m]
        for move, amount in [line.split() for line in inputs]
        for _, m in product(range(int(amount)), move)
    ]


def _catchup(head: Position, tail: Position) -> Position:
    dx = head[0] - tail[0]
    dy = head[1] - tail[1]
    if abs(dx) > 1 or abs(dy) > 1:
        return (
            tail[0] + (-1 if dx < 0 else 1 if dx > 0 else 0),
            tail[1] + (-1 if dy < 0 else 1 if dy > 0 else 0),
        )
    return tail


def _move_rope(rope: list[Position], move: Move) -> list[Position]:
    rope[0] = (rope[0][0] + move[0], rope[0][1] + move[1])
    for i in range(1, len(rope)):
        rope[i] = _catchup(rope[i - 1], rope[i])
    return rope


def _solve(size: int, moves: list[Move]) -> int:
    rope = [(0, 0)] * size
    seen = {(rope := _move_rope(rope, move))[-1] for move in moves}  # noqa F841
    return len(seen)


def part_1(inputs: tuple[str]) -> int:
    moves = _parse(inputs)
    return _solve(2, moves)


def part_2(inputs: tuple[str]) -> int:
    moves = _parse(inputs)
    return _solve(10, moves)


TEST1 = """\
R 4
U 4
L 3
D 1
R 4
D 1
L 5
R 2
""".splitlines()
TEST2 = """\
R 5
U 8
L 8
D 3
R 17
D 10
L 25
U 20
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2022, 9)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST1) == 13
    assert part_2(TEST1) == 1
    assert part_2(TEST2) == 36

    inputs = my_aocd.get_input_data(puzzle, 2000)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()
