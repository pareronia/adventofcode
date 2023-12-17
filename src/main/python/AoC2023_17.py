#! /usr/bin/env python3
#
# Advent of Code 2023 Day 17
#

import sys
from collections import defaultdict
from queue import PriorityQueue
from typing import Callable
from typing import Iterator
from typing import NamedTuple
from typing import TypeVar

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
# from aoc.common import log
from aoc.geometry import Direction
from aoc.geometry import Turn
from aoc.grid import Cell
from aoc.grid import IntGrid

Input = IntGrid
Output1 = int
Output2 = int
T = TypeVar("T")


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


class Move(NamedTuple):
    cell: Cell
    dir: Direction | None
    cost: int

    def __eq__(self, other) -> bool:  # type:ignore[no-untyped-def]
        return True

    def __lt__(self, other) -> bool:  # type:ignore[no-untyped-def]
        return True


def dijkstra(
    start: T,
    is_end: Callable[[T], bool],
    adjacent: Callable[[T], Iterator[T]],
    get_cost: Callable[[T], int],
) -> tuple[int, dict[T, int], list[T]]:
    q: PriorityQueue[tuple[int, T]] = PriorityQueue()
    q.put((0, start))
    best: defaultdict[T, int] = defaultdict(lambda: 1_000_000_000)
    best[start] = 0
    parent: dict[T, T] = {}
    path = []
    while not q.empty():
        cost, node = q.get()
        if is_end(node):
            path = [node]
            curr = node
            while curr in parent:
                curr = parent[curr]
                path.append(curr)
            break
        c_total = best[node]
        for n in adjacent(node):
            new_risk = c_total + get_cost(n)
            if new_risk < best[n]:
                best[n] = new_risk
                parent[n] = node
                q.put((new_risk, n))
    return cost, best, path


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return IntGrid.from_strings([line for line in input_data])

    def part_1(self, grid: Input) -> Output1:
        # log(grid)

        def adjacent(block: Move) -> Iterator[Move]:
            # log(block.cell)
            for dir in Direction.capitals():
                if dir == block.dir:
                    continue
                if block.dir is not None and dir == block.dir.turn(
                    Turn.AROUND
                ):
                    continue
                it = grid.get_cells_dir(block.cell, dir)
                tot = 0
                for n in [next(it, None), next(it, None), next(it, None)]:
                    if n is None:
                        continue
                    tot += grid.get_value(n)
                    # log(f"-> {(n, dir, tot)}")
                    yield Move(n, dir, tot)

        def get_cost(block: Move) -> int:
            return block.cost

        start = Cell(0, 0)
        end = Cell(grid.get_max_row_index(), grid.get_max_col_index())
        cost, best, path = dijkstra(
            Move(start, None, 0),
            lambda block: block.cell == end,
            adjacent,
            get_cost,
        )
        return cost

    def part_2(self, input: Input) -> Output2:
        return 0

    @aoc_samples(
        (
            ("part_1", TEST, 102),
            # ("part_2", TEST, "TODO"),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 17)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
