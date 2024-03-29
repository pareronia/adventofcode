#![allow(non_snake_case)]

use aoc::Puzzle;
use std::collections::HashSet;

struct AoC2022_03 {}

impl AoC2022_03 {
    fn priority(&self, ch: char) -> u32 {
        match ch.is_ascii_lowercase() {
            true => ch as u32 - 'a' as u32 + 1,
            false => ch as u32 - 'A' as u32 + 27,
        }
    }
}

impl aoc::Puzzle for AoC2022_03 {
    type Input = Vec<String>;
    type Output1 = u32;
    type Output2 = u32;

    aoc::puzzle_year_day!(2022, 3);

    fn parse_input(&self, lines: Vec<String>) -> Vec<String> {
        lines
    }

    fn part_1(&self, lines: &Vec<String>) -> u32 {
        (0..lines.len())
            .map(|i| {
                let line = &lines[i];
                let cutoff = line.len() / 2;
                let s1: HashSet<char> = line.chars().take(cutoff).collect();
                let s2: HashSet<char> = line.chars().skip(cutoff).collect();
                self.priority(*s1.intersection(&s2).next().unwrap())
            })
            .sum()
    }

    fn part_2(&self, lines: &Vec<String>) -> u32 {
        (0..lines.len())
            .step_by(3)
            .map(|i| {
                let s1: HashSet<char> = lines[i].chars().collect();
                let s2: HashSet<char> = lines[i + 1].chars().collect();
                let s3: HashSet<char> = lines[i + 2].chars().collect();
                let intersection = [&s2, &s3]
                    .iter()
                    .fold(s1, |i, s| i.intersection(s).copied().collect());
                self.priority(*intersection.iter().next().unwrap())
            })
            .sum()
    }

    fn samples(&self) {
        let test = "vJrwpWtwJgWrhcsFMMfFFhFp\n\
             jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL\n\
             PmmdzqPrVvPwwTWBwg\n\
             wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn\n\
             ttgJtRGJQctTZtZT\n\
             CrZsJsPPZsGzwwsLwLmpwMDw";
        aoc::puzzle_samples! {
            self, part_1, test, 157,
            self, part_2, test, 70
        };
    }
}

fn main() {
    AoC2022_03 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2022_03 {}.samples();
    }
}
