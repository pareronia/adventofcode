#! /usr/bin/env python3
#
# Advent of Code 2022 Day 10
#


from typing import Iterator

import advent_of_code_ocr as ocr
from aoc.common import aoc_main
from aoc.common import log

FILL = "▒"
EMPTY = " "


def _run_program(inputs: tuple[str, ...]) -> Iterator[tuple[int, int]]:
    x = 1
    cycles = 0
    for line in inputs:
        splits = line.split()
        if splits[0] == "noop":
            yield cycles, x
            cycles += 1
        elif splits[0] == "addx":
            yield cycles, x
            cycles += 1
            yield cycles, x
            cycles += 1
            x += int(splits[1])


def _check(cycles: int, x: int) -> int:
    return x * cycles if cycles % 40 == 20 else 0


def _draw(cycles: int, x: int) -> str:
    return FILL if cycles % 40 in set(range(x - 1, x + 2)) else EMPTY


def _get_pixels(inputs: tuple[str, ...]) -> list[str]:
    pixels = "".join(_draw(cycles, x) for cycles, x in _run_program(inputs))
    return [pixels[i * 40 : i * 40 + 40] for i in range(6)]  # noqa


def part_1(inputs: tuple[str, ...]) -> int:
    return sum(_check(cycles + 1, x) for cycles, x in _run_program(inputs))


def part_2(inputs: tuple[str, ...]) -> str:
    pixels = _get_pixels(inputs)
    log(pixels)
    return str(
        ocr.convert_6("\n".join(pixels), fill_pixel=FILL, empty_pixel=EMPTY)
    )


TEST = """\
addx 15
addx -11
addx 6
addx -3
addx 5
addx -1
addx -8
addx 13
addx 4
noop
addx -1
addx 5
addx -1
addx 5
addx -1
addx 5
addx -1
addx 5
addx -1
addx -35
addx 1
addx 24
addx -19
addx 1
addx 16
addx -11
noop
noop
addx 21
addx -15
noop
noop
addx -3
addx 9
addx 1
addx -3
addx 8
addx 1
addx 5
noop
noop
noop
noop
noop
addx -36
noop
addx 1
addx 7
noop
noop
noop
addx 2
addx 6
noop
noop
noop
noop
noop
addx 1
noop
noop
addx 7
addx 1
noop
addx -13
addx 13
addx 7
noop
addx 1
addx -33
noop
noop
noop
addx 2
noop
noop
noop
addx 8
noop
addx -1
addx 2
addx 1
noop
addx 17
addx -9
addx 1
addx 1
addx -3
addx 11
noop
noop
addx 1
noop
addx 1
noop
noop
addx -13
addx -19
addx 1
addx 3
addx 26
addx -30
addx 12
addx -1
addx 3
addx 1
noop
noop
noop
addx -9
addx 18
addx 1
addx 2
noop
noop
addx 9
noop
noop
noop
addx -1
addx 2
addx -37
addx 1
addx 3
noop
addx 15
addx -21
addx 22
addx -6
addx 1
noop
addx 2
addx 1
noop
addx -10
noop
noop
addx 20
addx 1
addx 2
addx 2
addx -6
addx -11
noop
noop
noop
""".splitlines()


@aoc_main(2022, 10, part_1, part_2)
def main() -> None:
    assert part_1(TEST) == 13_140  # type:ignore[arg-type]
    assert _get_pixels(TEST) == [  # type:ignore[arg-type]
        "▒▒  ▒▒  ▒▒  ▒▒  ▒▒  ▒▒  ▒▒  ▒▒  ▒▒  ▒▒  ",
        "▒▒▒   ▒▒▒   ▒▒▒   ▒▒▒   ▒▒▒   ▒▒▒   ▒▒▒ ",
        "▒▒▒▒    ▒▒▒▒    ▒▒▒▒    ▒▒▒▒    ▒▒▒▒    ",
        "▒▒▒▒▒     ▒▒▒▒▒     ▒▒▒▒▒     ▒▒▒▒▒     ",
        "▒▒▒▒▒▒      ▒▒▒▒▒▒      ▒▒▒▒▒▒      ▒▒▒▒",
        "▒▒▒▒▒▒▒       ▒▒▒▒▒▒▒       ▒▒▒▒▒▒▒     ",
    ]


if __name__ == "__main__":
    main()
