import unittest
from aoc.collections import index_of_sublist
from aoc.collections import indexes_of_sublist
from aoc.collections import subtract_all


class TestCollections(unittest.TestCase):

    def test_index_of_sublist(self) -> None:
        self.assertEqual(index_of_sublist([1, 2, 3], [4]), -1)
        self.assertEqual(index_of_sublist([1, 2, 3], [2, 3]), 1)

    def test_indexes_of_sublist(self) -> None:
        lst = [1, 2, 3, 1, 2, 3]
        self.assertEqual(indexes_of_sublist([], []), [])
        self.assertEqual(indexes_of_sublist(lst, [4]), [])
        self.assertEqual(indexes_of_sublist(lst, [1]), [0, 3])
        self.assertEqual(indexes_of_sublist(lst, [2, 3]), [1, 4])
        self.assertEqual(indexes_of_sublist(lst, [1, 2, 3]), [0, 3])
        self.assertEqual(indexes_of_sublist(lst, [1, 2, 3, 1]), [0])
        self.assertEqual(indexes_of_sublist(lst, [1, 2, 3, 1, 2, 3]), [0])
        self.assertEqual(indexes_of_sublist(lst[:-1], [1, 2, 3]), [0])

    def test_subtract_all(self) -> None:
        self.assertEqual(subtract_all([1, 2, 3], [1, 2, 3]), [])
        lst = [1, 2, 3, 4, 1, 2, 3, 4, 1, 2]
        self.assertEqual(subtract_all(lst, []), lst)
        self.assertEqual(subtract_all(lst, [5]), lst)
        self.assertEqual(subtract_all(lst, [1, 2, 3]), [4, 4, 1, 2])
