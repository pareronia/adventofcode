#![allow(non_snake_case)]

use aoc::Puzzle;
use lazy_static::lazy_static;
use regex::Regex;

lazy_static! {
    static ref REGEX: Regex = Regex::new(r"\\x[0-9a-f]{2}").unwrap();
}

enum Mode {
    Decode,
    Encode,
}

impl Mode {
    fn overhead(&self, s: &str) -> usize {
        fn count_matches(string: &str, substring: &str) -> usize {
            string
                .as_bytes()
                .windows(substring.len())
                .filter(|&w| w == substring.as_bytes())
                .count()
        }

        match self {
            Self::Decode => {
                let mut string = String::from(s);
                assert!(string.chars().next().is_some_and(|ch| ch == '"'));
                assert!(string.chars().last().is_some_and(|ch| ch == '"'));
                let mut cnt = 2;
                while string.contains(r#"\\"#) {
                    string = string.replacen(r#"\\"#, "", 1);
                    cnt += 1;
                }
                cnt + count_matches(&string, r#"\""#)
                    + 3 * REGEX.find_iter(&string).count()
            }
            Self::Encode => 2 + count_matches(s, "\\") + count_matches(s, "\""),
        }
    }
}

struct AoC2015_08;

impl AoC2015_08 {
    fn solve(&self, input: &[String], mode: Mode) -> usize {
        input.iter().map(|s| mode.overhead(s)).sum()
    }
}

impl aoc::Puzzle for AoC2015_08 {
    type Input = Vec<String>;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2015, 8);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        lines
    }

    fn part_1(&self, input: &Self::Input) -> Self::Output1 {
        self.solve(input, Mode::Decode)
    }

    fn part_2(&self, input: &Self::Input) -> Self::Output2 {
        self.solve(input, Mode::Encode)
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 12,
            self, part_2, TEST, 19
        };
    }
}

fn main() {
    AoC2015_08 {}.run(std::env::args());
}

const TEST: &str = r#"""
"abc"
"aaa\"aaa"
"\x27"
"#;

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2015_08 {}.samples();
    }
}
