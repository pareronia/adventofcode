from collections.abc import Generator
from typing import NamedTuple, Callable


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

    def get_value_at(self, row: int, col: int) -> int:
        return self.values[row][col]

    def set_value(self, c: Cell, value: int) -> None:
        self.values[c.row][c.col] = value

    def increment(self, c: Cell) -> None:
        self.values[c.row][c.col] += 1

    def get_cells(self) -> Generator[Cell]:
        return (
            Cell(r, c)
            for r in range(self.get_height())
            for c in range(self.get_width())
        )

    def get_capital_neighbours(self, cell: Cell) -> Generator[Cell]:
        return (
            Cell(cell.row + dr, cell.col + dc)
            for dr, dc in ((-1, 0), (0, 1), (1, 0), (0, -1))
            if cell.row + dr >= 0
            and cell.row + dr < self.get_height()
            and cell.col + dc >= 0
            and cell.col + dc < self.get_width()
        )

    def find_all_matching(
        self, predicate: Callable[[int], bool]
    ) -> Generator[Cell]:
        return (cell for cell in self.get_cells() if predicate(cell))

    def get_all_equal_to(self, value: int) -> Generator[Cell]:
        return self.find_all_matching(
            lambda cell: self.get_value(cell) == value
        )
