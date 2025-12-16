#![allow(non_snake_case)]

use aoc::range::merge;
use aoc::Puzzle;
use std::ops::RangeInclusive;

struct Database {
    id_ranges: Vec<RangeInclusive<isize>>,
    available_ids: Vec<u64>,
}

struct AoC2025_05;

impl AoC2025_05 {}

impl aoc::Puzzle for AoC2025_05 {
    type Input = Database;
    type Output1 = usize;
    type Output2 = isize;

    aoc::puzzle_year_day!(2025, 5);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        let blocks = aoc::to_blocks(&lines);
        let mut id_ranges = vec![];
        for line in blocks.first().unwrap().iter() {
            let (left, right) = line.split_once('-').unwrap();
            id_ranges.push(
                left.parse::<isize>().unwrap()
                    ..=right.parse::<isize>().unwrap(),
            );
        }
        id_ranges = merge(&id_ranges);
        let available_ids = blocks
            .last()
            .unwrap()
            .iter()
            .map(|id| id.parse::<u64>().unwrap())
            .collect::<Vec<_>>();
        Database {
            id_ranges,
            available_ids,
        }
    }

    fn part_1(&self, database: &Self::Input) -> Self::Output1 {
        database
            .available_ids
            .iter()
            .filter(|id| {
                database
                    .id_ranges
                    .iter()
                    .any(|rng| rng.contains(&(**id as isize)))
            })
            .count()
    }

    fn part_2(&self, database: &Self::Input) -> Self::Output2 {
        database
            .id_ranges
            .iter()
            .map(|rng| rng.end() - rng.start() + 1)
            .sum()
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 3,
            self, part_2, TEST, 14
        };
    }
}

fn main() {
    AoC2025_05 {}.run(std::env::args());
}

const TEST: &str = "\
3-5
10-14
16-20
12-18

1
5
8
11
17
32
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2025_05 {}.samples();
    }
}
