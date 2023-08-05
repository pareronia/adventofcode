#![allow(non_snake_case)]

use aoc::Puzzle;

struct AoC2019_01;

impl AoC2019_01 {
    fn fuel_for_mass(&self, m: &u32) -> u32 {
        match m / 3 {
            f if f > 2 => f - 2,
            _ => 0,
        }
    }

    fn rocket_equation(&self, m: &u32) -> u32 {
        let mut total_fuel = 0;
        let mut fuel = self.fuel_for_mass(m);
        while fuel > 0 {
            total_fuel += fuel;
            fuel = self.fuel_for_mass(&fuel);
        }
        total_fuel
    }

    fn solve<F>(&self, input: &Vec<u32>, mut method: F) -> u32
    where
        F: FnMut(&u32) -> u32,
    {
        input.iter().map(|m| method(m)).sum()
    }
}

impl aoc::Puzzle for AoC2019_01 {
    type Input = Vec<u32>;
    type Output1 = u32;
    type Output2 = u32;

    aoc::puzzle_year_day!(2019, 1);

    fn parse_input(&self, lines: Vec<String>) -> Vec<u32> {
        lines
            .iter()
            .map(|line| line.parse::<u32>().unwrap())
            .collect()
    }

    fn part_1(&self, input: &Vec<u32>) -> u32 {
        self.solve(input, |m| self.fuel_for_mass(m))
    }

    fn part_2(&self, input: &Vec<u32>) -> u32 {
        self.solve(input, |m| self.rocket_equation(m))
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, "12", 2,
            self, part_1, "14", 2,
            self, part_1, "1969", 654,
            self, part_1, "100756", 33583,
            self, part_2, "12", 2,
            self, part_2, "1969", 966,
            self, part_2, "100756", 50346
        };
    }
}

fn main() {
    AoC2019_01 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2019_01 {}.samples();
    }
}
