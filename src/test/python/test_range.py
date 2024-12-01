import unittest

from aoc.range import Range


class RangeTest(unittest.TestCase):
    def test_left_join(self) -> None:
        self.assertEqual(Range.left_join((0, 5), (5, 10)), (None, {(0, 5)}))
        self.assertEqual(Range.left_join((0, 5), (3, 10)), ((3, 5), {(0, 3)}))
        self.assertEqual(Range.left_join((3, 6), (4, 10)), ((4, 6), {(3, 4)}))
        self.assertEqual(Range.left_join((5, 7), (3, 10)), ((5, 7), set()))
        self.assertEqual(
            Range.left_join((3, 10), (5, 7)), ((5, 7), {(3, 5), (7, 10)})
        )
