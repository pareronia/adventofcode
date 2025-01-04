#! /usr/bin/env python3
#
# Advent of Code 2024 Day 15
#

from __future__ import annotations

import sys
from enum import Enum
from enum import auto
from enum import unique
from typing import Iterator
from typing import NamedTuple

from aoc import my_aocd
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.geometry import Direction
from aoc.grid import Cell
from aoc.grid import CharGrid

FLOOR, WALL, ROBOT = ".", "#", "@"
BOX, BIG_BOX_LEFT, BIG_BOX_RIGHT = "O", "[", "]"
SCALE_UP = {
    WALL: WALL + WALL,
    BOX: BIG_BOX_LEFT + BIG_BOX_RIGHT,
    FLOOR: FLOOR + FLOOR,
    ROBOT: ROBOT + FLOOR,
}

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


@unique
class WarehouseType(Enum):
    WAREHOUSE_1 = auto()
    WAREHOUSE_2 = auto()


class Warehouse(NamedTuple):
    type: WarehouseType
    grid: CharGrid

    @classmethod
    def create(cls, type: WarehouseType, grid_in: list[str]) -> Warehouse:
        match type:
            case WarehouseType.WAREHOUSE_1:
                grid = CharGrid.from_strings(grid_in)
            case WarehouseType.WAREHOUSE_2:
                strings = [
                    "".join(SCALE_UP[ch] for ch in line) for line in grid_in
                ]
                grid = CharGrid.from_strings(strings)
        return Warehouse(type, grid)

    def get_to_move(self, robot: Cell, dir: Direction) -> list[Cell]:
        to_move = [robot]
        for cell in to_move:
            nxt = cell.at(dir)
            if nxt in to_move:
                continue
            nxt_val = self.grid.get_value(nxt)
            if nxt_val == WALL:
                return []
            match self.type:
                case WarehouseType.WAREHOUSE_1:
                    if nxt_val != BOX:
                        continue
                case WarehouseType.WAREHOUSE_2:
                    if nxt_val == BIG_BOX_LEFT:
                        to_move.append(nxt.at(Direction.RIGHT))
                    elif nxt_val == BIG_BOX_RIGHT:
                        to_move.append(nxt.at(Direction.LEFT))
                    else:
                        continue
            to_move.append(nxt)
        return to_move

    def get_boxes(self) -> Iterator[Cell]:
        match self.type:
            case WarehouseType.WAREHOUSE_1:
                ch = BOX
            case WarehouseType.WAREHOUSE_2:
                ch = BIG_BOX_LEFT
        return self.grid.get_all_equal_to(ch)


Input = tuple[list[str], list[Direction]]
Output1 = int
Output2 = int


class Solution(SolutionBase[Input, Output1, Output2]):
    def parse_input(self, input_data: InputData) -> Input:
        blocks = my_aocd.to_blocks(input_data)
        dirs = [Direction.from_str(ch) for ch in "".join(blocks[1])]
        return blocks[0], dirs

    def solve(self, warehouse: Warehouse, dirs: list[Direction]) -> int:
        robot = next(warehouse.grid.get_all_equal_to(ROBOT))
        for dir in dirs:
            to_move = warehouse.get_to_move(robot, dir)
            if len(to_move) == 0:
                continue
            vals = {tm: warehouse.grid.get_value(tm) for tm in to_move}
            robot = robot.at(dir)
            for cell in to_move:
                warehouse.grid.set_value(cell, FLOOR)
            for cell in to_move:
                warehouse.grid.set_value(cell.at(dir), vals[cell])
        return sum(cell.row * 100 + cell.col for cell in warehouse.get_boxes())

    def part_1(self, input: Input) -> Output1:
        grid, dirs = input
        return self.solve(
            Warehouse.create(WarehouseType.WAREHOUSE_1, grid), dirs
        )

    def part_2(self, input: Input) -> Output2:
        grid, dirs = input
        return self.solve(
            Warehouse.create(WarehouseType.WAREHOUSE_2, grid), dirs
        )

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
