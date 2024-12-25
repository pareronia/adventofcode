#! /usr/bin/env python3
#
# Advent of Code 2024 Day 6
#

import multiprocessing
import os
import sys
from collections import OrderedDict

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

Cell = tuple[int, int]
Input = tuple[int, int, set[Cell], Cell]
Output1 = int
Output2 = int

DIRS = [(0, 1), (1, 0), (0, -1), (-1, 0)]


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


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        lines = list(input_data)
        obs = set[Cell]()
        h, w = len(lines), len(lines[0])
        for r in range(h):
            for c in range(w):
                if lines[r][c] == "^":
                    start = (r, c)
                elif lines[r][c] == "#":
                    obs.add((r, c))
        return h, w, obs, start

    def route(
        self,
        h: int,
        w: int,
        obs: set[Cell],
        extra_obs: Cell | None,
        pos: Cell,
        dir: int,
    ) -> tuple[bool, OrderedDict[Cell, list[int]]]:
        seen = OrderedDict[Cell, list[int]]()
        seen.setdefault(pos, list()).append(dir)
        while True:
            nxt = (pos[0] - DIRS[dir][1], pos[1] + DIRS[dir][0])
            if not (0 <= nxt[0] < h and 0 <= nxt[1] < w):
                return False, seen
            if extra_obs is not None and nxt == extra_obs or nxt in obs:
                dir = (dir + 1) % len(DIRS)
            else:
                pos = nxt
            if pos in seen and dir in seen[pos]:
                return True, OrderedDict()
            seen.setdefault(pos, list()).append(dir)

    def part_1(self, input: Input) -> Output1:
        h, w, obs, start = input
        _, seen = self.route(h, w, obs, None, start, 0)
        return len(seen)

    def part_2(self, input: Input) -> Output2:
        n = os.process_cpu_count()
        ans = multiprocessing.Array("i", [0 for _ in range(n)])

        def worker(
            id: int, data: list[tuple[int, int, set[Cell], Cell, Cell, int]]
        ) -> None:
            ans[id] = sum(self.route(*row)[0] for row in data)

        h, w, obs, start = input
        _, seen = self.route(h, w, obs, None, start, 0)
        data = list[tuple[int, int, set[Cell], Cell, Cell, int]]()
        prev_pos, prev_dirs = seen.popitem(last=False)
        while len(seen) > 0:
            pos, dirs = seen.popitem(last=False)
            data.append((h, w, obs, pos, prev_pos, prev_dirs.pop(0)))
            prev_pos, prev_dirs = pos, dirs
        if sys.platform.startswith("win"):
            worker(0, data)
        else:
            batch = [
                list[tuple[int, int, set[Cell], Cell, Cell, int]]()
                for _ in range(n)
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
