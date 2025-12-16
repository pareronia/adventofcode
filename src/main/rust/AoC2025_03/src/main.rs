#![allow(non_snake_case)]

use aoc::Puzzle;

struct AoC2025_03;

impl AoC2025_03 {
    fn solve(&self, lines: &[String], digits: usize) -> u64 {
        fn solve_line(line: &str, digits: usize) -> u64 {
            let mut pos = 0;
            let mut s = String::with_capacity(digits);
            for i in (1..=digits).rev() {
                let mut best = '0';
                let mut new_pos = pos;
                for j in pos..=line.len() - i {
                    let val = line.chars().nth(j).unwrap();
                    if val > best {
                        best = val;
                        new_pos = j + 1;
                    }
                }
                s.push(best);
                pos = new_pos;
            }
            s.parse::<u64>().unwrap()
        }

        lines.iter().map(|line| solve_line(line, digits)).sum()
    }
}

impl aoc::Puzzle for AoC2025_03 {
    type Input = Vec<String>;
    type Output1 = u64;
    type Output2 = u64;

    aoc::puzzle_year_day!(2025, 3);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        lines
    }

    fn part_1(&self, input: &Self::Input) -> Self::Output1 {
        self.solve(input, 2)
    }

    fn part_2(&self, input: &Self::Input) -> Self::Output2 {
        self.solve(input, 12)
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 357,
            self, part_2, TEST, 3121910778619_u64
        };
    }
}

fn main() {
    AoC2025_03 {}.run(std::env::args());
}

const TEST: &str = "\
987654321111111
811111111111119
234234234234278
818181911112111
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2025_03 {}.samples();
    }
}
