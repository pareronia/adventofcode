#! /usr/bin/env python3
#

import unittest

from aoc.geometry3d import Cuboid
from aoc.geometry3d import Position3D


class TestGeometry3D(unittest.TestCase):
    def test_manhattan_distance(self) -> None:
        self.assertEqual(
            Position3D.of(2, 2, 2).manhattan_distance(Position3D.of(0, 0, 0)),
            6,
        )
        self.assertEqual(
            Position3D.of(2, 2, 2).manhattan_distance(Position3D.of(1, 1, 1)),
            3,
        )

    def test_manhattan_distance_to_origin(self) -> None:
        self.assertEqual(
            Position3D.of(2, 2, 2).manhattan_distance_to_origin(), 6
        )
        self.assertEqual(
            Position3D.of(5, -3, 2).manhattan_distance_to_origin(), 10
        )

    def test_cuboid(self) -> None:
        cuboid = Cuboid.of(0, 2, 0, 1, 0, 1)
        self.assertEqual(
            set(cuboid.positions()),
            {
                Position3D.of(0, 0, 0),
                Position3D.of(0, 0, 1),
                Position3D.of(0, 1, 0),
                Position3D.of(0, 1, 1),
                Position3D.of(1, 0, 0),
                Position3D.of(1, 0, 1),
                Position3D.of(1, 1, 0),
                Position3D.of(1, 1, 1),
                Position3D.of(2, 0, 0),
                Position3D.of(2, 0, 1),
                Position3D.of(2, 1, 0),
                Position3D.of(2, 1, 1),
            },
        )
