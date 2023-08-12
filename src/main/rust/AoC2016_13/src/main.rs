#![allow(non_snake_case)]

use aoc::{
    graph::{self, AStar},
    grid::Cell,
    Puzzle,
};
use lazy_static::lazy_static;

lazy_static! {
    static ref START: Cell = Cell::at(1, 1);
}

struct AoC2016_13;

impl AoC2016_13 {
    fn run_astar(&self, input: usize) -> graph::Result<Cell> {
        let is_open_space = |cell: &Cell| {
            let (x, y) = (cell.row, cell.col);
            let t = input + x * x + 3 * x + 2 * x * y + y + y * y;
            let ones = format!("{t:b}").chars().filter(|&ch| ch == '1').count();
            ones % 2 == 0
        };
        let adjacent = |cell: Cell| {
            cell.capital_neighbours()
                .into_iter()
                .filter(is_open_space)
                .collect()
        };

        AStar::execute(*START, |_| false, adjacent, |_| 1)
    }

    fn get_distance(&self, input: usize, cell: &Cell) -> usize {
        self.run_astar(input).get_distance(&cell).unwrap()
    }
}

impl aoc::Puzzle for AoC2016_13 {
    type Input = usize;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2016, 13);

    fn parse_input(&self, lines: Vec<String>) -> usize {
        lines[0].parse().unwrap()
    }

    fn part_1(&self, input: &usize) -> usize {
        self.get_distance(*input, &Cell::at(31, 39))
    }

    fn part_2(&self, input: &usize) -> usize {
        self.run_astar(*input)
            .get_distances()
            .values()
            .filter(|&v| *v <= 50)
            .count()
    }

    fn samples(&self) {
        let test = 10;
        assert_eq!(self.get_distance(test, &Cell::at(1, 1)), 0);
        assert_eq!(self.get_distance(test, &Cell::at(7, 4)), 11);
    }
}

fn main() {
    AoC2016_13 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2016_13 {}.samples();
    }
}
