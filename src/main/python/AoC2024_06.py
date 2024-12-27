#! /usr/bin/env python3
#
# Advent of Code 2024 Day 6
#

from __future__ import annotations

import multiprocessing
import os
import sys
from collections import OrderedDict
from typing import Iterator
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
        cls, grid: CharGrid, starts: Iterator[Cell], dir: Direction
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
                nr, nc = r - dir[1], c + dir[0]
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
        self, start: Cell, dir: Direction, extra: Cell
    ) -> Cell | None:
        obs = self.obs[dir][start[0]][start[1]]
        if obs is None:
            return (
                extra
                if (
                    dir[1] == 0
                    and start[0] == extra[0]
                    and dir == (RIGHT if start[1] < extra[1] else LEFT)
                    or dir[0] == 0
                    and start[1] == extra[1]
                    and dir == (DOWN if start[0] < extra[0] else UP)
                )
                else None
            )
        else:
            if (
                dir[1] == 0
                and obs[0] == extra[0]
                and dir == (RIGHT if start[1] < extra[1] else LEFT)
            ):
                dextra = abs(extra[1] - start[1])
                dobs = abs(obs[1] - start[1])
                return obs if dobs < dextra else extra
            elif (
                dir[0] == 0
                and obs[1] == extra[1]
                and dir == (DOWN if start[0] < extra[0] else UP)
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
        dir: int,
    ) -> OrderedDict[Cell, list[int]]:
        start = next(grid.get_all_equal_to("^"))
        pos = (start.row, start.col)
        h, w = grid.get_height(), grid.get_width()
        seen = OrderedDict[Cell, list[int]]()
        seen.setdefault(pos, list()).append(dir)
        while True:
            nxt = (pos[0] - DIRS[dir][1], pos[1] + DIRS[dir][0])
            if not (0 <= nxt[0] < h and 0 <= nxt[1] < w):
                return seen
            if grid.values[nxt[0]][nxt[1]] == "#":
                dir = (dir + 1) % len(DIRS)
            else:
                pos = nxt
            seen.setdefault(pos, list()).append(dir)

    def is_loop(
        self, pos: Cell, dir: int, obs: Obstacles, extra: Cell
    ) -> bool:
        seen = dict[Cell, int]()
        seen[pos] = 1 << dir
        while True:
            nxt_obs = obs.get_next(pos, DIRS[dir], extra)
            if nxt_obs is None:
                return False
            pos = (nxt_obs[0] + DIRS[dir][1], nxt_obs[1] - DIRS[dir][0])
            dir = (dir + 1) % len(DIRS)
            if pos in seen and seen[pos] & (1 << dir) != 0:
                return True
            seen[pos] = seen.get(pos, 0) | (1 << dir)

    def part_1(self, grid: Input) -> Output1:
        return len(self.visited(grid, 0))

    def part_2(self, grid: Input) -> Output2:
        n = os.process_cpu_count()
        ans = multiprocessing.Array("i", [0 for _ in range(n)])

        def worker(
            id: int, data: list[tuple[Cell, int, Obstacles, Cell]]
        ) -> None:
            ans[id] = sum(self.is_loop(*row) for row in data)

        obs = Obstacles.from_grid(grid)
        visited = self.visited(grid, 0)
        data = list[tuple[Cell, int, Obstacles, Cell]]()
        prev_pos, prev_dirs = visited.popitem(last=False)
        while len(visited) > 0:
            pos, dirs = visited.popitem(last=False)
            data.append((prev_pos, prev_dirs.pop(0), obs, pos))
            prev_pos, prev_dirs = pos, dirs
        if sys.platform.startswith("win"):
            worker(0, data)
        else:
            batch = [
                list[tuple[Cell, int, Obstacles, Cell]]() for _ in range(n)
            ]
            cnt = 0
            for row in data:
                batch[cnt % n].append(row)
                cnt += 1
            jobs = []
            for i in range(n):
                p = multiprocessing.Process(target=worker, args=(i, batch[i]))
                jobs.append(p)
                p.start()
            for p in jobs:
                p.join()
        return sum(ans)  # type:ignore

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
