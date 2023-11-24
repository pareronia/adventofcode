#! /usr/bin/env python3
#
# Advent of Code 2020 Day 17
#

from aoc import my_aocd
from aoc.game_of_life import GameOfLife, InfiniteGrid, ClassicRules
import aocd

ON = "#"
GENERATIONS = 6


def _parse(inputs: tuple[str, ...], dim: int) -> GameOfLife:
    alive = {(r, c, 0, 0)
             for r, row in enumerate(inputs)
             for c, state in enumerate(row)
             if state == ON}
    return GameOfLife(alive, InfiniteGrid(dim), ClassicRules())


def _solve(inputs: tuple[str, ...], dim: int) -> int:
    game_of_life = _parse(inputs, dim)
    for i in range(GENERATIONS):
        game_of_life.next_generation()
    return sum(1 for _ in game_of_life.alive)


def part_1(inputs: tuple[str, ...]) -> int:
    return _solve(inputs, 3)


def part_2(inputs: tuple[str, ...]) -> int:
    return _solve(inputs, 4)


TEST = """\
.#.
..#
###
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2020, 17)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 112  # type:ignore[arg-type]
    assert part_2(TEST) == 848  # type:ignore[arg-type]

    inputs = my_aocd.get_input_data(puzzle, 8)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()
