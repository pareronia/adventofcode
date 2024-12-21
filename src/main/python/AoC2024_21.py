#! /usr/bin/env python3
#
# Advent of Code 2024 Day 21
#

import itertools
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
        "^": [["<"]],
        ">": [["v"]],
        "v": [["<", "v"], ["v", "<"]],
        "<": [["v", "<", "<"], ["<", "v", "<"]],
        "A": [],
    },
    "^": {
        "^": [],
        ">": [["v", ">"], [">", "v"]],
        "v": [["v"]],
        "<": [["v", "<"]],
        "A": [[">"]],
    },
    ">": {
        "^": [["<", "^"], ["^", "<"]],
        ">": [],
        "v": [["<"]],
        "<": [["<", "<"]],
        "A": [["^"]],
    },
    "v": {
        "^": [["^"]],
        ">": [[">"]],
        "v": [],
        "<": [["<"]],
        "A": [[">", "^"], ["^", ">"]],
    },
    "<": {
        "^": [[">", "^"]],
        ">": [[">", ">"]],
        "v": [[">"]],
        "<": [],
        "A": [[">", ">", "^"], [">", "^", ">"]],
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

    @cache
    def do_dir_keypad(self, seq: tuple[str, ...], level: int) -> int:
        assert len(seq) > 0
        if level == 1:
            ans = 0
            for first, second in zip(("A",) + seq, seq):
                moves = DIR_KEYPAD[first][second]
                ans += 1 if len(moves) == 0 else len(moves[0] + ["A"])
            return ans
        else:
            ans = 0
            for first, second in zip(("A",) + seq, seq):
                moves = DIR_KEYPAD[first][second]
                if len(moves) > 0:
                    ans += min(
                        self.do_dir_keypad(
                            tuple(_ for _ in move + ["A"]), level - 1
                        )
                        for move in moves
                    )
                else:
                    ans += self.do_dir_keypad(("A",), level - 1)
            return ans

    def do_num_keypad(self, seq: str) -> list[list[str]]:
        lst = [_ for _ in seq]
        ans = list[list[str]]()

        all_moves = []
        for prev, nxt in zip(["A"] + lst, lst):
            start = NUM_KEYPAD[prev]
            end = NUM_KEYPAD[nxt]
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
            paths = result.get_paths(end)
            moves = []
            for path in paths:
                move = []
                for c1, c2 in zip(path, path[1:]):
                    move.append(c1.to(c2).arrow)
                moves.append(move)
            all_moves.append(moves)

        tmp = [p for p in itertools.product(*all_moves)]
        for t in tmp:
            a = []
            for tt in t:
                x = list(tt) + ["A"]
                a.extend(x)
            if a is not None:
                ans.append(a)  # type:ignore
        return ans

    def solve(self, input: str, levels: int) -> int:
        best = sys.maxsize
        seqs = self.do_num_keypad(input)
        for seq in seqs:
            best = min(best, self.do_dir_keypad(tuple(_ for _ in seq), levels))
        return best

    def part_1(self, input: Input) -> Output1:
        return sum(self.solve(combo, 2) * int(combo[:-1]) for combo in input)

    def part_2(self, input: Input) -> Output2:
        return sum(self.solve(combo, 25) * int(combo[:-1]) for combo in input)

    @aoc_samples((("part_1", TEST, 126384),))
    def samples(self) -> None:
        pass


solution = Solution(2024, 21)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
