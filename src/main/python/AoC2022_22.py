#! /usr/bin/env python3
#
# Advent of Code 2022 Day 22
#


import re

import aocd
from aoc import my_aocd
from aoc.common import log


def _parse(inputs: tuple[str]):
    blocks = my_aocd.to_blocks(inputs)
    moves = [
        (a, int(b))
        for a, b in re.findall(r"([LR])([0-9]+)", "R" + blocks[1][0])
    ]
    return blocks[0], moves


def part_1(inputs: tuple[str]) -> int:
    grid, moves = _parse(inputs)
    start = 0, next(c for c, _ in enumerate(grid[0]) if _ != " ")
    log(start)
    r, c = start
    facing = "N"
    for turn, steps in moves:
        if facing == "N":
            facing = "E" if turn == "R" else "W"
        elif facing == "E":
            facing = "S" if turn == "R" else "N"
        elif facing == "S":
            facing = "W" if turn == "R" else "E"
        elif facing == "W":
            facing = "N" if turn == "R" else "S"
        for i in range(steps):
            if facing == "N":
                rr = r - 1
                cc = c
                rows = [
                    r
                    for r, _ in enumerate(
                        row[c] for row in [row for row in grid if c < len(row)]
                    )
                    if _ != " "
                ]
                if rr < rows[0]:
                    rr = rows[-1]
                elif rr > rows[-1]:
                    rr = rows[0]
                if grid[rr][cc] == "#":
                    break
                else:
                    r, c = rr, cc
            elif facing == "E":
                rr = r
                cc = c + 1
                cols = [c for c, _ in enumerate(grid[r]) if _ != " "]
                if cc < cols[0]:
                    cc = cols[-1]
                elif cc > cols[-1]:
                    cc = cols[0]
                if grid[rr][cc] == "#":
                    break
                else:
                    r, c = rr, cc
            elif facing == "S":
                rr = r + 1
                cc = c
                rows = [
                    r
                    for r, _ in enumerate(
                        row[c] for row in [row for row in grid if c < len(row)]
                    )
                    if _ != " "
                ]
                if rr < rows[0]:
                    rr = rows[-1]
                elif rr > rows[-1]:
                    rr = rows[0]
                if grid[rr][cc] == "#":
                    break
                else:
                    r, c = rr, cc
            elif facing == "W":
                rr = r
                cc = c - 1
                cols = [c for c, _ in enumerate(grid[r]) if _ != " "]
                if cc < cols[0]:
                    cc = cols[-1]
                elif cc > cols[-1]:
                    cc = cols[0]
                if grid[rr][cc] == "#":
                    break
                else:
                    r, c = rr, cc
            log((r, c))
    f = (
        0
        if facing == "E"
        else 1
        if facing == "S"
        else 2
        if facing == "W"
        else 3
    )
    ans = 1000 * (r + 1) + 4 * (c + 1) + f
    log(ans)
    return ans


def part_2(inputs: tuple[str]) -> int:
    return 0


TEST = """\
        ...#
        .#..
        #...
        ....
...#.......#
........#...
..#....#....
..........#.
        ...#....
        .....#..
        .#......
        ......#.

10R5L5R10L4R5L5
""".splitlines()


def main() -> None:
    aocd.get_data(year=2022, day=22, block=True)
    puzzle = aocd.models.Puzzle(2022, 22)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 6032
    assert part_2(TEST) == 0

    inputs = my_aocd.get_input_data(puzzle, 202)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()
