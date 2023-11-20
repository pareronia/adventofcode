#![allow(non_snake_case)]

use {
    aoc::{grid::CharGrid, log, ocr, Puzzle},
    std::str::FromStr,
};

const FILL: char = '▒';
const EMPTY: char = ' ';
const PERIOD: usize = 40;
const MAX: usize = 220;

struct AoC2022_10;

enum OpCode {
    Noop,
    Addx,
}

#[derive(Debug)]
struct ParseOpCodeError {}

impl FromStr for OpCode {
    type Err = ParseOpCodeError;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        match s {
            "noop" => Ok(OpCode::Noop),
            "addx" => Ok(OpCode::Addx),
            _ => Err(ParseOpCodeError {}),
        }
    }
}

struct Instruction {
    operation: OpCode,
    operand: Option<i32>,
}

#[derive(Debug)]
struct ParseInstructionError {}

impl FromStr for Instruction {
    type Err = ParseInstructionError;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        let splits: Vec<&str> = s.split_whitespace().collect();
        let operation = splits[0].parse().unwrap();
        let operand = match operation {
            OpCode::Noop => None,
            OpCode::Addx => splits[1].parse().ok(),
        };
        Ok(Instruction { operation, operand })
    }
}

impl Instruction {
    fn get_operand(&self) -> i32 {
        match self.operation {
            OpCode::Noop => panic!("NOOP has no operand"),
            OpCode::Addx => self.operand.unwrap(),
        }
    }
}

impl AoC2022_10 {
    fn get_x_values(&self, input: &[Instruction]) -> Vec<i32> {
        let mut xs: Vec<i32> = vec![];
        let mut x = 1;
        input.iter().for_each(|ins| match ins.operation {
            OpCode::Noop => {
                xs.push(x);
            }
            OpCode::Addx => {
                xs.push(x);
                xs.push(x);
                x += ins.get_operand();
            }
        });
        xs
    }

    fn get_pixels(&self, input: &[Instruction]) -> Vec<String> {
        fn draw(cycle: usize, x: i32) -> char {
            match (cycle % PERIOD) as i32 - x {
                val if (-1..=1).contains(&val) => FILL,
                _ => EMPTY,
            }
        }

        let pixels = self
            .get_x_values(input)
            .iter()
            .enumerate()
            .map(|(i, x)| draw(i, *x))
            .collect::<String>();
        (0..=MAX)
            .step_by(PERIOD)
            .map(|i| pixels.chars().skip(i).take(PERIOD).collect())
            .collect()
    }
}

impl aoc::Puzzle for AoC2022_10 {
    type Input = Vec<Instruction>;
    type Output1 = i32;
    type Output2 = String;

    aoc::puzzle_year_day!(2022, 10);

    fn parse_input(&self, lines: Vec<String>) -> Vec<Instruction> {
        lines.iter().map(|line| line.parse().unwrap()).collect()
    }

    fn part_1(&self, input: &Vec<Instruction>) -> i32 {
        let xs = self.get_x_values(input);
        (20..=MAX)
            .step_by(PERIOD)
            .map(|i| i as i32 * xs[i - 1])
            .sum()
    }

    fn part_2(&self, input: &Vec<Instruction>) -> String {
        let pixels = self.get_pixels(input);
        let pixels: Vec<&str> = pixels.iter().map(AsRef::as_ref).collect();
        pixels.iter().for_each(|_line| log!(&_line));
        ocr::convert_6(&CharGrid::from(&pixels), FILL, EMPTY)
    }

    fn samples(&self) {
        #[rustfmt::skip]
        let test =
            "addx 15\n\
             addx -11\n\
             addx 6\n\
             addx -3\n\
             addx 5\n\
             addx -1\n\
             addx -8\n\
             addx 13\n\
             addx 4\n\
             noop\n\
             addx -1\n\
             addx 5\n\
             addx -1\n\
             addx 5\n\
             addx -1\n\
             addx 5\n\
             addx -1\n\
             addx 5\n\
             addx -1\n\
             addx -35\n\
             addx 1\n\
             addx 24\n\
             addx -19\n\
             addx 1\n\
             addx 16\n\
             addx -11\n\
             noop\n\
             noop\n\
             addx 21\n\
             addx -15\n\
             noop\n\
             noop\n\
             addx -3\n\
             addx 9\n\
             addx 1\n\
             addx -3\n\
             addx 8\n\
             addx 1\n\
             addx 5\n\
             noop\n\
             noop\n\
             noop\n\
             noop\n\
             noop\n\
             addx -36\n\
             noop\n\
             addx 1\n\
             addx 7\n\
             noop\n\
             noop\n\
             noop\n\
             addx 2\n\
             addx 6\n\
             noop\n\
             noop\n\
             noop\n\
             noop\n\
             noop\n\
             addx 1\n\
             noop\n\
             noop\n\
             addx 7\n\
             addx 1\n\
             noop\n\
             addx -13\n\
             addx 13\n\
             addx 7\n\
             noop\n\
             addx 1\n\
             addx -33\n\
             noop\n\
             noop\n\
             noop\n\
             addx 2\n\
             noop\n\
             noop\n\
             noop\n\
             addx 8\n\
             noop\n\
             addx -1\n\
             addx 2\n\
             addx 1\n\
             noop\n\
             addx 17\n\
             addx -9\n\
             addx 1\n\
             addx 1\n\
             addx -3\n\
             addx 11\n\
             noop\n\
             noop\n\
             addx 1\n\
             noop\n\
             addx 1\n\
             noop\n\
             noop\n\
             addx -13\n\
             addx -19\n\
             addx 1\n\
             addx 3\n\
             addx 26\n\
             addx -30\n\
             addx 12\n\
             addx -1\n\
             addx 3\n\
             addx 1\n\
             noop\n\
             noop\n\
             noop\n\
             addx -9\n\
             addx 18\n\
             addx 1\n\
             addx 2\n\
             noop\n\
             noop\n\
             addx 9\n\
             noop\n\
             noop\n\
             noop\n\
             addx -1\n\
             addx 2\n\
             addx -37\n\
             addx 1\n\
             addx 3\n\
             noop\n\
             addx 15\n\
             addx -21\n\
             addx 22\n\
             addx -6\n\
             addx 1\n\
             noop\n\
             addx 2\n\
             addx 1\n\
             noop\n\
             addx -10\n\
             noop\n\
             noop\n\
             addx 20\n\
             addx 1\n\
             addx 2\n\
             addx 2\n\
             addx -6\n\
             addx -11\n\
             noop\n\
             noop\n\
             noop";
        aoc::puzzle_samples! {
            self, part_1, test, 13140,
            self, get_pixels, test, vec![
                String::from("▒▒  ▒▒  ▒▒  ▒▒  ▒▒  ▒▒  ▒▒  ▒▒  ▒▒  ▒▒  "),
                String::from("▒▒▒   ▒▒▒   ▒▒▒   ▒▒▒   ▒▒▒   ▒▒▒   ▒▒▒ "),
                String::from("▒▒▒▒    ▒▒▒▒    ▒▒▒▒    ▒▒▒▒    ▒▒▒▒    "),
                String::from("▒▒▒▒▒     ▒▒▒▒▒     ▒▒▒▒▒     ▒▒▒▒▒     "),
                String::from("▒▒▒▒▒▒      ▒▒▒▒▒▒      ▒▒▒▒▒▒      ▒▒▒▒"),
                String::from("▒▒▒▒▒▒▒       ▒▒▒▒▒▒▒       ▒▒▒▒▒▒▒     ")
            ]
        };
    }
}

fn main() {
    AoC2022_10 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2022_10 {}.samples();
    }
}
