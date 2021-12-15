from collections.abc import Generator
from typing import NamedTuple


class Cell(NamedTuple):
    row: int
    col: int


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

    def set_value(self, c: Cell, value: int) -> None:
        self.values[c.row][c.col] = value

    def increment(self, c: Cell) -> None:
        self.values[c.row][c.col] += 1

    def get_cells(self) -> Generator[Cell]:
        return (Cell(r, c)
                for r in range(self.get_height())
                for c in range(self.get_width()))
