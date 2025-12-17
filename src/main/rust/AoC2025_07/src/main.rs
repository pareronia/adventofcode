#![allow(non_snake_case)]

use aoc::Puzzle;

const START: char = 'S';
const SPLITTER: char = '^';

struct AoC2025_07;

impl AoC2025_07 {
    fn solve(&self, grid: &[String]) -> (usize, usize) {
        let start = grid[0].find(START).unwrap();
        let mut beams: Vec<usize> = vec![0; grid[0].len()];
        beams[start] = 1;
        let mut splits: usize = 0;
        grid.iter().skip(1).for_each(|row| {
            let mut new_beams: Vec<usize> = vec![0; row.len()];
            row.chars().enumerate().for_each(|(c, ch)| match ch {
                SPLITTER => {
                    new_beams[c - 1] += beams[c];
                    new_beams[c + 1] += beams[c];
                    splits += if beams[c] > 0 { 1 } else { 0 };
                }
                _ => new_beams[c] += beams[c],
            });
            beams = new_beams;
        });
        (splits, beams.iter().sum())
    }
}

impl aoc::Puzzle for AoC2025_07 {
    type Input = Vec<String>;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2025, 7);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        lines
    }

    fn part_1(&self, input: &Self::Input) -> Self::Output1 {
        self.solve(input).0
    }

    fn part_2(&self, input: &Self::Input) -> Self::Output2 {
        self.solve(input).1
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 21,
            self, part_2, TEST, 40
        };
    }
}

fn main() {
    AoC2025_07 {}.run(std::env::args());
}

const TEST: &str = "\
.......S.......
...............
.......^.......
...............
......^.^......
...............
.....^.^.^.....
...............
....^.^...^....
...............
...^.^...^.^...
...............
..^...^.....^..
...............
.^.^.^.^.^...^.
...............
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2025_07 {}.samples();
    }
}
