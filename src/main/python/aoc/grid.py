from __future__ import annotations
from collections.abc import Iterator
from enum import Enum, unique
from typing import NamedTuple, Callable
from aoc.geometry import Direction


class Cell(NamedTuple):
    row: int
    col: int

    def at(self, direction: Direction) -> Cell:
        return Cell(self.row - direction.y, self.col + direction.x)

    def get_capital_neighbours(self) -> Iterator[Cell]:
        return (self.at(d) for d in Direction.capitals())

    def get_all_neighbours(self) -> Iterator[Cell]:
        return (self.at(d) for d in Direction.octants())


@unique
class IterDir(Enum):
    def __new__(cls, value: Direction) -> IterDir:
        obj = object.__new__(cls)
        obj._value_ = value
        return obj

    FORWARD = None
    UP = Direction.UP
    RIGHT_AND_UP = Direction.RIGHT_AND_UP
    RIGHT = Direction.RIGHT
    RIGHT_AND_DOWN = Direction.RIGHT_AND_DOWN
    DOWN = Direction.DOWN
    LEFT_AND_DOWN = Direction.LEFT_AND_DOWN
    LEFT = Direction.LEFT
    LEFT_AND_UP = Direction.LEFT_AND_UP


class GridIterator(Iterator[Cell]):
    def __init__(self, grid: IntGrid, next: Cell, direction: IterDir):
        self.grid: IntGrid = grid
        self.next: Cell | None = next
        self.direction: IterDir = direction

    def __iter__(self) -> GridIterator:
        return self

    def has_next(self) -> bool:
        if self.next is None:
            return False
        if self.direction == IterDir.FORWARD:
            return True
        else:
            ans = self.next.at(self.direction.value)
            if self.grid.is_in_bounds(ans):
                self.next = ans
                return True
            else:
                self.next = None
                return False

    def __next__(self) -> Cell:
        if not self.has_next():
            raise StopIteration
        if self.next is None:
            raise RuntimeError
        prev: Cell = self.next
        if self.direction == IterDir.FORWARD:
            if prev.col + 1 < self.grid.get_width():
                self.next = Cell(prev.row, prev.col + 1)
            elif prev.row + 1 < self.grid.get_height():
                self.next = Cell(prev.row + 1, 0)
            else:
                self.next = None
            return prev
        else:
            return self.next


# TODO numpy?
class IntGrid(NamedTuple):
    values: list[list[int]]

    def get_width(self) -> int:
        assert len(self.values) > 0
        return len(self.values[0])

    def get_height(self) -> int:
        return len(self.values)

    def size(self) -> int:
        return self.get_height() * self.get_width()

    def get_value(self, c: Cell) -> int:
        return self.values[c.row][c.col]

    def get_value_at(self, row: int, col: int) -> int:
        return self.values[row][col]

    def set_value(self, c: Cell, value: int) -> None:
        self.values[c.row][c.col] = value

    def increment(self, c: Cell) -> None:
        self.values[c.row][c.col] += 1

    def get_cells(self) -> Iterator[Cell]:
        return (
            Cell(r, c)
            for r in range(self.get_height())
            for c in range(self.get_width())
        )

    def get_cells_n(self, cell: Cell) -> Iterator[Cell]:
        return (c for c in GridIterator(self, cell, IterDir.UP))

    def get_cells_e(self, cell: Cell) -> Iterator[Cell]:
        return (c for c in GridIterator(self, cell, IterDir.RIGHT))

    def get_cells_s(self, cell: Cell) -> Iterator[Cell]:
        return (c for c in GridIterator(self, cell, IterDir.DOWN))

    def get_cells_w(self, cell: Cell) -> Iterator[Cell]:
        return (c for c in GridIterator(self, cell, IterDir.LEFT))

    def get_capital_neighbours(self, cell: Cell) -> Iterator[Cell]:
        return (
            n for n in cell.get_capital_neighbours() if self.is_in_bounds(n)
        )

    def get_all_neighbours(self, cell: Cell) -> Iterator[Cell]:
        return (n for n in cell.get_all_neighbours() if self.is_in_bounds(n))

    def find_all_matching(
        self, predicate: Callable[[Cell], bool]
    ) -> Iterator[Cell]:
        return (cell for cell in self.get_cells() if predicate(cell))

    def get_all_equal_to(self, value: int) -> Iterator[Cell]:
        return self.find_all_matching(
            lambda cell: self.get_value(cell) == value
        )

    def get_max_col_index(self) -> int:
        return self.get_width() - 1

    def get_max_row_index(self) -> int:
        return self.get_height() - 1

    def is_valid_row_index(self, row: int) -> bool:
        return 0 <= row <= self.get_max_row_index()

    def is_valid_col_index(self, col: int) -> bool:
        return 0 <= col <= self.get_max_col_index()

    def is_in_bounds(self, cell: Cell) -> bool:
        return self.is_valid_row_index(cell.row) and self.is_valid_col_index(
            cell.col
        )
