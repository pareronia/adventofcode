#! /usr/bin/env python3
#
# Advent of Code 2022 Day 25
#


from aoc.common import aoc_main

DECODE = {"0": 0, "1": 1, "2": 2, "-": -1, "=": -2}
ENCODE = {
    0: ("0", 0),
    1: ("1", 0),
    2: ("2", 0),
    3: ("=", 1),
    4: ("-", 1),
    5: ("0", 1),
}


def part_1(inputs: tuple[str, ...]) -> str:
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


def part_2(inputs: tuple[str, ...]) -> str:
    return "🎄"


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


@aoc_main(2022, 25, part_1, part_2)
def main() -> None:
    assert part_1(TEST) == "2=-1=0"
    assert part_1(TEST1) == "1=11-2"
    assert part_1(TEST2) == "1-0---0"
    assert part_1(TEST3) == "1121-1110-1=0"


if __name__ == "__main__":
    main()
