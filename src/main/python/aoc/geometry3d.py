from __future__ import annotations

import itertools
from typing import Iterator
from typing import NamedTuple

from aoc.range import RangeInclusive


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


class Cuboid(NamedTuple):
    x1: int
    x2: int
    y1: int
    y2: int
    z1: int
    z2: int

    @classmethod
    def of(
        cls, x1: int, x2: int, y1: int, y2: int, z1: int, z2: int
    ) -> Cuboid:
        return Cuboid(x1, x2, y1, y2, z1, z2)

    def positions(self) -> Iterator[Position3D]:
        for (x, y), z in itertools.product(
            itertools.product(
                range(self.x1, self.x2 + 1), range(self.y1, self.y2 + 1)
            ),
            range(self.z1, self.z2 + 1),
        ):
            yield Position3D(x, y, z)

    def overlap_x(self, other: Cuboid) -> bool:
        return RangeInclusive.between(self.x1, self.x2).is_overlapped_by(
            RangeInclusive.between(other.x1, other.x2)
        )

    def overlap_y(self, other: Cuboid) -> bool:
        return RangeInclusive.between(self.y1, self.y2).is_overlapped_by(
            RangeInclusive.between(other.y1, other.y2)
        )

    def overlap_z(self, other: Cuboid) -> bool:
        return RangeInclusive.between(self.z1, self.z2).is_overlapped_by(
            RangeInclusive.between(other.z1, other.z2)
        )
