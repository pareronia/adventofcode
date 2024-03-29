#![allow(non_snake_case)]

use aoc::Puzzle;

#[derive(Debug)]
struct Race {
    time: u64,
    distance: u64,
}

struct AoC2023_06;

impl AoC2023_06 {
    #![allow(dead_code)]
    fn brute_force(&self, race: &Race) -> u64 {
        (1..race.time)
            .filter(|t| (race.time - t) * t > race.distance)
            .count() as u64
    }

    fn equation(&self, race: &Race) -> u64 {
        let r = ((race.time.pow(2) - 4 * race.distance) as f64).sqrt();
        let fx1 = (race.time as f64 - r) / 2.0;
        let fx2 = (race.time as f64 + r) / 2.0;
        let x1 = match (fx1.ceil() - fx1).abs() < f64::EPSILON {
            true => fx1 as u64 + 1,
            false => fx1.ceil() as u64,
        };
        let x2 = match (fx2 - fx2.floor()).abs() < f64::EPSILON {
            true => fx2 as u64 - 1,
            false => fx2.floor() as u64,
        };
        x2 - x1 + 1
    }

    fn solve(&self, races: &[Race]) -> u64 {
        races
            .iter()
            .map(|race| self.equation(race))
            .product::<u64>()
    }
}

impl aoc::Puzzle for AoC2023_06 {
    type Input = Vec<Race>;
    type Output1 = u64;
    type Output2 = u64;

    aoc::puzzle_year_day!(2023, 6);

    fn parse_input(&self, lines: Vec<String>) -> Vec<Race> {
        let values: Vec<Vec<u64>> = (0..=1)
            .map(|i| {
                lines[i]
                    .split(':')
                    .nth(1)
                    .unwrap()
                    .split_whitespace()
                    .map(|x| x.parse().unwrap())
                    .collect()
            })
            .collect();
        std::iter::zip(&values[0], &values[1])
            .map(|(&time, &distance)| Race { time, distance })
            .collect()
    }

    fn part_1(&self, races: &Vec<Race>) -> u64 {
        self.solve(races)
    }

    fn part_2(&self, races: &Vec<Race>) -> u64 {
        let fs = [|r: &Race| r.time, |r: &Race| r.distance];
        let new: Vec<u64> = (0..=1)
            .map(|i| {
                races
                    .iter()
                    .map(|r| fs[i](r))
                    .map(|t| t.to_string())
                    .collect::<String>()
                    .parse()
                    .unwrap()
            })
            .collect();
        self.solve(&[Race {
            time: new[0],
            distance: new[1],
        }])
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 288,
            self, part_2, TEST, 71503
        };
    }
}

fn main() {
    AoC2023_06 {}.run(std::env::args());
}

const TEST: &str = "\
Time:      7  15   30
Distance:  9  40  200
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2023_06 {}.samples();
    }
}
