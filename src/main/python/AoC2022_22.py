#! /usr/bin/env python3
#
# Advent of Code 2022 Day 22
#
# TODO: make generalized part2 solution that works on sample input as well


import re

import aocd
from aoc import my_aocd
from aoc.common import log

DIRS = {"N": (-1, 0), "E": (0, 1), "S": (1, 0), "W": (0, -1)}
TURNS = {
    "R": {"N": "E", "E": "S", "S": "W", "W": "N"},
    "L": {"N": "W", "E": "N", "S": "E", "W": "S"},
}


def _parse(inputs: tuple[str]) -> tuple[list[str], list[tuple[str, int]]]:
    blocks = my_aocd.to_blocks(inputs)
    moves = [
        (a, int(b))
        for a, b in re.findall(r"([LR])([0-9]+)", "R" + blocks[1][0])
    ]
    return blocks[0], moves


def _ans(r: int, c: int, facing: str) -> int:
    f = (
        0
        if facing == "E"
        else 1
        if facing == "S"
        else 2
        if facing == "W"
        else 3
    )
    return 1000 * (r + 1) + 4 * (c + 1) + f


def part_1(inputs: tuple[str]) -> int:
    grid, moves = _parse(inputs)
    size = len(grid) // 3
    start = 0, 2 * size
    log(start)
    r, c = start
    facing = "N"
    for turn, steps in moves:
        facing = TURNS[turn][facing]
        for i in range(steps):
            dr, dc = DIRS[facing]
            rr = r + dr
            cc = c + dc
            if facing == "N" or facing == "S":
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
            elif facing == "E" or facing == "W":
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
    return _ans(r, c, facing)


def part_2(inputs: tuple[str]) -> int:
    grid, moves = _parse(inputs)
    start = 0, next(c for c, _ in enumerate(grid[0]) if _ != " ")
    log(start)
    r, c = start
    facing = "N"
    for turn, steps in moves:
        facing = TURNS[turn][facing]
        for i in range(steps):
            # assert cnt < 10
            dr, dc = DIRS[facing]
            log((r, c, dr, dc))
            rr = r + dr
            cc = c + dc
            ff = facing
            # top edge 1
            if rr == -1 and 50 <= cc < 100:
                assert ff == "N", ((r, c, facing), (rr, cc))
                rr = cc + 100
                cc = 0
                assert rr >= 0 and cc >= 0, ((r, c, facing), (rr, cc))
                # assert 6
                assert 150 <= rr < 200 and 0 <= cc < 50, (
                    (r, c, facing),
                    (rr, cc),
                )
                ff = "E"
            # left edge 1
            elif cc == 49 and 0 <= rr < 50:
                assert ff == "W", ((r, c, facing), (rr, cc))
                rr = 149 - rr
                cc = 0
                assert rr >= 0 and cc >= 0, ((r, c, facing), (rr, cc))
                # assert 4
                assert 100 <= rr < 150 and 0 <= cc < 50, (
                    (r, c, facing),
                    (rr, cc),
                )
                ff = "E"
            # top edge 2
            elif rr == -1 and 100 <= cc < 150:
                assert ff == "N", ((r, c, facing), (rr, cc))
                rr = 199
                cc -= 100
                assert rr >= 0 and cc >= 0, ((r, c, facing), (rr, cc))
                # assert 6
                assert 150 <= rr < 200 and 0 <= cc < 50, (
                    (r, c, facing),
                    (rr, cc),
                )
                ff = "N"
            # right edge 2
            elif cc == 150 and 0 <= rr < 50:
                assert ff == "E", ((r, c, facing), (rr, cc))
                rr = 149 - rr
                cc = 99
                assert rr >= 0 and cc >= 0, ((r, c, facing), (rr, cc))
                # assert 5
                assert 100 <= rr < 150 and 50 <= cc < 100, (
                    (r, c, facing),
                    (rr, cc),
                )
                ff = "W"
            # bottom edge 2
            elif rr == 50 and 100 <= cc < 150 and ff == "S":
                assert ff == "S", ((r, c, facing), (rr, cc))
                rr = cc - 50
                cc = 99
                assert rr >= 0 and cc >= 0, ((r, c, facing), (rr, cc))
                # assert 3
                assert 50 <= rr < 100 and 50 <= cc < 100, (
                    (r, c, facing),
                    (rr, cc),
                )
                ff = "W"
            # left edge 3
            elif cc == 49 and 50 <= rr < 100:
                assert ff == "W", ((r, c, facing), (rr, cc))
                cc = rr - 50
                rr = 100
                assert rr >= 0 and cc >= 0, ((r, c, facing), (rr, cc))
                # assert 4
                assert 100 <= rr < 150 and 0 <= cc < 50, (
                    (r, c, facing),
                    (rr, cc),
                )
                ff = "S"
            # right edge 3
            elif cc == 100 and 50 <= rr < 100 and ff == "E":
                assert ff == "E", ((r, c, facing), (rr, cc))
                cc = rr + 50
                rr = 49
                assert rr >= 0 and cc >= 0, ((r, c, facing), (rr, cc))
                # assert 2
                assert 0 <= rr < 50 and 100 <= cc < 150, (
                    (r, c, facing),
                    (rr, cc),
                )
                ff = "N"
            # left edge 4
            elif cc == -1 and 100 <= rr < 150:
                assert ff == "W", ((r, c, facing), (rr, cc))
                rr = 149 - rr
                cc = 50
                assert rr >= 0 and cc >= 0, ((r, c, facing), (rr, cc))
                # assert 1
                assert 0 <= rr < 50 and 50 <= cc < 100, (
                    (r, c, facing),
                    (rr, cc),
                )
                ff = "E"
            # top edge 4
            elif rr == 99 and 0 <= cc < 50:
                assert ff == "N", ((r, c, facing), (rr, cc))
                rr = cc + 50
                cc = 50
                assert rr >= 0 and cc >= 0, ((r, c, facing), (rr, cc))
                # assert 3
                assert 50 <= rr < 100 and 50 <= cc < 100, (
                    (r, c, facing),
                    (rr, cc),
                )
                ff = "E"
            # right edge 5
            elif cc == 100 and 100 <= rr < 150:
                assert ff == "E", ((r, c, facing), (rr, cc))
                rr = 149 - rr
                cc = 149
                assert rr >= 0 and cc >= 0, ((r, c, facing), (rr, cc))
                # assert 2
                assert 0 <= rr < 50 and 100 <= cc < 150, (
                    (r, c, facing),
                    (rr, cc),
                )
                ff = "W"
            # bottom edge 5
            elif rr == 150 and 50 <= cc < 100 and ff == "S":
                assert ff == "S", ((r, c, facing), (rr, cc))
                rr = 100 + cc
                cc = 49
                assert rr >= 0 and cc >= 0, ((r, c, facing), (rr, cc))
                # assert 6
                assert 150 <= rr < 200 and 0 <= cc < 50, (
                    (r, c, facing),
                    (rr, cc),
                )
                ff = "W"
            # left edge 6
            elif cc == -1 and 150 <= rr < 200:
                assert ff == "W", ((r, c, facing), (rr, cc))
                cc = rr - 100
                rr = 0
                assert rr >= 0 and cc >= 0, ((r, c, facing), (rr, cc))
                # assert 1
                assert 0 <= rr < 50 and 50 <= cc < 100, (
                    (r, c, facing),
                    (rr, cc),
                )
                ff = "S"
            # right edge 6
            elif cc == 50 and 150 <= rr < 200 and ff == "E":
                assert ff == "E", ((r, c, facing), (rr, cc))
                cc = rr - 100
                rr = 149
                assert rr >= 0 and cc >= 0, ((r, c, facing), (rr, cc))
                # assert 5
                assert 100 <= rr < 150 and 50 <= cc < 100, (
                    (r, c, facing),
                    (rr, cc),
                )
                ff = "N"
            # bottom edge 6
            elif rr == 200 and 0 <= cc < 50:
                assert ff == "S", ((r, c, facing), (rr, cc))
                rr = 0
                cc = cc + 100
                assert rr >= 0 and cc >= 0, ((r, c, facing), (rr, cc))
                # assert 2
                assert 0 <= rr < 50 and 100 <= cc < 150, (
                    (r, c, facing),
                    (rr, cc),
                )
                ff = "S"
            try:
                val = grid[rr][cc]
            except IndexError as e:
                log(((r, c, facing), (rr, cc)))
                raise e
            if val == "#":
                break
            else:
                r = rr
                c = cc
                facing = ff
    return _ans(r, c, facing)


TEST = tuple(
    """\
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
)


def main() -> None:
    aocd.get_data(year=2022, day=22, block=True)
    puzzle = aocd.models.Puzzle(2022, 22)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 6032
    # assert part_2(TEST) == 5031

    inputs = my_aocd.get_input_data(puzzle, 202)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()
