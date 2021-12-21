#! /usr/bin/env python3
#
# Advent of Code 2021 Day 21
#

from aoc import my_aocd
import aocd
from aoc.common import clog


def _parse(inputs: tuple[str]) -> int:
    assert len(inputs) == 2
    return int(inputs[0][-1]), int(inputs[1][-1])


def part_1(inputs: tuple[str]) -> int:
    start1, start2 = _parse(inputs)
    pos1 = start1 - 1
    pos2 = start2 - 1
    score1 = score2 = 0
    die = 0
    cnt = 0
    while True:
        throws = (die + 1, die + 2, die + 3)
        pos1 = (pos1 + sum(throws)) % 10
        score1 += pos1 + 1
        clog(lambda: "Player 1 rolls " + "+".join(map(str, throws)) +
             f" and moves to space {str(pos1 + 1)}" +
             f" for a total score of {str(score1)}")
        die += 3
        cnt += 3
        if score1 >= 1000:
            break
        throws = (die + 1, die + 2, die + 3)
        pos2 = (pos2 + sum(throws)) % 10
        score2 += pos2 + 1
        clog(lambda: "Player 2 rolls " + "+".join(map(str, throws)) +
             f" and moves to space {str(pos2 + 1)}" +
             f" for a total score of {str(score2)}")
        die += 3
        cnt += 3
        if score2 >= 1000:
            break
    if score1 < score2:
        return score1 * cnt
    else:
        return score2 * cnt


def part_2(inputs: tuple[str]) -> int:
    return None


TEST = """\
Player 1 starting position: 4
Player 2 starting position: 8
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2021, 21)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 739785
    assert part_2(TEST) == 0

    inputs = my_aocd.get_input(puzzle.year, puzzle.day, 2)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()
