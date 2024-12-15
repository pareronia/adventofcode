#! /usr/bin/env python3
#
# Advent of Code 2024 Day 15
#

import sys
from typing import Callable
from typing import NamedTuple

from aoc import my_aocd
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.geometry import Direction
from aoc.grid import Cell
from aoc.grid import CharGrid

TEST1 = """\
########
#..O.O.#
##@.O..#
#...O..#
#.#.O..#
#...O..#
#......#
########

<^^>>>vv<v>>v<<
"""
TEST2 = """\
##########
#..O..O.O#
#......O.#
#.OO..O.O#
#..O@..O.#
#O#..O...#
#O..O..O.#
#.OO.O.OO#
#....O...#
##########

<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
>^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
"""


class GridSupplier(NamedTuple):
    grid_in: list[str]

    def get_grid(self) -> CharGrid:
        return CharGrid.from_strings(self.grid_in)

    def get_wide_grid(self) -> CharGrid:
        grid = [
            "".join(Solution.SCALE_UP[ch] for ch in line)
            for line in self.grid_in
        ]
        return CharGrid.from_strings(grid)


Input = tuple[GridSupplier, list[Direction]]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    FLOOR, WALL, ROBOT = ".", "#", "@"
    BOX, BIG_BOX_LEFT, BIG_BOX_RIGHT = "O", "[", "]"
    SCALE_UP = {
        WALL: WALL + WALL,
        BOX: BIG_BOX_LEFT + BIG_BOX_RIGHT,
        FLOOR: FLOOR + FLOOR,
        ROBOT: ROBOT + FLOOR,
    }

    def parse_input(self, input_data: InputData) -> Input:
        blocks = my_aocd.to_blocks(input_data)
        dirs = [Direction.from_str(ch) for ch in "".join(blocks[1])]
        return GridSupplier(blocks[0]), dirs

    def solve(
        self,
        grid: CharGrid,
        dirs: list[Direction],
        get_to_move: Callable[[CharGrid, Cell, Direction], list[Cell]],
    ) -> int:
        robot = next(grid.get_all_equal_to(Solution.ROBOT))
        for dir in dirs:
            to_move = get_to_move(grid, robot, dir)
            if len(to_move) == 0:
                continue
            vals = {tm: grid.get_value(tm) for tm in to_move}
            robot = robot.at(dir)
            for cell in to_move:
                grid.set_value(cell, Solution.FLOOR)
            for cell in to_move:
                grid.set_value(cell.at(dir), vals[cell])
        return sum(
            cell.row * 100 + cell.col
            for cell in grid.find_all_matching(
                lambda cell: grid.get_value(cell)
                in {Solution.BOX, Solution.BIG_BOX_LEFT}
            )
        )

    def part_1(self, input: Input) -> Output1:
        def get_to_move(
            grid: CharGrid, robot: Cell, dir: Direction
        ) -> list[Cell]:
            to_move = [robot]
            for cell in to_move:
                nxt = cell.at(dir)
                if nxt in to_move:
                    continue
                match grid.get_value(nxt):
                    case Solution.WALL:
                        return []
                    case Solution.BOX:
                        to_move.append(nxt)
            return to_move

        grid, dirs = input
        return self.solve(grid.get_grid(), dirs, get_to_move)

    def part_2(self, input: Input) -> Output2:
        def get_to_move(
            grid: CharGrid, robot: Cell, dir: Direction
        ) -> list[Cell]:
            to_move = [robot]
            for cell in to_move:
                nxt = cell.at(dir)
                if nxt in to_move:
                    continue
                match grid.get_value(nxt):
                    case Solution.WALL:
                        return []
                    case Solution.BIG_BOX_LEFT:
                        to_move.append(nxt)
                        to_move.append(nxt.at(Direction.RIGHT))
                    case Solution.BIG_BOX_RIGHT:
                        to_move.append(nxt)
                        to_move.append(nxt.at(Direction.LEFT))
            return to_move

        grid, dirs = input
        return self.solve(grid.get_wide_grid(), dirs, get_to_move)

    @aoc_samples(
        (
            ("part_1", TEST1, 2028),
            ("part_1", TEST2, 10092),
            ("part_2", TEST2, 9021),
        )
    )
    def samples(self) -> None:
        pass


solution = Solution(2024, 15)


def main() -> None:
    solution.run(sys.argv)


if __name__ == "__main__":
    main()
