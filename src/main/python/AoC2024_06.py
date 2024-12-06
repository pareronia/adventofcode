#! /usr/bin/env python3
#
# Advent of Code 2024 Day 6
#

import sys
from collections import defaultdict

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
        self, h: int, w: int, obs: set[Cell], pos: Cell
    ) -> tuple[bool, set[Cell]]:
        d, dir = 0, DIRS[0]
        seen = defaultdict[Cell, set[tuple[int, int]]](set)
        seen[pos].add(dir)
        while True:
            nxt = (pos[0] - dir[1], pos[1] + dir[0])
            if not (0 <= nxt[0] < h and 0 <= nxt[1] < w):
                return False, {_ for _ in seen.keys()}
            if nxt in obs:
                d = (d + 1) % len(DIRS)
                dir = DIRS[d]
            else:
                pos = nxt
            if dir in seen[pos]:
                return True, set()
            seen[pos].add(dir)

    def part_1(self, input: Input) -> Output1:
        h, w, obs, start = input
        _, seen = self.route(h, w, obs, start)
        return len(seen)

    def part_2(self, input: Input) -> Output2:
        h, w, obs, start = input
        _, cells = self.route(h, w, obs, start)
        ans = 0
        for cell in cells:
            obs.add(cell)
            loop, _ = self.route(h, w, obs, start)
            if loop:
                ans += 1
            obs.remove(cell)
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
