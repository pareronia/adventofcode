#![allow(non_snake_case)]

use aoc::{clog, Puzzle};

#[derive(Debug)]
struct Nums {
    nums: Vec<i64>,
    prev: Vec<usize>,
    next: Vec<usize>,
}

impl Nums {
    #[cfg(debug_assertions)]
    fn print(&self, zero: usize) -> Vec<i64> {
        let mut s = vec![];
        let mut tmp = zero;
        loop {
            s.push(self.nums[tmp]);
            tmp = self.next[tmp];
            if tmp == zero {
                break;
            }
        }
        s
    }

    fn find_zero(&self) -> usize {
        self.nums
            .iter()
            .enumerate()
            .filter(|&(_, &num)| num == 0)
            .map(|(i, _)| i)
            .next()
            .unwrap()
    }

    fn round(&mut self) {
        let size = self.nums.len();
        for (i, &to_move) in self.nums.iter().enumerate() {
            if to_move == 0 {
                continue;
            }
            self.prev[self.next[i]] = self.prev[i];
            self.next[self.prev[i]] = self.next[i];
            let (move_to, _amount) = match to_move {
                _ if to_move > 0 => {
                    let mut move_to = self.prev[i];
                    let amount = to_move as usize % (size - 1);
                    (0..amount).for_each(|_| move_to = self.next[move_to]);
                    (move_to, amount)
                }
                _ => {
                    let mut move_to = self.next[i];
                    let amount = to_move.abs() as usize % (size - 1) + 1;
                    (0..amount).for_each(|_| move_to = self.prev[move_to]);
                    (move_to, amount)
                }
            };
            clog!(
                (|| format!(
                    "move {} to {} ({})",
                    self.nums[i], self.nums[move_to], _amount
                ))
            );
            let (before, after) = (move_to, self.next[move_to]);
            self.next[before] = i;
            self.prev[i] = before;
            self.prev[after] = i;
            self.next[i] = after;
        }
    }
}

impl From<Vec<i64>> for Nums {
    fn from(nums: Vec<i64>) -> Nums {
        let size = nums.len();
        let mut prev = vec![usize::MAX; size];
        let mut next = vec![usize::MAX; size];
        (1..=size).for_each(|i| {
            prev[i % size] = (i - 1) % size;
            next[i % size] = (i + 1) % size;
        });
        Nums { nums, prev, next }
    }
}

struct AoC2022_20;

impl AoC2022_20 {
    fn solve(&self, numbers: &Vec<i64>, rounds: usize, factor: i64) -> i64 {
        let mut nums = Nums::from(
            numbers.iter().map(|num| num * factor).collect::<Vec<i64>>(),
        );
        let zero = nums.find_zero();
        (0..rounds).for_each(|_round| {
            clog!((|| format!("Round {_round}")));
            nums.round();
            clog!((|| nums.print(zero)));
        });
        let mut n = zero;
        let mut ans = 0;
        (1..=3000).for_each(|i| {
            n = nums.next[n];
            if i % 1000 == 0 {
                ans += nums.nums[n];
            }
        });
        ans
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
