from __future__ import annotations
from enum import Enum
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

    def __init__(self, position: Position):
        self.position = position


class NavigationWithHeading(Navigation):
    heading: Heading

    def __init__(self, position: Position, heading: Heading):
        super().__init__(position)
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
        self.position.translate(vector=self.heading, amplitude=amount)

    def drift(self, heading: Heading, amount: int) -> None:
        self.position.translate(vector=heading, amplitude=amount)


class NavigationWithWaypoint(Navigation):
    waypoint: Waypoint

    def __init__(self, position: Position, waypoint: Waypoint):
        super().__init__(position)
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
        self.position.translate(vector=self.waypoint, amplitude=amount)

    def update_waypoint(self, heading: Heading, amount: int) -> None:
        self.waypoint.add(heading, amplitude=amount)
