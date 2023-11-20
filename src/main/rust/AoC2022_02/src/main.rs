#![allow(non_snake_case)]

use aoc::Puzzle;
use itertools::Itertools;
use std::str::FromStr;

enum Play {
    Rock,
    Paper,
    Scissors,
}

#[derive(Debug)]
struct ParsePlayError {}

impl FromStr for Play {
    type Err = ParsePlayError;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        match s {
            "A" => Ok(Play::Rock),
            "B" => Ok(Play::Paper),
            "C" => Ok(Play::Scissors),
            _ => panic!("Invalid Play: '{}'", s),
        }
    }
}

enum Reply {
    Rock,
    Paper,
    Scissors,
}

#[derive(Debug)]
struct ParseReplyError {}

impl FromStr for Reply {
    type Err = ParseReplyError;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        match s {
            "X" => Ok(Reply::Rock),
            "Y" => Ok(Reply::Paper),
            "Z" => Ok(Reply::Scissors),
            _ => panic!("Invalid Reply: '{}'", s),
        }
    }
}

enum Outcome {
    Win,
    Draw,
    Loss,
}

#[derive(Debug)]
struct ParseOutcomeError {}

impl FromStr for Outcome {
    type Err = ParseOutcomeError;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        match s {
            "X" => Ok(Outcome::Loss),
            "Y" => Ok(Outcome::Draw),
            "Z" => Ok(Outcome::Win),
            _ => panic!("Invalid Outcome: '{}'", s),
        }
    }
}

struct AoC2022_02 {}

impl AoC2022_02 {
    fn reply_points(&self, reply: &Reply) -> u32 {
        match reply {
            Reply::Rock => 1,
            Reply::Paper => 2,
            Reply::Scissors => 3,
        }
    }

    fn outcome_points(&self, outcome: &Outcome) -> u32 {
        match outcome {
            Outcome::Win => 6,
            Outcome::Draw => 3,
            Outcome::Loss => 0,
        }
    }

    fn play_1(&self, play: &Play, reply: &Reply) -> (Reply, Outcome) {
        match play {
            Play::Rock => match reply {
                Reply::Rock => (Reply::Rock, Outcome::Draw),
                Reply::Paper => (Reply::Paper, Outcome::Win),
                Reply::Scissors => (Reply::Scissors, Outcome::Loss),
            },
            Play::Paper => match reply {
                Reply::Rock => (Reply::Rock, Outcome::Loss),
                Reply::Paper => (Reply::Paper, Outcome::Draw),
                Reply::Scissors => (Reply::Scissors, Outcome::Win),
            },
            Play::Scissors => match reply {
                Reply::Rock => (Reply::Rock, Outcome::Win),
                Reply::Paper => (Reply::Paper, Outcome::Loss),
                Reply::Scissors => (Reply::Scissors, Outcome::Draw),
            },
        }
    }

    fn play_2(&self, play: &Play, outcome: &Outcome) -> (Reply, Outcome) {
        match play {
            Play::Rock => match outcome {
                Outcome::Loss => (Reply::Scissors, Outcome::Loss),
                Outcome::Draw => (Reply::Rock, Outcome::Draw),
                Outcome::Win => (Reply::Paper, Outcome::Win),
            },
            Play::Paper => match outcome {
                Outcome::Loss => (Reply::Rock, Outcome::Loss),
                Outcome::Draw => (Reply::Paper, Outcome::Draw),
                Outcome::Win => (Reply::Scissors, Outcome::Win),
            },
            Play::Scissors => match outcome {
                Outcome::Loss => (Reply::Paper, Outcome::Loss),
                Outcome::Draw => (Reply::Scissors, Outcome::Draw),
                Outcome::Win => (Reply::Rock, Outcome::Win),
            },
        }
    }

    fn solve(&self, games: Vec<(Reply, Outcome)>) -> u32 {
        games
            .iter()
            .map(|(reply, outcome)| {
                self.reply_points(reply) + self.outcome_points(outcome)
            })
            .sum::<u32>()
    }
}

impl aoc::Puzzle for AoC2022_02 {
    type Input = Vec<(String, String)>;
    type Output1 = u32;
    type Output2 = u32;

    aoc::puzzle_year_day!(2022, 2);

    fn parse_input(&self, lines: Vec<String>) -> Vec<(String, String)> {
        lines
            .iter()
            .map(|line| {
                line.split_whitespace()
                    .map(|s| s.to_string())
                    .collect_tuple()
                    .unwrap()
            })
            .collect()
    }

    fn part_1(&self, input: &Vec<(String, String)>) -> u32 {
        self.solve(
            input
                .iter()
                .map(|(col1, col2)| {
                    (
                        Play::from_str(col1).unwrap(),
                        Reply::from_str(col2).unwrap(),
                    )
                })
                .map(|(play, reply)| self.play_1(&play, &reply))
                .collect(),
        )
    }

    fn part_2(&self, input: &Vec<(String, String)>) -> u32 {
        self.solve(
            input
                .iter()
                .map(|(col1, col2)| {
                    (
                        Play::from_str(col1).unwrap(),
                        Outcome::from_str(col2).unwrap(),
                    )
                })
                .map(|(play, outcome)| self.play_2(&play, &outcome))
                .collect(),
        )
    }

    fn samples(&self) {
        let test = "A Y\n\
             B X\n\
             C Z";
        aoc::puzzle_samples! {
            self, part_1, test, 15,
            self, part_2, test, 12
        };
    }
}

fn main() {
    AoC2022_02 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2022_02 {}.samples();
    }
}
