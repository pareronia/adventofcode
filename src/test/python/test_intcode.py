import unittest

from aoc import intcode


class TestIntcode(unittest.TestCase):
    def test(self):
        ans = intcode.run([1, 9, 10, 3, 2, 3, 11, 0, 99, 30, 40, 50])
        self.assertEqual(
            ans,
            [
                3500,
                9,
                10,
                70,
                2,
                3,
                11,
                0,
                99,
                30,
                40,
                50,
            ],
        )

        ans = intcode.run([1, 0, 0, 0, 99])
        self.assertEqual(ans, [2, 0, 0, 0, 99])

        ans = intcode.run([2, 3, 0, 3, 99])
        self.assertEqual(ans, [2, 3, 0, 6, 99])

        ans = intcode.run([2, 4, 4, 5, 99, 0])
        self.assertEqual(ans, [2, 4, 4, 5, 99, 9801])

        ans = intcode.run([1, 1, 1, 4, 99, 5, 6, 0, 99])
        self.assertEqual(ans, [30, 1, 1, 4, 2, 5, 6, 0, 99])
