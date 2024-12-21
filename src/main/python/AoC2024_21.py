#! /usr/bin/env python3
#
# Advent of Code 2024 Day 21
#

import itertools
import sys

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log
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
        "A": [[""]],
    },
    "^": {
        "^": [[""]],
        ">": [["v", ">"], [">", "v"]],
        "v": [["v"]],
        "<": [["v", "<"]],
        "A": [[">"]],
    },
    ">": {
        "^": [["<", "^"], ["^", "<"]],
        ">": [[""]],
        "v": [["<"]],
        "<": [["<", "<"]],
        "A": [["^"]],
    },
    "v": {
        "^": [["^"]],
        ">": [[">"]],
        "v": [[""]],
        "<": [["<"]],
        "A": [[">", "^"], ["^", ">"]],
    },
    "<": {
        "^": [[">", "^"]],
        ">": [[">", ">"]],
        "v": [[">"]],
        "<": [[""]],
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

    def do_dir_keypad(self, seq: list[str]) -> list[list[str]]:
        ans = []

        def dfs(seq: list[str], pos: int, path: list[str]) -> None:
            if pos == len(seq):
                ans.append(path)
                return
            prev, nxt = seq[pos - 1], seq[pos]
            for s in DIR_KEYPAD[prev][nxt]:
                new_path = path[:]
                if s != [""]:
                    for x in s:
                        new_path.append(x)
                new_path.append("A")
                dfs(seq, pos + 1, new_path)

        dfs(["A"] + seq, 1, [])
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
            # log((prev, nxt))
            paths = result.get_paths(end)
            moves = []
            for path in paths:
                move = []
                for c1, c2 in zip(path, path[1:]):
                    move.append(c1.to(c2).arrow)
                moves.append(move)
            all_moves.append(moves)

        # log(all_moves)
        tmp = [p for p in itertools.product(*all_moves)]
        # log(tmp)
        for t in tmp:
            a = []
            for tt in t:
                x = list(tt) + ["A"]
                a.extend(x)
            if a is not None:
                ans.append(a)  # type:ignore
        log(seq)
        log(ans)
        return ans

    def solve_1(self, input: str) -> int:
        best = sys.maxsize
        seqs = self.do_num_keypad(input)
        for seq1 in seqs:
            seq2 = self.do_dir_keypad(seq1)
            seq3 = []
            for seq in seq2:
                seq3.extend(self.do_dir_keypad(seq))
            shortest = sorted(seq3, key=len)[0]
            if len(shortest) < best:
                best = len(shortest)
        return best

    def part_1(self, input: Input) -> Output1:
        return sum(self.solve_1(combo) * int(combo[:-1]) for combo in input)

    def part_2(self, input: Input) -> Output2:
        return 0

    @aoc_samples(
        (
            ("part_1", TEST, 126384),
            # ("part_2", TEST, "TODO"),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2024, 21)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
