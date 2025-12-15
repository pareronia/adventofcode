#![allow(non_snake_case)]

use aoc::Puzzle;
use std::ops::RangeInclusive;

enum Mode {
    ExactlyOnce,
    AtLeastOnce,
}

impl Mode {
    fn check(&self, n: u64) -> bool {
        fn sub_check(s: &str, sub_len: usize) -> bool {
            let sz = s.len();
            if !sz.is_multiple_of(sub_len) {
                return false;
            }
            let sub = &s[..sub_len];
            for i in (sub_len..sz).step_by(sub_len) {
                if !s[i..].starts_with(sub) {
                    return false;
                }
            }
            true
        }

        let s = n.to_string();
        match self {
            Mode::ExactlyOnce => {
                s.len().is_multiple_of(2) && sub_check(&s, s.len() / 2)
            }
            Mode::AtLeastOnce => {
                for i in 1..=s.len().div_euclid(2) {
                    if sub_check(&s, i) {
                        return true;
                    }
                }
                false
            }
        }
    }
}

struct AoC2025_02;

impl AoC2025_02 {
    fn solve(&self, ranges: &[RangeInclusive<u64>], mode: Mode) -> u64 {
        ranges
            .iter()
            .flat_map(|rng| rng.clone().step_by(1))
            .filter(|n| mode.check(*n))
            .sum()
    }
}

impl aoc::Puzzle for AoC2025_02 {
    type Input = Vec<RangeInclusive<u64>>;
    type Output1 = u64;
    type Output2 = u64;

    aoc::puzzle_year_day!(2025, 2);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        lines[0]
            .split(',')
            .map(|sp| {
                let (lo, hi) = sp.split_once('-').unwrap();
                RangeInclusive::new(
                    lo.parse::<u64>().unwrap(),
                    hi.parse::<u64>().unwrap(),
                )
            })
            .collect::<Vec<_>>()
    }

    fn part_1(&self, ranges: &Self::Input) -> Self::Output1 {
        self.solve(ranges, Mode::ExactlyOnce)
    }

    fn part_2(&self, ranges: &Self::Input) -> Self::Output2 {
        self.solve(ranges, Mode::AtLeastOnce)
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 1227775554_u64,
            self, part_2, TEST, 4174379265_u64
        };
    }
}

fn main() {
    AoC2025_02 {}.run(std::env::args());
}

const TEST: &str = "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,\
                    1698522-1698528,446443-446449,38593856-38593862,\
                    565653-565659,824824821-824824827,2121212118-2121212124";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2025_02 {}.samples();
    }
}
