#! /usr/bin/env python3
#
# Advent of Code 2022 Day 25
#


import aocd
from aoc import my_aocd

DECODE = {"0": 0, "1": 1, "2": 2, "-": -1, "=": -2}
ENCODE = {
    0: ("0", 0),
    1: ("1", 0),
    2: ("2", 0),
    3: ("=", 1),
    4: ("-", 1),
    5: ("0", 1),
}


def part_1(inputs: tuple[str]) -> str:
    total = sum(
        sum(DECODE[digit] * 5**i for i, digit in enumerate(line[::-1]))
        for line in inputs
    )
    ans = ""
    while total:
        digit, carry = ENCODE[total % 5]
        ans += digit
        total = total // 5 + carry
    return ans[::-1]


def part_2(inputs: tuple[str]) -> None:
    return


TEST = tuple(
    """\
1=-0-2
12111
2=0=
21
2=01
111
20012
112
1=-1=
1-12
12
1=
122
""".splitlines()
)
TEST1 = tuple(["1=11-2"])
TEST2 = tuple(["1-0---0"])
TEST3 = tuple(["1121-1110-1=0"])


def main() -> None:
    puzzle = aocd.models.Puzzle(2022, 25)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == "2=-1=0"
    assert part_1(TEST1) == "1=11-2"
    assert part_1(TEST2) == "1-0---0"
    assert part_1(TEST3) == "1121-1110-1=0"
    assert part_2(TEST) is None

    inputs = my_aocd.get_input_data(puzzle, 119)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()
