#![allow(non_snake_case)]

use aoc::Puzzle;

struct AoC2022_20;

impl AoC2022_20 {
    fn solve(&self, numbers: &[i64], rounds: usize, factor: i64) -> i64 {
        let nums = numbers.iter().map(|num| num * factor).collect::<Vec<i64>>();
        let mut idxs: Vec<usize> = (0..nums.len()).collect();
        (0..rounds).for_each(|_| {
            for (i, &num) in nums.iter().enumerate() {
                let idx = idxs.iter().position(|&idx| idx == i).unwrap();
                idxs.remove(idx);
                let new_idx = (idx as i64 + num).rem_euclid(idxs.len() as i64);
                idxs.insert(new_idx as usize, i);
            }
        });
        let orig_zero_idx = nums.iter().position(|&i| i == 0).unwrap();
        let zero_idx = idxs.iter().position(|&i| i == orig_zero_idx).unwrap();
        (1000..=3000)
            .step_by(1000)
            .map(|i| nums[idxs[(zero_idx + i) % idxs.len()]])
            .sum()
    }
}

impl aoc::Puzzle for AoC2022_20 {
    type Input = Vec<i64>;
    type Output1 = i64;
    type Output2 = i64;

    aoc::puzzle_year_day!(2022, 20);

    fn parse_input(&self, lines: Vec<String>) -> Vec<i64> {
        lines
            .iter()
            .map(|line| line.parse::<i64>().unwrap())
            .collect()
    }

    fn part_1(&self, numbers: &Vec<i64>) -> i64 {
        self.solve(numbers, 1, 1)
    }

    fn part_2(&self, numbers: &Vec<i64>) -> i64 {
        self.solve(numbers, 10, 811_589_153)
    }

    fn samples(&self) {
        #[rustfmt::skip]
        let test =
            "1\n\
             2\n\
             -3\n\
             3\n\
             -2\n\
             0\n\
             4";
        aoc::puzzle_samples! {
            self, part_1, test, 3,
            self, part_2, test, 1_623_178_306
        };
    }
}

fn main() {
    AoC2022_20 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2022_20 {}.samples();
    }
}
