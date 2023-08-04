#![allow(non_snake_case)]

use std::iter::zip;

static ELEMENTS: &'static [i16] = &[0, 1, 0, -1];
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
    fn new() -> Box<dyn aoc::Puzzle> {
        Box::new(AoC2019_16 {})
    }

    fn parse(&self, lines: &Vec<String>) -> Vec<i16> {
        lines[0]
            .chars()
            .filter_map(|c| c.to_digit(10))
            .map(|i| i as i16)
            .collect()
    }

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
    aoc::puzzle_year_day!(2019, 16);

    fn part_1(&self, lines: &Vec<String>) -> String {
        let mut nums = self.parse(&lines);
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

    fn part_2(&self, lines: &Vec<String>) -> String {
        let mut nums = self.parse(&lines);
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
        let test1 = aoc::split_lines("80871224585914546619083218645595");
        let test2 = aoc::split_lines("19617804207202209144916044189917");
        let test3 = aoc::split_lines("69317163492948606335995924319873");
        let test4 = aoc::split_lines("03036732577212944063491565474664");
        let test5 = aoc::split_lines("02935109699940807407585447034323");
        let test6 = aoc::split_lines("03081770884921959731165446850517");

        assert_eq!(self.part_1(&test1), "24176176");
        assert_eq!(self.part_1(&test2), "73745418");
        assert_eq!(self.part_1(&test3), "52432133");
        assert_eq!(self.part_2(&test4), "84462026");
        assert_eq!(self.part_2(&test5), "78725270");
        assert_eq!(self.part_2(&test6), "53553731");
    }
}

fn main() {
    AoC2019_16::new().run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2019_16::new().samples();
    }
}
