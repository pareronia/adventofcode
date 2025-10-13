#![allow(non_snake_case)]

use aoc::geometry::Direction;
use aoc::grid::{Cell, CharGrid, Grid};
use aoc::Puzzle;
use std::collections::{HashMap, VecDeque};
use std::str::FromStr;

enum WarehouseType {
    Type1,
    Type2,
}

struct Warehouse {
    r#type: WarehouseType,
    grid: CharGrid,
}

impl Warehouse {
    fn new(r#type: WarehouseType, lines: &[String]) -> Self {
        let grid = match r#type {
            WarehouseType::Type1 => CharGrid::from(
                &lines.iter().map(AsRef::as_ref).collect::<Vec<_>>(),
            ),
            WarehouseType::Type2 => {
                let mut grid: Vec<String> = vec![];
                for line in lines {
                    let mut row = String::new();
                    line.chars().for_each(|ch| match ch {
                        '.' => row.push_str(".."),
                        'O' => row.push_str("[]"),
                        '@' => row.push_str("@."),
                        '#' => row.push_str("##"),
                        _ => panic!(),
                    });
                    grid.push(row);
                }
                CharGrid::from(
                    &grid.iter().map(AsRef::as_ref).collect::<Vec<_>>(),
                )
            }
        };
        Self { r#type, grid }
    }

    fn get_to_move(&self, robot: Cell, dir: &Direction) -> Vec<Cell> {
        let mut to_move = vec![robot];
        let mut q: VecDeque<Cell> = VecDeque::from(vec![robot]);
        while let Some(cell) = q.pop_front() {
            let nxt = cell.try_at(*dir).unwrap();
            if to_move.contains(&nxt) {
                continue;
            }
            let nxt_val = self.grid.get(&nxt);
            if nxt_val == '#' {
                return vec![];
            }
            match self.r#type {
                WarehouseType::Type1 => {
                    if nxt_val != 'O' {
                        continue;
                    }
                }
                WarehouseType::Type2 => match nxt_val {
                    '[' => {
                        let right = nxt.try_at(Direction::Right).unwrap();
                        to_move.push(right);
                        q.push_back(right);
                    }
                    ']' => {
                        let left = nxt.try_at(Direction::Left).unwrap();
                        to_move.push(left);
                        q.push_back(left);
                    }
                    _ => continue,
                },
            }
            to_move.push(nxt);
            q.push_back(nxt);
        }
        to_move
    }

    #[inline]
    fn get_box(&self) -> char {
        match self.r#type {
            WarehouseType::Type1 => 'O',
            WarehouseType::Type2 => '[',
        }
    }
}

struct AoC2024_15;

impl AoC2024_15 {
    fn solve(&self, warehouse: &mut Warehouse, dirs: &Vec<Direction>) -> usize {
        let mut robot =
            warehouse.grid.find_first_matching(|ch| ch == '@').unwrap();
        for dir in dirs {
            let to_move = warehouse.get_to_move(robot, dir);
            if to_move.is_empty() {
                continue;
            }
            let vals: HashMap<(usize, usize), char> = to_move
                .iter()
                .map(|cell| ((cell.row, cell.col), warehouse.grid.get(cell)))
                .collect();
            robot = robot.try_at(*dir).unwrap();
            for cell in to_move.iter() {
                warehouse.grid.get_data_mut()[cell.row][cell.col] = '.';
            }
            for cell in to_move.iter() {
                let nxt = cell.try_at(*dir).unwrap();
                warehouse.grid.get_data_mut()[nxt.row][nxt.col] =
                    *(vals.get(&(cell.row, cell.col)).unwrap());
            }
        }
        warehouse
            .grid
            .cells()
            .filter(|cell| warehouse.grid.get(cell) == warehouse.get_box())
            .map(|cell| cell.row * 100 + cell.col)
            .sum()
    }
}

impl aoc::Puzzle for AoC2024_15 {
    type Input = (Vec<String>, Vec<Direction>);
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2024, 15);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        let mut dirs: Vec<Direction> = vec![];
        let mut blocks = lines.split(|line| line.is_empty());
        let grid = blocks.next().unwrap().to_vec();
        blocks.next().unwrap().iter().for_each(|line| {
            line.chars()
                .map(|ch| Direction::from_str(&ch.to_string()).unwrap())
                .for_each(|d| dirs.push(d));
        });
        (grid, dirs)
    }

    fn part_1(&self, input: &Self::Input) -> Self::Output1 {
        let (grid, dirs) = input;
        self.solve(&mut Warehouse::new(WarehouseType::Type1, grid), dirs)
    }

    fn part_2(&self, input: &Self::Input) -> Self::Output2 {
        let (grid, dirs) = input;
        self.solve(&mut Warehouse::new(WarehouseType::Type2, grid), dirs)
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST1, 2028,
            self, part_1, TEST2, 10092,
            self, part_2, TEST2, 9021
        };
    }
}

fn main() {
    AoC2024_15 {}.run(std::env::args());
}

const TEST1: &str = "\
########
#..O.O.#
##@.O..#
#...O..#
#.#.O..#
#...O..#
#......#
########

<^^>>>vv<v>>v<<
";
const TEST2: &str = "\
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
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2024_15 {}.samples();
    }
}
