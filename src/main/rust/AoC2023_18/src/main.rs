#![allow(non_snake_case)]

use std::str::FromStr;

use aoc::{
    geometry::{Direction, Translate, XY},
    Puzzle,
};

struct Polygon {
    vertices: Vec<XY>,
}

impl Polygon {
    fn shoelace(&self) -> u64 {
        let size = self.vertices.len();
        let s = (0..size)
            .map(|i| {
                self.vertices[i].x() as i64
                    * (self.vertices[(i + 1) % size].y() as i64
                        - self.vertices[(size + i - 1) % size].y() as i64)
            })
            .sum::<i64>()
            .unsigned_abs();
        s / 2
    }

    fn circumference(&self) -> u64 {
        (1..self.vertices.len())
            .map(|i| self.vertices[i].manhattan_distance(&self.vertices[i - 1]))
            .sum::<u32>() as u64
    }

    fn inside_area(&self) -> u64 {
        self.shoelace() + self.circumference() / 2 + 1
    }
}

struct Instruction {
    direction: Direction,
    amount: u32,
}

struct AoC2023_18;

impl AoC2023_18 {
    fn solve(&self, instructions: &[Instruction]) -> u64 {
        let mut vertices: Vec<XY> = vec![XY::of(0, 0)];
        instructions.iter().for_each(|instruction| {
            vertices.push(vertices.last().unwrap().translate(
                &XY::try_from(instruction.direction).unwrap(),
                instruction.amount as i32,
            ))
        });
        Polygon { vertices }.inside_area()
    }
}

impl aoc::Puzzle for AoC2023_18 {
    type Input = Vec<String>;
    type Output1 = u64;
    type Output2 = u64;

    aoc::puzzle_year_day!(2023, 18);

    fn parse_input(&self, lines: Vec<String>) -> Vec<String> {
        lines
    }

    fn part_1(&self, input: &Vec<String>) -> u64 {
        let instructions: Vec<Instruction> = input
            .iter()
            .map(|line| {
                let splits: Vec<&str> = line.split_whitespace().collect();
                Instruction {
                    direction: Direction::from_str(splits[0]).unwrap(),
                    amount: splits[1].parse::<u32>().unwrap(),
                }
            })
            .collect();
        self.solve(&instructions)
    }

    fn part_2(&self, input: &Vec<String>) -> u64 {
        let dirs = [
            Direction::Right,
            Direction::Down,
            Direction::Left,
            Direction::Up,
        ];
        let instructions: Vec<Instruction> = input
            .iter()
            .map(|line| {
                let splits: Vec<&str> = line.split_whitespace().collect();
                Instruction {
                    direction: dirs[splits[2][7..8].parse::<usize>().unwrap()],
                    amount: u32::from_str_radix(&splits[2][2..7], 16).unwrap(),
                }
            })
            .collect();
        self.solve(&instructions)
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 62_u64,
            self, part_2, TEST, 952408144115_u64
        };
    }
}

fn main() {
    AoC2023_18 {}.run(std::env::args());
}

const TEST: &str = "\
R 6 (#70c710)
D 5 (#0dc571)
L 2 (#5713f0)
D 2 (#d2c081)
R 2 (#59c680)
D 2 (#411b91)
L 5 (#8ceee2)
U 2 (#caa173)
L 1 (#1b58a2)
U 2 (#caa171)
R 2 (#7807d2)
U 3 (#a77fa3)
L 2 (#015232)
U 2 (#7a21e3)
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2023_18 {}.samples();
    }
}
