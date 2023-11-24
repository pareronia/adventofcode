#![allow(non_snake_case)]

use aoc::Puzzle;
use std::{iter::Sum, ops::AddAssign};

const UP: char = '(';
const DOWN: char = ')';

#[derive(Clone, Copy)]
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

impl AddAssign<&Direction> for i32 {
    fn add_assign(&mut self, rhs: &Direction) {
        *self = *self + *rhs as i32
    }
}

impl<'a> Sum<&'a Direction> for i32 {
    fn sum<I>(iter: I) -> i32
    where
        I: Iterator<Item = &'a Direction>,
    {
        iter.fold(0, |a, b| a + *b as i32)
    }
}

struct AoC2015_01;

impl AoC2015_01 {}

impl aoc::Puzzle for AoC2015_01 {
    type Input = Vec<Direction>;
    type Output1 = i32;
    type Output2 = usize;

    aoc::puzzle_year_day!(2015, 1);

    fn parse_input(&self, lines: Vec<String>) -> Vec<Direction> {
        lines[0]
            .chars()
            .map(|ch| Direction::from_char(&ch))
            .collect()
    }

    fn part_1(&self, input: &Vec<Direction>) -> i32 {
        input.iter().sum()
    }

    fn part_2(&self, input: &Vec<Direction>) -> usize {
        let mut sum: i32 = 0;
        input
            .iter()
            .enumerate()
            .skip_while(|(_, dir)| {
                sum += *dir;
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
