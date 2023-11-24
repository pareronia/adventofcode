#! /usr/bin/env python3
#

import unittest
from aoc.navigation import NavigationWithWaypoint, NavigationWithHeading, \
        Waypoint, Heading
from aoc.geometry import Position, Turn


class TestNavigation(unittest.TestCase):

    def test_navigation_with_waypoint(self) -> None:
        navigation = NavigationWithWaypoint(
            Position(0, 0),
            Waypoint(0, 0),
            lambda pos: pos.x <= 4 and pos.y <= 4
        )
        navigation.update_waypoint(Heading.NORTH, 1)
        navigation.forward(1)
        navigation.left(90)
        navigation.forward(1)
        navigation.right(180)
        navigation.forward(10)

        self.assertEqual(navigation.position, Position(4, 1))
        visited_positions = navigation.get_visited_positions(True)
        self.assertEqual(visited_positions, [Position(0, 0),
                                             Position(0, 1),
                                             Position(-1, 1),
                                             Position(4, 1)])
        visited_positions = navigation.get_visited_positions(False)
        self.assertEqual(visited_positions, [Position(0, 1),
                                             Position(-1, 1),
                                             Position(4, 1)])

    def test_navigation_with_heading(self) -> None:
        navigation = NavigationWithHeading(
            Position(0, 0),
            Heading.NORTH,
            lambda pos: pos.x <= 4 and pos.y <= 4
        )
        navigation.drift(Heading.NORTH, 1)
        navigation.turn(Turn.LEFT)
        navigation.forward(1)
        navigation.turn(Turn.AROUND)
        navigation.forward(10)

        self.assertEqual(navigation.position, Position(4, 1))
        visited_positions = navigation.get_visited_positions(True)
        self.assertEqual(visited_positions, [Position(0, 0),
                                             Position(0, 1),
                                             Position(-1, 1),
                                             Position(4, 1)])
        visited_positions = navigation.get_visited_positions(False)
        self.assertEqual(visited_positions, [Position(0, 1),
                                             Position(-1, 1),
                                             Position(4, 1)])
