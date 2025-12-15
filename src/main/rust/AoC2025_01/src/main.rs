#![allow(non_snake_case)]

use aoc::Puzzle;

const START: isize = 50;
const TOTAL: isize = 100;

enum Count {
    LandedOnZero,
    PassedByZero,
}

impl Count {
    fn count(&self, dial: isize, rotation: isize) -> usize {
        match self {
            Count::LandedOnZero => {
                if (dial + rotation) % TOTAL == 0 {
                    1
                } else {
                    0
                }
            }
            Count::PassedByZero => {
                if rotation >= 0 {
                    ((dial + rotation) / TOTAL) as usize
                } else {
                    (((TOTAL - dial) % TOTAL - rotation) / TOTAL) as usize
                }
            }
        }
    }
}

struct AoC2025_01;

impl AoC2025_01 {
    fn solve(&self, rotations: &[isize], count: Count) -> usize {
        let mut dial = START;
        let mut ans = 0;
        rotations.iter().for_each(|rotation| {
            ans += count.count(dial, *rotation);
            dial = (dial + *rotation).rem_euclid(TOTAL);
        });
        ans
    }
}

impl aoc::Puzzle for AoC2025_01 {
    type Input = Vec<isize>;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2025, 1);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        lines
            .iter()
            .map(|line| {
                let s = match line.chars().nth(0).unwrap() {
                    'R' => 1,
                    'L' => -1,
                    _ => panic!(),
                };
                s * line[1..].parse::<isize>().unwrap()
            })
            .collect::<Vec<_>>()
    }

    fn part_1(&self, rotations: &Self::Input) -> Self::Output1 {
        self.solve(rotations, Count::LandedOnZero)
    }

    fn part_2(&self, rotations: &Self::Input) -> Self::Output2 {
        self.solve(rotations, Count::PassedByZero)
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 3,
            self, part_2, TEST, 6
        };
    }
}

fn main() {
    AoC2025_01 {}.run(std::env::args());
}

const TEST: &str = "\
L68
L30
R48
L5
R60
L55
L1
L99
R14
L82
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2025_01 {}.samples();
    }
}
