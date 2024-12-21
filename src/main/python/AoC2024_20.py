#! /usr/bin/env python3
#
# Advent of Code 2024 Day 20
#

import multiprocessing
import os
import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.graph import bfs_full
from aoc.grid import CharGrid

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

        def count_shortcuts(
            id: str,
            cells: list[tuple[int, int]],
            dist: dict[tuple[int, int], int],
        ) -> None:
            count = 0
            for r, c in cells:
                for md in range(2, cheat_len + 1):
                    for dr in range(md + 1):
                        dc = md - dr
                        for rr, cc in {
                            (r + dr, c + dc),
                            (r + dr, c - dc),
                            (r - dr, c + dc),
                            (r - dr, c - dc),
                        }:
                            if (rr, cc) not in dist:
                                continue
                            if dist[(rr, cc)] - dist[(r, c)] >= target + md:
                                count += 1
            ans[id] = count

        start = next(
            cell for cell in grid.get_cells() if grid.get_value(cell) == "S"
        )
        distances, _ = bfs_full(
            start,
            lambda cell: grid.get_value(cell) != "#",
            lambda cell: (
                n
                for n in grid.get_capital_neighbours(cell)
                if grid.get_value(n) != "#"
            ),
        )
        dist = {(k.row, k.col): v for k, v in distances.items()}
        if sys.platform.startswith("win"):
            count_shortcuts("main", [(r, c) for r, c in dist.keys()], dist)
        else:
            n = os.process_cpu_count()
            cells: list[list[tuple[int, int]]] = [[] for _ in range(n)]
            cnt = 0
            for r, c in dist.keys():
                cells[cnt % n].append((r, c))
                cnt += 1
            jobs = []
            for i in range(n):
                p = multiprocessing.Process(
                    target=count_shortcuts, args=(str(i), cells[i], dist)
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
