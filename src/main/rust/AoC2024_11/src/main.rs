#![allow(non_snake_case)]

use aoc::Puzzle;
use cached::proc_macro::cached;

struct AoC2024_11;

impl AoC2024_11 {
    fn solve(&self, stones: &[u64], blinks: usize) -> u64 {
        #[cached]
        fn count(s: u64, cnt: usize) -> u64 {
            if cnt == 0 {
                return 1;
            }
            if s == 0 {
                return count(1, cnt - 1);
            }
            let ss = s.to_string();
            let size = ss.len();
            if size.is_multiple_of(2) {
                let s1 = ss[..size / 2].parse::<u64>().unwrap();
                let s2 = ss[size / 2..].parse::<u64>().unwrap();
                return count(s1, cnt - 1) + count(s2, cnt - 1);
            }
            count(s * 2024, cnt - 1)
        }

        stones.iter().map(|s| count(*s, blinks)).sum()
    }
}

impl aoc::Puzzle for AoC2024_11 {
    type Input = Vec<u64>;
    type Output1 = u64;
    type Output2 = u64;

    aoc::puzzle_year_day!(2024, 11);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        lines[0]
            .split_whitespace()
            .map(|s| s.parse::<u64>().unwrap())
            .collect()
    }

    fn part_1(&self, stones: &Self::Input) -> Self::Output1 {
        self.solve(stones, 25)
    }

    fn part_2(&self, stones: &Self::Input) -> Self::Output2 {
        self.solve(stones, 75)
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 55312
        };
    }
}

fn main() {
    AoC2024_11 {}.run(std::env::args());
}

const TEST: &str = "\
125 17
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2024_11 {}.samples();
    }
}
