#! /usr/bin/env python3
#
# Advent of Code 2021 Day 21
#

import itertools
from collections import Counter
from functools import lru_cache
from aoc import my_aocd
import aocd
from aoc.common import clog, log


ROLLS = Counter(sum(_)
                for _ in itertools.product((1, 2, 3), (1, 2, 3), (1, 2, 3)))


def _parse(inputs: tuple[str]) -> int:
    assert len(inputs) == 2
    return int(inputs[0][-1]), int(inputs[1][-1])


def part_1(inputs: tuple[str]) -> int:
    pos = list(map(lambda x: x - 1, _parse(inputs)))
    score = [0, 0]
    die = 0
    while True:
        for p in [0, 1]:
            throws = (die + 1, die + 2, die + 3)
            die += 3
            pos[p] = (pos[p] + sum(throws)) % 10
            score[p] += pos[p] + 1
            clog(lambda: f"Player {p + 1} rolls " + "+".join(map(str, throws))
                 + f" and moves to space {str(pos[p] + 1)}"
                 + f" for a total score of {str(score[p])}")
            if score[p] >= 1000:
                return score[(p + 1) % 2] * die


@lru_cache(maxsize=None)
def _solve2(pos1: int, pos2: int, score1: int, score2: int) -> tuple[int, int]:
    wins1, wins2 = 0, 0
    for t, cnt in ROLLS.items():
        npos = (pos1 + t) % 10
        nscore = score1 + npos + 1
        if (nscore >= 21):
            wins1 += cnt
        else:
            nwins2, nwins1 = map(lambda x: cnt * x,
                                 _solve2(pos2, npos, score2, nscore))
            wins1 += nwins1
            wins2 += nwins2
    return wins1, wins2


def part_2(inputs: tuple[str]) -> int:
    pos1, pos2 = map(lambda x: x - 1, _parse(inputs))
    ans = _solve2(pos1, pos2, score1=0, score2=0)
    log(ans)
    return max(ans)


TEST = """\
Player 1 starting position: 4
Player 2 starting position: 8
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2021, 21)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 739785
    assert part_2(TEST) == 444356092776315

    inputs = my_aocd.get_input(puzzle.year, puzzle.day, 2)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()
