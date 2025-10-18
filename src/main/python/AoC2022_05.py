#! /usr/bin/env python3
#
# Advent of Code 2022 Day 5
#


import re
import sys

from aoc import my_aocd
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples

TEST = """\
    [D]    \n\
[N] [C]    \n\
[Z] [M] [P]\n\
 1   2   3 \n\

move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2
"""


Move = tuple[int, int, int]
Stack = tuple[str, ...]
Input = tuple[tuple[Stack, ...], tuple[Move, ...]]
Output1 = str
Output2 = str
RE = re.compile(r"[0-9]+")
CM_9000 = "CrateMover 9000"
CM_9001 = "CrateMover 9001"


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        blocks = my_aocd.to_blocks(list(input_data))
        size = int(blocks[0][-1].replace(" ", "")[-1])
        stacks: list[list[str]] = [[] for _ in range(size)]
        for i in range(len(blocks[0]) - 2, -1, -1):
            line = blocks[0][i]
            for j in range(len(line)):
                if j % 4 == 1 and line[j] != " ":
                    stacks[j // 4].append(line[j])
        moves = tuple(
            (n1, n2 - 1, n3 - 1)
            for n1, n2, n3 in (
                (int(n) for n in RE.findall(line)) for line in blocks[1]
            )
        )
        return tuple(tuple(stack) for stack in stacks), moves

    def simulate_for(
        self,
        stacks: tuple[Stack, ...],
        moves: tuple[Move, ...],
        crate_mover: str,
    ) -> str:
        new_stacks = [list(stack) for stack in stacks]
        for amount, _from, to in moves:
            tmp = []
            for _ in range(amount):
                tmp.append(new_stacks[_from][-1])
                new_stacks[_from] = new_stacks[_from][:-1]
            new_stacks[to].extend(
                tmp if crate_mover == CM_9000 else reversed(tmp)
            )
        return "".join(stack[-1] for stack in new_stacks)

    def part_1(self, inputs: Input) -> Output1:
        stacks, moves = inputs
        return self.simulate_for(stacks, moves, crate_mover=CM_9000)

    def part_2(self, inputs: Input) -> Output2:
        stacks, moves = inputs
        return self.simulate_for(stacks, moves, crate_mover=CM_9001)

    @aoc_samples(
        (
            ("part_1", TEST, "CMZ"),
            ("part_2", TEST, "MCD"),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2022, 5)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
