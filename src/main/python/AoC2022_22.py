#! /usr/bin/env python3
#
# Advent of Code 2022 Day 22
#
# TODO: make generalized part2 solution that works on sample input as well


import re
import sys

from aoc import my_aocd
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log

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
"""

DIRS = {"N": (-1, 0), "E": (0, 1), "S": (1, 0), "W": (0, -1)}
TURNS = {
    "R": {"N": "E", "E": "S", "S": "W", "W": "N"},
    "L": {"N": "W", "E": "N", "S": "E", "W": "S"},
}
RE = re.compile(r"([LR])([0-9]+)")

Grid = tuple[str, ...]
Move = tuple[str, int]
Params = tuple[int, int, str, int, int]
Input = tuple[Grid, tuple[Move, ...]]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        blocks = my_aocd.to_blocks(input_data)
        moves = tuple((a, int(b)) for a, b in RE.findall("R" + blocks[1][0]))
        return tuple(blocks[0]), moves

    def ans(self, r: int, c: int, facing: str) -> int:
        return 1000 * (r + 1) + 4 * (c + 1) + "ESWN".index(facing)

    def part_1(self, inputs: Input) -> Output1:  # noqa:C901
        grid, moves = inputs
        size = len(grid) // 3
        start = 0, 2 * size
        log(start)
        rows_cache = dict[int, tuple[int, ...]]()
        cols_cache = dict[int, tuple[int, ...]]()

        def do_step(r: int, c: int, facing: str) -> tuple[int, int]:
            def get_rows(col: int) -> tuple[int, ...]:
                return tuple(
                    r
                    for r, line in enumerate(grid)
                    if col < len(line) and line[col] != " "
                )

            def get_cols(row: int) -> tuple[int, ...]:
                return tuple(c for c, _ in enumerate(grid[row]) if _ != " ")

            dr, dc = DIRS[facing]
            rr, cc = r + dr, c + dc
            if facing in {"N", "S"}:
                rows = rows_cache.setdefault(c, get_rows(c))
                if rr < rows[0]:
                    rr = rows[-1]
                elif rr > rows[-1]:
                    rr = rows[0]
            else:
                cols = cols_cache.setdefault(r, get_cols(r))
                if cc < cols[0]:
                    cc = cols[-1]
                elif cc > cols[-1]:
                    cc = cols[0]
            return rr, cc

        r, c = start
        facing = "N"
        for turn, steps in moves:
            facing = TURNS[turn][facing]
            for _ in range(steps):
                rr, cc = do_step(r, c, facing)
                if grid[rr][cc] == "#":
                    break
                r, c = rr, cc
                log((r, c))
        return self.ans(r, c, facing)

    def part_2(self, inputs: Input) -> Output2:  # noqa:C901,PLR0912,PLR0915
        def top_edge_1(params: Params) -> Params:
            r, c, facing, rr, cc = params
            assert facing == "N", ((r, c, facing), (rr, cc))
            rr = cc + 100
            cc = 0
            assert rr >= 0 and cc >= 0, ((r, c, facing), (rr, cc))
            # assert 6
            assert 150 <= rr < 200 and 0 <= cc < 50, (
                (r, c, facing),
                (rr, cc),
            )
            facing = "E"
            return r, c, facing, rr, cc

        def left_edge_1(params: Params) -> Params:
            r, c, facing, rr, cc = params
            assert facing == "W", ((r, c, facing), (rr, cc))
            rr = 149 - rr
            cc = 0
            assert rr >= 0 and cc >= 0, ((r, c, facing), (rr, cc))
            # assert 4
            assert 100 <= rr < 150 and 0 <= cc < 50, (
                (r, c, facing),
                (rr, cc),
            )
            facing = "E"
            return r, c, facing, rr, cc

        def top_edge_2(params: Params) -> Params:
            r, c, facing, rr, cc = params
            assert facing == "N", ((r, c, facing), (rr, cc))
            rr = 199
            cc -= 100
            assert rr >= 0 and cc >= 0, ((r, c, facing), (rr, cc))
            # assert 6
            assert 150 <= rr < 200 and 0 <= cc < 50, (
                (r, c, facing),
                (rr, cc),
            )
            facing = "N"
            return r, c, facing, rr, cc

        def right_edge_2(params: Params) -> Params:
            r, c, facing, rr, cc = params
            assert facing == "E", ((r, c, facing), (rr, cc))
            rr = 149 - rr
            cc = 99
            assert rr >= 0 and cc >= 0, ((r, c, facing), (rr, cc))
            # assert 5
            assert 100 <= rr < 150 and 50 <= cc < 100, (
                (r, c, facing),
                (rr, cc),
            )
            facing = "W"
            return r, c, facing, rr, cc

        def bottom_edge_2(params: Params) -> Params:
            r, c, facing, rr, cc = params
            assert facing == "S", ((r, c, facing), (rr, cc))
            rr = cc - 50
            cc = 99
            assert rr >= 0 and cc >= 0, ((r, c, facing), (rr, cc))
            # assert 3
            assert 50 <= rr < 100 and 50 <= cc < 100, (
                (r, c, facing),
                (rr, cc),
            )
            facing = "W"
            return r, c, facing, rr, cc

        def left_edge_3(params: Params) -> Params:
            r, c, facing, rr, cc = params
            assert facing == "W", ((r, c, facing), (rr, cc))
            cc = rr - 50
            rr = 100
            assert rr >= 0 and cc >= 0, ((r, c, facing), (rr, cc))
            # assert 4
            assert 100 <= rr < 150 and 0 <= cc < 50, (
                (r, c, facing),
                (rr, cc),
            )
            facing = "S"
            return r, c, facing, rr, cc

        def right_edge_3(params: Params) -> Params:
            r, c, facing, rr, cc = params
            assert facing == "E", ((r, c, facing), (rr, cc))
            cc = rr + 50
            rr = 49
            assert rr >= 0 and cc >= 0, ((r, c, facing), (rr, cc))
            # assert 2
            assert 0 <= rr < 50 and 100 <= cc < 150, (
                (r, c, facing),
                (rr, cc),
            )
            facing = "N"
            return r, c, facing, rr, cc

        def left_edge_4(params: Params) -> Params:
            r, c, facing, rr, cc = params
            assert facing == "W", ((r, c, facing), (rr, cc))
            rr = 149 - rr
            cc = 50
            assert rr >= 0 and cc >= 0, ((r, c, facing), (rr, cc))
            # assert 1
            assert 0 <= rr < 50 and 50 <= cc < 100, (
                (r, c, facing),
                (rr, cc),
            )
            facing = "E"
            return r, c, facing, rr, cc

        def top_edge_4(params: Params) -> Params:
            r, c, facing, rr, cc = params
            assert facing == "N", ((r, c, facing), (rr, cc))
            rr = cc + 50
            cc = 50
            assert rr >= 0 and cc >= 0, ((r, c, facing), (rr, cc))
            # assert 3
            assert 50 <= rr < 100 and 50 <= cc < 100, (
                (r, c, facing),
                (rr, cc),
            )
            facing = "E"
            return r, c, facing, rr, cc

        def right_edge_5(params: Params) -> Params:
            r, c, facing, rr, cc = params
            assert facing == "E", ((r, c, facing), (rr, cc))
            rr = 149 - rr
            cc = 149
            assert rr >= 0 and cc >= 0, ((r, c, facing), (rr, cc))
            # assert 2
            assert 0 <= rr < 50 and 100 <= cc < 150, (
                (r, c, facing),
                (rr, cc),
            )
            facing = "W"
            return r, c, facing, rr, cc

        def bottom_edge_5(params: Params) -> Params:
            r, c, facing, rr, cc = params
            assert facing == "S", ((r, c, facing), (rr, cc))
            rr = 100 + cc
            cc = 49
            assert rr >= 0 and cc >= 0, ((r, c, facing), (rr, cc))
            # assert 6
            assert 150 <= rr < 200 and 0 <= cc < 50, (
                (r, c, facing),
                (rr, cc),
            )
            facing = "W"
            return r, c, facing, rr, cc

        def left_edge_6(params: Params) -> Params:
            r, c, facing, rr, cc = params
            assert facing == "W", ((r, c, facing), (rr, cc))
            cc = rr - 100
            rr = 0
            assert rr >= 0 and cc >= 0, ((r, c, facing), (rr, cc))
            # assert 1
            assert 0 <= rr < 50 and 50 <= cc < 100, (
                (r, c, facing),
                (rr, cc),
            )
            facing = "S"
            return r, c, facing, rr, cc

        def right_edge_6(params: Params) -> Params:
            r, c, facing, rr, cc = params
            assert facing == "E", ((r, c, facing), (rr, cc))
            cc = rr - 100
            rr = 149
            assert rr >= 0 and cc >= 0, ((r, c, facing), (rr, cc))
            # assert 5
            assert 100 <= rr < 150 and 50 <= cc < 100, (
                (r, c, facing),
                (rr, cc),
            )
            facing = "N"
            return r, c, facing, rr, cc

        def bottom_edge_6(params: Params) -> Params:
            r, c, facing, rr, cc = params
            assert facing == "S", ((r, c, facing), (rr, cc))
            rr = 0
            cc = cc + 100
            assert rr >= 0 and cc >= 0, ((r, c, facing), (rr, cc))
            # assert 2
            assert 0 <= rr < 50 and 100 <= cc < 150, (
                (r, c, facing),
                (rr, cc),
            )
            facing = "S"
            return r, c, facing, rr, cc

        grid, moves = inputs
        start = 0, next(c for c, _ in enumerate(grid[0]) if _ != " ")
        log(start)
        r, c = start
        facing = "N"
        for turn, steps in moves:
            facing = TURNS[turn][facing]
            for _ in range(steps):
                dr, dc = DIRS[facing]
                log((r, c, dr, dc))
                rr = r + dr
                cc = c + dc
                ff = facing
                if rr == -1 and 50 <= cc < 100:
                    r, c, ff, rr, cc = top_edge_1((r, c, ff, rr, cc))
                elif cc == 49 and 0 <= rr < 50:
                    r, c, ff, rr, cc = left_edge_1((r, c, ff, rr, cc))
                elif rr == -1 and 100 <= cc < 150:
                    r, c, ff, rr, cc = top_edge_2((r, c, ff, rr, cc))
                elif cc == 150 and 0 <= rr < 50:
                    r, c, ff, rr, cc = right_edge_2((r, c, ff, rr, cc))
                elif rr == 50 and 100 <= cc < 150 and ff == "S":
                    r, c, ff, rr, cc = bottom_edge_2((r, c, ff, rr, cc))
                elif cc == 49 and 50 <= rr < 100:
                    r, c, ff, rr, cc = left_edge_3((r, c, ff, rr, cc))
                elif cc == 100 and 50 <= rr < 100 and ff == "E":
                    r, c, ff, rr, cc = right_edge_3((r, c, ff, rr, cc))
                elif cc == -1 and 100 <= rr < 150:
                    r, c, ff, rr, cc = left_edge_4((r, c, ff, rr, cc))
                elif rr == 99 and 0 <= cc < 50:
                    r, c, ff, rr, cc = top_edge_4((r, c, ff, rr, cc))
                elif cc == 100 and 100 <= rr < 150:
                    r, c, ff, rr, cc = right_edge_5((r, c, ff, rr, cc))
                elif rr == 150 and 50 <= cc < 100 and ff == "S":
                    r, c, ff, rr, cc = bottom_edge_5((r, c, ff, rr, cc))
                elif cc == -1 and 150 <= rr < 200:
                    r, c, ff, rr, cc = left_edge_6((r, c, ff, rr, cc))
                elif cc == 50 and 150 <= rr < 200 and ff == "E":
                    r, c, ff, rr, cc = right_edge_6((r, c, ff, rr, cc))
                elif rr == 200 and 0 <= cc < 50:
                    r, c, ff, rr, cc = bottom_edge_6((r, c, ff, rr, cc))
                try:
                    val = grid[rr][cc]
                except IndexError:
                    log(((r, c, facing), (rr, cc)))
                    raise
                if val == "#":
                    break
                r = rr
                c = cc
                facing = ff
        return self.ans(r, c, facing)

    @aoc_samples(
        (
            ("part_1", TEST, 6032),
            # TODO ("part_2", TEST, 5031),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2022, 22)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
