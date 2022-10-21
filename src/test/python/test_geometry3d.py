#! /usr/bin/env python3
#

import unittest
from aoc.geometry3d import Position3D


class TestGeometry3D(unittest.TestCase):
    def test_manhattan_distance(self):
        self.assertEqual(
            Position3D.of(2, 2, 2).manhattan_distance(Position3D.of(0, 0, 0)),
            6,
        )
        self.assertEqual(
            Position3D.of(2, 2, 2).manhattan_distance(Position3D.of(1, 1, 1)),
            3,
        )

    def test_manhattan_distance_to_origin(self):
        self.assertEqual(
            Position3D.of(2, 2, 2).manhattan_distance_to_origin(), 6
        )
        self.assertEqual(
            Position3D.of(5, -3, 2).manhattan_distance_to_origin(), 10
        )
