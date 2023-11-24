#! /usr/bin/env python3
#
# Advent of Code 2022 Day 3
#


from aoc.common import aoc_main


def _priority(ch: str) -> int:
    if "a" <= ch <= "z":
        return ord(ch) - ord("a") + 1
    else:
        return ord(ch) - ord("A") + 27


def part_1(inputs: tuple[str, ...]) -> int:
    ans = 0
    for input_ in inputs:
        ln = len(input_) // 2
        s1 = {_ for _ in input_[:ln]}
        s2 = {_ for _ in input_[ln:]}
        ch = (s1 & s2).pop()
        ans += _priority(ch)
    return ans


def part_2(inputs: tuple[str, ...]) -> int:
    ans = 0
    for i in range(0, len(inputs), 3):
        s1 = {_ for _ in inputs[i]}
        s2 = {_ for _ in inputs[i + 1]}
        s3 = {_ for _ in inputs[i + 2]}
        ch = (s1 & s2 & s3).pop()
        ans += _priority(ch)
    return ans


TEST = """\
vJrwpWtwJgWrhcsFMMfFFhFp
jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
PmmdzqPrVvPwwTWBwg
wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
ttgJtRGJQctTZtZT
CrZsJsPPZsGzwwsLwLmpwMDw
""".splitlines()


@aoc_main(2022, 3, part_1, part_2)
def main() -> None:
    assert part_1(TEST) == 157  # type:ignore[arg-type]
    assert part_2(TEST) == 70  # type:ignore[arg-type]


if __name__ == "__main__":
    main()
