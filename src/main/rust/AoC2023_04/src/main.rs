#![allow(non_snake_case)]

use aoc::Puzzle;
use std::collections::{HashMap, HashSet};

#[derive(Debug)]
struct ScratchCard {
    matched: u32,
}

impl ScratchCard {
    fn from_input(line: &str) -> Self {
        fn get_numbers(s: &str) -> HashSet<u32> {
            s.split_whitespace()
                .map(|s| s.parse::<u32>().unwrap())
                .collect::<HashSet<_>>()
        }

        let (winning, have) =
            line.split(": ").nth(1).unwrap().split_once(" | ").unwrap();
        let matched = get_numbers(winning)
            .intersection(&get_numbers(have))
            .count() as u32;
        ScratchCard { matched }
    }
}

struct AoC2023_04;

impl AoC2023_04 {}

impl aoc::Puzzle for AoC2023_04 {
    type Input = Vec<ScratchCard>;
    type Output1 = u32;
    type Output2 = u32;

    aoc::puzzle_year_day!(2023, 4);

    fn parse_input(&self, lines: Vec<String>) -> Vec<ScratchCard> {
        lines
            .iter()
            .map(|s| ScratchCard::from_input(s))
            .collect::<Vec<_>>()
    }

    fn part_1(&self, cards: &Vec<ScratchCard>) -> u32 {
        cards
            .iter()
            .filter(|c| c.matched > 0)
            .map(|c| 1 << (c.matched - 1))
            .sum()
    }

    fn part_2(&self, cards: &Vec<ScratchCard>) -> u32 {
        let mut count: HashMap<usize, u32> =
            (0..cards.len()).map(|i| (i, 1)).collect();
        cards.iter().enumerate().for_each(|(i, card)| {
            (0..card.matched).for_each(|j| {
                *count.get_mut(&(i + 1 + j as usize)).unwrap() +=
                    *count.get(&i).unwrap();
            });
        });
        count.values().sum()
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 13,
            self, part_2, TEST, 30
        };
    }
}

fn main() {
    AoC2023_04 {}.run(std::env::args());
}

const TEST: &str = "\
Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2023_04 {}.samples();
    }
}
