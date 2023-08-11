#![allow(non_snake_case)]

use {
    aoc::{
        graph::BFS,
        grid::{Cell, CharGrid, Grid},
        Puzzle,
    },
    std::collections::HashSet,
};

const START: char = 'S';
const END: char = 'E';
const MIN: char = 'a';
const MAX: char = 'z';

struct HeightMap {
    grid: CharGrid,
    start: Cell,
}

struct AoC2022_12;

impl AoC2022_12 {
    fn solve(&self, map: &HeightMap, end_points: &HashSet<char>) -> usize {
        let is_end = |cell| end_points.contains(&map.grid.get(&cell));
        let get_value = |cell| match map.grid.get(&cell) {
            START => MIN as i8,
            END => MAX as i8,
            ch => ch as i8,
        };
        let adjacent = |cell| {
            map.grid
                .capital_neighbours(&cell)
                .iter()
                .filter(|&n| get_value(cell) - get_value(*n) <= 1)
                .cloned()
                .collect::<Vec<Cell>>()
        };
        BFS::execute(map.start, is_end, adjacent)
    }
}

impl aoc::Puzzle for AoC2022_12 {
    type Input = HeightMap;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2022, 12);

    fn parse_input(&self, lines: Vec<String>) -> HeightMap {
        let grid = CharGrid::from(&lines.iter().map(AsRef::as_ref).collect());
        let start = grid.find_first_matching(|val| val == END).unwrap();
        HeightMap { grid, start }
    }

    fn part_1(&self, map: &HeightMap) -> usize {
        self.solve(map, &HashSet::from([START]))
    }

    fn part_2(&self, map: &HeightMap) -> usize {
        self.solve(map, &HashSet::from([START, MIN]))
    }

    fn samples(&self) {
        #[rustfmt::skip]
        let test =
            "Sabqponm\n\
             abcryxxl\n\
             accszExk\n\
             acctuvwj\n\
             abdefghi";
        aoc::puzzle_samples! {
            self, part_1, test, 31,
            self, part_2, test, 29
        };
    }
}

fn main() {
    AoC2022_12 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2022_12 {}.samples();
    }
}
