#![allow(non_snake_case)]

use aoc::geometry::{Direction, Turn};
use aoc::graph::AStar;
use aoc::grid::{Cell, Grid, IntGrid};
use aoc::Puzzle;

#[derive(Clone, Copy, Eq, Hash, PartialEq)]
struct Move {
    cell: Cell,
    dir: Option<Direction>,
    cost: usize,
}

struct AoC2023_17;

impl AoC2023_17 {
    fn solve(grid: &IntGrid, min_moves: usize, max_moves: usize) -> u32 {
        let adjacent: &dyn Fn(Move) -> Vec<Move> = &|r#move: Move| {
            let mut moves = vec![];
            for dir in Direction::capital() {
                if r#move.dir.is_some()
                    && (r#move.dir.unwrap() == dir
                        || r#move.dir.unwrap() == dir.turn(Turn::Around))
                {
                    continue;
                }
                let mut cell = r#move.cell;
                let mut hl: usize = 0;
                for i in 1..=max_moves {
                    let o_cell = cell.try_at(dir);
                    if o_cell.is_none() || !grid.in_bounds(&o_cell.unwrap()) {
                        break;
                    }
                    cell = o_cell.unwrap();
                    hl += grid.get(&cell) as usize;
                    if i >= min_moves {
                        moves.push(Move {
                            cell,
                            dir: Some(dir),
                            cost: hl,
                        });
                    }
                }
            }
            moves
        };

        let end: Cell = Cell::at(grid.height() - 1, grid.width() - 1);
        AStar::distance(
            Move {
                cell: Cell::at(0, 0),
                dir: None,
                cost: 0,
            },
            |r#move| r#move.cell == end,
            adjacent,
            |r#move| r#move.cost,
        ) as u32
    }
}

impl aoc::Puzzle for AoC2023_17 {
    type Input = IntGrid;
    type Output1 = u32;
    type Output2 = u32;

    aoc::puzzle_year_day!(2023, 17);

    fn parse_input(&self, lines: Vec<String>) -> IntGrid {
        IntGrid::from(&lines.iter().map(AsRef::as_ref).collect())
    }

    fn part_1(&self, grid: &IntGrid) -> u32 {
        AoC2023_17::solve(grid, 1, 3)
    }

    fn part_2(&self, grid: &IntGrid) -> u32 {
        AoC2023_17::solve(grid, 4, 10)
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST1, 102,
            self, part_2, TEST1, 94,
            self, part_2, TEST2, 71
        };
    }
}

fn main() {
    AoC2023_17 {}.run(std::env::args());
}

const TEST1: &str = "\
2413432311323
3215453535623
3255245654254
3446585845452
4546657867536
1438598798454
4457876987766
3637877979653
4654967986887
4564679986453
1224686865563
2546548887735
4322674655533
";
const TEST2: &str = "\
111111111111
999999999991
999999999991
999999999991
999999999991
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2023_17 {}.samples();
    }
}
