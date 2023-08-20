#![allow(non_snake_case)]
use aoc::Puzzle;
use md5::{Digest, Md5};

struct AdventCoinsMiner {
    secret_key: String,
}

impl AdventCoinsMiner {
    fn find_md5_starting_with_zeroes(&self, zeroes: usize) -> usize {
        fn check_zeroes(digest: &[u8], zeroes: usize) -> bool {
            let mut cnt = 0;
            for i in 0..zeroes / 2 + zeroes % 2 {
                let c = digest[i];
                if c & 0xF0 != 0 {
                    break;
                }
                cnt += 1;
                if c & 0x0F != 0 {
                    break;
                }
                cnt += 1;
            }
            cnt == zeroes
        }

        (1..)
            .skip_while(|i| {
                let mut hasher = Md5::new();
                let data = String::from(&self.secret_key) + &i.to_string();
                hasher.update(data);
                let hash = hasher.finalize();
                !check_zeroes(hash.as_slice(), zeroes)
            })
            .next()
            .unwrap()
    }
}

struct AoC2015_04 {}

impl AoC2015_04 {}

impl aoc::Puzzle for AoC2015_04 {
    type Input = AdventCoinsMiner;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2015, 4);

    fn parse_input(&self, lines: Vec<String>) -> AdventCoinsMiner {
        AdventCoinsMiner {
            secret_key: lines[0].clone(),
        }
    }

    fn part_1(&self, miner: &AdventCoinsMiner) -> usize {
        miner.find_md5_starting_with_zeroes(5)
    }

    fn part_2(&self, miner: &AdventCoinsMiner) -> usize {
        miner.find_md5_starting_with_zeroes(6)
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, "abcdef", 609_043,
            self, part_1, "pqrstuv", 1_048_970
        };
    }
}

fn main() {
    AoC2015_04 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2015_04 {}.samples();
    }
}
