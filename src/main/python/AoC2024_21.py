#! /usr/bin/env python3
#
# Advent of Code 2024 Day 21
#

import sys
from functools import cache

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.graph import Dijkstra
from aoc.grid import Cell

Input = InputData
Output1 = int
Output2 = int


TEST = """\
029A
980A
179A
456A
379A
"""

DIR_KEYPAD = {
    "A": {
        "^": ["<A"],
        ">": ["vA"],
        "v": ["<vA", "v<A"],
        "<": ["v<<A", "<v<A"],
        "A": ["A"],
    },
    "^": {
        "^": ["A"],
        ">": ["v>A", ">vA"],
        "v": ["vA"],
        "<": ["v<A"],
        "A": [">A"],
    },
    ">": {
        "^": ["<^A", "^<A"],
        ">": ["A"],
        "v": ["<A"],
        "<": ["<<A"],
        "A": ["^A"],
    },
    "v": {
        "^": ["^A"],
        ">": [">A"],
        "v": ["A"],
        "<": ["<A"],
        "A": [">^A", "^>A"],
    },
    "<": {
        "^": [">^A"],
        ">": [">>A"],
        "v": [">A"],
        "<": ["A"],
        "A": [">>^A", ">^>A"],
    },
}

NUM_KEYPAD = {
    "7": Cell(0, 0),
    "8": Cell(0, 1),
    "9": Cell(0, 2),
    "4": Cell(1, 0),
    "5": Cell(1, 1),
    "6": Cell(1, 2),
    "1": Cell(2, 0),
    "2": Cell(2, 1),
    "3": Cell(2, 2),
    "0": Cell(3, 1),
    "A": Cell(3, 2),
}


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def solve(self, input: Input, levels: int) -> int:
        @cache
        def do_dir_keypad(seq: str, level: int) -> int:
            if level == 1:
                return sum(
                    len(DIR_KEYPAD[first][second][0])
                    for first, second in zip("A" + seq, seq)
                )
            else:
                return sum(
                    min(
                        do_dir_keypad(move, level - 1)
                        for move in DIR_KEYPAD[first][second]
                    )
                    for first, second in zip("A" + seq, seq)
                )

        def do_num_keypad(prev: str, nxt: str, levels: int) -> int:
            def get_paths(first: str, second: str) -> list[list[Cell]]:
                start, end = NUM_KEYPAD[first], NUM_KEYPAD[second]
                result = Dijkstra.all(
                    start,
                    lambda cell: cell == end,
                    lambda cell: (
                        n
                        for n in cell.get_capital_neighbours()
                        if n in NUM_KEYPAD.values()
                    ),
                    lambda curr, nxt: 1,
                )
                return result.get_paths(end)

            moves = [
                "".join(
                    cell_1.to(cell_2).arrow  # type:ignore
                    for cell_1, cell_2 in zip(path, path[1:])
                )
                for path in get_paths(prev, nxt)
            ]
            return min(do_dir_keypad(move + "A", levels) for move in moves)

        return sum(
            int(combo[:-1]) * do_num_keypad(a, b, levels)
            for combo in input
            for a, b in zip("A" + combo, combo)
        )

    def part_1(self, input: Input) -> Output1:
        return self.solve(input, 2)

    def part_2(self, input: Input) -> Output2:
        return self.solve(input, 25)

    @aoc_samples((("part_1", TEST, 126384),))
    def samples(self) -> None:
        pass


solution = Solution(2024, 21)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
