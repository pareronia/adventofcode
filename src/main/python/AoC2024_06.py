#! /usr/bin/env python3
#
# Advent of Code 2024 Day 6
#

from __future__ import annotations

import sys
from collections import OrderedDict
from typing import TYPE_CHECKING

if TYPE_CHECKING:
    from collections.abc import Iterator

from typing import NamedTuple

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.grid import CharGrid

Cell = tuple[int, int]
Direction = tuple[int, int]
Input = CharGrid
Output1 = int
Output2 = int

UP, RIGHT, DOWN, LEFT = (0, 1), (1, 0), (0, -1), (-1, 0)
DIRS = [UP, RIGHT, DOWN, LEFT]


TEST = """\
....#.....
.........#
..........
..#.......
.......#..
..........
.#..^.....
........#.
#.........
......#...
"""


class Obstacles(NamedTuple):
    obs: dict[Direction, list[list[Cell | None]]]

    @classmethod
    def _obstacles(
        cls, grid: CharGrid, starts: Iterator[Cell], direction: Direction
    ) -> list[list[Cell | None]]:
        h, w = grid.get_height(), grid.get_width()
        obs: list[list[Cell | None]] = [
            [None for _ in range(w)] for _ in range(h)
        ]
        for start in starts:
            last = start if grid.values[start[0]][start[1]] == "#" else None
            cell = start
            while True:
                r, c = cell
                obs[r][c] = last
                if grid.values[r][c] == "#":
                    last = cell
                nr, nc = r - direction[1], c + direction[0]
                if not (0 <= nr < h and 0 <= nc < w):
                    break
                cell = (nr, nc)
        return obs

    @classmethod
    def from_grid(cls, grid: CharGrid) -> Obstacles:
        h, w = grid.get_height(), grid.get_width()
        obs = dict[Direction, list[list[Cell | None]]]()
        mci, mri = grid.get_max_col_index(), grid.get_max_row_index()
        obs[RIGHT] = Obstacles._obstacles(
            grid, ((r, mci) for r in range(h)), LEFT
        )
        obs[LEFT] = Obstacles._obstacles(
            grid, ((r, 0) for r in range(h)), RIGHT
        )
        obs[UP] = Obstacles._obstacles(grid, ((0, c) for c in range(w)), DOWN)
        obs[DOWN] = Obstacles._obstacles(
            grid, ((mri, c) for c in range(w)), UP
        )
        return Obstacles(obs)

    def get_next(
        self, start: Cell, direction: Direction, extra: Cell
    ) -> Cell | None:
        obs = self.obs[direction][start[0]][start[1]]
        if obs is None:
            if (
                direction[1] == 0
                and start[0] == extra[0]
                and direction == (RIGHT if start[1] < extra[1] else LEFT)
            ) or (
                direction[0] == 0
                and start[1] == extra[1]
                and direction == (DOWN if start[0] < extra[0] else UP)
            ):
                return extra
            return None

        if (
            direction[1] == 0
            and obs[0] == extra[0]
            and direction == (RIGHT if start[1] < extra[1] else LEFT)
        ):
            dextra = abs(extra[1] - start[1])
            dobs = abs(obs[1] - start[1])
            return obs if dobs < dextra else extra

        if (
            direction[0] == 0
            and obs[1] == extra[1]
            and direction == (DOWN if start[0] < extra[0] else UP)
        ):
            dextra = abs(extra[0] - start[0])
            dobs = abs(obs[0] - start[0])
            return obs if dobs < dextra else extra

        return obs


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return CharGrid.from_strings(list(input_data))

    def visited(
        self,
        grid: CharGrid,
        direction: int,
    ) -> OrderedDict[Cell, list[int]]:
        start = next(grid.get_all_equal_to("^"))
        pos = (start.row, start.col)
        h, w = grid.get_height(), grid.get_width()
        seen = OrderedDict[Cell, list[int]]()
        seen.setdefault(pos, []).append(direction)
        while True:
            nxt = (pos[0] - DIRS[direction][1], pos[1] + DIRS[direction][0])
            if not (0 <= nxt[0] < h and 0 <= nxt[1] < w):
                return seen
            if grid.values[nxt[0]][nxt[1]] == "#":
                direction = (direction + 1) % len(DIRS)
            else:
                pos = nxt
            seen.setdefault(pos, []).append(direction)

    def is_loop(
        self, pos: Cell, direction: int, obs: Obstacles, extra: Cell
    ) -> bool:
        seen = dict[Cell, int]()
        seen[pos] = 1 << direction
        while True:
            nxt_obs = obs.get_next(pos, DIRS[direction], extra)
            if nxt_obs is None:
                return False
            pos = (
                nxt_obs[0] + DIRS[direction][1],
                nxt_obs[1] - DIRS[direction][0],
            )
            direction = (direction + 1) % len(DIRS)
            if pos in seen and seen[pos] & (1 << direction) != 0:
                return True
            seen[pos] = seen.get(pos, 0) | (1 << direction)

    def part_1(self, grid: Input) -> Output1:
        return len(self.visited(grid, 0))

    def part_2(self, grid: Input) -> Output2:
        obs = Obstacles.from_grid(grid)
        visited = self.visited(grid, 0)
        prev_pos, prev_dirs = visited.popitem(last=False)
        ans = 0
        while len(visited) > 0:
            pos, dirs = visited.popitem(last=False)
            ans += self.is_loop(prev_pos, prev_dirs.pop(0), obs, pos)
            prev_pos, prev_dirs = pos, dirs
        return ans

    @aoc_samples(
        (
            ("part_1", TEST, 41),
            ("part_2", TEST, 6),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2024, 6)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
