#![allow(non_snake_case)]

use aoc::Puzzle;
use std::ops::RangeInclusive;

type RangePair = (RangeInclusive<u32>, RangeInclusive<u32>);

trait ContainsRange {
    fn contains_range(&self, other: &RangeInclusive<u32>) -> bool;
}

impl ContainsRange for RangeInclusive<u32> {
    fn contains_range(&self, other: &RangeInclusive<u32>) -> bool {
        self.contains(other.start()) && self.contains(other.end())
    }
}

trait IsOverlappedBy {
    fn is_overlapped_by(&self, other: &RangeInclusive<u32>) -> bool;
}

impl IsOverlappedBy for RangeInclusive<u32> {
    fn is_overlapped_by(&self, other: &RangeInclusive<u32>) -> bool {
        other.contains(self.start())
            || other.contains(self.end())
            || self.contains(other.start())
    }
}

struct AoC2022_04 {}

impl AoC2022_04 {
    fn solve2(
        &self,
        input: &[RangePair],
        method: impl Fn(&RangePair) -> bool,
    ) -> usize {
        input.iter().filter(|pair| method(pair)).count()
    }
}

impl aoc::Puzzle for AoC2022_04 {
    type Input = Vec<RangePair>;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2022, 4);

    fn parse_input(&self, lines: Vec<String>) -> Vec<RangePair> {
        lines
            .iter()
            .map(|line| aoc::uints_with_check(line, 4))
            .map(|v| (v[0]..=v[1], v[2]..=v[3]))
            .collect()
    }

    fn part_1(&self, input: &Vec<RangePair>) -> usize {
        self.solve2(input, |pair| {
            pair.0.contains_range(&pair.1) || pair.1.contains_range(&pair.0)
        })
    }

    fn part_2(&self, input: &Vec<RangePair>) -> usize {
        self.solve2(input, |pair| pair.0.is_overlapped_by(&pair.1))
    }

    fn samples(&self) {
        #[rustfmt::skip]
        let test =
            "2-4,6-8\n\
             2-3,4-5\n\
             5-7,7-9\n\
             2-8,3,7\n\
             6-6,4-6\n\
             2-6,4-8";
        aoc::puzzle_samples! {
            self, part_1, test, 2,
            self, part_2, test, 4
        };
    }
}

fn main() {
    AoC2022_04 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2022_04 {}.samples();
    }
}
