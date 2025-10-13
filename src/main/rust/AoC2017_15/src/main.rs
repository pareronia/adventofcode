#![allow(non_snake_case)]

use aoc::Puzzle;

const FACTOR_A: u64 = 16807;
const FACTOR_B: u64 = 48271;
const MOD: u64 = 2147483647;

enum Criteria {
    A1,
    B1,
    A2,
    B2,
}

impl Criteria {
    fn apply(&self, val: u64) -> bool {
        match self {
            Self::A1 | Self::B1 => true,
            Self::A2 => val.is_multiple_of(4),
            Self::B2 => val.is_multiple_of(8),
        }
    }
}

struct Generator {
    factor: u64,
    val: u64,
    criteria: Criteria,
}

impl Generator {
    fn new(factor: u64, val: u64, criteria: Criteria) -> Self {
        Self {
            factor,
            val,
            criteria,
        }
    }
}

impl Iterator for Generator {
    type Item = u64;

    fn next(&mut self) -> Option<Self::Item> {
        let mut nxt_val = self.val;
        loop {
            nxt_val = (nxt_val * self.factor) % MOD;
            if self.criteria.apply(nxt_val) {
                self.val = nxt_val;
                return Some(nxt_val);
            }
        }
    }
}

struct AoC2017_15;

impl AoC2017_15 {
    fn solve(
        &self,
        seeds: (u64, u64),
        criteria: (Criteria, Criteria),
        reps: usize,
    ) -> usize {
        let mut gen_a = Generator::new(FACTOR_A, seeds.0, criteria.0);
        let mut gen_b = Generator::new(FACTOR_B, seeds.1, criteria.1);
        (0..reps)
            .filter(|_| {
                gen_a.next().unwrap() & 0xFFFF == gen_b.next().unwrap() & 0xFFFF
            })
            .count()
    }
}

impl aoc::Puzzle for AoC2017_15 {
    type Input = (u64, u64);
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2017, 15);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        let mut it = (0..=1).map(|i| {
            lines[i]
                .split_whitespace()
                .last()
                .unwrap()
                .parse::<u64>()
                .unwrap()
        });
        (it.next().unwrap(), it.next().unwrap())
    }

    fn part_1(&self, seeds: &Self::Input) -> Self::Output1 {
        self.solve(*seeds, (Criteria::A1, Criteria::B1), 40_000_000)
    }

    fn part_2(&self, seeds: &Self::Input) -> Self::Output2 {
        self.solve(*seeds, (Criteria::A2, Criteria::B2), 5_000_000)
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 588,
            self, part_2, TEST, 309
        };
    }
}

fn main() {
    AoC2017_15 {}.run(std::env::args());
}

const TEST: &str = "\
Generator A starts with 65
Generator B starts with 8921
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2017_15 {}.samples();
    }
}
