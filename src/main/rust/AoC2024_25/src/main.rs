#![allow(non_snake_case)]

use aoc::Puzzle;
use itertools::Itertools;

struct AoC2024_25;

impl AoC2024_25 {}

impl aoc::Puzzle for AoC2024_25 {
    type Input = (Vec<u64>, Vec<u64>);
    type Output1 = u64;
    type Output2 = String;

    aoc::puzzle_year_day!(2024, 25);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        let blocks = aoc::to_blocks(&lines);
        let mut keys: Vec<u64> = Vec::with_capacity(blocks.len());
        let mut locks: Vec<u64> = Vec::with_capacity(blocks.len());
        for block in blocks.iter() {
            let mut n: u64 = 0;
            let h = block.len();
            for (r, line) in block[1..h - 1].iter().enumerate() {
                line.chars().enumerate().for_each(|(c, ch)| {
                    if ch == '#' {
                        n += 1 << ((h * c) + r);
                    }
                });
            }
            if block[0].chars().nth(0).unwrap() == '#' {
                locks.push(n);
            } else {
                keys.push(n);
            }
        }
        (keys, locks)
    }

    fn part_1(&self, input: &Self::Input) -> Self::Output1 {
        let (keys, locks) = input;
        keys.iter()
            .cartesian_product(locks.iter())
            .map(|(key, lock)| (key & lock == 0) as u64)
            .sum()
    }

    fn part_2(&self, _: &Self::Input) -> Self::Output2 {
        String::from("🎄")
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 3
        };
    }
}

fn main() {
    AoC2024_25 {}.run(std::env::args());
}

const TEST: &str = "\
#####
.####
.####
.####
.#.#.
.#...
.....

#####
##.##
.#.##
...##
...#.
...#.
.....

.....
#....
#....
#...#
#.#.#
#.###
#####

.....
.....
#.#..
###..
###.#
###.#
#####

.....
.....
.....
#....
#.#..
#.#.#
#####
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2024_25 {}.samples();
    }
}
