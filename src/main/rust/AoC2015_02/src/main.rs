#![allow(non_snake_case)]

use aoc::Puzzle;

struct Present {
    length: usize,
    width: usize,
    height: usize,
}

impl Present {
    fn from_input(s: &String) -> Self {
        let sp: Vec<usize> = s
            .split("x")
            .map(|sp| sp.parse::<usize>().unwrap())
            .collect();
        Self {
            length: sp[0],
            width: sp[1],
            height: sp[2],
        }
    }

    fn required_area(&self) -> usize {
        let sides = vec![
            2 * self.length * self.width,
            2 * self.width * self.height,
            2 * self.height * self.length,
        ];
        sides.iter().sum::<usize>() + sides.iter().min().unwrap() / 2
    }

    fn required_length(&self) -> usize {
        let circumferences = vec![
            2 * (self.length + self.width),
            2 * (self.width + self.height),
            2 * (self.height + self.length),
        ];
        circumferences.iter().min().unwrap()
            + self.length * self.width * self.height
    }
}

struct AoC2015_02;

impl AoC2015_02 {}

impl aoc::Puzzle for AoC2015_02 {
    type Input = Vec<Present>;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2015, 2);

    fn parse_input(&self, lines: Vec<String>) -> Vec<Present> {
        lines.iter().map(Present::from_input).collect()
    }

    fn part_1(&self, presents: &Vec<Present>) -> usize {
        presents.iter().map(Present::required_area).sum()
    }

    fn part_2(&self, presents: &Vec<Present>) -> usize {
        presents.iter().map(Present::required_length).sum()
    }

    fn samples(&self) {
        let test1 = "2x3x4";
        let test2 = "1x1x10";
        aoc::puzzle_samples! {
            self, part_1, test1, 58,
            self, part_1, test2, 43,
            self, part_2, test1, 34,
            self, part_2, test2, 14
        };
    }
}

fn main() {
    AoC2015_02 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2015_02 {}.samples();
    }
}
