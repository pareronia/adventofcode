from __future__ import annotations
from collections.abc import Callable
from enum import Enum
from copy import deepcopy
from aoc.geometry import Position, Vector
from dataclasses import dataclass


@dataclass
class Heading(Vector):
    name: str

    def __init__(self, x: int, y: int, name: str):
        if x not in {-1, 0, 1} or y not in {-1, 0, 1}:
            raise ValueError(f"Invalid Heading: {x}, {y}")
        super().__init__(x, y)
        self.name = name

    @classmethod
    def get(cls, x: int, y: int) -> Heading:
        for h in Headings:
            if h.value.x == x and h.value.y == y:
                return h.value
        raise ValueError(f"Invalid Heading: {x}, {y}")

    @classmethod
    def rotate(cls, heading: Heading, degrees: int) -> Heading:
        v = Vector(heading.x, heading.y)
        v.rotate(degrees)
        return Heading.get(v.x, v.y)


NORTH = "N"
EAST = "E"
SOUTH = "S"
WEST = "W"


class Headings(Enum):
    N = Heading(0, 1, NORTH)
    E = Heading(1, 0, EAST)
    S = Heading(0, -1, SOUTH)
    W = Heading(-1, 0, WEST)


@dataclass
class Waypoint(Vector):
    pass


class Navigation:
    position: Position
    bounds: Callable[[Position], bool]
    visited_positions: list[Position]

    def __init__(self,
                 position: Position,
                 bounds: Callable[[Position], bool]):
        self.position = position
        self.bounds = bounds
        self.visited_positions = list[Position]()
        self._remember_visited_position(position)

    def _in_bounds(self, position: Position) -> bool:
        if self.bounds is None:
            return True
        else:
            return self.bounds(position)

    def _translate(self, vector: Vector, amount: int) -> None:
        new_position = Position.copy(self.position)
        for i in range(amount):
            new_position.translate(vector=vector, amplitude=1)
            if self._in_bounds(new_position):
                self.position = Position.copy(new_position)
        self._remember_visited_position(self.position)

    def _remember_visited_position(self, position: Position) -> None:
        self.visited_positions.append(deepcopy(position))

    def get_visited_positions(
            self, include_start_position: bool = False) -> list[Position]:
        if include_start_position:
            return self.visited_positions
        else:
            return self.visited_positions[1:]


class NavigationWithHeading(Navigation):
    heading: Heading

    def __init__(self,
                 position: Position,
                 heading: Heading,
                 bounds: Callable[[Position], bool] = None):
        super().__init__(position, bounds)
        self.heading = heading

    def __repr__(self) -> str:
        return str({"position": self.position,
                    "heading": Heading.get(
                        self.heading.x, self.heading.y).name,
                    })

    def right(self, degrees: int) -> None:
        self.heading = Heading.rotate(self.heading, degrees)

    def left(self, degrees: int) -> None:
        self.heading = Heading.rotate(self.heading, -degrees)

    def forward(self, amount: int) -> None:
        self._translate(vector=self.heading, amount=amount)

    def drift(self, heading: Heading, amount: int) -> None:
        self._translate(vector=heading, amount=amount)


class NavigationWithWaypoint(Navigation):
    waypoint: Waypoint

    def __init__(self,
                 position: Position,
                 waypoint: Waypoint,
                 bounds: Callable[[Position], bool] = None):
        super().__init__(position, bounds)
        self.waypoint = waypoint

    def __repr__(self) -> str:
        return str({"position": self.position,
                    "waypoint": self.waypoint,
                    })

    def right(self, degrees: int) -> None:
        self.waypoint.rotate(degrees)

    def left(self, degrees: int) -> None:
        self.waypoint.rotate(-degrees)

    def forward(self, amount: int) -> None:
        self._translate(vector=self.waypoint, amount=amount)

    def update_waypoint(self, heading: Heading, amount: int) -> None:
        self.waypoint.add(heading, amplitude=amount)
