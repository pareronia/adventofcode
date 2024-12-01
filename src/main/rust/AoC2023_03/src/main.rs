#![allow(non_snake_case)]

use aoc::{
    grid::{Cell, CharGrid, Grid},
    Puzzle,
};
use lazy_static::lazy_static;
use regex::Regex;
use std::collections::BTreeMap;

lazy_static! {
    static ref REGEX_N: Regex = Regex::new(r"[0-9]+").unwrap();
}

#[derive(Debug)]
struct EnginePart {
    part: char,
    number: u32,
    pos: Cell,
}

struct AoC2023_03;

impl AoC2023_03 {}

impl aoc::Puzzle for AoC2023_03 {
    type Input = Vec<EnginePart>;
    type Output1 = u32;
    type Output2 = u32;

    aoc::puzzle_year_day!(2023, 3);

    fn parse_input(&self, lines: Vec<String>) -> Vec<EnginePart> {
        fn find_engine_part(
            grid: &CharGrid,
            row: usize,
            colspan: (usize, usize),
        ) -> Option<EnginePart> {
            (colspan.0..colspan.1)
                .flat_map(|col| grid.all_neighbours(&Cell::at(row, col)))
                .filter(|n| !grid.get(n).is_ascii_digit())
                .filter(|n| grid.get(n) != '.')
                .map(|n| {
                    let number = grid.get_row_as_string(row)
                        [colspan.0..colspan.1]
                        .parse::<u32>()
                        .unwrap();
                    EnginePart {
                        part: grid.get(&n),
                        number,
                        pos: n,
                    }
                })
                .next()
        }

        let grid = CharGrid::from(
            &lines.iter().map(|s| s.as_str()).collect::<Vec<_>>(),
        );
        grid.get_rows_as_string()
            .iter()
            .enumerate()
            .flat_map(|(r, row)| {
                let mut eps = vec![];
                for m in REGEX_N.find_iter(row) {
                    eps.push(find_engine_part(&grid, r, (m.start(), m.end())));
                }
                eps.into_iter()
            })
            .flatten()
            .collect::<Vec<_>>()
    }

    fn part_1(&self, engine_parts: &Vec<EnginePart>) -> u32 {
        engine_parts.iter().map(|ep| ep.number).sum()
    }

    fn part_2(&self, engine_parts: &Vec<EnginePart>) -> u32 {
        let mut d = BTreeMap::<Cell, Vec<u32>>::new();
        engine_parts
            .iter()
            .filter(|ep| ep.part == '*')
            .for_each(|ep| d.entry(ep.pos).or_default().push(ep.number));
        d.iter()
            .filter(|(_, v)| v.len() == 2)
            .map(|(_, v)| v[0] * v[1])
            .sum()
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 4361,
            self, part_2, TEST, 467835
        };
    }
}

fn main() {
    AoC2023_03 {}.run(std::env::args());
}

const TEST: &str = "\
467..114..
...*......
..35..633.
......#...
617*......
.....+.58.
..592.....
......755.
...$.*....
.664.598..
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2023_03 {}.samples();
    }
}
