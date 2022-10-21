from __future__ import annotations
from typing import NamedTuple


class Point3D(NamedTuple):
    x: int
    y: int
    z: int


class Position3D(Point3D):
    @classmethod
    def copy(cls, position: Position3D) -> Position3D:
        return Position3D.of(position.x, position.y, position.z)

    @classmethod
    def of(cls, x: int, y: int, z: int) -> Position3D:
        return Position3D(x, y, z)

    def translate(self, vector: Vector3D, amplitude: int = 1) -> Position3D:
        return Position3D.of(
            self.x + vector.x * amplitude,
            self.y + vector.y * amplitude,
            self.z + vector.z * amplitude,
        )

    def manhattan_distance(self, other: Position3D) -> int:
        return (
            abs(self.x - other.x)
            + abs(self.y - other.y)
            + abs(self.z - other.z)
        )

    def manhattan_distance_to_origin(self) -> int:
        return self.manhattan_distance(Position3D.of(0, 0, 0))


class Vector3D(Point3D):
    @classmethod
    def of(cls, x: int, y: int, z: int) -> Vector3D:
        return Vector3D(x, y, z)

    @classmethod
    def to_from(
        cls, to: Position3D, from_: Position3D = Position3D.of(0, 0, 0)
    ) -> Vector3D:
        return Vector3D(to.x - from_.x, to.y - from_.y, to.z - from_.z)
