#![allow(non_snake_case)]

use aoc::{
    geometry::{Direction, XY},
    navigation::{Heading, NavigationWithHeading},
    Puzzle,
};
use itertools::Itertools;
use std::str::FromStr;

struct HouseVisits<'a> {
    navigation: NavigationWithHeading<'a>,
}

impl HouseVisits<'_> {
    fn new() -> Self {
        Self {
            navigation: NavigationWithHeading::new(
                XY::of(0, 0),
                Heading::North,
            ),
        }
    }

    fn go_visit(&mut self, direction: Direction) {
        self.navigation.navigate(Heading::from(direction), 1);
    }

    fn get_unique_visits(&self) -> impl Iterator<Item = &XY> {
        self.navigation.get_visited_positions(true).unique()
    }
}

struct AoC2015_03;

impl AoC2015_03 {}

impl aoc::Puzzle for AoC2015_03 {
    type Input = Vec<Direction>;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2015, 3);

    fn parse_input(&self, lines: Vec<String>) -> Vec<Direction> {
        lines[0]
            .chars()
            .map(|ch| Direction::from_str(&ch.to_string()).unwrap())
            .collect()
    }

    fn part_1(&self, directions: &Vec<Direction>) -> usize {
        let mut visits = HouseVisits::new();
        directions
            .iter()
            .for_each(|&direction| visits.go_visit(direction));
        visits.get_unique_visits().count()
    }

    fn part_2(&self, directions: &Vec<Direction>) -> usize {
        let mut santa_visits = HouseVisits::new();
        (0..directions.len())
            .step_by(2)
            .map(|i| directions[i])
            .for_each(|direction| santa_visits.go_visit(direction));
        let mut robot_visits = HouseVisits::new();
        (1..directions.len())
            .step_by(2)
            .map(|i| directions[i])
            .for_each(|direction| robot_visits.go_visit(direction));
        santa_visits
            .get_unique_visits()
            .chain(robot_visits.get_unique_visits())
            .unique()
            .count()
    }

    fn samples(&self) {
        let test1 = ">";
        let test2 = "^>v<";
        let test3 = "^v^v^v^v^v";
        let test4 = "^v";
        aoc::puzzle_samples! {
            self, part_1, test1, 2,
            self, part_1, test2, 4,
            self, part_1, test3, 2,
            self, part_2, test4, 3,
            self, part_2, test2, 3,
            self, part_2, test3, 11
        };
    }
}

fn main() {
    AoC2015_03 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2015_03 {}.samples();
    }
}
