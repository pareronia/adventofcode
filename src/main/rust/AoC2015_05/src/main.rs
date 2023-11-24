#![allow(non_snake_case)]
use aoc::Puzzle;
use fancy_regex::Regex;
use lazy_static::lazy_static;

lazy_static! {
    static ref VOWEL: Regex = Regex::new(r"(a|e|i|o|u)").unwrap();
    static ref TWIN: Regex = Regex::new(r"([a-z])\1").unwrap();
    static ref BAD: Regex = Regex::new(r"(ab|cd|pq|xy)").unwrap();
    static ref TWO_TWINS: Regex = Regex::new(r"([a-z]{2})[a-z]*\1").unwrap();
    static ref THREE_LETTER_PALINDROME: Regex =
        Regex::new(r"([a-z])[a-z]\1").unwrap();
}

struct AoC2015_05 {}

impl AoC2015_05 {
    fn count_matches(&self, line: &str, regex: &Regex) -> usize {
        regex.find_iter(line).count()
    }
}

impl aoc::Puzzle for AoC2015_05 {
    type Input = Vec<String>;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2015, 5);

    fn parse_input(&self, lines: Vec<String>) -> Vec<String> {
        lines
    }

    fn part_1(&self, lines: &Vec<String>) -> usize {
        lines
            .iter()
            .filter(|line| self.count_matches(line, &VOWEL) >= 3)
            .filter(|line| self.count_matches(line, &BAD) == 0)
            .filter(|line| self.count_matches(line, &TWIN) >= 1)
            .count()
    }

    fn part_2(&self, lines: &Vec<String>) -> usize {
        lines
            .iter()
            .filter(|line| self.count_matches(line, &TWO_TWINS) >= 1)
            .filter(|line| {
                self.count_matches(line, &THREE_LETTER_PALINDROME) >= 1
            })
            .count()
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, "ugknbfddgicrmopn", 1,
            self, part_1, "aaa", 1,
            self, part_1, "jchzalrnumimnmhp", 0,
            self, part_1, "haegwjzuvuyypxyu", 0,
            self, part_1, "dvszwmarrgswjxmb", 0,
            self, part_2, "qjhvhtzxzqqjkmpb", 1,
            self, part_2, "xxyxx", 1,
            self, part_2, "uurcxstgmygtbstg", 0,
            self, part_2, "ieodomkazucvgmuy", 0,
            self, part_2, "xyxy", 1
        };
    }
}

fn main() {
    AoC2015_05 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2015_05 {}.samples();
    }
}
