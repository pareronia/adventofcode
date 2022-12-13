#! /usr/bin/env python3
#
# Advent of Code 2022 Day 13
#


import aocd
from aoc import my_aocd
from aoc.common import log


def _has_list(r: list) -> bool:
    try:
        return sum(1 for _ in r if type(_) == list) > 0
    except TypeError:
        return False


def _compare(r1, r2):
    # breakpoint()
    log(f">Compare {r1} vs {r2}")
    # size1 = len(r1)
    # size2 = len(r2)
    # if size1 < size2:
    #     log("Right side ran out of numbers")
    #     return False
    # for i in range(size2):
    #     if type(r1[i]) != list:
    #         if type(r2[i]) == list:
    #             log("Mixed types; convert left to list and retry comparison")
    #             if not _compare([r1[i]], r2[i]):
    #                 return False
    #     elif type(r2[i]) != list:
    #         log("Mixed types; convert right to list and retry comparison")
    #         if not _compare(r1[i], [r2[i]]):
    #             return False
    #     else:
    #         log(f"Compare lists {r1[i]} vs {r2[i]}")
    #         if not _compare(r1[i], r2[i]):
    #             return False
    if type(r1) != list and type(r2) != list:
        if r1 > r2:
            return False
    elif type(r1) == list and type(r2) != list:
        log("Mixed types; convert right to list and retry comparison")
        if not _compare(r1, [r2]):
            return False
    elif type(r1) != list and type(r2) == list:
        log("Mixed types; convert left to list and retry comparison")
        if not _compare([r1], r2):
            return False
    else:
        log(f"Compare lists {r1} vs {r2}")
        size1 = len(r1)
        size2 = len(r2)
        for i in range(max(size1, size2)):
            if i == size1 and i <= size2:
                log("Left side ran out of numbers")
                return True
            elif i == size2 and i <= size1:
                log("Right side ran out of numbers")
                return False
            else:
                log(f"Compare {r1[i]} vs {r2[i]}")
                if not _compare(r1[i], r2[i]):
                    return False
    return True


def part_1(inputs: tuple[str]) -> int:
    ans = 0
    for i, block in enumerate(my_aocd.to_blocks(inputs)):
        r1 = eval(block[0])  # nosec
        log(r1)
        r2 = eval(block[1])  # nosec
        log(r2)
        if _compare(r1, r2):
            log(True)
            ans += (i + 1)
        else:
            log(False)
    return ans


def part_2(inputs: tuple[str]) -> int:
    return 0


TEST1 = """\
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
TEST2 = """\
[[1],[2,3,4]]
[[1],4]
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2022, 13)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST1) == 13
    assert part_1(TEST2) == 0
    # assert part_2(TEST1) == 0
    return

    inputs = my_aocd.get_input_data(puzzle, 449)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == "__main__":
    main()
