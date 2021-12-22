#! /usr/bin/env python3
#

import unittest
from AoC2021_18 import Parser, Exploder, Splitter, Number, Pair, Regular


class TestExploder(unittest.TestCase):

    def test(self):
        self.assertEqual(self._explode(
            "[[[[[9,8],1],2],3],4]"),
            "[[[[0,9],2],3],4]")
        self.assertEqual(self._explode(
            "[7,[6,[5,[4,[3,2]]]]]"),
            "[7,[6,[5,[7,0]]]]")
        self.assertEqual(self._explode(
            "[[6,[5,[4,[3,2]]]],1]"),
            "[[6,[5,[7,0]]],3]")
        self.assertEqual(self._explode(
            "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]"),
            "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]")
        self.assertEqual(self._explode(
            "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]"),
            "[[3,[2,[8,0]]],[9,[5,[7,0]]]]")
        self.assertEqual(self._explode(
            "[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]"),
            "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]")

    def _explode(self, string: str) -> str:
        number = Parser.parse(string)
        Exploder.explode(number, 0)
        return str(number)


class TestSplitter(unittest.TestCase):

    def test_1(self):
        # [[[[0,7],4],[15,[0,13]]],[1,1]]
        number = Pair.create(
            Pair.create(
                Pair.create(
                    Pair.create(
                        Regular(0), Regular(7)),
                    Regular(4)),
                Pair.create(
                    Regular(15),
                    Pair.create(
                        Regular(0), Regular(13)))),
            Pair.create(Regular(1), Regular(1)))
        self.assertEqual(str(number), "[[[[0,7],4],[15,[0,13]]],[1,1]]")
        self.assertEqual(self._split(number),
                         "[[[[0,7],4],[[7,8],[0,13]]],[1,1]]")
        self.assertEqual(self._split(number),
                         "[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]")

    def test_2(self):
        # [[[[7,7],[7,8]],[[9,5],[8,0]]],[[[9,10],20],[8,[9,0]]]]
        number = Pair.create(
            Pair.create(Regular(9), Regular(10)),
            Regular(20))
        self.assertEqual(str(number), "[[9,10],20]")
        self.assertEqual(self._split(number), "[[9,[5,5]],20]")

    def _split(self, number: Number) -> str:
        Splitter.split(number)
        return str(number)
