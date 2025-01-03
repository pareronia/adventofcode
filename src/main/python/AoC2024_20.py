#! /usr/bin/env python3
#
# Advent of Code 2024 Day 20
#

import multiprocessing
import os
import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.geometry import Direction
from aoc.geometry import Turn
from aoc.grid import CharGrid

Cell = tuple[int, int]
Input = CharGrid
Output1 = int
Output2 = int


TEST = """\
###############
#...#...#.....#
#.#.#.#.#.###.#
#S#...#.#.#...#
#######.#.#.###
#######.#.#...#
#######.#.###.#
###..E#...#...#
###.#######.###
#...###...#...#
#.#####.#.###.#
#.#...#.#.#...#
#.#.#.#.#.#.###
#...#...#...###
###############
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return CharGrid.from_strings(list(input_data))

    def solve(self, grid: CharGrid, cheat_len: int, target: int) -> int:
        ans = multiprocessing.Manager().dict()
        h, w = grid.get_height(), grid.get_width()

        def count_shortcuts(
            id: str,
            cells: list[Cell],
            dist: list[int],
        ) -> None:
            count = 0
            for r, c in cells:
                d_cell = dist[r * h + c]
                for md in range(2, cheat_len + 1):
                    min_req = target + md
                    for dr in range(md + 1):
                        dc = md - dr
                        for rr, cc in {
                            (r + dr, c + dc),
                            (r + dr, c - dc),
                            (r - dr, c + dc),
                            (r - dr, c - dc),
                        }:
                            if not (0 <= rr < h and 0 <= cc < w):
                                continue
                            d_n = dist[rr * h + cc]
                            if d_n == sys.maxsize:
                                continue
                            if d_n - d_cell >= min_req:
                                count += 1
            ans[id] = count

        start = next(
            cell for cell in grid.get_cells() if grid.get_value(cell) == "S"
        )
        end = next(
            cell for cell in grid.get_cells() if grid.get_value(cell) == "E"
        )
        dir = next(
            d
            for d in Direction.capitals()
            if grid.get_value(start.at(d)) != "#"
        )
        pos = start
        dist = 0
        track = list[Cell]()
        distances = [sys.maxsize for r in range(h) for c in range(w)]
        while True:
            distances[pos.row * h + pos.col] = dist
            track.append((pos.row, pos.col))
            if pos == end:
                break
            dir = next(
                d
                for d in (dir, dir.turn(Turn.RIGHT), dir.turn(Turn.LEFT))
                if grid.get_value(pos.at(d)) != "#"
            )
            pos = pos.at(dir)
            dist += 1
        if sys.platform.startswith("win"):
            count_shortcuts("main", track, distances)
        else:
            n = os.process_cpu_count()
            cells: list[list[Cell]] = [[] for _ in range(n)]
            cnt = 0
            for cell in track:
                cells[cnt % n].append(cell)
                cnt += 1
            jobs = []
            for i in range(n):
                p = multiprocessing.Process(
                    target=count_shortcuts, args=(str(i), cells[i], distances)
                )
                jobs.append(p)
                p.start()
            for p in jobs:
                p.join()
        return sum(ans.values())

    def part_1(self, grid: Input) -> Output1:
        return self.solve(grid, 2, 100)

    def part_2(self, grid: Input) -> Output2:
        return self.solve(grid, 20, 100)

    def samples(self) -> None:
        input = self.parse_input(TEST.splitlines())
        assert self.solve(input, 2, 2) == 44
        assert self.solve(input, 20, 50) == 285


solution = Solution(2024, 20)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
