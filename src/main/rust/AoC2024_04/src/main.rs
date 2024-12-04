#![allow(non_snake_case)]

use aoc::geometry::Direction;
use aoc::grid::{CharGrid, Grid};
use aoc::Puzzle;
use std::collections::HashSet;

struct AoC2024_04;

impl AoC2024_04 {}

impl aoc::Puzzle for AoC2024_04 {
    type Input = CharGrid;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2024, 4);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        CharGrid::from(&lines.iter().map(AsRef::as_ref).collect::<Vec<_>>())
    }

    fn part_1(&self, grid: &Self::Input) -> Self::Output1 {
        grid.cells()
            .filter(|cell| grid.get(cell) == 'X')
            .map(|cell| {
                Direction::octants()
                    .iter()
                    .filter(|dir| {
                        let mut it = grid.cells_direction(&cell, **dir);
                        ['M', 'A', 'S'].iter().all(|ch| match it.next() {
                            Some(n) => grid.get(&n) == *ch,
                            None => false,
                        })
                    })
                    .count()
            })
            .sum()
    }

    fn part_2(&self, grid: &Self::Input) -> Self::Output2 {
        let directions = [
            Direction::LeftAndUp,
            Direction::RightAndDown,
            Direction::RightAndUp,
            Direction::LeftAndDown,
        ];
        let matches = HashSet::from(["MSMS", "SMSM", "MSSM", "SMMS"]);
        grid.cells()
            .filter(|cell| {
                0 < cell.row
                    && cell.row < grid.height() - 1
                    && 0 < cell.col
                    && cell.col < grid.width() - 1
            })
            .filter(|cell| grid.get(cell) == 'A')
            .filter(|cell| {
                let s = directions
                    .iter()
                    .map(|d| grid.get(&cell.try_at(*d).unwrap()))
                    .collect::<String>();
                matches.contains(&s as &str)
            })
            .count()
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 18,
            self, part_2, TEST, 9
        };
    }
}

fn main() {
    AoC2024_04 {}.run(std::env::args());
}

const TEST: &str = "\
MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2024_04 {}.samples();
    }
}
