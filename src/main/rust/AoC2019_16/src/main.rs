#![allow(non_snake_case)]

use aoc::Puzzle;
use std::iter::zip;

static ELEMENTS: &[i16] = &[0, 1, 0, -1];
const PHASES: usize = 100;

struct Pattern {
    repeat: usize,
    i: usize,
    j: usize,
}

impl Iterator for Pattern {
    type Item = i16;

    fn next(&mut self) -> Option<Self::Item> {
        let ans = ELEMENTS[self.j];
        self.i += 1;
        if self.i == self.repeat {
            self.i = 0;
            self.j = (self.j + 1) % ELEMENTS.len();
        }
        Some(ans)
    }
}

struct AoC2019_16 {}

impl AoC2019_16 {
    fn pattern(&self, repeat: usize) -> Pattern {
        let mut pattern = Pattern { repeat, i: 0, j: 0 };
        pattern.next();
        pattern
    }

    fn as_string(&self, ints: &[i16]) -> String {
        ints.iter()
            .filter_map(|i| char::from_digit(*i as u32, 10))
            .collect()
    }
}

impl aoc::Puzzle for AoC2019_16 {
    type Input = Vec<i16>;
    type Output1 = String;
    type Output2 = String;

    aoc::puzzle_year_day!(2019, 16);

    fn parse_input(&self, lines: Vec<String>) -> Vec<i16> {
        lines[0]
            .chars()
            .filter_map(|c| c.to_digit(10))
            .map(|i| i as i16)
            .collect()
    }

    fn part_1(&self, input: &Vec<i16>) -> String {
        let mut nums = input.clone();
        let mut digits = nums.clone();
        (0..PHASES).for_each(|_| {
            (0..nums.len()).for_each(|j| {
                digits[j] = zip(&nums, self.pattern(j + 1))
                    .map(|(a, b)| a * b)
                    .sum::<i16>()
                    .abs()
                    % 10;
            });
            nums = digits.clone();
        });
        self.as_string(&nums[..8])
    }

    fn part_2(&self, input: &Vec<i16>) -> String {
        let mut nums = input.clone();
        let offset = self.as_string(&nums[..7]).parse::<usize>().unwrap();
        let tailsize: usize = 10_000 * nums.len() - offset;
        let repeat = tailsize / nums.len() + 1;
        let copy = nums.clone();
        (0..repeat).for_each(|_| {
            nums.append(&mut copy.clone());
        });
        (0..PHASES).for_each(|_| {
            (0..=nums.len() - 2)
                .rev()
                .for_each(|j| nums[j] = (nums[j] + nums[j + 1]) % 10);
        });
        self.as_string(&nums[nums.len() - tailsize..][..8])
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, "80871224585914546619083218645595", "24176176",
            self, part_1, "19617804207202209144916044189917", "73745418",
            self, part_1, "69317163492948606335995924319873", "52432133",
            self, part_2, "03036732577212944063491565474664", "84462026",
            self, part_2, "02935109699940807407585447034323", "78725270",
            self, part_2, "03081770884921959731165446850517", "53553731"
        };
    }
}

fn main() {
    AoC2019_16 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2019_16 {}.samples();
    }
}
