from itertools import product
from abc import ABC, abstractmethod


class GameOfLife:

    def __init__(self, alive, universe, rules):
        self.alive = alive
        self.universe = universe
        self.rules = rules

    def next_generation(self):
        def _is_alive(cell):
            count = self.universe.neigbour_count(cell, self.alive)
            return self.rules.alive(cell, count, self.alive)

        self.alive = {cell for cell in self.universe.cells(self.alive)
                      if _is_alive(cell)}

    class Universe(ABC):
        @abstractmethod
        def cells(self, alive):
            pass

        @abstractmethod
        def neigbour_count(self, alive):
            pass

    class Rules(ABC):
        @abstractmethod
        def alive(self, cell, count, alive):
            pass


class ClassicRules(GameOfLife.Rules):

    def alive(self, cell, count, alive):
        return count == 3 or (count == 2 and cell in alive)


class InfiniteGrid(GameOfLife.Universe):

    def __init__(self, dim):
        self.dim = dim
        self.ds = list(product((-1, 0, 1), repeat=4))
        self.ds.remove((0, 0, 0, 0))

    def cells(self, alive):
        def _expand(index):
            values = [a[index] for a in alive]
            return range(min(values) - 1, max(values) + 2)

        return product(
            _expand(0),
            _expand(1),
            _expand(2) if self.dim >= 3 else [0],
            _expand(3) if self.dim >= 4 else [0])

    def neigbour_count(self, cell, alive):
        x, y, z, w = cell
        return sum((x + dx, y + dy, z + dz, w + dw) in alive
                   for dx, dy, dz, dw in self.ds)
