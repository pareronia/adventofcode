#![allow(non_snake_case)]

use std::collections::HashSet;

use aoc::{
    grid::{Cell, CharGrid, Grid},
    Puzzle,
};

struct AoC2023_21;

impl AoC2023_21 {
    fn solve(&self, grid: &CharGrid, steps: &[usize]) -> Vec<u64> {
        let w = grid.width() as i32;
        let start = (w / 2, w / 2);
        let mut plots: HashSet<(i32, i32)> = HashSet::new();
        plots.insert(start);
        let mut ans: Vec<u64> = vec![];
        for i in 1..=*steps.iter().max().unwrap() {
            let mut new_plots: HashSet<(i32, i32)> = HashSet::new();
            for cell in &plots {
                for (dr, dc) in [(0, 1), (0, -1), (1, 0), (-1, 0)] {
                    let rr = cell.0 + dr;
                    let cc = cell.1 + dc;
                    let wr = ((rr % w) + w) % w;
                    let wc = ((cc % w) + w) % w;
                    if 0 <= wr
                        && wr < w
                        && 0 <= wc
                        && wc < w
                        && grid.get(&Cell::at(wr as usize, wc as usize)) != '#'
                    {
                        new_plots.insert((rr, cc));
                    }
                }
            }
            if steps.contains(&i) {
                ans.push(new_plots.len() as u64)
            }
            plots = new_plots;
        }
        ans
    }
}

impl aoc::Puzzle for AoC2023_21 {
    type Input = CharGrid;
    type Output1 = u64;
    type Output2 = u64;

    aoc::puzzle_year_day!(2023, 21);

    fn parse_input(&self, lines: Vec<String>) -> CharGrid {
        CharGrid::from(&lines.iter().map(AsRef::as_ref).collect())
    }

    fn part_1(&self, grid: &CharGrid) -> u64 {
        self.solve(grid, &[64])[0]
    }

    fn part_2(&self, grid: &CharGrid) -> u64 {
        let steps = 26_501_365;
        let modulo = steps % grid.width();
        let x: u64 = steps as u64 / grid.width() as u64;
        let stepses: Vec<usize> =
            (0..3).map(|i| i * grid.width() + modulo).collect();
        let values = self.solve(grid, &stepses);
        let a = (values[2] + values[0] - 2 * values[1]) / 2;
        let b = values[1] - values[0] - a;
        let c = values[0];
        a * x * x + b * x + c
    }

    fn samples(&self) {
        let grid = self.parse_input(TEST.lines().map(String::from).collect());
        assert_eq!(self.solve(&grid, &[6, 10, 50, 100]), &[16, 50, 1594, 6536]);
    }
}

fn main() {
    AoC2023_21 {}.run(std::env::args());
}

const TEST: &str = "\
...........
.....###.#.
.###.##..#.
..#.#...#..
....#.#....
.##..S####.
.##..#...#.
.......##..
.##.#.####.
.##..##.##.
...........
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2023_21 {}.samples();
    }
}
