#![allow(non_snake_case)]

use aoc::geometry::XY;
use aoc::range::merge;
use aoc::Puzzle;
use itertools::Itertools;
use std::collections::{HashMap, HashSet};
use std::ops::RangeInclusive;

struct AoC2022_15;

impl AoC2022_15 {
    fn solve_1(
        &self,
        sensors: &HashMap<XY, u32>,
        beacons: &HashSet<XY>,
        y: i32,
    ) -> isize {
        let mut ranges: Vec<RangeInclusive<isize>> = Vec::new();
        for (s, md) in sensors {
            let md = *md as isize;
            let dy = (s.y() - y).abs() as isize;
            if dy > md {
                continue;
            }
            ranges.push(s.x() as isize - md + dy..=s.x() as isize + md - dy);
        }
        merge(&ranges)
            .iter()
            .map(|r| {
                r.end() - r.start() + 1
                    - beacons
                        .iter()
                        .filter(|b| b.y() == y && r.contains(&(b.x() as isize)))
                        .count() as isize
            })
            .sum()
    }

    fn sample_part_1(&self, input: &(HashMap<XY, u32>, HashSet<XY>)) -> isize {
        let (sensors, beacons) = input;
        self.solve_1(sensors, beacons, 10)
    }

    fn solve_2(&self, sensors: &HashMap<XY, u32>, max: i32) -> u64 {
        let mut a_coeffs: HashSet<i32> = HashSet::new();
        let mut b_coeffs: HashSet<i32> = HashSet::new();
        for (s, md) in sensors {
            a_coeffs.insert(s.y() - s.x() + *md as i32 + 1);
            a_coeffs.insert(s.y() - s.x() - *md as i32 - 1);
            b_coeffs.insert(s.x() + s.y() + *md as i32 + 1);
            b_coeffs.insert(s.x() + s.y() - *md as i32 - 1);
        }
        a_coeffs
            .iter()
            .cartesian_product(b_coeffs.iter())
            .filter(|(a, b)| *a < *b && (*b - *a) % 2 == 0)
            .map(|(a, b)| XY::of((b - a) / 2, (a + b) / 2))
            .filter(|p| 0 < p.x() && p.x() < max)
            .filter(|p| 0 < p.y() && p.y() < max)
            .filter(|p| {
                sensors.iter().all(|(s, md)| p.manhattan_distance(s) > *md)
            })
            .map(|p| 4_000_000_u64 * p.x() as u64 + p.y() as u64)
            .next()
            .unwrap()
    }

    fn sample_part_2(&self, input: &(HashMap<XY, u32>, HashSet<XY>)) -> u64 {
        let (sensors, _) = input;
        self.solve_2(sensors, 20)
    }
}

impl aoc::Puzzle for AoC2022_15 {
    type Input = (HashMap<XY, u32>, HashSet<XY>);
    type Output1 = isize;
    type Output2 = u64;

    aoc::puzzle_year_day!(2022, 15);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        let mut sensors: HashMap<XY, u32> = HashMap::new();
        let mut beacons: HashSet<XY> = HashSet::new();
        for line in lines {
            let nums = aoc::ints_with_check(&line, 4);
            let s = XY::of(nums[0], nums[1]);
            let b = XY::of(nums[2], nums[3]);
            sensors.insert(s, s.manhattan_distance(&b));
            beacons.insert(b);
        }
        (sensors, beacons)
    }

    fn part_1(&self, input: &Self::Input) -> Self::Output1 {
        let (sensors, beacons) = input;
        self.solve_1(sensors, beacons, 2_000_000)
    }

    fn part_2(&self, input: &Self::Input) -> Self::Output2 {
        let (sensors, _) = input;
        self.solve_2(sensors, 4_000_000)
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, sample_part_1, TEST, 26,
            self, sample_part_2, TEST, 56_000_011
        };
    }
}

fn main() {
    AoC2022_15 {}.run(std::env::args());
}

const TEST: &str = "\
Sensor at x=2, y=18: closest beacon is at x=-2, y=15
Sensor at x=9, y=16: closest beacon is at x=10, y=16
Sensor at x=13, y=2: closest beacon is at x=15, y=3
Sensor at x=12, y=14: closest beacon is at x=10, y=16
Sensor at x=10, y=20: closest beacon is at x=10, y=16
Sensor at x=14, y=17: closest beacon is at x=10, y=16
Sensor at x=8, y=7: closest beacon is at x=2, y=10
Sensor at x=2, y=0: closest beacon is at x=2, y=10
Sensor at x=0, y=11: closest beacon is at x=2, y=10
Sensor at x=20, y=14: closest beacon is at x=25, y=17
Sensor at x=17, y=20: closest beacon is at x=21, y=22
Sensor at x=16, y=7: closest beacon is at x=15, y=3
Sensor at x=14, y=3: closest beacon is at x=15, y=3
Sensor at x=20, y=1: closest beacon is at x=15, y=3
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2022_15 {}.samples();
    }
}
