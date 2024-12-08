#![allow(non_snake_case)]

use aoc::geometry::{Translate, XY};
use aoc::Puzzle;
use itertools::Itertools;
use std::collections::HashMap;
use std::collections::HashSet;

struct AoC2024_08;

impl AoC2024_08 {
    fn get_antinodes(
        &self,
        pair: (&XY, &XY),
        h: usize,
        w: usize,
        max_count: usize,
    ) -> HashSet<XY> {
        let mut ans: HashSet<XY> = HashSet::new();
        let vec = XY::of(pair.0.x() - pair.1.x(), pair.0.y() - pair.1.y());
        for p in [pair.0, pair.1].iter().cartesian_product([-1_i32, 1_i32]) {
            let (pos, d) = p;
            for a in 1..=max_count {
                let antinode = pos.translate(&vec, d * a as i32);
                if 0_i32 <= antinode.x()
                    && antinode.x() < w as i32
                    && 0_i32 <= antinode.y()
                    && antinode.y() < h as i32
                {
                    ans.insert(antinode);
                } else {
                    break;
                }
            }
        }
        ans
    }

    fn solve<'a, F>(
        &self,
        antennae: &'a [HashSet<XY>],
        collect_antinodes: F,
    ) -> usize
    where
        F: Fn((&'a XY, &'a XY)) -> HashSet<XY>,
    {
        antennae
            .iter()
            .flat_map(|same_frequency| same_frequency.iter().combinations(2))
            .map(|pair| collect_antinodes((pair[0], pair[1])))
            .reduce(|acc, e| acc.union(&e).copied().collect())
            .unwrap()
            .len()
    }
}

impl aoc::Puzzle for AoC2024_08 {
    type Input = (usize, usize, Vec<HashSet<XY>>);
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2024, 8);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        let (h, w) = (lines.len(), lines[0].len());
        let mut antennae: HashMap<char, HashSet<XY>> = HashMap::new();
        for (r, line) in lines.iter().enumerate().take(h) {
            for (c, ch) in line.chars().enumerate().take(w) {
                if ch != '.' {
                    antennae
                        .entry(ch)
                        .or_default()
                        .insert(XY::of(c as i32, (h - r - 1) as i32));
                }
            }
        }
        (h, w, antennae.into_values().collect_vec())
    }

    fn part_1(&self, input: &Self::Input) -> Self::Output1 {
        let (h, w, antennae) = input;
        let collect_antinodes = |pair| {
            let mut antinodes = self.get_antinodes(pair, *h, *w, 1);
            antinodes.remove(pair.0);
            antinodes.remove(pair.1);
            antinodes
        };

        self.solve(antennae, collect_antinodes)
    }

    fn part_2(&self, input: &Self::Input) -> Self::Output2 {
        let (h, w, antennae) = input;
        let collect_antinodes =
            |pair| self.get_antinodes(pair, *h, *w, usize::MAX);

        self.solve(antennae, collect_antinodes)
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 14,
            self, part_2, TEST, 34
        };
    }
}

fn main() {
    AoC2024_08 {}.run(std::env::args());
}

const TEST: &str = "\
............
........0...
.....0......
.......0....
....0.......
......A.....
............
............
........A...
.........A..
............
............
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2024_08 {}.samples();
    }
}
