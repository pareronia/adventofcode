#![allow(non_snake_case)]

use aoc::{
    grid::{Cell, CharGrid, Grid},
    Puzzle,
};
use itertools::Itertools;
use std::collections::HashSet;

struct Observations {
    galaxies: Vec<Cell>,
    empty_rows: HashSet<usize>,
    empty_cols: HashSet<usize>,
}

struct AoC2023_11;

impl Observations {
    fn from_input(inputs: &Vec<String>) -> Self {
        let grid = CharGrid::from(
            &inputs.iter().map(|s| s.as_str()).collect::<Vec<_>>(),
        );
        let galaxies = grid
            .cells()
            .filter(|cell| grid.get(&cell) == '#')
            .collect::<Vec<_>>();
        let empty_rows = grid
            .get_rows_as_string()
            .iter()
            .enumerate()
            .filter(|(_, row)| !row.contains('#'))
            .map(|(idx, _)| idx)
            .collect::<HashSet<_>>();
        let empty_cols = grid
            .get_cols_as_string()
            .iter()
            .enumerate()
            .filter(|(_, col)| !col.contains('#'))
            .map(|(idx, _)| idx)
            .collect::<HashSet<_>>();
        Self {
            galaxies,
            empty_rows,
            empty_cols,
        }
    }
}

impl AoC2023_11 {
    fn solve(&self, observations: &Observations, factor: usize) -> usize {
        let distance = |first: &Cell, second: &Cell, factor: usize| {
            let mut dr = first.row.abs_diff(second.row);
            let lo = first.row.min(second.row);
            let rr = (lo..lo + dr).collect::<HashSet<_>>();
            dr += (&observations.empty_rows & &rr).len() * factor;
            let mut dc = first.col.abs_diff(second.col);
            let lo = first.col.min(second.col);
            let rc = (lo..lo + dc).collect::<HashSet<_>>();
            dc += (&observations.empty_cols & &rc).len() * factor;
            dr + dc
        };
        observations
            .galaxies
            .iter()
            .combinations(2)
            .map(|c| distance(&c[0], &c[1], factor - 1))
            .sum()
    }
}

impl aoc::Puzzle for AoC2023_11 {
    type Input = Observations;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2023, 11);

    fn parse_input(&self, lines: Vec<String>) -> Observations {
        Observations::from_input(&lines)
    }

    fn part_1(&self, observations: &Observations) -> usize {
        self.solve(observations, 2)
    }

    fn part_2(&self, observations: &Observations) -> usize {
        self.solve(observations, 1_000_000)
    }

    fn samples(&self) {
        let observations = self.parse_input(aoc::split_lines(TEST));
        assert_eq!(self.solve(&observations, 2), 374);
        assert_eq!(self.solve(&observations, 10), 1030);
        assert_eq!(self.solve(&observations, 100), 8410);
    }
}

fn main() {
    AoC2023_11 {}.run(std::env::args());
}

const TEST: &str = "\
...#......
.......#..
#.........
..........
......#...
.#........
.........#
..........
.......#..
#...#.....
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2023_11 {}.samples();
    }
}
