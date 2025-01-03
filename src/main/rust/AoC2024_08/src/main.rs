#![allow(non_snake_case)]

use aoc::geometry::{Translate, XY};
use aoc::Puzzle;
use itertools::Itertools;
use std::collections::HashMap;
use std::collections::HashSet;

enum Mode {
    Mode1,
    Mode2,
}

impl Mode {
    fn collect_antinodes(
        &self,
        h: usize,
        w: usize,
        pair: (&XY, &XY),
    ) -> HashSet<XY> {
        let get_antinodes = |max_count| {
            let mut ans: HashSet<XY> = HashSet::new();
            let vec = XY::of(pair.0.x() - pair.1.x(), pair.0.y() - pair.1.y());
            for p in [pair.0, pair.1].iter().cartesian_product([-1_i32, 1_i32])
            {
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
        };

        match self {
            Mode::Mode1 => {
                let mut antinodes = get_antinodes(1);
                antinodes.remove(pair.0);
                antinodes.remove(pair.1);
                antinodes
            }
            Mode::Mode2 => get_antinodes(usize::MAX),
        }
    }
}

struct AoC2024_08;

impl AoC2024_08 {
    fn solve<'a>(
        &self,
        h: usize,
        w: usize,
        antennae: &'a [HashSet<XY>],
        mode: Mode,
    ) -> usize {
        antennae
            .iter()
            .flat_map(|same_frequency| same_frequency.iter().combinations(2))
            .map(|pair| mode.collect_antinodes(h, w, (pair[0], pair[1])))
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
        self.solve(*h, *w, antennae, Mode::Mode1)
    }

    fn part_2(&self, input: &Self::Input) -> Self::Output2 {
        let (h, w, antennae) = input;
        self.solve(*h, *w, antennae, Mode::Mode2)
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
