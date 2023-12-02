#![allow(non_snake_case)]

use aoc::Puzzle;

#[derive(Debug)]
struct Draw {
    red: u32,
    green: u32,
    blue: u32,
}

impl Draw {
    fn from_input(s: &str) -> Self {
        let mut red = 0;
        let mut green = 0;
        let mut blue = 0;
        s.split(',').for_each(|cc| {
            let splits = cc.split_whitespace().collect::<Vec<_>>();
            match splits[1] {
                "red" => red = splits[0].parse::<u32>().unwrap(),
                "green" => green = splits[0].parse::<u32>().unwrap(),
                "blue" => blue = splits[0].parse::<u32>().unwrap(),
                _ => panic!(),
            }
        });
        Draw { red, green, blue }
    }
}

#[derive(Debug)]
struct Game {
    id: u32,
    draws: Vec<Draw>,
}

impl Game {
    fn from_input(line: &str) -> Self {
        let splits = line.split(':').collect::<Vec<_>>();
        let id = splits[0]
            .split_whitespace()
            .nth(1)
            .unwrap()
            .parse::<u32>()
            .unwrap();
        let draws = splits[1]
            .split(';')
            .map(Draw::from_input)
            .collect::<Vec<_>>();
        Game { id, draws }
    }
}

struct AoC2023_02;

impl AoC2023_02 {}

impl aoc::Puzzle for AoC2023_02 {
    type Input = Vec<Game>;
    type Output1 = u32;
    type Output2 = u32;

    aoc::puzzle_year_day!(2023, 2);

    fn parse_input(&self, lines: Vec<String>) -> Vec<Game> {
        lines
            .iter()
            .map(|s| Game::from_input(s))
            .collect::<Vec<_>>()
    }

    fn part_1(&self, games: &Vec<Game>) -> u32 {
        fn possible(draw: &Draw) -> bool {
            draw.red <= 12 && draw.green <= 13 && draw.blue <= 14
        }

        games
            .iter()
            .filter(|game| game.draws.iter().all(possible))
            .map(|game| game.id)
            .sum()
    }

    fn part_2(&self, games: &Vec<Game>) -> u32 {
        fn power(game: &Game) -> u32 {
            let max_red = game.draws.iter().map(|d| d.red).max().unwrap();
            let max_green = game.draws.iter().map(|d| d.green).max().unwrap();
            let max_blue = game.draws.iter().map(|d| d.blue).max().unwrap();
            max_red * max_green * max_blue
        }

        games.iter().map(power).sum()
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 8,
            self, part_2, TEST, 2286
        };
    }
}

fn main() {
    AoC2023_02 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2023_02 {}.samples();
    }
}

const TEST: &str = "\
Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
";
