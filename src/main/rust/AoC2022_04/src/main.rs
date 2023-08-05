#![allow(non_snake_case)]

use aoc::Puzzle;
use std::collections::HashSet;

struct AoC2022_04 {}

impl AoC2022_04 {
    fn solve<F>(
        &self,
        input: &Vec<((u32, u32), (u32, u32))>,
        mut method: F,
    ) -> usize
    where
        F: FnMut(&HashSet<u32>, &HashSet<u32>) -> bool,
    {
        input
            .iter()
            .filter(|(r1, r2)| {
                let s1: HashSet<u32> = (r1.0..=r1.1).collect();
                let s2: HashSet<u32> = (r2.0..=r2.1).collect();
                method(&s1, &s2)
            })
            .count()
    }
}

impl aoc::Puzzle for AoC2022_04 {
    type Input = Vec<((u32, u32), (u32, u32))>;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2022, 4);

    fn parse_input(&self, lines: Vec<String>) -> Vec<((u32, u32), (u32, u32))> {
        lines
            .iter()
            .map(|line| aoc::uints_with_check(line, 4))
            .map(|v| ((v[0], v[1]), (v[2], v[3])))
            .collect()
    }

    fn part_1(&self, input: &Vec<((u32, u32), (u32, u32))>) -> usize {
        self.solve(input, |s1, s2| s1.is_subset(s2) || s2.is_subset(s1))
    }

    fn part_2(&self, input: &Vec<((u32, u32), (u32, u32))>) -> usize {
        self.solve(input, |s1, s2| !s1.is_disjoint(s2))
    }

    fn samples(&self) {
        let test = "2-4,6-8\n\
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
