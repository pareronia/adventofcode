from itertools import product


class GameOfLife:

    def __init__(self, alive, dim):
        self.alive = alive
        self.dim = dim
        self.ds = list(product((-1, 0, 1), repeat=4))
        self.ds.remove((0, 0, 0, 0))

    def _expand(self, index):
        coords = [a[index] for a in self.alive]
        return range(min(coords) - 1, max(coords) + 2)

    def next_generation(self):
        new_alive = set()
        new = product(
            self._expand(0),
            self._expand(1),
            self._expand(2) if self.dim >= 3 else [0],
            self._expand(3) if self.dim >= 4 else [0]
        )
        for x, y, z, w in new:
            count = sum((x + dx, y + dy, z + dz, w + dw) in self.alive
                        for dx, dy, dz, dw in self.ds)
            cell = (x, y, z, w)
            if ((cell in self.alive and count in (2, 3)) or
               (cell not in self.alive and count == 3)):
                new_alive.add(cell)
        self.alive = new_alive
