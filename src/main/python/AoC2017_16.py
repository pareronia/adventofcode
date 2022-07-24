#! /usr/bin/env python3
#
# Advent of Code 2017 Day 16
#
from typing import NamedTuple
from string import ascii_lowercase
from aoc import my_aocd
import aocd


PROGRAMS = ascii_lowercase[:16]


class Spin(NamedTuple):
    amount: int


class Exchange(NamedTuple):
    pos_1: int
    pos_2: int


class Partner(NamedTuple):
    program_1: str
    program_2: str


def _parse_move(input_: str):
    if input_.startswith('s'):
        return Spin(int(input_[1:]))
    elif input_.startswith('x'):
        a = int(input_[1:].split('/')[0])
        b = int(input_[1:].split('/')[1])
        return Exchange(a, b)
    elif input_.startswith('p'):
        a = input_[1:].split('/')[0][0]
        b = input_[1:].split('/')[1][0]
        return Partner(a, b)
    else:
        raise ValueError()


def _parse(inputs: tuple[str]) -> list:
    return [_parse_move(_) for _ in inputs[0].split(",")]


def _from_string(string: str) -> dict:
    return {v: i for i, v in enumerate(string)}


def _to_string(d: dict) -> str:
    d2 = dict(sorted(d.items(), key=lambda x: x[1]))
    return "".join([_ for _ in d2])


def _swap(d: dict, prog_1: str, prog_2: str) -> None:
    tmp = d[prog_1]
    d[prog_1] = d[prog_2]
    d[prog_2] = tmp


def _dance(moves: list, d: dict) -> dict:
    for move in moves:
        if isinstance(move, Spin):
            for k in d:
                d[k] = (d[k] + move.amount) % len(d)
        elif isinstance(move, Exchange):
            for k in d:
                if d[k] == move.pos_1:
                    program_1 = k
                elif d[k] == move.pos_2:
                    program_2 = k
            _swap(d, program_1, program_2)
        elif isinstance(move, Partner):
            _swap(d, move.program_1, move.program_2)
    return d


def _solve(moves: list, string: str, reps: int):
    d = _from_string(string)
    for i in range(reps):
        d = _dance(moves, d)
    return _to_string(d)


def part_1(inputs: tuple[str]) -> int:
    moves = _parse(inputs)
    return _solve(moves, PROGRAMS, 1)


def part_2(inputs: tuple[str]) -> int:
    moves = _parse(inputs)
    cnt = 0
    ans = PROGRAMS
    while True:
        ans = _solve(moves, ans, 1)
        cnt += 1
        if ans == PROGRAMS:
            break
    return _solve(moves, PROGRAMS, 1_000_000_000 % cnt)


TEST = """\
s1,x3/4,pe/b
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2017, 16)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert _solve(_parse(TEST), "abcde", 1) == "baedc"
    assert _solve(_parse(TEST), "abcde", 2) == "ceadb"

    inputs = my_aocd.get_input(2017, 16, 1)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()
