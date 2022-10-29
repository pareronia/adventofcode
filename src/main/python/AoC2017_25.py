#! /usr/bin/env python3
#
# Advent of Code 2017 Day 25
#

from aoc import my_aocd
import aocd

Step = tuple[int, int, str]
State = tuple[Step, Step]


def _parse(inputs: tuple[str]) -> tuple[str, int, dict[str, State]]:
    def _get_last_word(line: str) -> str:
        return line.split()[-1][:-1]

    def _parse_step(lines: list[str]) -> tuple[int, int, str]:
        write = int(_get_last_word(lines[0]))
        move = 1 if _get_last_word(lines[1]) == "left" else -1
        goto = _get_last_word(lines[2])
        return write, move, goto

    blocks = my_aocd.to_blocks(inputs)
    start = _get_last_word(blocks[0][0])
    steps = int(blocks[0][1].split()[5])
    states = dict[str, State]()
    for i in range(1, len(blocks), 1):
        name = _get_last_word(blocks[i][0])
        step_0 = _parse_step(blocks[i][2:5])
        step_1 = _parse_step(blocks[i][6:9])
        states[name] = (step_0, step_1)
    return start, steps, states


def part_1(inputs: tuple[str]) -> int:
    start, steps, states = _parse(inputs)
    tape = dict[int, int]()
    pos = 0
    state = states[start]
    for i in range(steps):
        write, move, goto = state[tape.get(pos, 0)]
        tape[pos] = write
        pos += move
        state = states[goto]
    return sum(1 for _ in tape.values() if _ == 1)


def part_2(inputs: tuple[str]) -> None:
    return


TEST = """\
Begin in state A.
Perform a diagnostic checksum after 6 steps.

In state A:
  If the current value is 0:
    - Write the value 1.
    - Move one slot to the right.
    - Continue with state B.
  If the current value is 1:
    - Write the value 0.
    - Move one slot to the left.
    - Continue with state B.

In state B:
  If the current value is 0:
    - Write the value 1.
    - Move one slot to the left.
    - Continue with state A.
  If the current value is 1:
    - Write the value 1.
    - Move one slot to the right.
    - Continue with state A.
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2017, 25)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 3

    inputs = my_aocd.get_input(2017, 25, 62)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()
