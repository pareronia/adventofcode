#! /usr/bin/env python3
#
# Advent of Code 2024 Day 20
#

import multiprocessing
import os
import sys
from dataclasses import dataclass
from typing import Self

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.geometry import Direction
from aoc.geometry import Turn
from aoc.grid import CharGrid

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

Cell = tuple[int, int]


@dataclass(frozen=True)
class RaceTrack:
    track: list[Cell]
    distances: list[int]
    h: int
    w: int

    @classmethod
    def from_input(cls, input_data: InputData) -> Self:
        grid = CharGrid.from_strings(list(input_data))
        h, w = grid.get_height(), grid.get_width()
        start = next(
            cell for cell in grid.get_cells() if grid.get_value(cell) == "S"
        )
        end = next(
            cell for cell in grid.get_cells() if grid.get_value(cell) == "E"
        )
        direction = next(
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
            direction = next(
                d
                for d in (
                    direction,
                    direction.turn(Turn.RIGHT),
                    direction.turn(Turn.LEFT),
                )
                if grid.get_value(pos.at(d)) != "#"
            )
            pos = pos.at(direction)
            dist += 1
        return cls(track, distances, h, w)

    def count_shortcuts(self, cheat_len: int, target: int) -> int:
        count = 0
        for r, c in self.track:
            d_cell = self.distances[r * self.h + c]
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
                        if not (0 <= rr < self.h and 0 <= cc < self.w):
                            continue
                        d_n = self.distances[rr * self.h + cc]
                        if d_n == sys.maxsize:
                            continue
                        if d_n - d_cell >= min_req:
                            count += 1
        return count


Input = RaceTrack
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return RaceTrack.from_input(input_data)

    def solve(self, racetrack: RaceTrack, cheat_len: int, target: int) -> int:
        ans = multiprocessing.Manager().dict()

        def count_shortcuts(pid: str, racetrack_i: RaceTrack) -> None:
            ans[pid] = racetrack_i.count_shortcuts(cheat_len, target)

        if sys.platform.startswith("win"):
            count_shortcuts("main", racetrack)
        else:
            n = os.process_cpu_count()
            cells: list[list[Cell]] = [[] for _ in range(n)]
            for cnt, cell in enumerate(racetrack.track):
                cells[cnt % n].append(cell)
            jobs = []
            for i in range(n):
                racetrack_i = RaceTrack(
                    cells[i],
                    racetrack.distances,
                    racetrack.h,
                    racetrack.w,
                )
                p = multiprocessing.Process(
                    target=count_shortcuts,
                    args=(str(i), racetrack_i),
                )
                jobs.append(p)
                p.start()
            for p in jobs:
                p.join()
        return sum(ans.values())

    def part_1(self, racetrack: Input) -> Output1:
        return self.solve(racetrack, 2, 100)

    def part_2(self, racetrack: Input) -> Output2:
        return self.solve(racetrack, 20, 100)

    def samples(self) -> None:
        inputs = self.parse_input(TEST.splitlines())
        assert self.solve(inputs, 2, 2) == 44
        assert self.solve(inputs, 20, 50) == 285


solution = Solution(2024, 20)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
