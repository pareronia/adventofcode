#![allow(non_snake_case)]

use aoc::geometry::{Direction, Turn};
use aoc::graph::Dijkstra;
use aoc::grid::{Cell, CharGrid, Grid};
use aoc::Puzzle;
use itertools::Itertools;

#[derive(Copy, Clone, Debug, Eq, Hash, PartialEq)]
struct State {
    pos: Cell,
    dir: Direction,
}

struct AoC2024_16;

impl AoC2024_16 {
    fn adjacent(&self, grid: &CharGrid, state: State) -> Vec<State> {
        let mut dirs = vec![state.dir];
        [Turn::Right, Turn::Left]
            .into_iter()
            .map(|t| state.dir.turn(t))
            .for_each(|d| dirs.push(d));
        dirs.into_iter()
            .map(|dir| State {
                pos: state.pos.try_at(dir).unwrap(),
                dir,
            })
            .filter(|state| grid.get(&state.pos) != '#')
            .collect_vec()
    }
}

impl aoc::Puzzle for AoC2024_16 {
    type Input = CharGrid;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2024, 16);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        CharGrid::from(&lines.iter().map(AsRef::as_ref).collect::<Vec<_>>())
    }

    fn part_1(&self, grid: &Self::Input) -> Self::Output1 {
        let start = Cell::at(grid.height() - 2, 1);
        let end = Cell::at(1, grid.width() - 2);
        Dijkstra::distance(
            State {
                pos: start,
                dir: Direction::Right,
            },
            |state| state.pos == end,
            |state| self.adjacent(grid, state),
            |curr, next| if curr.dir == next.dir { 1 } else { 1001 },
        )
    }

    fn part_2(&self, grid: &Self::Input) -> Self::Output2 {
        let start = Cell::at(grid.height() - 2, 1);
        let end = Cell::at(1, grid.width() - 2);
        let result = Dijkstra::all(
            State {
                pos: start,
                dir: Direction::Right,
            },
            |state| state.pos == end,
            |state| self.adjacent(grid, state),
            |curr, next| if curr.dir == next.dir { 1 } else { 1001 },
        );
        Direction::capital()
            .into_iter()
            .flat_map(|dir| {
                result.get_paths(State { pos: end, dir }).into_iter()
            })
            .flat_map(|path| path.into_iter())
            .map(|state| state.pos)
            .unique()
            .count()
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST1, 7036,
            self, part_1, TEST2, 11048,
            self, part_2, TEST1, 45,
            self, part_2, TEST2, 64
        };
    }
}

fn main() {
    AoC2024_16 {}.run(std::env::args());
}

const TEST1: &str = "\
###############
#.......#....E#
#.#.###.#.###.#
#.....#.#...#.#
#.###.#####.#.#
#.#.#.......#.#
#.#.#####.###.#
#...........#.#
###.#.#####.#.#
#...#.....#.#.#
#.#.#.###.#.#.#
#.....#...#.#.#
#.###.#.#.#.#.#
#S..#.....#...#
###############
";
const TEST2: &str = "\
#################
#...#...#...#..E#
#.#.#.#.#.#.#.#.#
#.#.#.#...#...#.#
#.#.#.#.###.#.#.#
#...#.#.#.....#.#
#.#.#.#.#.#####.#
#.#...#.#.#.....#
#.#.#####.#.###.#
#.#.#.......#...#
#.#.###.#####.###
#.#.#...#.....#.#
#.#.#.#####.###.#
#.#.#.........#.#
#.#.#.#########.#
#S#.............#
#################
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2024_16 {}.samples();
    }
}
