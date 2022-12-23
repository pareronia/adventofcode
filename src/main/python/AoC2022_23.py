#! /usr/bin/env python3
#
# Advent of Code 2022 Day 23
#


from collections import defaultdict

import aocd
from aoc import my_aocd
from aoc.common import log

ELF = "#"
GROUND = "."
Tile = tuple[int, int]
Direction = tuple[int, int]
N = (-1, 0)
NW = (-1, -1)
NE = (-1, 1)
S = (1, 0)
SW = (1, -1)
SE = (1, 1)
W = (0, -1)
E = (0, 1)
DIRS = {N: {N, NE, NW}, S: {S, SE, SW}, W: {W, NW, SW}, E: {E, NE, SE}}


def _parse(inputs: tuple[str]) -> set[Tile]:
    return {
        (r, c)
        for r, line in enumerate(inputs)
        for c, ch in enumerate(line)
        if ch == ELF
    }


def _bounds(elves: set[Tile]) -> tuple[int, int, int, int]:
    min_r = min(r for r, _ in elves)
    min_c = min(c for _, c in elves)
    max_r = max(r for r, _ in elves)
    max_c = max(c for _, c in elves)
    return min_r, min_c, max_r, max_c


def _draw(elves: set[Tile]) -> None:
    if not __debug__:
        return
    min_r, min_c, max_r, max_c = _bounds(elves)
    for r in range(min_r, max_r + 1):
        [
            print(ELF if (r, c) in elves else GROUND, end="")
            for c in range(min_c, max_c + 1)
        ]
        print()


def _calculate_moves(
    elves: set[Tile], order: list[Direction]
) -> dict[Tile, list[Tile]]:
    moves = defaultdict(list[Tile])
    for r, c in elves:
        if all(
            (r + dr, c + dc) not in elves
            for dr, dc in {N, NE, E, SE, S, SW, W, NW}
        ):
            continue
        for d in order:
            if all((r + dr, c + dc) not in elves for dr, dc in DIRS[d]):
                moves[(r + d[0], c + d[1])].append((r, c))
                break
    return moves


def _execute_moves(
    elves: set[Tile], moves: dict[Tile, list[Tile]]
) -> list[Tile]:
    for move, candidates in moves.items():
        if len(candidates) > 1:
            continue
        elves.remove(candidates[0])
        elves.add(move)
    return elves


def part_1(inputs: tuple[str]) -> int:
    elves = _parse(inputs)
    order = [N, S, W, E]
    for i in range(10):
        log(f"Round {i + 1}:")
        moves = _calculate_moves(elves, order)
        elves = _execute_moves(elves, moves)
        order.append(order.pop(0))
        _draw(elves)
        log(order)
    min_r, min_c, max_r, max_c = _bounds(elves)
    return sum(
        1
        for c in range(min_c, max_c + 1)
        for r in range(min_r, max_r + 1)
        if (r, c) not in elves
    )


def part_2(inputs: tuple[str]) -> int:
    elves = _parse(inputs)
    order = [N, S, W, E]
    for i in range(1000):
        log(f"Round {i + 1}:")
        moves = _calculate_moves(elves, order)
        if len(moves) == 0:
            return i + 1
        elves = _execute_moves(elves, moves)
        order.append(order.pop(0))
    raise RuntimeError("unsolvable")


TEST = tuple(
    """\
....#..
..###.#
#...#.#
.#...##
#.###..
##.#.##
.#..#..
""".splitlines()
)


def main() -> None:
    puzzle = aocd.models.Puzzle(2022, 23)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 110
    assert part_2(TEST) == 20

    inputs = my_aocd.get_input_data(puzzle, 70)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()
