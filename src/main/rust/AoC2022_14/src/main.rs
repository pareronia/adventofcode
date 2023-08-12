#![allow(non_snake_case)]

use {
    aoc::{
        geometry::{Heading, Translate, XY},
        Puzzle,
    },
    lazy_static::lazy_static,
    std::collections::HashSet,
};

lazy_static! {
    static ref SOURCE: XY = XY::of(500, 0);
    static ref DIRS: Vec<Heading> =
        vec![Heading::South, Heading::SouthEast, Heading::SouthWest];
}

#[derive(Clone, Debug)]
struct Cave {
    rocks: HashSet<XY>,
    max_y: i32,
}

struct AoC2022_14;

impl AoC2022_14 {
    fn solve(&self, cave: &Cave, max_y: i32) -> usize {
        fn drop(cave: &Cave, sand: &mut HashSet<XY>, max_y: i32) -> Option<XY> {
            let mut curr = *SOURCE;
            loop {
                let x = curr.x();
                let y = curr.y();
                for h in DIRS.iter() {
                    let test = curr.translate(&XY::try_from(*h).unwrap(), -1);
                    if !sand.contains(&test) && !cave.rocks.contains(&test) {
                        curr = test;
                        break;
                    }
                }
                if curr.x() == x && curr.y() == y {
                    return Some(curr);
                }
                if curr.y() > max_y {
                    return None;
                }
            }
        }

        let mut sand: HashSet<XY> = HashSet::new();
        loop {
            match drop(cave, &mut sand, max_y) {
                None => break,
                Some(val) if val == *SOURCE => {
                    sand.insert(val);
                    break;
                }
                Some(other) => {
                    sand.insert(other);
                    continue;
                }
            }
        }
        sand.len()
    }
}

impl aoc::Puzzle for AoC2022_14 {
    type Input = Cave;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2022, 14);

    fn parse_input(&self, lines: Vec<String>) -> Cave {
        let mut rocks: HashSet<XY> = HashSet::new();
        let mut max_y = i32::MIN;
        lines.iter().for_each(|line| {
            let mut c = line
                .split(" -> ")
                .map(|split| {
                    split
                        .split(",")
                        .map(|s| s.parse::<i32>().unwrap())
                        .collect::<Vec<i32>>()
                })
                .collect::<Vec<Vec<i32>>>();
            let mut c1 = c.clone();
            for (v1, v2) in c.iter_mut().zip(c1[1..].iter_mut()) {
                let mut xs = vec![v1[0], v2[0]];
                let mut ys = vec![v1[1], v2[1]];
                xs.sort_unstable();
                ys.sort_unstable();
                for x in xs[0]..=xs[1] {
                    for y in ys[0]..=ys[1] {
                        rocks.insert(XY::of(x, y));
                        max_y = max_y.max(y);
                    }
                }
            }
        });
        Cave { rocks, max_y }
    }

    fn part_1(&self, cave: &Cave) -> usize {
        self.solve(cave, cave.max_y)
    }

    fn part_2(&self, input: &Cave) -> usize {
        let mut cave = input.clone();
        let mut xs: Vec<i32> = cave.rocks.iter().map(|r| r.x()).collect();
        xs.sort_unstable();
        let the_max = cave.max_y + 2;
        for x in xs[0] - the_max..=xs.last().unwrap() + the_max {
            cave.rocks.insert(XY::of(x, the_max));
        }
        self.solve(&cave, the_max)
    }

    fn samples(&self) {
        #[rustfmt::skip]
        let test =
            "498,4 -> 498,6 -> 496,6\n\
             503,4 -> 502,4 -> 502,9 -> 494,9";
        aoc::puzzle_samples! {
            self, part_1, test, 24,
            self, part_2, test, 93
        };
    }
}

fn main() {
    AoC2022_14 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2022_14 {}.samples();
    }
}
