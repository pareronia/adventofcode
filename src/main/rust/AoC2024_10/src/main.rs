#![allow(non_snake_case)]

use aoc::grid::{Cell, Grid, IntGrid};
use aoc::Puzzle;
use itertools::Itertools;

struct AoC2024_10;

impl AoC2024_10 {
    fn get_trails(&self, grid: &IntGrid) -> Vec<Vec<Cell>> {
        fn dfs(grid: &IntGrid, trails: &mut Vec<Vec<Cell>>, trail: Vec<Cell>) {
            if trail.len() == 10 {
                trails.push(trail);
                return;
            }
            let nxt = grid.get(trail.last().unwrap()) + 1;
            for n in grid.capital_neighbours(trail.last().unwrap()) {
                if grid.get(&n) == nxt {
                    let mut new_trail = trail.to_vec();
                    new_trail.push(n);
                    dfs(grid, trails, new_trail);
                }
            }
        }

        let mut trails = vec![];
        for s in grid.cells().filter(|cell| grid.get(cell) == 0) {
            dfs(grid, &mut trails, vec![s]);
        }
        trails
    }
}

impl aoc::Puzzle for AoC2024_10 {
    type Input = IntGrid;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2024, 10);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        IntGrid::from(&lines.iter().map(AsRef::as_ref).collect::<Vec<_>>())
    }

    fn part_1(&self, grid: &Self::Input) -> Self::Output1 {
        let trails = self.get_trails(grid);
        trails
            .iter()
            .map(|trail| trail[0])
            .unique()
            .map(|zero| {
                trails
                    .iter()
                    .filter(|trail| trail[0] == zero)
                    .map(|trail| trail[9])
                    .unique()
                    .count()
            })
            .sum()
    }

    fn part_2(&self, grid: &Self::Input) -> Self::Output2 {
        self.get_trails(grid).len()
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 36,
            self, part_2, TEST, 81
        };
    }
}

fn main() {
    AoC2024_10 {}.run(std::env::args());
}

const TEST: &str = "\
89010123
78121874
87430965
96549874
45678903
32019012
01329801
10456732
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2024_10 {}.samples();
    }
}
