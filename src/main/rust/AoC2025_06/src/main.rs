#![allow(non_snake_case)]

use std::str::FromStr;

use aoc::grid::{CharGrid, Grid};
use aoc::Puzzle;

#[derive(Clone)]
enum Operation {
    Add,
    Multiply,
}

impl FromStr for Operation {
    type Err = &'static str;

    fn from_str(string: &str) -> Result<Self, Self::Err> {
        match string {
            "+" => Ok(Operation::Add),
            "*" => Ok(Operation::Multiply),
            _ => panic!(),
        }
    }
}

enum Mode {
    ByRows,
    ByColumns,
}

struct Problem {
    nums: Vec<u64>,
    operation: Operation,
}

struct Worksheet {
    grid: CharGrid,
    ops: Vec<Operation>,
}

impl Worksheet {
    fn get_problems(&self, mode: Mode) -> Vec<Problem> {
        let mut problems = vec![];
        let mut nums: Vec<String> = vec![];
        let mut j = 0;
        for col in 0..self.grid.width() {
            let s = self.grid.get_col_as_string(col);
            if s.trim().is_empty() {
                let nums_grid = CharGrid::from(
                    &nums.iter().map(|s| s.as_str()).collect::<Vec<_>>(),
                );
                let rows = match mode {
                    Mode::ByRows => nums_grid.get_cols_as_string(),
                    Mode::ByColumns => nums_grid.get_rows_as_string(),
                };
                problems.push(Problem {
                    nums: rows
                        .iter()
                        .map(|row| row.trim().parse::<u64>().unwrap())
                        .collect::<Vec<_>>(),
                    operation: self.ops[j].clone(),
                });
                nums.clear();
                j += 1;
            } else {
                nums.push(s);
            }
        }
        problems
    }
}

struct AoC2025_06;

impl AoC2025_06 {
    fn solve(&self, worksheet: &Worksheet, mode: Mode) -> u64 {
        worksheet
            .get_problems(mode)
            .iter()
            .map(|p| match p.operation {
                Operation::Add => p.nums.iter().sum::<u64>(),
                Operation::Multiply => p.nums.iter().product(),
            })
            .sum()
    }
}

impl aoc::Puzzle for AoC2025_06 {
    type Input = Worksheet;
    type Output1 = u64;
    type Output2 = u64;

    aoc::puzzle_year_day!(2025, 6);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        let lines_ext = lines
            .iter()
            .map(|line| {
                let mut s = String::from(line);
                s.push(' ');
                s
            })
            .collect::<Vec<_>>();
        let grid = CharGrid::from(
            &lines_ext
                .iter()
                .take(lines.len() - 1)
                .map(|s| s.as_str())
                .collect::<Vec<_>>(),
        );
        let ops = lines
            .last()
            .unwrap()
            .split_whitespace()
            .map(|sp| Operation::from_str(sp).unwrap())
            .collect::<Vec<_>>();
        Worksheet { grid, ops }
    }

    fn part_1(&self, worksheet: &Self::Input) -> Self::Output1 {
        self.solve(worksheet, Mode::ByRows)
    }

    fn part_2(&self, worksheet: &Self::Input) -> Self::Output2 {
        self.solve(worksheet, Mode::ByColumns)
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 4277556,
            self, part_2, TEST, 3263827
        };
    }
}

fn main() {
    AoC2025_06 {}.run(std::env::args());
}

const TEST: &str =
    "123 328  51 64 \n 45 64  387 23 \n  6 98  215 314\n*   +   *   +  ";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2025_06 {}.samples();
    }
}
