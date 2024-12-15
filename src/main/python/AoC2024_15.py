#! /usr/bin/env python3
#
# Advent of Code 2024 Day 15
#

import sys

from aoc import my_aocd
from aoc.common import InputData
from aoc.common import SolutionBase
from aoc.common import aoc_samples
from aoc.geometry import Direction
from aoc.grid import Cell
from aoc.grid import CharGrid

Input = tuple[CharGrid, list[Direction]]
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
    def parse_input(self, input_data: InputData) -> Input:
        blocks = my_aocd.to_blocks(input_data)
        grid = CharGrid.from_strings(blocks[0])
        dirs = list[Direction]()
        for line in blocks[1]:
            dirs.extend([Direction.from_str(ch) for ch in line])
        return grid, dirs

    def get_gps(self, grid: CharGrid) -> int:
        return sum(
            cell.row * 100 + cell.col
            for cell in grid.get_cells()
            if grid.get_value(cell) in {"O", "["}
        )

    def part_1(self, input: Input) -> Output1:
        def move(line: list[str], r: int) -> list[str]:
            def swap(line: list[str], idx1: int, idx2: int) -> list[str]:
                tmp = line[idx1]
                line[idx1] = line[idx2]
                line[idx2] = tmp
                return line

            assert line[r] == "@"
            if line[r + 1] == "#":
                return line
            # max_idx = len(line) - 2
            # #....@.O....#  -> #.....@O....#
            if line[r + 1] == ".":
                line = swap(line, r, r + 1)
            # #.....@O....#  -> #......@O...#
            try:
                dot_idx = line.index(".", r)
                octo_idx = line.index("#", r)
                if dot_idx < octo_idx:
                    line.pop(dot_idx)
                    line.insert(r, ".")
            except ValueError:
                return line
            return line

        def to_array(s: str) -> list[str]:
            return [ch for ch in s]

        def to_str(a: list[str]) -> str:
            return "".join(a)

        assert to_str(move(to_array("#..@#"), 3)) == "#..@#"
        assert (
            to_str(move(to_array("#....@.O....#"), 5)) == "#.....@O....#"
        )  # noqa E501
        assert (
            to_str(move(to_array("#.....@O....#"), 6)) == "#......@O...#"
        )  # noqa E501
        assert to_str(move(to_array("#..@O#"), 3)) == "#..@O#"  # noqa E501
        assert to_str(move(to_array("#.@O.O.#"), 2)) == "#..@OO.#"  # noqa E501
        assert to_str(move(to_array("#.#@O..#"), 3)) == "#.#.@O.#"  # noqa E501
        assert to_str(move(to_array("#.@O#..#"), 2)) == "#.@O#..#"  # noqa E501
        grid, dirs = input
        grid = CharGrid.from_strings(["".join(row) for row in grid.values])
        robot = next(grid.get_all_equal_to("@"))
        for dir in dirs:
            match dir:
                case Direction.RIGHT:
                    row = grid.values[robot.row]
                    tmp = move(row, robot.col)
                    grid.values[robot.row] = tmp
                    robot = Cell(robot.row, tmp.index("@"))
                case Direction.LEFT:
                    row = grid.values[robot.row]
                    row.reverse()
                    tmp = move(row, row.index("@"))
                    tmp.reverse()
                    grid.values[robot.row] = tmp
                    robot = Cell(robot.row, tmp.index("@"))
                case Direction.DOWN:
                    col = grid.get_col(robot.col)
                    tmp = move(col, robot.row)
                    grid.replace_col(robot.col, tmp)
                    robot = Cell(tmp.index("@"), robot.col)
                case Direction.UP:
                    col = grid.get_col(robot.col)
                    col.reverse()
                    tmp = move(col, col.index("@"))
                    tmp.reverse()
                    grid.replace_col(robot.col, tmp)
                    robot = Cell(tmp.index("@"), robot.col)
        return self.get_gps(grid)

    def part_2(self, input: Input) -> Output2:
        grid_in, dirs = input
        grid = []
        for row in grid_in.get_rows_as_strings():
            line = list[str]()
            for ch in row:
                match ch:
                    case "#":
                        line.extend(["#", "#"])
                    case "O":
                        line.extend(["[", "]"])
                    case ".":
                        line.extend([".", "."])
                    case "@":
                        line.extend(["@", "."])
            grid.append(line)
        for r in range(len(grid)):
            for c in range(len(grid[r])):
                if grid[r][c] == "@":
                    robot = Cell(r, c)
                    break
            else:
                continue
            break
        for dir in dirs:
            to_move = [robot]
            for cell in to_move:
                nxt = cell.at(dir)
                if nxt in to_move:
                    continue
                match grid[nxt.row][nxt.col]:
                    case "#":
                        to_move = []
                        break
                    case "[":
                        to_move.append(nxt)
                        to_move.append(nxt.at(Direction.RIGHT))
                    case "]":
                        to_move.append(nxt)
                        to_move.append(nxt.at(Direction.LEFT))
            if len(to_move) == 0:
                continue
            to_move.pop(0)
            vals = [list(row) for row in grid]
            grid[robot.row][robot.col] = "."
            nxt_robot = robot.at(dir)
            grid[nxt_robot.row][nxt_robot.col] = "@"
            robot = nxt_robot
            for cell in to_move:
                grid[cell.row][cell.col] = "."
            for cell in to_move:
                nxt = cell.at(dir)
                grid[nxt.row][nxt.col] = vals[cell.row][cell.col]
        tmp = CharGrid.from_strings(["".join(row) for row in grid])
        return self.get_gps(tmp)

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
