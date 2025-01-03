#![allow(non_snake_case)]

use aoc::Puzzle;
use std::collections::HashSet;

#[derive(Eq, Hash, PartialEq)]
enum Op {
    Add,
    Multiply,
    Concatenate,
}

struct AoC2024_07;

impl AoC2024_07 {
    fn solve(
        &self,
        input: &<AoC2024_07 as aoc::Puzzle>::Input,
        ops: &HashSet<Op>,
    ) -> u64 {
        fn can_obtain(sol: u64, terms: &[u64], ops: &HashSet<Op>) -> bool {
            if terms.len() == 1 {
                return sol == terms[0];
            }
            let last = terms.last().unwrap();
            let prev_terms = &terms[..terms.len() - 1];
            if ops.contains(&Op::Multiply)
                && sol % last == 0
                && can_obtain(sol.div_euclid(*last), prev_terms, ops)
            {
                return true;
            }
            if ops.contains(&Op::Add)
                && sol > *last
                && can_obtain(sol - *last, prev_terms, ops)
            {
                return true;
            }
            if ops.contains(&Op::Concatenate) {
                let (s_sol, s_last) = (sol.to_string(), last.to_string());
                if s_sol.len() > s_last.len() && s_sol.ends_with(&s_last) {
                    let new_sol = &s_sol[..s_sol.len() - s_last.len()];
                    if can_obtain(
                        new_sol.parse::<u64>().unwrap(),
                        prev_terms,
                        ops,
                    ) {
                        return true;
                    }
                }
            }
            false
        }

        input
            .iter()
            .filter(|(sol, terms)| can_obtain(*sol, terms, ops))
            .map(|(sol, _)| sol)
            .sum()
    }
}

impl aoc::Puzzle for AoC2024_07 {
    type Input = Vec<(u64, Vec<u64>)>;
    type Output1 = u64;
    type Output2 = u64;

    aoc::puzzle_year_day!(2024, 7);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        lines
            .iter()
            .map(|line| {
                let (left, right) = line.split_once(": ").unwrap();
                let sol = left.parse::<u64>().unwrap();
                let terms = right
                    .split_whitespace()
                    .map(|s| s.parse::<u64>().unwrap())
                    .collect();
                (sol, terms)
            })
            .collect()
    }

    fn part_1(&self, input: &Self::Input) -> Self::Output1 {
        self.solve(input, &HashSet::from([Op::Add, Op::Multiply]))
    }

    fn part_2(&self, input: &Self::Input) -> Self::Output2 {
        self.solve(
            input,
            &HashSet::from([Op::Add, Op::Multiply, Op::Concatenate]),
        )
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 3749,
            self, part_2, TEST, 11387
        };
    }
}

fn main() {
    AoC2024_07 {}.run(std::env::args());
}

const TEST: &str = "\
190: 10 19
3267: 81 40 27
83: 17 5
156: 15 6
7290: 6 8 6 15
161011: 16 10 13
192: 17 8 14
21037: 9 7 18 13
292: 11 6 16 20
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2024_07 {}.samples();
    }
}
