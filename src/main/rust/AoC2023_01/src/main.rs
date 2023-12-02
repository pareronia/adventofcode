#![allow(non_snake_case)]

use aoc::Puzzle;

const NUMS: [&str; 9] = [
    "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
];

struct AoC2023_01;

impl AoC2023_01 {
    fn solve(&self, input: &[String], f: impl Fn(&str) -> Vec<u32>) -> u32 {
        input
            .iter()
            .map(|line| f(line))
            .map(|digits| digits.first().unwrap() * 10 + digits.last().unwrap())
            .sum()
    }
}

impl aoc::Puzzle for AoC2023_01 {
    type Input = Vec<String>;
    type Output1 = u32;
    type Output2 = u32;

    aoc::puzzle_year_day!(2023, 1);

    fn parse_input(&self, lines: Vec<String>) -> Vec<String> {
        lines
    }

    fn part_1(&self, input: &Vec<String>) -> u32 {
        fn get_digits(line: &str) -> Vec<u32> {
            line.chars()
                .filter(|c| c.is_ascii_digit())
                .map(|c| c.to_digit(10).unwrap())
                .collect::<Vec<_>>()
        }
        self.solve(input, get_digits)
    }

    fn part_2(&self, input: &Vec<String>) -> u32 {
        fn find_digit(s: &str) -> Option<u32> {
            let c = s.chars().next().unwrap();
            if c.is_ascii_digit() {
                return Some(c.to_digit(10).unwrap());
            } else {
                for (j, num) in NUMS.iter().enumerate() {
                    if s.starts_with(num) {
                        return Some(j as u32 + 1);
                    }
                }
            }
            None
        }

        fn get_digits(line: &str) -> Vec<u32> {
            (0..line.len())
                .map(|i| &line[i..])
                .filter_map(find_digit)
                .collect::<Vec<_>>()
        }
        self.solve(input, get_digits)
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST1, 142,
            self, part_2, TEST2, 281
        };
    }
}

fn main() {
    AoC2023_01 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2023_01 {}.samples();
    }
}

const TEST1: &str = "\
1abc2
pqr3stu8vwx
a1b2c3d4e5f
treb7uchet
";
const TEST2: &str = "\
two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen
";
