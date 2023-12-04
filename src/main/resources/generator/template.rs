#![allow(non_snake_case)]

use aoc::Puzzle;

struct AoC${year}_${day2};

impl AoC${year}_${day2} { }

impl aoc::Puzzle for AoC${year}_${day2} {
    type Input = Vec<String>;
    type Output1 = u32;
    type Output2 = u32;

    aoc::puzzle_year_day!(${year}, ${day});

    fn parse_input(&self, lines: Vec<String>) -> Vec<String> {
        lines
    }

    fn part_1(&self, input: &Vec<String>) -> u32 {
        todo!()
    }

    fn part_2(&self, input: &Vec<String>) -> u32 {
        todo!()
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, "TODO",
            self, part_2, TEST, "TODO"
        };
    }
}

fn main() {
    AoC${year}_${day2} {}.run(std::env::args());
}

const TEST: &str = "\
TODO
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC${year}_${day2} {}.samples();
    }
}
