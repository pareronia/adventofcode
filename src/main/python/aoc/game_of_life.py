from __future__ import annotations
from itertools import product
from abc import ABC, abstractmethod
from typing import Iterable, TypeVar

T = TypeVar("T")


class GameOfLife:
    def __init__(
        self, alive: Iterable[T], universe: Universe, rules: Rules
    ) -> None:
        self.alive = alive
        self.universe = universe
        self.rules = rules

    def next_generation(self) -> None:
        def _is_alive(cell: T) -> bool:
            count = self.universe.neigbour_count(cell, self.alive)
            return self.rules.alive(cell, count, self.alive)

        self.alive = {
            cell for cell in self.universe.cells(self.alive) if _is_alive(cell)
        }

    class Universe(ABC):
        @abstractmethod
        def cells(self, alive: Iterable[T]) -> Iterable[T]:
            pass

        @abstractmethod
        def neigbour_count(self, cell: T, alive: Iterable[T]) -> int:
            pass

    class Rules(ABC):
        @abstractmethod
        def alive(self, cell: T, count: int, alive: Iterable[T]) -> bool:
            pass


class ClassicRules(GameOfLife.Rules):
    def alive(self, cell: T, count: int, alive: Iterable[T]) -> bool:
        return count == 3 or (count == 2 and cell in alive)


class InfiniteGrid(GameOfLife.Universe):
    Cell = tuple[int, int, int, int]

    def __init__(self, dim: int) -> None:
        self.dim = dim
        self.ds = list(product((-1, 0, 1), repeat=4))
        self.ds.remove((0, 0, 0, 0))

    def cells(self, alive: Iterable[Cell]) -> set[Cell]:  # type:ignore[override]  # noqa E501
        def _expand(index: int) -> Iterable[int]:
            values = [a[index] for a in alive]
            return range(min(values) - 1, max(values) + 2)

        return {
            _
            for _ in product(
                _expand(0),
                _expand(1),
                _expand(2) if self.dim >= 3 else [0],
                _expand(3) if self.dim >= 4 else [0],
            )
        }

    def neigbour_count(self, cell: Cell, alive: Iterable[Cell]) -> int:  # type:ignore[override]  # noqa E501
        x, y, z, w = cell
        return sum(
            (x + dx, y + dy, z + dz, w + dw) in alive
            for dx, dy, dz, dw in self.ds
        )
