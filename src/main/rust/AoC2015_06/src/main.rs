#![allow(non_snake_case)]

use aoc::grid::Cell;
use aoc::Puzzle;
use std::str::FromStr;

enum Mode {
    Mode1,
    Mode2,
}

enum Action {
    TurnOn,
    TurnOff,
    Toggle,
}

impl FromStr for Action {
    type Err = &'static str;

    fn from_str(string: &str) -> Result<Self, Self::Err> {
        match string {
            "turn_on" => Ok(Action::TurnOn),
            "turn_off" => Ok(Action::TurnOff),
            "toggle" => Ok(Action::Toggle),
            _ => panic!("Invalid action '{}'", string),
        }
    }
}

impl Action {
    fn apply(
        &self,
        lights: &mut [u8; 1_000_000],
        start: &Cell,
        end: &Cell,
        mode: &Mode,
    ) {
        let rr = (start.row * 1_000..=end.row * 1_000).step_by(1_000);
        for r in rr {
            for light in
                lights.iter_mut().take(r + end.col + 1).skip(r + start.col)
            {
                match self {
                    Action::TurnOn => match mode {
                        Mode::Mode1 => *light = 1,
                        Mode::Mode2 => *light += 1,
                    },
                    Action::TurnOff => match mode {
                        Mode::Mode1 => *light = 0,
                        Mode::Mode2 => {
                            if *light > 0 {
                                *light -= 1
                            }
                        }
                    },
                    Action::Toggle => match mode {
                        Mode::Mode1 => *light = if *light == 0 { 1 } else { 0 },
                        Mode::Mode2 => *light += 2,
                    },
                }
            }
        }
    }
}

struct Instruction {
    action: Action,
    start: Cell,
    end: Cell,
}

impl FromStr for Instruction {
    type Err = &'static str;

    fn from_str(string: &str) -> Result<Self, Self::Err> {
        let tmp = string.replace("turn ", "turn_");
        let splits = tmp.split_once(" through ").unwrap();
        let (action_s, start_s) = splits.0.split_once(" ").unwrap();
        let start = start_s.split_once(",").unwrap();
        let end = splits.1.split_once(",").unwrap();
        Ok(Instruction {
            action: Action::from_str(action_s).unwrap(),
            start: Cell::at(start.0.parse().unwrap(), start.1.parse().unwrap()),
            end: Cell::at(end.0.parse().unwrap(), end.1.parse().unwrap()),
        })
    }
}

struct AoC2015_06;

impl AoC2015_06 {
    fn solve(&self, instructions: &Vec<Instruction>, mode: Mode) -> u32 {
        let mut lights = [0_u8; 1_000_000];
        for instruction in instructions {
            instruction.action.apply(
                &mut lights,
                &instruction.start,
                &instruction.end,
                &mode,
            );
        }
        lights.into_iter().map(|i| i as u32).sum()
    }
}

impl aoc::Puzzle for AoC2015_06 {
    type Input = Vec<Instruction>;
    type Output1 = u32;
    type Output2 = u32;

    aoc::puzzle_year_day!(2015, 6);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        lines
            .into_iter()
            .map(|line| Instruction::from_str(line.as_str()).unwrap())
            .collect()
    }

    fn part_1(&self, instructions: &Self::Input) -> Self::Output1 {
        self.solve(instructions, Mode::Mode1)
    }

    fn part_2(&self, instructions: &Self::Input) -> Self::Output2 {
        self.solve(instructions, Mode::Mode2)
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST1, 1_000_000,
            self, part_1, TEST2, 1_000,
            self, part_1, TEST3, 0,
            self, part_2, TEST4, 1,
            self, part_2, TEST5, 2_000_000
        };
    }
}

fn main() {
    AoC2015_06 {}.run(std::env::args());
}

const TEST1: &str = "turn on 0,0 through 999,999";
const TEST2: &str = "toggle 0,0 through 999,0";
const TEST3: &str = "turn off 499,499 through 500,500";
const TEST4: &str = "turn on 0,0 through 0,0";
const TEST5: &str = "toggle 0,0 through 999,999";

#[cfg(test)]
mod tests {
    // use super::*;

    #[test]
    pub fn samples() {
        // stack overflow ?
        // AoC2015_06 {}.samples();
    }
}
