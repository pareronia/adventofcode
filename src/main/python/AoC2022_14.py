#! /usr/bin/env python3
#
# Advent of Code 2022 Day 14
#


import aocd

from aoc import my_aocd

Position = tuple[int, int]
SOURCE = Position((500, 0))


def _parse(inputs: tuple[str]) -> tuple[list[list[bool]], int]:
    rocks = set[Position]()
    max_x = 0
    max_y = 0
    for line in inputs:
        c = [list(map(int, p.split(","))) for p in line.strip().split(" -> ")]
        for (x1, y1), (x2, y2) in zip(c, c[1:]):
            x1, x2 = sorted([x1, x2])
            y1, y2 = sorted([y1, y2])
            for x in range(x1, x2 + 1):
                for y in range(y1, y2 + 1):
                    rocks.add(Position((x, y)))
                    max_x = max(max_x, x)
                    max_y = max(max_y, y)
    occupied = [[False] * (max_x + 150) for _ in range(max_y + 2)]
    for x, y in rocks:
        occupied[y][x] = True
    return occupied, max_y


def _drop(occupied: list[list[bool]], max_y: int) -> Position:
    curr = SOURCE
    while True:
        p = curr
        curr_x, curr_y = curr
        for dx, dy in [(0, 1), (-1, 1), (1, 1)]:
            try:
                if not occupied[curr_y + dy][curr_x + dx]:
                    curr = (curr_x + dx, curr_y + dy)
                    break
            except IndexError:
                pass
        if curr == p:
            return curr
        if curr[1] > max_y:
            return None


def _solve(occupied: list[list[bool]], max_y: int) -> int:
    cnt = 0
    while True:
        p = _drop(occupied, max_y)
        if p is None:
            break
        occupied[p[1]][p[0]] = True
        cnt += 1
        if p == SOURCE:
            break
    return cnt


def part_1(inputs: tuple[str]) -> int:
    occupied, max_y = _parse(inputs)
    return _solve(occupied, max_y)


def part_2(inputs: tuple[str]) -> int:
    occupied, max_y = _parse(inputs)
    return _solve(occupied, max_y + 2)


TEST = tuple(
    """\
498,4 -> 498,6 -> 496,6
503,4 -> 502,4 -> 502,9 -> 494,9
""".splitlines()
)


def main() -> None:
    puzzle = aocd.models.Puzzle(2022, 14)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 24
    assert part_2(TEST) == 93

    inputs = my_aocd.get_input_data(puzzle, 164)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()
