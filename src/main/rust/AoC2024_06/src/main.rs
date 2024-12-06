#![allow(non_snake_case)]

use aoc::geometry::{Direction, Turn};
use aoc::grid::{Cell, CharGrid, Grid};
use aoc::Puzzle;
use indexmap::IndexMap;
use std::collections::HashSet;

struct AoC2024_06;

impl AoC2024_06 {
    fn route(
        &self,
        grid: &CharGrid,
        obs: &HashSet<Cell>,
        new_obs: Option<&Cell>,
        start_pos: &Cell,
        start_dir: Direction,
    ) -> (bool, IndexMap<Cell, Vec<Direction>>) {
        let mut pos = *start_pos;
        let mut dir = start_dir;
        let mut seen = IndexMap::new();
        seen.entry(pos).or_insert(vec![]).push(dir);
        loop {
            match pos.try_at(dir).filter(|cell| grid.in_bounds(cell)) {
                None => return (false, seen),
                Some(nxt) => match new_obs.is_some_and(|x| *x == nxt)
                    || obs.contains(&nxt)
                {
                    true => dir = dir.turn(Turn::Right),
                    false => pos = nxt,
                },
            };
            if seen.get(&pos).is_some_and(|x| x.contains(&dir)) {
                return (true, seen);
            }
            seen.entry(pos).or_insert(vec![]).push(dir);
        }
    }
}

impl aoc::Puzzle for AoC2024_06 {
    type Input = (CharGrid, HashSet<Cell>, Cell);
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2024, 6);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        let grid = CharGrid::from(
            &lines.iter().map(AsRef::as_ref).collect::<Vec<_>>(),
        );
        let obs: HashSet<Cell> =
            grid.cells().filter(|cell| grid.get(cell) == '#').collect();
        let start = grid.find_first_matching(|v| v == '^').unwrap();
        (grid, obs, start)
    }

    fn part_1(&self, input: &Self::Input) -> Self::Output1 {
        let (grid, obs, start) = input;
        let (_, seen) = self.route(grid, obs, None, start, Direction::Up);
        seen.len()
    }

    fn part_2(&self, input: &Self::Input) -> Self::Output2 {
        let (grid, obs, start) = input;
        let (_, seen) = self.route(grid, obs, None, start, Direction::Up);
        let mut it = seen.into_iter();
        let mut prev = it.next().unwrap();
        let mut ans = 0;
        for curr in it {
            let start = prev.0;
            let dir = prev.1.remove(0);
            let (is_loop, _) =
                self.route(grid, obs, Some(&curr.0), &start, dir);
            if is_loop {
                ans += 1;
            }
            prev = curr;
        }
        ans
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 41,
            self, part_2, TEST, 6
        };
    }
}

fn main() {
    AoC2024_06 {}.run(std::env::args());
}

const TEST: &str = "\
....#.....
.........#
..........
..#.......
.......#..
..........
.#..^.....
........#.
#.........
......#...
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2024_06 {}.samples();
    }
}
