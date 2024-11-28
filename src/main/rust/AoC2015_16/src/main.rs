#![allow(non_snake_case)]

use aoc::Puzzle;
use std::str::FromStr;
use strum::{EnumCount, EnumString};

static VALUES: [u8; Thing::COUNT] = [3, 7, 2, 3, 0, 0, 5, 3, 2, 1];

#[derive(EnumCount, EnumString)]
#[strum(ascii_case_insensitive)]
enum Thing {
    Children,
    Cats,
    Samoyeds,
    Pomeranians,
    Akitas,
    Vizslas,
    Goldfish,
    Trees,
    Cars,
    Perfumes,
}

enum Op {
    Equal,
    GreaterThan,
    LessThan,
}

impl Op {
    fn matches(&self, lhs: Option<u8>, rhs: u8) -> bool {
        if lhs.is_none() {
            return false;
        }
        match self {
            Op::Equal => lhs.unwrap() == rhs,
            Op::GreaterThan => lhs.unwrap() > rhs,
            Op::LessThan => lhs.unwrap() < rhs,
        }
    }
}

#[derive(Debug)]
struct AuntSue {
    nbr: u16,
    things: [Option<u8>; Thing::COUNT],
}

impl AuntSue {
    fn from_input(line: &str) -> Self {
        let mut things = [None; Thing::COUNT];
        let line = line.replace(",", "");
        let line = line.replace(":", "");
        let splits: Vec<&str> = line.split_whitespace().collect();
        let nbr = splits[1].parse::<u16>().unwrap();
        (2..splits.len()).step_by(2).for_each(|i| {
            let idx = Thing::from_str(splits[i]).unwrap() as usize;
            things[idx] = Some(splits[i + 1].parse::<u8>().unwrap());
        });
        Self { nbr, things }
    }
}

struct AoC2015_16;

impl AoC2015_16 {
    fn find_aunt_sue_with_best_score<'a>(
        &'a self,
        aunt_sues: &'a [AuntSue],
        ops: &[Op; Thing::COUNT],
    ) -> &'a AuntSue {
        aunt_sues
            .iter()
            .map(|sue| {
                let score = (0..Thing::COUNT)
                    .filter(|&thing| {
                        ops[thing].matches(sue.things[thing], VALUES[thing])
                    })
                    .count();
                (sue, score)
            })
            .max_by(|a, b| a.1.cmp(&b.1))
            .unwrap()
            .0
    }
}

impl aoc::Puzzle for AoC2015_16 {
    type Input = Vec<AuntSue>;
    type Output1 = u16;
    type Output2 = u16;

    aoc::puzzle_year_day!(2015, 16);

    fn parse_input(&self, lines: Vec<String>) -> Vec<AuntSue> {
        lines.iter().map(|line| AuntSue::from_input(line)).collect()
    }

    fn part_1(&self, aunt_sues: &Vec<AuntSue>) -> u16 {
        let ops = [
            Op::Equal,
            Op::Equal,
            Op::Equal,
            Op::Equal,
            Op::Equal,
            Op::Equal,
            Op::Equal,
            Op::Equal,
            Op::Equal,
            Op::Equal,
        ];
        self.find_aunt_sue_with_best_score(aunt_sues, &ops).nbr
    }

    fn part_2(&self, aunt_sues: &Vec<AuntSue>) -> u16 {
        let ops = [
            Op::Equal,
            Op::GreaterThan,
            Op::Equal,
            Op::LessThan,
            Op::Equal,
            Op::Equal,
            Op::LessThan,
            Op::GreaterThan,
            Op::Equal,
            Op::Equal,
        ];
        self.find_aunt_sue_with_best_score(aunt_sues, &ops).nbr
    }

    fn samples(&self) {}
}

fn main() {
    AoC2015_16 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2015_16 {}.samples();
    }
}
