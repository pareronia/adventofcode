#![allow(non_snake_case)]

use aoc::Puzzle;
use std::cmp::Ordering;
use std::collections::HashMap;

#[derive(PartialEq)]
enum Mode {
    UseCorrect,
    UseIncorrect,
}

struct AoC2024_05;

impl AoC2024_05 {
    fn solve(
        &self,
        input: &<AoC2024_05 as aoc::Puzzle>::Input,
        mode: Mode,
    ) -> u32 {
        let (order, updates) = input;
        let mut ans = 0;
        for update in updates {
            let mut correct = update.to_vec();
            correct.sort_by(|a, b| {
                match order.get(a).unwrap_or(&vec![]).contains(b) {
                    true => Ordering::Less,
                    false => Ordering::Greater,
                }
            });
            if !((mode == Mode::UseCorrect) ^ (*update == correct)) {
                ans += correct[correct.len() / 2]
            }
        }
        ans
    }
}

impl aoc::Puzzle for AoC2024_05 {
    type Input = (HashMap<u32, Vec<u32>>, Vec<Vec<u32>>);
    type Output1 = u32;
    type Output2 = u32;

    aoc::puzzle_year_day!(2024, 5);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        let blocks = aoc::to_blocks(&lines);
        let mut order: HashMap<u32, Vec<u32>> = HashMap::new();
        for line in &blocks[0] {
            let (sa, sb) = line.split_once("|").unwrap();
            let (a, b) =
                (sa.parse::<u32>().unwrap(), sb.parse::<u32>().unwrap());
            order.entry(a).and_modify(|e| e.push(b)).or_insert(vec![b]);
        }
        let updates = blocks[1]
            .iter()
            .map(|line| {
                line.split(",").map(|n| n.parse::<u32>().unwrap()).collect()
            })
            .collect();
        (order, updates)
    }

    fn part_1(&self, input: &Self::Input) -> Self::Output1 {
        self.solve(input, Mode::UseCorrect)
    }

    fn part_2(&self, input: &Self::Input) -> Self::Output2 {
        self.solve(input, Mode::UseIncorrect)
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 143,
            self, part_2, TEST, 123
        };
    }
}

fn main() {
    AoC2024_05 {}.run(std::env::args());
}

const TEST: &str = "\
47|53
97|13
97|61
97|47
75|29
61|13
75|53
29|13
97|29
53|29
61|53
97|53
61|29
47|13
75|47
97|75
47|61
75|61
47|29
75|13
53|13

75,47,61,53,29
97,61,53,29,13
75,29,13
75,97,47,61,53
61,13,29
97,13,75,29,47
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2024_05 {}.samples();
    }
}
