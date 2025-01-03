#![allow(non_snake_case)]

use aoc::grid::{Cell, Grid, IntGrid};
use aoc::Puzzle;
use itertools::Itertools;
use std::collections::VecDeque;

enum Grading {
    Score,
    Rating,
}

impl Grading {
    fn get(&self, trails: &Vec<Vec<Cell>>) -> usize {
        match self {
            Grading::Score => {
                trails.iter().map(|trail| trail.last()).unique().count()
            }
            Grading::Rating => trails.len(),
        }
    }
}

struct AoC2024_10;

impl AoC2024_10 {
    fn solve(&self, grid: &IntGrid, grading: Grading) -> usize {
        fn bfs(grid: &IntGrid, trail_head: Cell) -> Vec<Vec<Cell>> {
            let mut trails: Vec<Vec<Cell>> = Vec::new();
            let mut q: VecDeque<Vec<Cell>> =
                VecDeque::from(vec![vec![trail_head]]);
            while !q.is_empty() {
                let trail = q.pop_front().unwrap();
                let nxt = trail.len() as u32;
                if nxt == 10 {
                    trails.push(trail);
                    continue;
                }
                grid.capital_neighbours(&trail.last().unwrap())
                    .into_iter()
                    .filter(|n| grid.get(&n) == nxt)
                    .for_each(|n| {
                        let mut new_trail = trail.to_vec();
                        new_trail.push(n);
                        q.push_back(new_trail);
                    });
            }
            trails
        }

        grid.cells()
            .filter(|cell| grid.get(cell) == 0)
            .map(|trail_head| grading.get(&bfs(grid, trail_head)))
            .sum()
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
        self.solve(grid, Grading::Score)
    }

    fn part_2(&self, grid: &Self::Input) -> Self::Output2 {
        self.solve(grid, Grading::Rating)
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
