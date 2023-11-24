#![allow(non_snake_case)]

use aoc::Puzzle;

struct AoC2022_01;

impl AoC2022_01 {
    fn solve(&self, groups: &[Vec<u32>], count: usize) -> u32 {
        let mut sums: Vec<u32> =
            groups.iter().map(|group| group.iter().sum()).collect();
        sums.sort();
        sums.iter().rev().take(count).sum()
    }
}

impl aoc::Puzzle for AoC2022_01 {
    type Input = Vec<Vec<u32>>;
    type Output1 = u32;
    type Output2 = u32;

    aoc::puzzle_year_day!(2022, 1);

    fn parse_input(&self, lines: Vec<String>) -> Vec<Vec<u32>> {
        let blocks = aoc::to_blocks(&lines);
        blocks
            .iter()
            .map(|block| {
                block
                    .iter()
                    .map(|line| line.parse::<u32>().unwrap())
                    .collect()
            })
            .collect()
    }

    fn part_1(&self, input: &Vec<Vec<u32>>) -> u32 {
        self.solve(input, 1)
    }

    fn part_2(&self, input: &Vec<Vec<u32>>) -> u32 {
        self.solve(input, 3)
    }

    fn samples(&self) {
        let test = "1000\n\
             2000\n\
             3000\n\
             \n\
             4000\n\
             \n\
             5000\n\
             6000\n\
             \n\
             7000\n\
             8000\n\
             9000\n\
             \n\
             10000";
        aoc::puzzle_samples! {
            self, part_1, test, 24000,
            self, part_2, test, 45000
        };
    }
}

fn main() {
    AoC2022_01 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2022_01 {}.samples();
    }
}
