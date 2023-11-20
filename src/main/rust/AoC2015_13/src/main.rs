#![allow(non_snake_case)]

use itertools::Itertools;
use std::collections::HashMap;

#[derive(Debug)]
struct Happiness {
    matrix: Vec<Vec<i16>>,
}

impl Happiness {
    fn from_input(input: &[String]) -> Self {
        let mut map: HashMap<String, usize> = HashMap::new();
        let mut cnt = 0;
        let values: HashMap<(usize, usize), i16> = input
            .iter()
            .map(|line| line.replace('.', ""))
            .map(|line| {
                line.split_whitespace()
                    .map(str::to_string)
                    .collect::<Vec<String>>()
            })
            .map(|splits| {
                let d1 = splits[0].clone();
                let d2 = splits[10].clone();
                let idx1 = *map.entry(d1).or_insert_with(|| {
                    let x = cnt;
                    cnt += 1;
                    x
                });
                let idx2 = *map.entry(d2).or_insert_with(|| {
                    let x = cnt;
                    cnt += 1;
                    x
                });
                let key = (idx1, idx2);
                let val = splits[3].parse::<i16>().unwrap();
                match &splits[2][..] {
                    "lose" => (key, -val),
                    _ => (key, val),
                }
            })
            .collect();
        let mut matrix = vec![vec![0_i16; map.len() + 1]; map.len() + 1];
        values
            .iter()
            .for_each(|(key, val)| matrix[key.0][key.1] = *val);
        Self { matrix }
    }

    fn solve(&self, size: usize) -> i16 {
        let idxs = (0..size).collect_vec();
        idxs.iter()
            .permutations(idxs.len())
            .map(|p| {
                (0..p.len())
                    .map(|i| {
                        let d1 = p[i];
                        let d2 = p[(i + 1) % p.len()];
                        self.matrix[*d1][*d2] + self.matrix[*d2][*d1]
                    })
                    .sum()
            })
            .max()
            .unwrap()
    }

    fn optimal_happiness_change_without_me(&self) -> i16 {
        self.solve(self.matrix.len() - 1)
    }

    fn optimal_happiness_change_with_me(&self) -> i16 {
        self.solve(self.matrix.len())
    }
}

use aoc::Puzzle;

struct AoC2015_13;

impl AoC2015_13 {}

impl aoc::Puzzle for AoC2015_13 {
    type Input = Happiness;
    type Output1 = i16;
    type Output2 = i16;

    aoc::puzzle_year_day!(2015, 13);

    fn parse_input(&self, lines: Vec<String>) -> Happiness {
        Happiness::from_input(&lines)
    }

    fn part_1(&self, input: &Happiness) -> i16 {
        input.optimal_happiness_change_without_me()
    }

    fn part_2(&self, input: &Happiness) -> i16 {
        input.optimal_happiness_change_with_me()
    }

    fn samples(&self) {
        #[rustfmt::skip]
        let test =
            "Alice would gain 54 happiness units by sitting next to Bob.\n\
             Alice would lose 79 happiness units by sitting next to Carol.\n\
             Alice would lose 2 happiness units by sitting next to David.\n\
             Bob would gain 83 happiness units by sitting next to Alice.\n\
             Bob would lose 7 happiness units by sitting next to Carol.\n\
             Bob would lose 63 happiness units by sitting next to David.\n\
             Carol would lose 62 happiness units by sitting next to Alice.\n\
             Carol would gain 60 happiness units by sitting next to Bob.\n\
             Carol would gain 55 happiness units by sitting next to David.\n\
             David would gain 46 happiness units by sitting next to Alice.\n\
             David would lose 7 happiness units by sitting next to Bob.\n\
             David would gain 41 happiness units by sitting next to Carol.";

        aoc::puzzle_samples! {
            self, part_1, test, 330
        };
    }
}

fn main() {
    AoC2015_13 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2015_13 {}.samples();
    }
}
