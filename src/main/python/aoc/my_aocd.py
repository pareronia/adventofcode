""" Some aocd util methods """
import os
import aocd
from typing import Any


def to_blocks(inputs: tuple[str]) -> list[list[str]]:
    blocks = list[list[str]]()
    idx = 0
    blocks.append([])
    for input_ in inputs:
        if len(input_) == 0:
            blocks.append([])
            idx += 1
        else:
            blocks[idx].append(input_)
    return blocks


def check_input_data(inputs: list[str], expected: int | None = None) -> None:
    if expected is not None and len(inputs) != expected:
        raise AssertionError(f"Expected {expected} lines")


def get_input_data(
    puzzle: aocd.models.Puzzle, expected: int | None = None
) -> tuple[str, ...]:
    inputs: list[str] = puzzle.input_data.splitlines()
    check_input_data(inputs, expected)
    return tuple(inputs)


def get_input(
    year: int, day: int, expected: int | None = None
) -> tuple[str, ...]:
    inputs: list[str] = aocd.get_data(year=year, day=day).splitlines()
    check_input_data(inputs, expected)
    return tuple(inputs)


def print_header(year: int, day: int) -> None:
    s = (
        "AoC {year} Day {day}"
        " - https://adventofcode.com/{year}/day/{day}".format(
            year=year, day=day
        )
    )
    print("=" * len(s))
    print(s)
    print("=" * len(s))
    puzzle = aocd.models.Puzzle(year, day)
    print(puzzle.title)
    print("=" * len(s))
    print("")


def check_results(
    puzzle: aocd.models.Puzzle, result1: Any, result2: Any
) -> None:
    try:
        fail_a, fail_b = "", ""
        if puzzle.answered_a and result1 is not None:
            expected_a = puzzle.answer_a
            if expected_a != str(result1):
                fail_a = f"Part 1: Expected: '{expected_a}', got: '{result1}'"
        if puzzle.answered_b and result2 is not None:
            expected_b = puzzle.answer_b
            if expected_b != str(result2):
                fail_b = f"Part 2: Expected: '{expected_b}', got: '{result2}'"
        message = os.linesep.join([fail_a, fail_b])
        if message.strip() != "":
            raise ValueError(os.linesep + message)
    except aocd.exceptions.PuzzleLockedError:
        pass
