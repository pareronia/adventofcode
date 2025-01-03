#![allow(non_snake_case)]

use aoc::Puzzle;
use std::collections::HashMap;

struct AoC2024_19 {}

impl AoC2024_19 {
    #[allow(clippy::only_used_in_recursion)]
    fn count(
        &self,
        cache: &mut HashMap<String, usize>,
        design: String,
        towels: &[String],
    ) -> usize {
        if let Some(ans) = cache.get(&design) {
            return *ans;
        }
        if design.is_empty() {
            return 1;
        }
        let ans = towels
            .iter()
            .filter(|towel| design.starts_with(*towel))
            .map(|towel| {
                self.count(cache, String::from(&design[towel.len()..]), towels)
            })
            .sum();
        cache.insert(design, ans);
        ans
    }
}

impl aoc::Puzzle for AoC2024_19 {
    type Input = (Vec<String>, Vec<String>);
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2024, 19);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        let towels = lines[0].split(", ").map(String::from).collect();
        let designs = lines[2..].to_vec();
        (towels, designs)
    }

    fn part_1(&self, input: &Self::Input) -> Self::Output1 {
        let (towels, designs) = input;
        let mut cache: HashMap<String, usize> = HashMap::new();
        designs
            .iter()
            .filter(|design| {
                self.count(&mut cache, String::from(*design), towels) > 0
            })
            .count()
    }

    fn part_2(&self, input: &Self::Input) -> Self::Output2 {
        let (towels, designs) = input;
        let mut cache: HashMap<String, usize> = HashMap::new();
        designs
            .iter()
            .map(|design| self.count(&mut cache, String::from(design), towels))
            .sum()
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 6,
            self, part_2, TEST, 16
        };
    }
}

fn main() {
    AoC2024_19 {}.run(std::env::args());
}

const TEST: &str = "\
r, wr, b, g, bwu, rb, gb, br

brwrr
bggr
gbbr
rrbgbr
ubwu
bwurrg
brgr
bbrgwb
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2024_19 {}.samples();
    }
}
