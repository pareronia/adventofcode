#! /usr/bin/env python3
#
# Advent of Code 2016 Day 19
#

from __future__ import annotations
import aocd
from aoc import my_aocd


class Node:
    value: int
    prev: Node
    next: Node

    def __init__(self, value: int):
        self.value = value
        self.prev = None
        self.next = None


class DoublyLinkedList:
    size: int
    head: Node
    tail: Node

    def __init__(self):
        self.size = 0

    def add_tail(self, value: int):
        node = Node(value)
        if self.size == 0:
            self.tail = node
            self.head = node
        else:
            self.tail.next = node
            node.prev = self.tail
            self.tail = node
        self.size += 1

    def close(self):
        self.head.prev = self.tail
        self.tail.next = self.head

    def remove(self, node: Node):
        node.prev.next = node.next
        node.next.prev = node.prev
        self.size -= 1


def _parse(inputs: tuple[str]) -> str:
    assert len(inputs) == 1
    elves = DoublyLinkedList()
    for i in range(int(inputs[0])):
        elves.add_tail(i + 1)
    elves.close()
    return elves


def part_1(inputs: tuple[str]) -> int:
    elves = _parse(inputs)
    curr = elves.head
    while elves.size > 1:
        loser = curr.next
        elves.remove(loser)
        curr = curr.next
    return curr.value


def part_2(inputs: tuple[str]) -> int:
    elves = _parse(inputs)
    curr = elves.head
    opposite = elves.head
    for i in range(elves.size // 2):
        opposite = opposite.next
    while elves.size > 1:
        loser = opposite
        elves.remove(loser)
        if elves.size % 2:
            opposite = opposite.next
        else:
            opposite = opposite.next.next
        curr = curr.next
    return curr.value


TEST = '''\
5
'''.splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2016, 19)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 3
    assert part_2(TEST) == 2

    inputs = my_aocd.get_input(puzzle.year, puzzle.day, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()
