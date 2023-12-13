#![allow(non_snake_case)]

use aoc::Puzzle;

struct AoC2023_09;

impl AoC2023_09 {
    fn solve(&self, line_in: &Vec<i32>) -> i32 {
        let mut line = vec![];
        for n in line_in {
            line.push(*n);
        }
        let mut tails: Vec<i32> = vec![*line.last().unwrap()];
        loop {
            if line.iter().all(|x| x == tails.last().unwrap()) {
                break;
            }
            let new_line = line
                .iter()
                .zip(line[1..].iter())
                .map(|(a, b)| b - a)
                .collect::<Vec<_>>();
            tails.push(*new_line.last().unwrap());
            line.clear();
            for n in &new_line {
                line.push(*n);
            }
        }
        tails.iter().sum()
    }
}

impl aoc::Puzzle for AoC2023_09 {
    type Input = Vec<Vec<i32>>;
    type Output1 = i32;
    type Output2 = i32;

    aoc::puzzle_year_day!(2023, 9);

    fn parse_input(&self, lines: Vec<String>) -> Vec<Vec<i32>> {
        lines
            .iter()
            .map(|line| {
                line.split_whitespace()
                    .map(|i| i.parse().unwrap())
                    .collect()
            })
            .collect()
    }

    fn part_1(&self, input: &Vec<Vec<i32>>) -> i32 {
        input.iter().map(|line| self.solve(line)).sum()
    }

    fn part_2(&self, input: &Vec<Vec<i32>>) -> i32 {
        input
            .iter()
            .map(|line| {
                let mut rev = vec![];
                line.iter().rev().for_each(|n| rev.push(*n));
                rev
            })
            .map(|line| self.solve(&line))
            .sum()
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 114,
            self, part_2, TEST, 2
        };
    }
}

fn main() {
    AoC2023_09 {}.run(std::env::args());
}

const TEST: &str = "\
0 3 6 9 12 15
1 3 6 10 15 21
10 13 16 21 30 45
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2023_09 {}.samples();
    }
}
