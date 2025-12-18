#![allow(non_snake_case)]

use aoc::geometry::XY;
use aoc::Puzzle;
use itertools::Itertools;

struct Rectangle {
    min_x: usize,
    max_x: usize,
    min_y: usize,
    max_y: usize,
}

impl Rectangle {
    fn from(p1: &XY, p2: &XY) -> Self {
        let min_x = p1.x().min(p2.x()) as usize;
        let max_x = p1.x().max(p2.x()) as usize;
        let min_y = p1.y().min(p2.y()) as usize;
        let max_y = p1.y().max(p2.y()) as usize;
        Rectangle {
            min_x,
            max_x,
            min_y,
            max_y,
        }
    }

    fn area(&self) -> usize {
        (self.max_x - self.min_x + 1) * (self.max_y - self.min_y + 1)
    }

    fn intersect(&self, other: &Self) -> bool {
        !(self.max_x <= other.min_x
            || self.min_x >= other.max_x
            || self.max_y <= other.min_y
            || self.min_y >= other.max_y)
    }
}

struct AoC2025_09;

impl AoC2025_09 {
    fn rectangles<'a>(
        reds: &'a <Self as Puzzle>::Input,
    ) -> impl Iterator<Item = Rectangle> + 'a {
        reds.iter()
            .combinations(2)
            .map(move |c| Rectangle::from(c[0], c[1]))
    }
}

impl aoc::Puzzle for AoC2025_09 {
    type Input = Vec<XY>;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2025, 9);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        lines
            .iter()
            .map(|line| {
                let (x, y) = line.split_once(',').unwrap();
                XY::of(x.parse::<i32>().unwrap(), y.parse::<i32>().unwrap())
            })
            .collect::<Vec<_>>()
    }

    fn part_1(&self, reds: &Self::Input) -> Self::Output1 {
        Self::rectangles(reds).map(|r| r.area()).max().unwrap()
    }

    fn part_2(&self, reds: &Self::Input) -> Self::Output2 {
        let border_segments = reds
            .iter()
            .chain([reds[0]].iter())
            .tuple_windows()
            .map(|(a, b)| Rectangle::from(a, b))
            .collect::<Vec<_>>();
        Self::rectangles(reds)
            .filter(|r| !border_segments.iter().any(|bs| r.intersect(bs)))
            .map(|r| r.area())
            .max()
            .unwrap()
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 50,
            self, part_2, TEST, 24
        };
    }
}

fn main() {
    AoC2025_09 {}.run(std::env::args());
}

const TEST: &str = "\
7,1
11,1
11,7
9,7
9,5
2,5
2,3
7,3
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2025_09 {}.samples();
    }
}
