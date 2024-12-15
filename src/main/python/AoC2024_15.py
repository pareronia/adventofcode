#! /usr/bin/env python3
#
# Advent of Code 2024 Day 15
#

import sys
from typing import Callable

from aoc import my_aocd
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.geometry import Direction
from aoc.grid import Cell

Grid = list[list[str]]
Input = tuple[Grid, list[Direction]]
Output1 = int
Output2 = int


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


class Solution(SolutionBase[Input, Output1, Output2]):
    FLOOR, WALL, ROBOT = ".", "#", "@"
    BOX, BIG_BOX_LEFT, BIG_BOX_RIGHT = "O", "[", "]"
    SCALE_UP = {
        WALL: [WALL, WALL],
        BOX: [BIG_BOX_LEFT, BIG_BOX_RIGHT],
        FLOOR: [FLOOR, FLOOR],
        ROBOT: [ROBOT, FLOOR],
    }

    def parse_input(self, input_data: InputData) -> Input:
        blocks = my_aocd.to_blocks(input_data)
        grid = [list(line) for line in blocks[0]]
        dirs = [Direction.from_str(ch) for ch in "".join(blocks[1])]
        return grid, dirs

    def solve(
        self,
        grid: Grid,
        robot: Cell,
        dirs: list[Direction],
        get_to_move: Callable[[Grid, Cell, Direction], list[Cell]],
    ) -> int:
        for dir in dirs:
            to_move = get_to_move(grid, robot, dir)
            if len(to_move) == 0:
                continue
            to_move.pop(0)
            vals = [list(row) for row in grid]
            grid[robot.row][robot.col] = Solution.FLOOR
            nxt_robot = robot.at(dir)
            grid[nxt_robot.row][nxt_robot.col] = Solution.ROBOT
            robot = nxt_robot
            for cell in to_move:
                grid[cell.row][cell.col] = Solution.FLOOR
            for cell in to_move:
                nxt = cell.at(dir)
                grid[nxt.row][nxt.col] = vals[cell.row][cell.col]
        return sum(
            r * 100 + c
            for r in range(len(grid))
            for c in range(len(grid[r]))
            if grid[r][c] in {Solution.BOX, Solution.BIG_BOX_LEFT}
        )

    def part_1(self, input: Input) -> Output1:
        def get_to_move(grid: Grid, robot: Cell, dir: Direction) -> list[Cell]:
            to_move = [robot]
            for cell in to_move:
                nxt = cell.at(dir)
                if nxt in to_move:
                    continue
                match grid[nxt.row][nxt.col]:
                    case Solution.WALL:
                        return []
                    case Solution.BOX:
                        to_move.append(nxt)
            return to_move

        grid_in, dirs = input
        grid = [list(row) for row in grid_in]
        for r in range(len(grid)):
            for c in range(len(grid[r])):
                if grid[r][c] == Solution.ROBOT:
                    robot = Cell(r, c)
                    break
            else:
                continue
            break
        return self.solve(grid, robot, dirs, get_to_move)

    def part_2(self, input: Input) -> Output2:
        def get_to_move(grid: Grid, robot: Cell, dir: Direction) -> list[Cell]:
            to_move = [robot]
            for cell in to_move:
                nxt = cell.at(dir)
                if nxt in to_move:
                    continue
                match grid[nxt.row][nxt.col]:
                    case Solution.WALL:
                        return []
                    case Solution.BIG_BOX_LEFT:
                        to_move.append(nxt)
                        to_move.append(nxt.at(Direction.RIGHT))
                    case Solution.BIG_BOX_RIGHT:
                        to_move.append(nxt)
                        to_move.append(nxt.at(Direction.LEFT))
            return to_move

        grid_in, dirs = input
        grid = []
        for r, row in enumerate(grid_in):
            line = []
            for c, ch in enumerate(row):
                if ch == Solution.ROBOT:
                    robot = Cell(r, 2 * c)
                line.extend(Solution.SCALE_UP[ch])
            grid.append(line)
        return self.solve(grid, robot, dirs, get_to_move)

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
