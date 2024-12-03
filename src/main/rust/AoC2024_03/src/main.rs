#![allow(non_snake_case)]

use aoc::Puzzle;
use lazy_static::lazy_static;
use regex::Regex;

lazy_static! {
    static ref REGEX: Regex =
        Regex::new(r"(do(n't)?)\(\)|mul\((\d{1,3}),(\d{1,3})\)").unwrap();
}

struct AoC2024_03;

impl AoC2024_03 {
    fn solve(&self, input: &str, use_conditionals: bool) -> u32 {
        let mut enabled = true;
        let mut ans = 0;
        for cap in REGEX.captures_iter(input) {
            if &cap[0] == "do()" {
                enabled = true;
            } else if &cap[0] == "don't()" {
                enabled = false;
            } else {
                if !use_conditionals || enabled {
                    ans += &cap[3].parse::<u32>().unwrap()
                        * &cap[4].parse::<u32>().unwrap();
                }
            }
        }
        ans
    }
}

impl aoc::Puzzle for AoC2024_03 {
    type Input = String;
    type Output1 = u32;
    type Output2 = u32;

    aoc::puzzle_year_day!(2024, 3);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        lines.join("\n")
    }

    fn part_1(&self, input: &Self::Input) -> Self::Output1 {
        self.solve(&input, false)
    }

    fn part_2(&self, input: &Self::Input) -> Self::Output2 {
        self.solve(&input, true)
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST_1, 161,
            self, part_2, TEST_2, 48
        };
    }
}

fn main() {
    AoC2024_03 {}.run(std::env::args());
}

const TEST_1: &str = "\
xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))
";
const TEST_2: &str = "\
xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2024_03 {}.samples();
    }
}
