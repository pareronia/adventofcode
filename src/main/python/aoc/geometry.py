from __future__ import annotations
from dataclasses import dataclass
from enum import Enum, unique


@dataclass
class Point:
    x: int
    y: int


@dataclass
class Position(Point):
    def translate(self, vector: Vector, amplitude: int = 1) -> None:
        self.x = self.x + vector.x * amplitude
        self.y = self.y + vector.y * amplitude

    @classmethod
    def copy(cls, position: Position) -> Position:
        return Position.of(position.x, position.y)

    @classmethod
    def of(cls, x: int, y: int) -> Position:
        return Position(x, y)

    def manhattan_distance(self, other: Position) -> int:
        return abs(self.x - other.x) + abs(self.y - other.y)

    def manhattan_distance_to_origin(self) -> int:
        return self.manhattan_distance(Position.of(0, 0))


@dataclass
class Vector(Point):
    def _rotate_90(self) -> None:
        new_x = self.y
        new_y = -self.x
        self.x = new_x
        self.y = new_y

    def __hash__(self) -> int:
        return hash((self.x, self.y))

    @classmethod
    def of(cls, x: int, y: int) -> Vector:
        return Vector(x, y)

    def rotate(self, degrees: int) -> None:
        if degrees < 0:
            degrees = 360 + degrees
        if degrees % 90 != 0:
            raise ValueError("invalid input")
        for _ in range(degrees // 90):
            self._rotate_90()

    def add(self, vector: Vector, amplitude: int = 1) -> None:
        self.x = self.x + vector.x * amplitude
        self.y = self.y + vector.y * amplitude


class Turn(Enum):
    def __new__(cls, value, letter):
        obj = object.__new__(cls)
        obj._value_ = value
        obj.letter = letter
        return obj

    LEFT = (270, "L")
    RIGHT = (90, "R")
    AROUND = (180, None)

    @classmethod
    def from_str(cls, s: str) -> Turn:
        for v in Turn:
            if v.letter is not None and v.letter == s:
                return v
        raise ValueError

    @classmethod
    def from_degrees(cls, degrees: int):
        val = degrees % 360
        for v in Turn:
            if v.value == val:
                return v
        raise ValueError


@unique
class Direction(Enum):
    def __new__(cls, value, heading_letter=None, letter=None, arrow=None):
        obj = object.__new__(cls)
        obj._value_ = value
        obj.heading_letter = heading_letter
        obj.letter = letter
        obj.arrow = arrow
        return obj

    NONE = Vector.of(0, 0)
    UP = (Vector.of(0, 1), "N", "U", "^")
    RIGHT_AND_UP = (Vector.of(1, 1), "NE")
    RIGHT = (Vector.of(1, 0), "E", "R", ">")
    RIGHT_AND_DOWN = (Vector.of(1, -1), "SE")
    DOWN = (Vector.of(0, -1), "S", "D", "v")
    LEFT_AND_DOWN = (Vector.of(-1, -1), "SW")
    LEFT = (Vector.of(-1, 0), "W", "L", "<")
    LEFT_AND_UP = (Vector.of(-1, 1), "NW")

    @classmethod
    def from_str(cls, s: str) -> Direction:
        for v in Direction:
            if (
                v.heading_letter is not None
                and v.heading_letter == s
                or v.letter is not None
                and v.letter == s
                or v.arrow is not None
                and v.arrow == s
            ):
                return v
        raise ValueError

    @property
    def vector(self) -> Vector:
        return self.value

    @property
    def x(self) -> int:
        return self.vector.x

    @property
    def y(self) -> int:
        return self.vector.y

    def turn(self, turn: Turn) -> Direction:
        if self == Direction.UP:
            return (
                Direction.DOWN
                if turn == Turn.AROUND
                else Direction.LEFT
                if turn == Turn.LEFT
                else Direction.RIGHT
            )
        elif self == Direction.RIGHT:
            return (
                Direction.LEFT
                if turn == Turn.AROUND
                else Direction.UP
                if turn == Turn.LEFT
                else Direction.DOWN
            )
        elif self == Direction.DOWN:
            return (
                Direction.UP
                if turn == Turn.AROUND
                else Direction.RIGHT
                if turn == Turn.LEFT
                else Direction.LEFT
            )
        elif self == Direction.LEFT:
            return (
                Direction.RIGHT
                if turn == Turn.AROUND
                else Direction.DOWN
                if turn == Turn.LEFT
                else Direction.UP
            )
        else:
            raise ValueError


Direction.OCTANTS = {
    Direction.UP,
    Direction.RIGHT_AND_UP,
    Direction.RIGHT,
    Direction.RIGHT_AND_DOWN,
    Direction.DOWN,
    Direction.LEFT_AND_DOWN,
    Direction.LEFT,
    Direction.LEFT_AND_UP,
}

Direction.CAPITAL = {
    Direction.UP,
    Direction.RIGHT,
    Direction.DOWN,
    Direction.LEFT,
}
