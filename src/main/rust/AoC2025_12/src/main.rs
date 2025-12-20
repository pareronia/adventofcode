#![allow(non_snake_case)]

use aoc::Puzzle;
use std::collections::HashMap;
use std::str::FromStr;

struct Shape {
    area: usize,
}

impl Shape {
    fn from_input(input: &[&String]) -> Self {
        Self {
            area: input.len() * input.first().unwrap().len(),
        }
    }
}

struct Region {
    area: usize,
    quantities: HashMap<u8, usize>,
}

impl FromStr for Region {
    type Err = &'static str;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let (sa, sq) = s.split_once(": ").unwrap();
        let (w, h) = sa.split_once('x').unwrap();
        let area = w.parse::<usize>().unwrap() * h.parse::<usize>().unwrap();
        let quantities = HashMap::from_iter(
            sq.split_whitespace()
                .enumerate()
                .map(|(i, sp)| (i as u8, sp.parse::<usize>().unwrap())),
        );
        Ok(Region { area, quantities })
    }
}

struct AoC2025_12;

impl AoC2025_12 {}

impl aoc::Puzzle for AoC2025_12 {
    type Input = (Vec<Region>, HashMap<u8, Shape>);
    type Output1 = usize;
    type Output2 = String;

    aoc::puzzle_year_day!(2025, 12);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        let blocks = aoc::to_blocks(&lines);
        let regions = blocks
            .last()
            .unwrap()
            .iter()
            .map(|line| Region::from_str(line).unwrap())
            .collect::<Vec<_>>();
        let shapes = HashMap::from_iter((0..blocks.len() - 1).map(|i| {
            (
                blocks[i]
                    .first()
                    .unwrap()
                    .trim_end_matches(':')
                    .parse::<u8>()
                    .unwrap(),
                Shape::from_input(&blocks[i][1..]),
            )
        }));
        (regions, shapes)
    }

    fn part_1(&self, input: &Self::Input) -> Self::Output1 {
        let (regions, shapes) = input;
        regions
            .iter()
            .filter(|r| {
                r.quantities
                    .iter()
                    .map(|(k, v)| *v * shapes[k].area)
                    .sum::<usize>()
                    <= r.area
            })
            .count()
    }

    fn part_2(&self, _input: &Self::Input) -> Self::Output2 {
        String::from("🎄")
    }

    fn samples(&self) {}
}

fn main() {
    AoC2025_12 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2025_12 {}.samples();
    }
}
