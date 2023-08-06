#![allow(non_snake_case)]

use aoc::geometry::{Heading, Translate, XY};
use aoc::Puzzle;
use itertools::Itertools;
use std::str::FromStr;

struct AoC2022_09;

impl AoC2022_09 {
    fn catchup(&self, head: &XY, tail: &XY) -> Option<XY> {
        let dx = head.x() - tail.x();
        let dy = head.y() - tail.y();
        match dx.abs() > 1 || dy.abs() > 1 {
            false => None,
            true => {
                let x = match dx {
                    dx if dx < 0 => -1,
                    dx if dx > 0 => 1,
                    _ => 0,
                };
                let y = match dy {
                    dy if dy < 0 => -1,
                    dy if dy > 0 => 1,
                    _ => 0,
                };
                Some(tail.translate(&XY::of(x, y), 1))
            }
        }
    }

    fn move_rope(&self, rope: &mut Vec<XY>, move_: &Heading) {
        let xy = XY::try_from(*move_).unwrap();
        rope[0] = rope[0].translate(&xy, 1);
        (1..rope.len()).for_each(|j| {
            match self.catchup(&rope[j - 1], &rope[j]) {
                None => {}
                Some(xy) => {
                    rope[j] = xy;
                }
            };
        });
    }

    fn solve(&self, moves: &Vec<Heading>, size: usize) -> usize {
        let mut rope = vec![XY::of(0, 0); size];
        moves
            .iter()
            .map(|move_| {
                self.move_rope(&mut rope, move_);
                rope.last().copied()
            })
            .unique()
            .count()
    }
}

impl aoc::Puzzle for AoC2022_09 {
    type Input = Vec<Heading>;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2022, 9);

    fn parse_input(&self, lines: Vec<String>) -> Vec<Heading> {
        lines
            .iter()
            .flat_map(|line| {
                let splits: Vec<&str> = line.split(" ").collect();
                let n = splits[1].parse::<usize>().unwrap();
                let heading = Heading::from_str(splits[0]).unwrap();
                std::iter::repeat(heading).take(n)
            })
            .collect::<Vec<Heading>>()
    }

    fn part_1(&self, input: &Vec<Heading>) -> usize {
        self.solve(input, 2)
    }

    fn part_2(&self, input: &Vec<Heading>) -> usize {
        self.solve(input, 10)
    }

    fn samples(&self) {
        #[rustfmt::skip]
        let test1 =
            "R 4\n\
             U 4\n\
             L 3\n\
             D 1\n\
             R 4\n\
             D 1\n\
             L 5\n\
             R 2";
        #[rustfmt::skip]
        let test2 =
            "R 5\n\
             U 8\n\
             L 8\n\
             D 3\n\
             R 17\n\
             D 10\n\
             L 25\n\
             U 20";
        aoc::puzzle_samples! {
            self, part_1, test1, 13,
            self, part_2, test1, 1,
            self, part_2, test2, 36
        };
    }
}

fn main() {
    AoC2022_09 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2022_09 {}.samples();
    }
}
