#![allow(non_snake_case)]

use aoc::Puzzle;
use std::collections::HashMap;

struct AoC2024_22;

impl AoC2024_22 {
    fn secret(&self, mut num: u64) -> u64 {
        num = (num ^ (num * 64)) % 16777216;
        num = (num ^ (num / 32)) % 16777216;
        (num ^ (num * 2048)) % 16777216
    }
}

impl aoc::Puzzle for AoC2024_22 {
    type Input = Vec<u64>;
    type Output1 = u64;
    type Output2 = u16;

    aoc::puzzle_year_day!(2024, 22);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        lines
            .iter()
            .map(|line| line.parse::<u64>().unwrap())
            .collect()
    }

    fn part_1(&self, seeds: &Self::Input) -> Self::Output1 {
        let mut ans = 0;
        seeds.iter().for_each(|n| {
            let mut num = *n;
            for _ in 0..2000 {
                num = self.secret(num);
            }
            ans += num
        });
        ans
    }

    fn part_2(&self, seeds: &Self::Input) -> Self::Output2 {
        let mut p: HashMap<usize, u16> = HashMap::new();
        let mut seen = [u16::MAX; 19_usize.pow(4)];
        for (i, n) in seeds.iter().enumerate() {
            let i = i as u16;
            let mut num = *n;
            let na = num;
            let nb = self.secret(na);
            let nc = self.secret(nb);
            let nd = self.secret(nc);
            let mut a;
            let mut b = (9 + na % 10 - nb % 10) as usize;
            let mut c = (9 + nb % 10 - nc % 10) as usize;
            let mut d = (9 + nc % 10 - nd % 10) as usize;
            num = nd;
            let mut prev_price = num % 10;
            for _ in 3..2000 {
                num = self.secret(num);
                let price = num % 10;
                (a, b, c, d) = (b, c, d, (9 + prev_price - price) as usize);
                prev_price = price;
                let key = 6589 * a + 361 * b + 19 * c + d;
                if seen[key] == i {
                    continue;
                }
                seen[key] = i;
                p.entry(key)
                    .and_modify(|v| *v += price as u16)
                    .or_insert(price as u16);
            }
        }
        *p.values().max().unwrap()
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST1, 37327623,
            self, part_2, TEST2, 23
        };
    }
}

fn main() {
    AoC2024_22 {}.run(std::env::args());
}

const TEST1: &str = "\
1
10
100
2024
";
const TEST2: &str = "\
1
2
3
2024
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2024_22 {}.samples();
    }
}
