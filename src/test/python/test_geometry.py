#! /usr/bin/env python3
#

import unittest
from aoc.geometry import Position


class TestGeometry(unittest.TestCase):
    def test_manhattan_distance(self):
        self.assertEqual(
            Position.of(2, 2).manhattan_distance(Position.of(0, 0)), 4)
        self.assertEqual(
            Position.of(2, 2).manhattan_distance(Position.of(1, 1)), 2)

    def test_manhattan_distance_to_origin(self):
        self.assertEqual(
            Position.of(2, 2).manhattan_distance_to_origin(), 4)
        self.assertEqual(
            Position.of(5, -3).manhattan_distance_to_origin(), 8)
