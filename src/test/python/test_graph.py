import unittest
from typing import Iterator
from aoc.graph import a_star
from aoc.grid import IntGrid, Cell


class AStarTest(unittest.TestCase):
    def adjacent(self, grid: IntGrid, cell: Cell) -> Iterator[Cell]:
        return (
            c
            for c in grid.get_capital_neighbours(cell)
            if grid.get_value(c) != 9
        )

    def test_a_star_ok(self) -> None:
        # fmt: off
        v = [
            [0, 9, 9, 9],
            [0, 0, 9, 9],
            [9, 0, 0, 9],
            [9, 9, 0, 0],
            [9, 9, 9, 0]
        ]
        # fmt: on
        grid = IntGrid(v)
        cost, distances, path = a_star(
            Cell(0, 0),
            lambda cell: cell == Cell(4, 3),
            lambda cell: self.adjacent(grid, cell),
            lambda cell: 1,
        )
        self.assertEqual(cost, 7)
        self.assertEqual(distances[Cell(2, 2)], 4)
        self.assertEqual(distances[Cell(4, 3)], 7)
        self.assertEqual(
            path,
            [
                Cell(4, 3),
                Cell(3, 3),
                Cell(3, 2),
                Cell(2, 2),
                Cell(2, 1),
                Cell(1, 1),
                Cell(1, 0),
                Cell(0, 0),
            ],
        )
        self.assertEqual(distances[Cell(4, 0)], 1_000_000_000)
