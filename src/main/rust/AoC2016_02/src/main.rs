#![allow(non_snake_case)]
#![allow(unused)]

use aoc::{
    geometry::{Direction, XY},
    navigation::{Heading, NavigationWithHeading},
    Puzzle,
};
use lazy_static::lazy_static;
use std::{collections::HashMap, str::FromStr};

lazy_static! {
    static ref LAYOUT1: HashMap<XY, char> = HashMap::from([
        (XY::of(-1, 1), '1'),
        (XY::of(0, 1), '2'),
        (XY::of(1, 1), '3'),
        (XY::of(-1, 0), '4'),
        (XY::of(0, 0), '5'),
        (XY::of(1, 0), '6'),
        (XY::of(-1, -1), '7'),
        (XY::of(0, -1), '8'),
        (XY::of(1, -1), '9'),
    ]);
    static ref LAYOUT2: HashMap<XY, char> = HashMap::from([
        (XY::of(2, 2), '1'),
        (XY::of(1, 1), '2'),
        (XY::of(2, 1), '3'),
        (XY::of(3, 1), '4'),
        (XY::of(0, 0), '5'),
        (XY::of(1, 0), '6'),
        (XY::of(2, 0), '7'),
        (XY::of(3, 0), '8'),
        (XY::of(4, 0), '9'),
        (XY::of(1, -1), 'A'),
        (XY::of(2, -1), 'B'),
        (XY::of(3, -1), 'C'),
        (XY::of(2, -2), 'D'),
    ]);
}

struct Keypad {
    layout: &'static HashMap<XY, char>,
    current: XY,
}

impl Keypad {
    fn new(layout: &'static HashMap<XY, char>) -> Self {
        Keypad {
            layout,
            current: XY::of(0, 0),
        }
    }

    fn execute_instructions(
        &mut self,
        directions: &[Vec<Direction>],
    ) -> String {
        let mut execute_instruction = |directions: &Vec<Direction>| {
            let mut nav = NavigationWithHeading::new_with_bounds(
                self.current,
                Heading::North,
                Box::new(|pos| self.layout.contains_key(pos)),
            );
            directions
                .iter()
                .for_each(|&step| nav.navigate(Heading::from(step), 1));
            self.current = *nav.get_position();
            *self.layout.get(&self.current).unwrap()
        };

        directions.iter().map(execute_instruction).collect()
    }
}

struct AoC2016_02;

impl AoC2016_02 {}

impl aoc::Puzzle for AoC2016_02 {
    type Input = Vec<Vec<Direction>>;
    type Output1 = String;
    type Output2 = String;

    aoc::puzzle_year_day!(2016, 2);

    fn parse_input(&self, lines: Vec<String>) -> Vec<Vec<Direction>> {
        lines
            .iter()
            .map(|line| {
                line.chars()
                    .map(|ch| Direction::from_str(&ch.to_string()).unwrap())
                    .collect()
            })
            .collect()
    }

    fn part_1(&self, input: &Vec<Vec<Direction>>) -> String {
        Keypad::new(&LAYOUT1).execute_instructions(input)
    }

    fn part_2(&self, input: &Vec<Vec<Direction>>) -> String {
        Keypad::new(&LAYOUT2).execute_instructions(input)
    }

    fn samples(&self) {
        #[rustfmt::skip]
        let test =
            "ULL\n\
             RRDDD\n\
             LURDL\n\
             UUUUD";
        aoc::puzzle_samples! {
            self, part_1, test, "1985",
            self, part_2, test, "5DB3"
        };
    }
}

fn main() {
    AoC2016_02 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2016_02 {}.samples();
    }
}
