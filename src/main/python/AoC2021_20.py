#! /usr/bin/env python3
#
# Advent of Code 2021 Day 20
#

from prettyprinter import register_pretty, pretty_call
from aoc import my_aocd
import aocd


DARK = '.'
LIGHT = '#'


class ImageEnhancement:
    algorithm: str
    data: set[tuple[int, int]]
    store_dark: bool

    def __init__(self, algorithm: str, data: set[tuple[int, int]]):
        assert len(algorithm) == 512
        self.algorithm = algorithm
        self.data = data
        self.store_dark = False

    def _is_flicker(self) -> bool:
        return self.algorithm[0] == LIGHT and self.algorithm[-1] == DARK

    def _row_range(self):
        rows = {row for row, _ in self.data}
        return range(min(rows) - 2, max(rows) + 3)

    def _col_range(self):
        cols = {col for _, col in self.data}
        return range(min(cols) - 2, max(cols) + 3)

    def _print(self) -> None:
        if not __debug__:
            return
        row_range = self._row_range()
        col_range = self._col_range()
        lines = ["".join(LIGHT if (row, col) in self.data else DARK
                         for col in col_range)
                 for row in row_range]
        [print(line) for line in lines]

    def _get_square_around(self, row: int, col: int) -> str:
        found, not_found = ('1', '0') \
                if not self._is_flicker() or not self.store_dark \
                else ('0', '1')
        return "".join(found
                       if (row + dr, col + dc) in self.data
                       else not_found
                       for dr in [-1, 0, 1]
                       for dc in [-1, 0, 1])

    def run(self) -> None:
        data2 = set[tuple[int, int]]()
        row_range = self._row_range()
        col_range = self._col_range()
        for row in row_range:
            for col in col_range:
                square = self._get_square_around(row, col)
                idx = int(square, 2)
                assert 0 <= idx < len(self.algorithm)
                light = self.algorithm[idx] == LIGHT
                if ((not self._is_flicker() or self.store_dark) and light) or \
                   (self._is_flicker() and not self.store_dark and not light):
                    data2.add((row, col))
        self.data = data2
        self.store_dark = not self.store_dark

    def get_lit_pixels(self) -> int:
        return len(self.data)


@register_pretty(ImageEnhancement)
def _pretty(value, ctx):
    return pretty_call(
        ctx,
        ImageEnhancement,
        algorithm=value.algorithm,
        data=value.data
    )


def _parse(inputs: tuple[str]) -> int:
    blocks = my_aocd.to_blocks(inputs)
    algorithm = blocks[0][0]
    data = {(r, c)
            for r, line in enumerate(blocks[1])
            for c, ch in enumerate(line)
            if ch == LIGHT}
    return ImageEnhancement(algorithm, data)


def _solve(ie: ImageEnhancement, cycles: int) -> int:
    for i in range(cycles):
        ie.run()
    return ie.get_lit_pixels()


def part_1(inputs: tuple[str]) -> int:
    ie = _parse(inputs)
    return _solve(ie, 2)


def part_2(inputs: tuple[str]) -> int:
    ie = _parse(inputs)
    return _solve(ie, 50)


TEST = """\
..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..###..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#..#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#......#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#.....####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.......##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#

#..#.
#....
##..#
..#..
..###
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2021, 20)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 35
    assert part_2(TEST) == 3351

    inputs = my_aocd.get_input(puzzle.year, puzzle.day, 102)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()
