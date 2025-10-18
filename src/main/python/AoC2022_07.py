#! /usr/bin/env python3
#
# Advent of Code 2022 Day 7
#


import sys
from collections import defaultdict

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST = """\
$ cd /
$ ls
dir a
14848514 b.txt
8504156 c.dat
dir d
$ cd a
$ ls
dir e
29116 f
2557 g
62596 h.lst
$ cd e
$ ls
584 i
$ cd ..
$ cd ..
$ cd d
$ ls
4060174 j
8033020 d.log
5626152 d.ext
7214296 k
"""


Input = InputData
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return input_data

    def get_sizes(self, inputs: InputData) -> dict[str, int]:
        path = list[str]()
        sizes = defaultdict[str, int](int)
        for line in tuple(inputs)[1:]:
            if line.startswith("$ cd "):
                name = line[5:]
                if name == "..":
                    path = path[:-1]
                else:
                    path.append(name)
            elif not line.startswith("$"):
                first, _ = line.split()
                if first != "dir":
                    size = int(first)
                    for i in range(len(path) + 1):
                        pp = "/".join(_ for _ in path[:i])
                        sizes["/" + pp] += size
        return sizes

    def part_1(self, inputs: Input) -> Output1:
        return sum(v for v in self.get_sizes(inputs).values() if v <= 100_000)

    def part_2(self, inputs: Input) -> Output2:
        sizes = self.get_sizes(inputs)
        wanted = 30_000_000 - (70_000_000 - sizes["/"])
        return next(v for v in sorted(sizes.values()) if v >= wanted)

    @aoc_samples(
        (
            ("part_1", TEST, 95_437),
            ("part_2", TEST, 24_933_642),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2022, 7)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
