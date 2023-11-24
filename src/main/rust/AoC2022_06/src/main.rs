#![allow(non_snake_case)]

use aoc::Puzzle;
use itertools::Itertools;

struct AoC2022_06;

impl AoC2022_06 {
    fn solve(&self, line: &str, count: usize) -> usize {
        for i in count..line.chars().count() {
            if line.chars().skip(i - count).take(count).unique().count()
                == count
            {
                return i;
            }
        }
        panic!("Unsolvable");
    }
}

impl aoc::Puzzle for AoC2022_06 {
    type Input = String;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2022, 6);

    fn parse_input(&self, lines: Vec<String>) -> String {
        lines[0].to_string()
    }

    fn part_1(&self, input: &String) -> usize {
        self.solve(input, 4)
    }

    fn part_2(&self, input: &String) -> usize {
        self.solve(input, 14)
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, "mjqjpqmgbljsphdztnvjfqwrcgsmlb", 7,
            self, part_1, "bvwbjplbgvbhsrlpgdmjqwftvncz", 5,
            self, part_1, "nppdvjthqldpwncqszvftbrmjlhg", 6,
            self, part_1, "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 10,
            self, part_1, "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 11,
            self, part_2, "mjqjpqmgbljsphdztnvjfqwrcgsmlb", 19,
            self, part_2, "bvwbjplbgvbhsrlpgdmjqwftvncz", 23,
            self, part_2, "nppdvjthqldpwncqszvftbrmjlhg", 23,
            self, part_2, "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg", 29,
            self, part_2, "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw", 26
        };
    }
}

fn main() {
    AoC2022_06 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2022_06 {}.samples();
    }
}
