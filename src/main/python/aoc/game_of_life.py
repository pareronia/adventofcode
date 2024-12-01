from __future__ import annotations

from abc import ABC
from abc import abstractmethod
from collections import Counter
from itertools import product
from typing import Generic
from typing import Iterable
from typing import TypeVar

T = TypeVar("T")


class GameOfLife:
    def __init__(
        self, alive: Iterable[T], universe: Universe[T], rules: Rules[T]
    ) -> None:
        self.alive = alive
        self.universe = universe
        self.rules = rules

    def next_generation(self) -> None:
        self.alive = {
            cell
            for cell, count in self.universe.neighbour_count(
                self.alive
            ).items()
            if self.rules.alive(cell, count, self.alive)
        }

    class Universe(ABC, Generic[T]):
        @abstractmethod
        def neighbour_count(self, alive: Iterable[T]) -> dict[T, int]:
            pass

    class Rules(ABC, Generic[T]):
        @abstractmethod
        def alive(self, cell: T, count: int, alive: Iterable[T]) -> bool:
            pass


class ClassicRules(GameOfLife.Rules[tuple[int, ...]]):
    def alive(self, cell: T, count: int, alive: Iterable[T]) -> bool:
        return count == 3 or (count == 2 and cell in alive)


class InfiniteGrid(GameOfLife.Universe[tuple[int, ...]]):

    def _neighbours(self, cell: tuple[int, ...]) -> set[tuple[int, ...]]:
        tmp = {tuple(_) for _ in product([-1, 0, 1], repeat=len(cell))}
        tmp.remove(tuple(0 for i in range(len(cell))))
        return {tuple(cell[i] + a[i] for i in range(len(cell))) for a in tmp}

    def neighbour_count(
        self,
        alive: Iterable[tuple[int, ...]],
    ) -> dict[tuple[int, ...], int]:
        return Counter(n for cell in alive for n in self._neighbours(cell))
