#![allow(non_snake_case)]

use aoc::Puzzle;

const UP: char = '(';
const DOWN: char = ')';

enum Direction {
    Up = 1,
    Down = -1,
}

impl Direction {
    fn from_char(ch: &char) -> Self {
        match *ch {
            UP => Direction::Up,
            DOWN => Direction::Down,
            _ => unreachable!(),
        }
    }
}

struct AoC2015_01;

impl AoC2015_01 {}

impl aoc::Puzzle for AoC2015_01 {
    type Input = String;
    type Output1 = i32;
    type Output2 = usize;

    aoc::puzzle_year_day!(2015, 1);

    fn parse_input(&self, lines: Vec<String>) -> String {
        lines[0].clone()
    }

    fn part_1(&self, input: &String) -> i32 {
        input
            .chars()
            .map(|ch| Direction::from_char(&ch) as i32)
            .sum()
    }

    fn part_2(&self, input: &String) -> usize {
        let mut sum = 0;
        input
            .chars()
            .map(|ch| Direction::from_char(&ch) as i32)
            .enumerate()
            .skip_while(|(_, dir)| {
                sum += dir;
                sum != -1
            })
            .map(|(i, _)| i + 1)
            .next()
            .unwrap()
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, "(())", 0,
            self, part_1, "()()", 0,
            self, part_1, "(((", 3,
            self, part_1, "))(((((", 3,
            self, part_1, "())", -1,
            self, part_1, "))(", -1,
            self, part_1, ")))", -3,
            self, part_1, ")())())", -3,
            self, part_2, ")", 1,
            self, part_2, "()())", 5
        };
    }
}

fn main() {
    AoC2015_01 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2015_01 {}.samples();
    }
}
