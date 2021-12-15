#! /usr/bin/env python3
#
# Advent of Code 2021 Day 15
#

from typing import NamedTuple
from collections import defaultdict
from collections.abc import Generator
from queue import PriorityQueue
from aoc import my_aocd
from aoc.grid import Cell, IntGrid
import aocd


class State(NamedTuple):
    cell: Cell
    risk: int

    def __eq__(self, other) -> bool:
        return self.risk == other.risk

    def __lt__(self, other) -> bool:
        return self.risk < other.risk


def _parse(inputs: tuple[str]) -> IntGrid:
    return IntGrid([[int(_) for _ in list(r)] for r in inputs])


def _get_risk(grid: IntGrid, cell: Cell) -> int:
    value = grid.get_value(Cell(cell.row % grid.get_height(),
                                cell.col % grid.get_width())) \
            + cell.row // grid.get_height() \
            + cell.col // grid.get_width()
    while value > 9:
        value -= 9
    return value


def _find_neighbours(grid: IntGrid, c: Cell, tiles: int) -> Generator[Cell]:
    return (Cell(c.row + dr, c.col + dc)
            for dr, dc in ((-1, 0), (0, 1), (1, 0), (0, -1))
            if c.row + dr >= 0
            and c.row + dr < tiles * grid.get_height()
            and c.col + dc >= 0
            and c.col + dc < tiles * grid.get_width())


def _find_least_risk_path(grid: IntGrid, tiles: int) -> list[Cell]:
    start = Cell(0, 0)
    end = Cell(tiles * grid.get_height() - 1, tiles * grid.get_width() - 1)
    q = PriorityQueue[State]()
    q.put(State(start, 0))
    best = defaultdict(lambda: 1E9)
    best[start] = 0
    seen = set[Cell]()
    seen.add(start)
    parent = {}
    while not q.empty():
        state = q.get()
        if state.cell == end:
            path = [end]
            curr = end
            while curr in parent:
                curr = parent[curr]
                path.append(curr)
            return path
        seen.add(state.cell)
        c_total = best[state.cell]
        for n in _find_neighbours(grid, state.cell, tiles):
            if n in seen:
                continue
            new_risk = c_total + _get_risk(grid, n)
            if new_risk < best[n]:
                best[n] = new_risk
                parent[n] = state.cell
                q.put(State(n, new_risk))
    raise RuntimeError("Unsolvable")


def _solve(grid: IntGrid, tiles: int) -> int:
    return sum(_get_risk(grid, c)
               for c in _find_least_risk_path(grid, tiles)
               if c != Cell(0, 0))


def part_1(inputs: tuple[str]) -> int:
    grid = _parse(inputs)
    return _solve(grid, 1)


def part_2(inputs: tuple[str]) -> int:
    grid = _parse(inputs)
    return _solve(grid, 5)


TEST = """\
1163751742
1381373672
2136511328
3694931569
7463417111
1319128137
1359912421
3125421639
1293138521
2311944581
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2021, 15)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 40
    assert part_2(TEST) == 315

    inputs = my_aocd.get_input(puzzle.year, puzzle.day, 100)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()
