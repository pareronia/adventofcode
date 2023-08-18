#! /usr/bin/env python3
#
# Advent of Code 2022 Day 20
#


from __future__ import annotations

import aocd

from aoc import my_aocd


def _solve(inputs: tuple[str], rounds: int, factor: int = 1) -> int:
    nums = [int(line) * factor for line in inputs]
    idxs = [i for i in range(len(nums))]
    for _ in range(rounds):
        for i, num in enumerate(nums):
            idx = idxs.index(i)
            idxs.remove(i)
            new_idx = (idx + num) % len(idxs)
            idxs.insert(new_idx, i)
    zero_idx = idxs.index(nums.index(0))
    return sum(
        nums[idxs[(zero_idx + i) % len(idxs)]] for i in [1000, 2000, 3000]
    )


def part_1(inputs: tuple[str]) -> int:
    return _solve(inputs, rounds=1)


def part_2(inputs: tuple[str]) -> int:
    return _solve(inputs, rounds=10, factor=811_589_153)


TEST1 = tuple(
    """\
1
2
-3
3
-2
0
4
""".splitlines()
)
TEST2 = tuple(
    """\
3
1
0
""".splitlines()
)


def main() -> None:
    puzzle = aocd.models.Puzzle(2022, 20)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST1) == 3
    assert part_1(TEST2) == 4
    assert part_2(TEST1) == 1_623_178_306

    inputs = my_aocd.get_input_data(puzzle, 5000)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()
