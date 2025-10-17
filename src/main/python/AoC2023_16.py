#! /usr/bin/env python3
#
# Advent of Code 2023 Day 16
#

import itertools
import sys
from collections.abc import Iterator
from typing import NamedTuple

from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.geometry import Direction
from aoc.geometry import Turn
from aoc.graph import flood_fill
from aoc.grid import Cell
from aoc.grid import CharGrid

TEST = """\
.|...\\....
|.-.\\.....
.....|-...
........|.
..........
.........\\
..../.\\\\..
.-.-/..|..
.|....-|.\\
..//.|....
"""


class Beam(NamedTuple):
    cell: Cell
    dir: Direction


class Contraption(NamedTuple):
    grid: CharGrid

    def _get_energised(self, initial_beam: Beam) -> int:
        def adjacent(beam: Beam) -> Iterator[Beam]:
            val = self.grid.get_value(beam.cell)
            if (
                val == "."
                or (val == "|" and beam.dir.is_vertical)
                or (val == "-" and beam.dir.is_horizontal)
            ):
                yield Beam(beam.cell.at(beam.dir), beam.dir)
            elif val == "/" and beam.dir.is_horizontal:
                new_dir = beam.dir.turn(Turn.LEFT)
                yield Beam(beam.cell.at(new_dir), new_dir)
            elif (val == "/" and beam.dir.is_vertical) or (
                val == "\\" and beam.dir.is_horizontal
            ):
                new_dir = beam.dir.turn(Turn.RIGHT)
                yield Beam(beam.cell.at(new_dir), new_dir)
            elif val == "\\" and beam.dir.is_vertical:
                new_dir = beam.dir.turn(Turn.LEFT)
                yield Beam(beam.cell.at(new_dir), new_dir)
            elif val == "|" and beam.dir.is_horizontal:
                yield Beam(beam.cell.at(Direction.UP), Direction.UP)
                yield Beam(beam.cell.at(Direction.DOWN), Direction.DOWN)
            elif val == "-" and beam.dir.is_vertical:
                yield Beam(beam.cell.at(Direction.LEFT), Direction.LEFT)
                yield Beam(beam.cell.at(Direction.RIGHT), Direction.RIGHT)
            else:
                raise AssertionError

        energised = flood_fill(
            initial_beam,
            lambda beam: (
                b for b in adjacent(beam) if self.grid.is_in_bounds(b.cell)
            ),
        )
        return len({beam.cell for beam in energised})

    def get_initial_energy(self) -> int:
        return self._get_energised(Beam(Cell(0, 0), Direction.RIGHT))

    def get_maximal_energy(self) -> int:
        return max(
            self._get_energised(beam)
            for beam in itertools.chain(
                (
                    Beam(Cell(row, 0), Direction.RIGHT)
                    for row in range(self.grid.get_height())
                ),
                (
                    Beam(Cell(row, self.grid.get_width() - 1), Direction.LEFT)
                    for row in range(self.grid.get_height())
                ),
                (
                    Beam(Cell(0, col), Direction.DOWN)
                    for col in range(self.grid.get_width())
                ),
                (
                    Beam(Cell(self.grid.get_height() - 1, col), Direction.UP)
                    for col in range(self.grid.get_width())
                ),
            )
        )


Input = Contraption
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        return Contraption(CharGrid.from_strings(list(input_data)))

    def part_1(self, contraption: Input) -> Output1:
        return contraption.get_initial_energy()

    def part_2(self, contraption: Input) -> Output2:
        return contraption.get_maximal_energy()

    @aoc_samples(
        (
            ("part_1", TEST, 46),
            ("part_2", TEST, 51),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2023, 16)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
