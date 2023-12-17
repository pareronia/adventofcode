#! /usr/bin/env python3
#
# Advent of Code 2023 Day 17
#

import sys
from collections import defaultdict
from queue import PriorityQueue

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.grid import IntGrid

Input = IntGrid
Output1 = int
Output2 = int


TEST = """\
2413432311323
3215453535623
3255245654254
3446585845452
4546657867536
1438598798454
4457876987766
3637877979653
4654967986887
4564679986453
1224686865563
2546548887735
4322674655533
"""

DIRS = {(0, 1), (0, -1), (1, 0), (-1, 0)}
Move = tuple[int, int, int, int]


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return IntGrid.from_strings([line for line in input_data])

    def solve(self, grid: Input, min_moves: int, max_moves: int) -> int:
        max_r = grid.get_height() - 1
        max_c = grid.get_width() - 1

        q: PriorityQueue[tuple[int, Move]] = PriorityQueue()
        q.put((0, (0, 0, 0, 0)))
        best: defaultdict[Move, int] = defaultdict(lambda: int(1e9))
        best[(0, 0, 0, 0)] = 0
        while not q.empty():
            cost, move = q.get()
            r, c, dr, dc = move
            if r == max_r and c == max_c:
                return cost
            heatloss = best[move]
            for drr, dcc in DIRS - {(dr, dc), (-dr, -dc)}:
                rr, cc, hl = r, c, 0
                for i in range(1, max_moves + 1):
                    rr += drr
                    cc += dcc
                    if not (0 <= rr <= max_r and 0 <= cc <= max_c):
                        break
                    hl += grid.values[rr][cc]
                    if i >= min_moves:
                        n = (rr, cc, drr, dcc)
                        new_heatloss = heatloss + hl
                        if new_heatloss < best[n]:
                            best[n] = new_heatloss
                            q.put((new_heatloss, n))
        raise RuntimeError("unsolvable")

    def part_1(self, grid: Input) -> Output1:
        return self.solve(grid, 1, 3)

    def part_2(self, grid: Input) -> Output2:
        return self.solve(grid, 4, 10)

    @aoc_samples(
        (
            ("part_1", TEST, 102),
            ("part_2", TEST, 94),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 17)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
