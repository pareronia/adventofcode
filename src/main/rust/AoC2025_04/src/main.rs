#![allow(non_snake_case)]

use aoc::grid::{Cell, CharGrid, Grid};
use aoc::Puzzle;
use std::collections::{HashSet, VecDeque};

const ROLL: char = '@';
const EMPTY: char = '.';

struct AoC2025_04;

impl AoC2025_04 {
    fn is_removable(&self, grid: &CharGrid, cell: &Cell) -> bool {
        grid.get(cell) == ROLL
            && grid
                .all_neighbours(cell)
                .iter()
                .filter(|n| grid.get(n) == ROLL)
                .count()
                < 4
    }
}

impl aoc::Puzzle for AoC2025_04 {
    type Input = Vec<String>;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2025, 4);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        lines
    }

    fn part_1(&self, lines: &Self::Input) -> Self::Output1 {
        let grid = CharGrid::from(
            &lines.iter().map(|s| s.as_str()).collect::<Vec<_>>(),
        );
        grid.cells()
            .filter(|cell| self.is_removable(&grid, cell))
            .count()
    }

    fn part_2(&self, lines: &Self::Input) -> Self::Output2 {
        let mut grid = CharGrid::from(
            &lines.iter().map(|s| s.as_str()).collect::<Vec<_>>(),
        );
        let mut q = VecDeque::new();
        grid.cells()
            .filter(|cell| self.is_removable(&grid, cell))
            .for_each(|cell| q.push_back(cell));
        let mut seen = HashSet::new();
        while !q.is_empty() {
            let cell = q.pop_front().unwrap();
            if seen.contains(&cell) {
                continue;
            }
            seen.insert(cell);
            grid.get_data_mut()[cell.row][cell.col] = EMPTY;
            grid.all_neighbours(&cell)
                .iter()
                .filter(|n| self.is_removable(&grid, n))
                .for_each(|n| q.push_back(*n));
        }
        seen.len()
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 13,
            self, part_2, TEST, 43
        };
    }
}

fn main() {
    AoC2025_04 {}.run(std::env::args());
}

const TEST: &str = "\
..@@.@@@@.
@@@.@.@.@@
@@@@@.@.@@
@.@@@@..@.
@@.@@@@.@@
.@@@@@@@.@
.@.@.@.@@@
@.@@@.@@@@
.@@@@@@@@.
@.@.@@@.@.
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2025_04 {}.samples();
    }
}
