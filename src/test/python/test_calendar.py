#! /usr/bin/env python3
#

import unittest

from aoc import calendar


class TestCalendar(unittest.TestCase):
    def test_get_days(self) -> None:
        self.assertEqual(calendar.get_days(2025), 12)
        self.assertEqual(calendar.get_days(2015), 25)
        with self.assertRaises(AssertionError):
            calendar.get_days(2014)

    def test_is_valid_day(self) -> None:
        self.assertTrue(calendar.is_valid_day(2025, 5))
        self.assertFalse(calendar.is_valid_day(2025, 0))
        self.assertFalse(calendar.is_valid_day(2025, 20))
        self.assertFalse(calendar.is_valid_day(2024, 26))
        self.assertFalse(calendar.is_valid_day(2125, 1))
        self.assertFalse(calendar.is_valid_day(2014, 1))

    def test_get_now_aoc(self) -> None:
        self.assertIsNotNone(calendar.get_now_aoc())

    def test_get_now_utc(self) -> None:
        self.assertIsNotNone(calendar.get_now_utc())


if __name__ == "__main__":
    unittest.main()
