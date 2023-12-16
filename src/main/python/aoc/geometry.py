from __future__ import annotations
from enum import Enum, unique
from typing import NamedTuple


class Point(NamedTuple):
    x: int
    y: int


class Position(Point):
    def translate(self, vector: Vector, amplitude: int = 1) -> Position:
        return Position.of(
            self.x + vector.x * amplitude, self.y + vector.y * amplitude
        )

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


class Vector(Point):
    def _rotate_90(self) -> Vector:
        return Vector.of(self.y, -self.x)

    def __hash__(self) -> int:
        return hash((self.x, self.y))

    @classmethod
    def of(cls, x: int, y: int) -> Vector:
        return Vector(x, y)

    def rotate(self, degrees: int) -> Vector:
        if degrees < 0:
            degrees = 360 + degrees
        if degrees % 90 != 0:
            raise ValueError("invalid input")
        ans = self
        for _ in range(degrees // 90):
            ans = ans._rotate_90()
        return ans

    def add(self, vector: Vector, amplitude: int = 1) -> Vector:
        return Vector.of(
            self.x + vector.x * amplitude, self.y + vector.y * amplitude
        )


class Turn(Enum):
    letter: str

    def __new__(cls, value: int, letter: str) -> Turn:
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
    def from_degrees(cls, degrees: int) -> Turn:
        val = degrees % 360
        for v in Turn:
            if v.value == val:
                return v
        raise ValueError


@unique
class Direction(Enum):
    heading_letter: str | None
    letter: str | None
    arrow: str | None

    def __new__(
        cls,
        value: Vector,
        heading_letter: str | None = None,
        letter: str | None = None,
        arrow: str | None = None,
    ) -> Direction:
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
    def capitals(cls) -> set[Direction]:
        return {
            Direction.UP,
            Direction.RIGHT,
            Direction.DOWN,
            Direction.LEFT,
        }

    @classmethod
    def octants(cls) -> set[Direction]:
        return {
            Direction.UP,
            Direction.RIGHT_AND_UP,
            Direction.RIGHT,
            Direction.RIGHT_AND_DOWN,
            Direction.DOWN,
            Direction.LEFT_AND_DOWN,
            Direction.LEFT,
            Direction.LEFT_AND_UP,
        }

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
        return self.value  # type:ignore[no-any-return]

    @property
    def x(self) -> int:
        return int(self.vector.x)

    @property
    def y(self) -> int:
        return self.vector.y

    @property
    def is_horizontal(self) -> bool:
        return self == Direction.LEFT or self == Direction.RIGHT

    @property
    def is_vertical(self) -> bool:
        return self == Direction.UP or self == Direction.DOWN

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


class Draw:
    @classmethod
    def draw(
        cls, positions: set[Position], fill: str, empty: str
    ) -> list[str]:
        min_x = min(p.x for p in positions)
        max_x = max(p.x for p in positions)
        min_y = min(p.y for p in positions)
        max_y = max(p.y for p in positions)
        return list(
            reversed(
                [
                    "".join(
                        fill if Position.of(x, y) in positions else empty
                        for x in range(min_x, max_x + 2)
                    )
                    for y in range(min_y, max_y + 1)
                ]
            )
        )
