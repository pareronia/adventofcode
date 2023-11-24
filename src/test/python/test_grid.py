import unittest
from aoc.grid import CharGrid, GridIterator, IntGrid, IterDir, Cell


class CharGridIteratorTest(unittest.TestCase):

    grid: CharGrid

    def setUp(self) -> None:
        self.grid = CharGrid(["###", "###", "###"])

    def test_forward(self) -> None:
        it = GridIterator(self.grid, Cell(0, 0), IterDir.FORWARD)
        self.assertEqual(next(it), Cell(0, 0))
        self.assertEqual(next(it), Cell(0, 1))
        self.assertEqual(next(it), Cell(0, 2))
        self.assertEqual(next(it), Cell(1, 0))
        self.assertEqual(next(it), Cell(1, 1))
        self.assertEqual(next(it), Cell(1, 2))
        self.assertEqual(next(it), Cell(2, 0))
        self.assertEqual(next(it), Cell(2, 1))
        self.assertEqual(next(it), Cell(2, 2))
        with self.assertRaises(StopIteration):
            next(it)

    def test_up(self) -> None:
        it = GridIterator(self.grid, Cell(2, 1), IterDir.UP)
        self.assertEqual(next(it), Cell(1, 1))
        self.assertEqual(next(it), Cell(0, 1))
        with self.assertRaises(StopIteration):
            next(it)

    def test_get_cells(self) -> None:
        self.assertEqual(
            list(self.grid.get_cells()),
            [
                Cell(0, 0),
                Cell(0, 1),
                Cell(0, 2),
                Cell(1, 0),
                Cell(1, 1),
                Cell(1, 2),
                Cell(2, 0),
                Cell(2, 1),
                Cell(2, 2),
            ],
        )


class IntGridIteratorTest(unittest.TestCase):

    grid: IntGrid

    def setUp(self) -> None:
        self.grid = IntGrid([[0, 1, 2], [3, 4, 5], [6, 7, 8]])

    def test_forward(self) -> None:
        it = GridIterator(self.grid, Cell(0, 0), IterDir.FORWARD)
        self.assertEqual(next(it), Cell(0, 0))
        self.assertEqual(next(it), Cell(0, 1))
        self.assertEqual(next(it), Cell(0, 2))
        self.assertEqual(next(it), Cell(1, 0))
        self.assertEqual(next(it), Cell(1, 1))
        self.assertEqual(next(it), Cell(1, 2))
        self.assertEqual(next(it), Cell(2, 0))
        self.assertEqual(next(it), Cell(2, 1))
        self.assertEqual(next(it), Cell(2, 2))
        with self.assertRaises(StopIteration):
            next(it)

    def test_up(self) -> None:
        it = GridIterator(self.grid, Cell(2, 1), IterDir.UP)
        self.assertEqual(next(it), Cell(1, 1))
        self.assertEqual(next(it), Cell(0, 1))
        with self.assertRaises(StopIteration):
            next(it)

    def test_get_cells(self) -> None:
        self.assertEqual(
            list(self.grid.get_cells()),
            [
                Cell(0, 0),
                Cell(0, 1),
                Cell(0, 2),
                Cell(1, 0),
                Cell(1, 1),
                Cell(1, 2),
                Cell(2, 0),
                Cell(2, 1),
                Cell(2, 2),
            ],
        )
