#! /usr/bin/env python3
#
# Advent of Code 2022 Day 25
#


import aocd
from aoc import my_aocd
from aoc.common import log


def part_1(inputs: tuple[str]) -> int:
    total = 0
    for line in inputs:
        log(line)
        subtotal = 0
        for i, digit in enumerate(line[::-1]):
            log(digit)
            if digit in {"0", "1", "2"}:
                subtotal += int(digit) * 5 ** i
            elif digit == "-":
                subtotal -= 5 ** i
            elif digit == "=":
                subtotal -= 2 * 5 ** i
        log(f" -> {subtotal}")
        total += subtotal
    log(f"total: {total}")
    if total == 0:
        return "0"
    ans = []
    divs = []
    while total:
        log(total)
        digit = int(total % 5)
        log(digit)
        divs.append(digit)
        total //= 5
    log(f"divs: {divs}")
    carry = None
    for div in divs:
        if carry is not None:
            div += carry
        if div in {0, 1, 2}:
            ans.insert(0, str(div))
            carry = None
        elif div == 5:
            ans.insert(0, "0")
            carry = 1
        elif div == 4:
            ans.insert(0, "-")
            carry = 1
        elif div == 3:
            ans.insert(0, "=")
            carry = 1
    log(f"ans: {ans}")
    return "".join(ans)


def part_2(inputs: tuple[str]) -> int:
    return None


TEST = """\
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


def main() -> None:
    puzzle = aocd.models.Puzzle(2022, 25)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == "2=-1=0"
    assert part_2(TEST) is None

    inputs = my_aocd.get_input_data(puzzle, 119)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()
