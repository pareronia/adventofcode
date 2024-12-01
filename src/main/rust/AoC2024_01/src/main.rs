#![allow(non_snake_case)]

use aoc::Puzzle;
use itertools::Itertools;
use std::collections::HashMap;

struct AoC2024_01;

impl AoC2024_01 {}

impl aoc::Puzzle for AoC2024_01 {
    type Input = (Vec<u32>, Vec<u32>);
    type Output1 = u32;
    type Output2 = u32;

    aoc::puzzle_year_day!(2024, 1);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        let nums = lines
            .iter()
            .map(|line| line.split_once("   ").unwrap())
            .map(|(left, right)| {
                (left.parse::<u32>().unwrap(), right.parse::<u32>().unwrap())
            })
            .collect::<Vec<(u32, u32)>>();
        let left = nums.iter().map(|(left, _)| left.clone()).collect();
        let right = nums.iter().map(|(_, right)| right.clone()).collect();
        return (left, right);
    }

    fn part_1(&self, input: &Self::Input) -> u32 {
        let (left, right) = input;
        left.iter()
            .sorted()
            .zip(right.iter().sorted())
            .map(|(n1, n2)| n1.abs_diff(*n2))
            .sum()
    }

    fn part_2(&self, input: &Self::Input) -> u32 {
        let (left, right) = input;
        let mut ctr: HashMap<u32, u32> = HashMap::new();
        right.iter().for_each(|n| {
            ctr.entry(*n).and_modify(|e| *e += 1).or_insert(1);
        });
        left.iter().map(|n| n * ctr.get(n).unwrap_or(&0)).sum()
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 11,
            self, part_2, TEST, 31
        };
    }
}

fn main() {
    AoC2024_01 {}.run(std::env::args());
}

const TEST: &str = "\
3   4
4   3
2   5
1   3
3   9
3   3
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2024_01 {}.samples();
    }
}
