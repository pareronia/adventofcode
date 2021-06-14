#! /usr/bin/env python3
#

import unittest

from aoc.geometry import Position, Rectangle


class TestGeometry(unittest.TestCase):

    def test_position_bounded_by_rect_in_bounds(self):
        rect = Rectangle.of(Position.of(-4, -4), Position.of(4, 4))

        pos = Position.of(0, 0).bounded(rect)

        self.assertEqual(pos.x, 0)
        self.assertEqual(pos.y, 0)

    def test_position_bounded_by_rect_out_of_bounds(self):
        rect = Rectangle.of(Position.of(-4, -4), Position.of(4, 4))
        pos = Position.of(6, -6)

        pos.bounded(rect)

        self.assertEqual(pos.x, 4)
        self.assertEqual(pos.y, -4)

    def test_position_bounded_by_none(self):
        pos = Position.of(6, -6)

        pos.bounded(None)

        self.assertEqual(pos.x, 6)
        self.assertEqual(pos.y, -6)
