#! /usr/bin/env python3
#
# Advent of Code 2022 Day 13
#

from functools import cmp_to_key

import aocd
from aoc import my_aocd
from aoc.common import log


def _compare(r1, r2) -> int:
    log(f">Compare {r1} vs {r2}")
    if type(r1) != list and type(r2) != list:
        if r1 == r2:
            return 0
        elif r1 < r2:
            return -1
        else:
            return 1
    elif type(r1) == list and type(r2) != list:
        log("Mixed types; convert right to list and retry comparison")
        return _compare(r1, [r2])
    elif type(r1) != list and type(r2) == list:
        log("Mixed types; convert left to list and retry comparison")
        return _compare([r1], r2)
    else:
        log(f"Compare lists {r1} vs {r2}")
        size1 = len(r1)
        size2 = len(r2)
        for i in range(size1):
            n1 = r1[i]
            try:
                n2 = r2[i]
            except IndexError:
                log("Right side ran out of items")
                return 1
            res = _compare(n1, n2)
            if res == 0:
                continue
            return res
        log("Left side ran out of items")
        return -1 if size1 < size2 else 0


def part_1(inputs: tuple[str]) -> int:
    ans = 0
    for i, block in enumerate(my_aocd.to_blocks(inputs)):
        r1 = eval(block[0])  # nosec
        log(r1)
        r2 = eval(block[1])  # nosec
        log(r2)
        if _compare(r1, r2) <= 0:
            log(True)
            ans += i + 1
        else:
            log(False)
    return ans


def part_2(inputs: tuple[str]) -> int:
    s = [eval(_) for _ in inputs if len(_) > 0]  # nosec
    s.append(eval("[[2]]"))  # nosec
    s.append(eval("[[6]]"))  # nosec
    s.sort(key=cmp_to_key(_compare))
    return (s.index([[2]]) + 1) * (s.index([[6]]) + 1)


TEST1 = tuple(
    """\
[1,1,3,1,1]
[1,1,5,1,1]

[[1],[2,3,4]]
[[1],4]

[9]
[[8,7,6]]

[[4,4],4,4]
[[4,4],4,4,4]

[7,7,7,7]
[7,7,7]

[]
[3]

[[[]]]
[[]]

[1,[2,[3,[4,[5,6,7]]]],8,9]
[1,[2,[3,[4,[5,6,0]]]],8,9]
""".splitlines()
)
TEST2 = tuple(
    """\
[[1],[2,3,4]]
[[1],4]
""".splitlines()
)


def main() -> None:
    puzzle = aocd.models.Puzzle(2022, 13)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST1) == 13
    assert part_1(TEST2) == 1
    assert part_2(TEST1) == 140

    inputs = my_aocd.get_input_data(puzzle, 449)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()
