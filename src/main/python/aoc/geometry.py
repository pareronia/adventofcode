from __future__ import annotations
from dataclasses import dataclass


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

    def rotate(self, degrees: int) -> None:
        if degrees < 0:
            degrees = 360 + degrees
        if degrees % 90 != 0:
            raise ValueError("invalid input")
        for _ in range(degrees//90):
            self._rotate_90()

    def add(self, vector: Vector, amplitude: int = 1) -> None:
        self.x = self.x + vector.x * amplitude
        self.y = self.y + vector.y * amplitude
