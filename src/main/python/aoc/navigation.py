from __future__ import annotations
from collections.abc import Callable
from enum import Enum, unique
from copy import deepcopy
from aoc.geometry import Position, Vector, Direction, Turn
from dataclasses import dataclass


@unique
class Heading(Enum):
    NORTH = Direction.UP
    NORTHEAST = Direction.RIGHT_AND_UP
    EAST = Direction.RIGHT
    SOUTHEAST = Direction.RIGHT_AND_DOWN
    SOUTH = Direction.DOWN
    SOUTHWEST = Direction.LEFT_AND_DOWN
    WEST = Direction.LEFT
    NORTHWEST = Direction.LEFT_AND_UP

    @classmethod
    def from_direction(cls, direction: Direction) -> Heading:
        for v in Heading:
            if v.value == direction:
                return v
        raise ValueError

    @classmethod
    def from_str(cls, s: str) -> Heading:
        return Heading.from_direction(Direction.from_str(s))

    @property
    def vector(self) -> Vector:
        return self.value.vector

    def turn(self, turn: Turn) -> Heading:
        return Heading.from_direction(self.value.turn(turn))


@dataclass
class Waypoint(Vector):
    pass


class Navigation:
    _position: Position
    bounds: Callable[[Position], bool]
    visited_positions: list[Position]

    def __init__(self, position: Position, bounds: Callable[[Position], bool]):
        self._position = position
        self.bounds = bounds
        self.visited_positions = list[Position]()
        self._remember_visited_position(position)

    def _in_bounds(self, position: Position) -> bool:
        if self.bounds is None:
            return True
        else:
            return self.bounds(position)

    def _translate(self, vector: Vector, amount: int) -> None:
        new_position = Position.copy(self._position)
        for _ in range(amount):
            new_position.translate(vector=vector, amplitude=1)
            if self._in_bounds(new_position):
                self._position = Position.copy(new_position)
        self._remember_visited_position(self._position)

    def _remember_visited_position(self, position: Position) -> None:
        self.visited_positions.append(deepcopy(position))

    def get_visited_positions(
        self, include_start_position: bool = False
    ) -> list[Position]:
        if include_start_position:
            return self.visited_positions
        else:
            return self.visited_positions[1:]

    @property
    def position(self):
        return self._position


class NavigationWithHeading(Navigation):
    heading: Heading

    def __init__(
        self,
        position: Position,
        heading: Heading,
        bounds: Callable[[Position], bool] = None,
    ):
        super().__init__(position, bounds)
        self.heading = heading

    def __repr__(self) -> str:
        return str(
            {
                "position": self._position,
                "heading": self.heading.name,
            }
        )

    def turn(self, turn: Turn) -> None:
        self.heading = self.heading.turn(turn)

    def right(self, degrees: int) -> None:
        self.heading = Heading.rotate(self.heading, degrees)

    def left(self, degrees: int) -> None:
        self.heading = Heading.rotate(self.heading, -degrees)

    def forward(self, amount: int) -> None:
        self._translate(vector=self.heading.vector, amount=amount)

    def drift(self, heading: Heading, amount: int) -> None:
        self._translate(vector=heading.vector, amount=amount)


class NavigationWithWaypoint(Navigation):
    waypoint: Waypoint

    def __init__(
        self,
        position: Position,
        waypoint: Waypoint,
        bounds: Callable[[Position], bool] = None,
    ):
        super().__init__(position, bounds)
        self.waypoint = waypoint

    def __repr__(self) -> str:
        return str(
            {
                "position": self._position,
                "waypoint": self.waypoint,
            }
        )

    def right(self, degrees: int) -> None:
        self.waypoint.rotate(degrees)

    def left(self, degrees: int) -> None:
        self.waypoint.rotate(-degrees)

    def forward(self, amount: int) -> None:
        self._translate(vector=self.waypoint, amount=amount)

    def update_waypoint(self, heading: Heading, amount: int) -> None:
        self.waypoint.add(heading.vector, amplitude=amount)
