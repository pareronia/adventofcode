#![allow(non_snake_case)]

use aoc::graph::BFS;
use aoc::grid::Cell;
use aoc::Puzzle;
use std::collections::HashSet;

const START: Cell = Cell { row: 0, col: 0 };
const SIZE: usize = 71;
const TIME: usize = 1024;

struct AoC2024_18;

impl AoC2024_18 {
    fn adjacent(
        &self,
        cell: &Cell,
        size: usize,
        occupied: &HashSet<Cell>,
    ) -> Vec<Cell> {
        cell.capital_neighbours()
            .into_iter()
            .filter(|n| n.row < size && n.col < size && !occupied.contains(n))
            .collect()
    }

    fn solve_1(&self, cells: &[Cell], size: usize, time: usize) -> usize {
        let end = Cell::at(size - 1, size - 1);
        let mut occupied: HashSet<Cell> = HashSet::new();
        cells[..time].iter().for_each(|cell| {
            occupied.insert(*cell);
        });
        BFS::execute(
            START,
            |cell| cell == end,
            |cell| self.adjacent(&cell, size, &occupied),
        )
    }

    fn sample_part_1(&self, cells: &[Cell]) -> usize {
        self.solve_1(cells, 7, 12)
    }

    fn solve_2(&self, cells: &[Cell], size: usize, time: usize) -> String {
        let free = |time: usize| {
            let mut occupied: HashSet<Cell> = HashSet::new();
            cells[..time].iter().for_each(|cell| {
                occupied.insert(*cell);
            });
            BFS::flood_fill(START, |cell| self.adjacent(&cell, size, &occupied))
        };

        let end = Cell::at(size - 1, size - 1);
        let (mut lo, mut hi) = (time, cells.len());
        while lo < hi {
            let mid = (lo + hi) / 2;
            match free(mid).contains(&end) {
                true => lo = mid + 1,
                false => hi = mid,
            }
        }
        format!("{},{}", cells[lo - 1].col, cells[lo - 1].row)
    }

    fn sample_part_2(&self, cells: &[Cell]) -> String {
        self.solve_2(cells, 7, 12)
    }
}

impl aoc::Puzzle for AoC2024_18 {
    type Input = Vec<Cell>;
    type Output1 = usize;
    type Output2 = String;

    aoc::puzzle_year_day!(2024, 18);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        lines
            .iter()
            .map(|line| {
                let mut split = line.split(",");
                let c = split.next().unwrap();
                let r = split.next().unwrap();
                Cell::at(
                    r.parse::<usize>().unwrap(),
                    c.parse::<usize>().unwrap(),
                )
            })
            .collect()
    }

    fn part_1(&self, cells: &Self::Input) -> Self::Output1 {
        self.solve_1(cells, SIZE, TIME)
    }

    fn part_2(&self, cells: &Self::Input) -> Self::Output2 {
        self.solve_2(cells, SIZE, TIME)
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, sample_part_1, TEST, 22,
            self, sample_part_2, TEST, String::from("6,1")
        };
    }
}

fn main() {
    AoC2024_18 {}.run(std::env::args());
}

const TEST: &str = "\
5,4
4,2
4,5
3,0
2,1
6,3
2,4
1,5
0,6
3,3
2,6
5,1
1,2
5,5
2,5
6,5
1,4
0,4
6,4
1,1
6,1
1,0
0,5
1,6
2,0
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2024_18 {}.samples();
    }
}
