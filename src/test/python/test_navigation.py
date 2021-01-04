#! /usr/bin/env python3
#

import unittest
from aoc.navigation import NavigationWithWaypoint, NavigationWithHeading,\
        Waypoint, Headings
from aoc.geometry import Position


class TestNavigation(unittest.TestCase):

    def test_navigation_with_waypoint(self):
        navigation = NavigationWithWaypoint(Position(0, 0), Waypoint(0, 0))
        navigation.update_waypoint(Headings["N"].value, 1)
        navigation.forward(1)
        navigation.left(90)
        navigation.forward(1)
        navigation.right(180)
        navigation.forward(1)

        self.assertEqual(navigation.position, Position(0, 1))
        visited_positions = navigation.get_visited_positions(True)
        self.assertEqual(visited_positions, [Position(0, 0),
                                             Position(0, 1),
                                             Position(-1, 1),
                                             Position(0, 1)])
        visited_positions = navigation.get_visited_positions(False)
        self.assertEqual(visited_positions, [Position(0, 1),
                                             Position(-1, 1),
                                             Position(0, 1)])

    def test_navigation_with_heading(self):
        navigation = NavigationWithHeading(Position(0, 0), Headings["N"].value)
        navigation.drift(Headings["N"].value, 1)
        navigation.left(90)
        navigation.forward(1)
        navigation.right(180)
        navigation.forward(1)

        self.assertEqual(navigation.position, Position(0, 1))
        visited_positions = navigation.get_visited_positions(True)
        self.assertEqual(visited_positions, [Position(0, 0),
                                             Position(0, 1),
                                             Position(-1, 1),
                                             Position(0, 1)])
        visited_positions = navigation.get_visited_positions(False)
        self.assertEqual(visited_positions, [Position(0, 1),
                                             Position(-1, 1),
                                             Position(0, 1)])
