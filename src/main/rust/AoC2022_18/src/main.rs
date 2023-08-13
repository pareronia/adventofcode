#![allow(non_snake_case)]

use {
    aoc::{
        geometry3d::{Cuboid, XYZ},
        graph::BFS,
        Puzzle,
    },
    std::{collections::HashSet, ops::RangeInclusive},
};

#[derive(Debug)]
struct Bounds {
    x: RangeInclusive<i32>,
    y: RangeInclusive<i32>,
    z: RangeInclusive<i32>,
}

struct AoC2022_18;

impl AoC2022_18 {
    fn surface_area(&self, cubes: &HashSet<XYZ>) -> usize {
        cubes
            .iter()
            .flat_map(|cube| cube.capital_neighbours())
            .filter(|n| !cubes.contains(&n))
            .count()
    }

    fn find_inside(&self, cubes: &HashSet<XYZ>) -> HashSet<XYZ> {
        fn get_bounds(cubes: &HashSet<XYZ>) -> Bounds {
            Bounds {
                x: cubes.iter().map(XYZ::x).min().unwrap()
                    ..=cubes.iter().map(XYZ::x).max().unwrap(),
                y: cubes.iter().map(XYZ::y).min().unwrap()
                    ..=cubes.iter().map(XYZ::y).max().unwrap(),
                z: cubes.iter().map(XYZ::z).min().unwrap()
                    ..=cubes.iter().map(XYZ::z).max().unwrap(),
            }
        }

        fn find_outside(cubes: &HashSet<XYZ>, bounds: &Bounds) -> HashSet<XYZ> {
            let start = XYZ::of(
                bounds.x.start() - 1,
                bounds.y.start() - 1,
                bounds.z.start() - 1,
            );
            let search_space = Cuboid::of(
                bounds.x.start() - 1..=bounds.x.end() + 1,
                bounds.y.start() - 1..=bounds.y.end() + 1,
                bounds.z.start() - 1..=bounds.z.end() + 1,
            );
            let adjacent = |xyz: XYZ| {
                xyz.capital_neighbours()
                    .filter(|n| search_space.contains(&n))
                    .filter(|n| !cubes.contains(&n))
                    .collect::<Vec<XYZ>>()
            };
            BFS::flood_fill(start, adjacent)
        }

        let bounds = get_bounds(cubes);
        let outside = find_outside(cubes, &bounds);
        let cuboid =
            Cuboid::of(bounds.x.clone(), bounds.y.clone(), bounds.z.clone());
        cuboid
            .get_points()
            .filter(|p| !cubes.contains(&p))
            .filter(|p| !outside.contains(&p))
            .collect()
    }
}

impl aoc::Puzzle for AoC2022_18 {
    type Input = HashSet<XYZ>;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2022, 18);

    fn parse_input(&self, lines: Vec<String>) -> HashSet<XYZ> {
        lines
            .iter()
            .map(|line| aoc::uints_with_check(&line, 3).into_iter().collect())
            .collect()
    }

    fn part_1(&self, cubes: &HashSet<XYZ>) -> usize {
        self.surface_area(cubes)
    }

    fn part_2(&self, cubes: &HashSet<XYZ>) -> usize {
        let inside = self.find_inside(cubes);
        let diff = cubes
            .symmetric_difference(&inside)
            .map(XYZ::clone)
            .collect();
        self.surface_area(&diff)
    }

    fn samples(&self) {
        #[rustfmt::skip]
        let test_1 =
            "1,1,1\n\
             2,1,1";
        #[rustfmt::skip]
        let test_2 =
            "2,2,2\n\
             1,2,2\n\
             3,2,2\n\
             2,1,2\n\
             2,3,2\n\
             2,2,1\n\
             2,2,3\n\
             2,2,4\n\
             2,2,6\n\
             1,2,5\n\
             3,2,5\n\
             2,1,5\n\
             2,3,5";
        aoc::puzzle_samples! {
            self, part_1, test_1, 10,
            self, part_1, test_2, 64,
            self, part_2, test_2, 58
        };
    }
}

fn main() {
    AoC2022_18 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2022_18 {}.samples();
    }
}
