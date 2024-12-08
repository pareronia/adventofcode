#! /usr/bin/env python3
#
# Advent of Code 2024 Day 8
#

import itertools
import sys
from collections import defaultdict

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.common import log
from aoc.geometry import Position
from aoc.geometry import Vector

Input = InputData
Output1 = int
Output2 = int


TEST = """\
............
........0...
.....0......
.......0....
....0.......
......A.....
............
............
........A...
.........A..
............
............
"""


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def part_1(self, input: Input) -> Output1:
        lines = list(input)
        h, w = len(lines), len(lines[0])
        antennae = defaultdict[str, set[Position]](set)
        for r in range(h):
            for c in range(w):
                freq = lines[r][c]
                if freq == ".":
                    continue
                antennae[freq].add(Position(c, h - r - 1))
        ans = set[Position]()
        for p in antennae.values():
            for a, b in itertools.combinations(p, 2):
                v = Vector.of(a.x - b.x, a.y - b.y)
                tmp = set[Position]()
                tmp.add(a.translate(v, 1))
                tmp.add(a.translate(v, -1))
                tmp.add(b.translate(v, 1))
                tmp.add(b.translate(v, -1))
                tmp.remove(a)
                tmp.remove(b)
                ans |= tmp
        return len({an for an in ans if 0 <= an.x < w and 0 <= an.y < h})

    def part_2(self, input: Input) -> Output2:
        lines = list(input)
        h, w = len(lines), len(lines[0])
        antennae = defaultdict[str, set[Position]](set)
        for r in range(h):
            for c in range(w):
                freq = lines[r][c]
                if freq == ".":
                    continue
                antennae[freq].add(Position(c, h - r - 1))
        ans = set[Position]()
        for p in antennae.values():
            for a, b in itertools.combinations(p, 2):
                v = Vector.of(a.x - b.x, a.y - b.y)
                amp = 1
                while True:
                    an = a.translate(v, amp)
                    if not (0 <= an.x < w and 0 <= an.y < h):
                        break
                    ans.add(an)
                    amp += 1
                amp = -1
                while True:
                    an = a.translate(v, amp)
                    if not (0 <= an.x < w and 0 <= an.y < h):
                        break
                    ans.add(an)
                    amp -= 1
                amp = 1
                while True:
                    an = b.translate(v, amp)
                    if not (0 <= an.x < w and 0 <= an.y < h):
                        break
                    ans.add(an)
                    amp += 1
                amp = -1
                while True:
                    an = b.translate(v, amp)
                    if not (0 <= an.x < w and 0 <= an.y < h):
                        break
                    ans.add(an)
                    amp -= 1
        for r in range(h):
            line = ""
            for c in range(w):
                pos = Position(c, h - r - 1)
                for k, val in antennae.items():
                    if pos in val:
                        line += k
                        break
                else:
                    if pos in ans:
                        line += "#"
                    else:
                        line += "."
            log(line)
        return len(ans)

    @aoc_samples(
        (
            ("part_1", TEST, 14),
            ("part_2", TEST, 34),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2024, 8)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
