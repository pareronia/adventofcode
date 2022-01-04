#! /usr/bin/env python3
#
# Advent of Code 2021 Day 18
#

from __future__ import annotations
from queue import deque
import itertools
from aoc import my_aocd
# from aoc.common import log
import aocd


class Number:
    parent: Number

    def __init__(self, parent: Number):
        self.parent = parent


class Regular(Number):
    value: int

    def __init__(self, value: int):
        self.value = value
        Number.__init__(self, None)

    def __repr__(self) -> str:
        return str(self.value)


class Pair(Number):
    left: Number
    right: Number

    def __init__(self, left: Number, right: Number):
        self.left = left
        self.right = right
        Number.__init__(self, None)

    @classmethod
    def create(cls, left: Number, right: Number) -> Pair:
        pair = Pair(left, right)
        left.parent = pair
        right.parent = pair
        return pair

    def __repr__(self) -> str:
        return f"[{self.left},{self.right}]"

    def left_adjacent(self) -> Regular:
        if self.parent is None:
            return None
        if self == self.parent.right:
            left = self.parent.left
            while isinstance(left, Pair):
                left = left.right
            return left
        return self.parent.left_adjacent()

    def right_adjacent(self) -> Regular:
        if self.parent is None:
            return None
        if self == self.parent.left:
            right = self.parent.right
            while isinstance(right, Pair):
                right = right.left
            return right
        return self.parent.right_adjacent()


class Parser:
    @classmethod
    def parse(cls, line: str) -> Number:
        stack = deque[Number]()
        for _, ch in enumerate(line):
            if ch == '[':
                continue
            elif ch.isdigit():
                stack.append(Regular(int(ch)))
            elif ch == ',':
                continue
            elif ch == ']':
                right = stack.pop()
                left = stack.pop()
                pair = Pair.create(left, right)
                stack.append(pair)
            else:
                raise ValueError
        assert len(stack) == 1
        return stack.pop()


class Exploder:
    @classmethod
    def explode(cls, number: Number, depth: int) -> bool:
        if isinstance(number, Regular):
            return False
        pair = number
        if depth != 4:
            return cls.explode(pair.left, depth + 1) \
                    or cls.explode(pair.right, depth + 1)
        left_adjacent = pair.left_adjacent()
        right_adjacent = pair.right_adjacent()
        if left_adjacent is not None:
            left_adjacent.value += pair.left.value
        if right_adjacent is not None:
            right_adjacent.value += pair.right.value
        parent = pair.parent
        assert parent is not None
        if pair == parent.left:
            parent.left = Regular(0)
            parent.left.parent = parent
        elif pair == parent.right:
            parent.right = Regular(0)
            parent.right.parent = parent
        else:
            raise RuntimeError
        return True


class Splitter:
    @classmethod
    def split(cls, number: Number) -> bool:
        if not isinstance(number, Regular):
            pair = number
            return cls.split(pair.left) or cls.split(pair.right)
        regular = number
        value = regular.value
        if value < 10:
            return False
        pair = Pair.create(Regular(value // 2), Regular(value - value // 2))
        pair.parent = regular.parent
        parent = regular.parent
        if regular == parent.left:
            parent.left = pair
        elif regular == parent.right:
            parent.right = pair
        else:
            raise RuntimeError
        return True


class Reducer:
    @classmethod
    def reduce(cls, number: Number) -> None:
        while True:
            if Exploder.explode(number, 0) or Splitter.split(number):
                continue
            break


class Adder:
    @classmethod
    def add(cls, left: Number, right: Number) -> Number:
        return Pair.create(left, right)


class Magnitude:
    @classmethod
    def magnitude(cls, number: Number) -> int:
        if isinstance(number, Regular):
            return number.value
        return 3 * cls.magnitude(number.left) + 2 * cls.magnitude(number.right)


def _solve(numbers: list[Number]) -> Number:
    sum_ = numbers[0]
    for number in numbers[1:]:
        sum_ = Adder.add(sum_, number)
        Reducer.reduce(sum_)
    return sum_


def _solve_1(inputs: tuple[str]) -> Number:
    return _solve([Parser.parse(line) for line in inputs])


def part_1(inputs: tuple[str]) -> int:
    return Magnitude.magnitude(_solve_1(inputs))


def part_2(inputs: tuple[str]) -> int:
    return max(
        Magnitude.magnitude(_solve([Parser.parse(s1), Parser.parse(s2)]))
        for s1, s2 in itertools.product(inputs, inputs)
        if s1 != s2
    )


TEST1 = """\
[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]
[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]
[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]
[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]
[7,[5,[[3,8],[1,4]]]]
[[2,[2,2]],[8,[8,1]]]
[2,9]
[1,[[[9,3],9],[[9,0],[0,7]]]]
[[[5,[7,4]],7],1]
[[[[4,2],2],6],[8,7]]
""".splitlines()
TEST2 = """\
[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]
[[[5,[2,8]],4],[5,[[9,9],0]]]
[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]
[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]
[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]
[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]
[[[[5,4],[7,7]],8],[[8,3],8]]
[[9,3],[[9,9],[6,[4,9]]]]
[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]
[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2021, 18)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert str(_solve_1(["[1,1]", "[2,2]", "[3,3]",
                         "[4,4]", "[5,5]", "[6,6]"])) \
        == "[[[[5,0],[7,4]],[5,5]],[6,6]]"
    assert str(_solve_1(TEST1)) \
        == "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]"
    assert part_1(TEST2) == 4140
    assert part_2(TEST2) == 3993

    inputs = my_aocd.get_input(puzzle.year, puzzle.day, 100)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()
