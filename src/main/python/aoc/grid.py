from __future__ import annotations

from abc import ABC
from abc import abstractmethod
from collections.abc import Iterator
from dataclasses import dataclass
from enum import Enum
from enum import unique
from typing import Callable
from typing import Generic
from typing import NamedTuple
from typing import TypeVar

from aoc.geometry import Direction

T = TypeVar("T")


class Cell(NamedTuple):
    row: int
    col: int

    def at(self, direction: Direction) -> Cell:
        return Cell(self.row - direction.y, self.col + direction.x)

    def get_capital_neighbours(self) -> Iterator[Cell]:
        return (self.at(d) for d in Direction.capitals())

    def get_all_neighbours(self) -> Iterator[Cell]:
        return (self.at(d) for d in Direction.octants())

    def to(self, other: Cell) -> Direction:
        if self.row == other.row:
            if self.col == other.col:
                return Direction.NONE
            return Direction.RIGHT if self.col < other.col else Direction.LEFT
        elif self.col == other.col:
            return Direction.DOWN if self.row < other.row else Direction.UP
        raise ValueError("not supported")


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
    def __init__(self, grid: Grid[T], next: Cell, direction: IterDir):
        self.grid: Grid[T] = grid
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


class Grid(ABC, Generic[T]):
    @abstractmethod
    def get_width(self) -> int:
        pass

    @classmethod
    @abstractmethod
    def from_strings(cls, strings: list[str]) -> Grid[T]:
        pass

    @classmethod
    def merge(cls, grids: list[list[Grid[T]]]) -> Grid[T]:
        strings = list[str]()
        for r in range(len(grids)):
            rows_list = list[list[str]]()
            for c in range(len(grids[r])):
                rows_list.append(
                    [row for row in grids[r][c].get_rows_as_strings()]
                )
            n = 0
            for j in range(len(rows_list[0])):
                strings.append("".join(rows[n] for rows in rows_list))
                n += 1
        return cls.from_strings(strings)

    @abstractmethod
    def get_height(self) -> int:
        pass

    @abstractmethod
    def get_value(self, c: Cell) -> T:
        pass

    @abstractmethod
    def get_value_at(self, row: int, col: int) -> T:
        pass

    @abstractmethod
    def set_value(self, c: Cell, value: T) -> None:
        pass

    @abstractmethod
    def get_row_as_string(self, row: int) -> str:
        pass

    @abstractmethod
    def get_col(self, col: int) -> list[T]:
        pass

    @abstractmethod
    def replace_col(self, col: int, val: list[T]) -> None:
        pass

    def size(self) -> int:
        return self.get_height() * self.get_width()

    def get_cells(self) -> Iterator[Cell]:
        return (
            Cell(r, c)
            for r in range(self.get_height())
            for c in range(self.get_width())
        )

    def get_cells_without_border(self) -> Iterator[Cell]:
        return (
            Cell(r, c)
            for r in range(1, self.get_height() - 1)
            for c in range(1, self.get_width() - 1)
        )

    def get_cells_dir(self, cell: Cell, dir: Direction) -> Iterator[Cell]:
        if dir == Direction.UP:
            iter_dir = IterDir.UP
        elif dir == Direction.RIGHT:
            iter_dir = IterDir.RIGHT
        elif dir == Direction.DOWN:
            iter_dir = IterDir.DOWN
        elif dir == Direction.LEFT:
            iter_dir = IterDir.LEFT
        elif dir == Direction.RIGHT_AND_UP:
            iter_dir = IterDir.RIGHT_AND_UP
        elif dir == Direction.RIGHT_AND_DOWN:
            iter_dir = IterDir.RIGHT_AND_DOWN
        elif dir == Direction.LEFT_AND_UP:
            iter_dir = IterDir.LEFT_AND_UP
        elif dir == Direction.LEFT_AND_DOWN:
            iter_dir = IterDir.LEFT_AND_DOWN
        else:
            raise ValueError(f"Not supported: {dir}")
        return (c for c in GridIterator(self, cell, iter_dir))

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

    def get_all_equal_to(self, value: T) -> Iterator[Cell]:
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

    def get_rows_as_strings(self) -> Iterator[str]:
        return (
            self.get_row_as_string(row) for row in range(self.get_height())
        )


@dataclass(frozen=True)
class IntGrid(Grid[int]):
    values: list[list[int]]

    @classmethod
    def from_strings(cls, strings: list[str]) -> IntGrid:
        return IntGrid([[int(ch) for ch in line] for line in strings])

    def get_width(self) -> int:
        assert len(self.values) > 0
        return len(self.values[0])

    def get_height(self) -> int:
        return len(self.values)

    def get_value(self, c: Cell) -> int:
        return self.values[c.row][c.col]

    def get_value_at(self, row: int, col: int) -> int:
        return self.values[row][col]

    def set_value(self, c: Cell, value: int) -> None:
        self.values[c.row][c.col] = value

    def increment(self, c: Cell) -> None:
        self.values[c.row][c.col] += 1

    def get_row_as_string(self, row: int) -> str:
        return "".join(str(_) for _ in self.values[row])

    def get_col(self, col: int) -> list[int]:
        raise NotImplementedError

    def replace_col(self, col: int, val: list[int]) -> None:
        raise NotImplementedError


@dataclass(frozen=True)
class CharGrid(Grid[str]):
    values: list[list[str]]

    @classmethod
    def from_strings(cls, strings: list[str]) -> CharGrid:
        return CharGrid([[ch for ch in line] for line in strings])

    def get_width(self) -> int:
        assert len(self.values) > 0
        return len(self.values[0])

    def get_height(self) -> int:
        return len(self.values)

    def get_value(self, c: Cell) -> str:
        return self.values[c.row][c.col]

    def get_value_at(self, row: int, col: int) -> str:
        return self.values[row][col]

    def set_value(self, c: Cell, value: str) -> None:
        assert self.is_valid_row_index(c.row) and self.is_valid_col_index(
            c.col
        )
        self.values[c.row][c.col] = value

    def get_row_as_string(self, row: int) -> str:
        return "".join(self.values[row])

    def get_col(self, col: int) -> list[str]:
        return [self.values[row][col] for row in range(self.get_height())]

    def replace_col(self, col: int, val: list[str]) -> None:
        assert len(val) == self.get_height()
        for row in range(self.get_height()):
            self.values[row][col] = val[row]

    def get_col_as_string(self, col: int) -> str:
        return "".join(
            self.values[row][col] for row in range(self.get_height())
        )

    def get_cols_as_strings(self) -> Iterator[str]:
        return (self.get_col_as_string(col) for col in range(self.get_width()))

    def roll_row(self, row_idx: int, amount: int) -> None:
        assert self.is_valid_row_index(row_idx)
        new_row = (
            self.values[row_idx][-amount:] + self.values[row_idx][:-amount]
        )
        self.values[row_idx] = new_row

    def roll_column(self, col_idx: int, amount: int) -> None:
        assert self.is_valid_col_index(col_idx)
        old_col = self.get_col_as_string(col_idx)
        new_col = old_col[-amount:] + old_col[:-amount]
        for r, ch in enumerate(new_col):
            self.set_value(Cell(r, col_idx), ch)
