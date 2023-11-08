#! /usr/bin/env python3
#
# Advent of Code 2022 Day 5
#


import re

from aoc import my_aocd
from aoc.common import aoc_main

Move = tuple[int, int, int]

CM_9000 = "CrateMover 9000"
CM_9001 = "CrateMover 9001"


def _parse(inputs: tuple[str, ...]) -> tuple[list[list[str]], list[Move]]:
    blocks = my_aocd.to_blocks(inputs)
    size = int(blocks[0][-1].replace(" ", "")[-1])
    stacks: list[list[str]] = [[] for _ in range(size)]
    for i in range(len(blocks[0]) - 2, -1, -1):
        line = blocks[0][i]
        for j in range(len(line)):
            if j % 4 == 1 and line[j] != " ":
                stacks[j // 4].append(line[j])
    moves = [
        (n1, n2 - 1, n3 - 1)
        for n1, n2, n3 in (
            (int(n) for n in re.findall(r"[0-9]+", line)) for line in blocks[1]
        )
    ]
    return stacks, moves


def _simulate_for(
    stacks: list[list[str]], moves: list[Move], crate_mover: str
) -> str:
    for amount, _from, to in moves:
        tmp = []
        for _ in range(amount):
            crate = stacks[_from][-1]
            stacks[_from] = stacks[_from][:-1]
            if crate_mover == CM_9000:
                tmp.append(crate)
            else:
                tmp.insert(0, crate)
        for c in tmp:
            stacks[to].append(c)
    return "".join(stack[-1] for stack in stacks)


def part_1(inputs: tuple[str, ...]) -> str:
    stacks, moves = _parse(inputs)
    return _simulate_for(stacks, moves, CM_9000)


def part_2(inputs: tuple[str, ...]) -> str:
    stacks, moves = _parse(inputs)
    return _simulate_for(stacks, moves, CM_9001)


TEST = tuple(
    [
        "    [D]    ",
        "[N] [C]    ",
        "[Z] [M] [P]",
        " 1   2   3 ",
        "",
        "move 1 from 2 to 1",
        "move 3 from 1 to 3",
        "move 2 from 2 to 1",
        "move 1 from 1 to 2",
    ]
)


@aoc_main(2022, 5, part_1, part_2)
def main() -> None:
    assert part_1(TEST) == "CMZ"
    assert part_2(TEST) == "MCD"


if __name__ == "__main__":
    main()
