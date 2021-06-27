#! /usr/bin/env python3
#

import unittest

from aoc.math import Fibonacci


class TestFibonacci(unittest.TestCase):

    def test_binet(self):
        self.assertEqual(Fibonacci.binet(0), 0)
        self.assertEqual(Fibonacci.binet(1), 1)
        self.assertEqual(Fibonacci.binet(2), 1)
        self.assertEqual(Fibonacci.binet(3), 2)
        self.assertEqual(Fibonacci.binet(4), 3)
        self.assertEqual(Fibonacci.binet(5), 5)
        self.assertEqual(Fibonacci.binet(27), 196_418)
        self.assertEqual(Fibonacci.binet(35), 9_227_465)
