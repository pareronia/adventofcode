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

    def bounded(self, rect: Rectangle) -> Position:
        if rect is None:
            return self
        if self.x < rect.bottom_left.x:
            self.x = rect.bottom_left.x
        if self.x > rect.top_right.x:
            self.x = rect.top_right.x
        if self.y < rect.bottom_left.y:
            self.y = rect.bottom_left.y
        if self.y > rect.top_right.y:
            self.y = rect.top_right.y
        return self

    @classmethod
    def of(cls, x: int, y: int) -> Position:
        return Position(x, y)


@dataclass
class Rectangle:
    bottom_left: Position
    top_right: Position

    @classmethod
    def of(cls, bottom_left: Position, top_right: Position) -> Rectangle:
        return Rectangle(bottom_left, top_right)


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
