#![allow(non_snake_case)]

use aoc::geometry::{Direction, Turn};
use aoc::grid::{Cell, CharGrid, Grid};
use aoc::Puzzle;
use indexmap::IndexMap;
use std::collections::HashMap;

struct Obstacles {
    obs: HashMap<Direction, Vec<Vec<Option<Cell>>>>,
}

impl Obstacles {
    fn from_grid(grid: &CharGrid) -> Self {
        fn obstacles(
            grid: &CharGrid,
            starts: impl Iterator<Item = Cell>,
            dir: Direction,
        ) -> Vec<Vec<Option<Cell>>> {
            let (h, w) = (grid.height(), grid.width());
            let mut obs = Vec::with_capacity(h);
            (0..h).for_each(|_| {
                let mut row = Vec::with_capacity(w);
                (0..w).for_each(|_| row.push(None));
                obs.push(row);
            });
            starts.for_each(|start| {
                let mut last = match grid.get(&start) == '#' {
                    true => Some(start),
                    false => None,
                };
                for cell in grid.cells_direction(&start, dir) {
                    obs[cell.row][cell.col] = last;
                    if grid.get(&cell) == '#' {
                        last = Some(cell);
                    }
                }
            });
            obs
        }

        let (h, w) = (grid.height(), grid.width());
        let mut obs: HashMap<Direction, Vec<Vec<Option<Cell>>>> =
            HashMap::new();
        obs.insert(
            Direction::Right,
            obstacles(
                grid,
                (0..h).map(|r| Cell::at(r, w - 1)),
                Direction::Left,
            ),
        );
        obs.insert(
            Direction::Left,
            obstacles(grid, (0..h).map(|r| Cell::at(r, 0)), Direction::Right),
        );
        obs.insert(
            Direction::Up,
            obstacles(grid, (0..w).map(|c| Cell::at(0, c)), Direction::Down),
        );
        obs.insert(
            Direction::Down,
            obstacles(grid, (0..w).map(|c| Cell::at(h - 1, c)), Direction::Up),
        );
        Self { obs }
    }

    fn get_next(
        &self,
        start: &Cell,
        dir: Direction,
        extra: &Cell,
    ) -> Option<Cell> {
        let o = self.obs[&dir][start.row][start.col];
        if let Some(obs) = o {
            if dir.is_horizontal()
                && obs.row == extra.row
                && start.to(extra).is_some_and(|d| d == dir)
            {
                let d_extra = extra.col.abs_diff(start.col);
                let d_obs = obs.col.abs_diff(start.col);
                match d_obs < d_extra {
                    true => Some(obs),
                    false => Some(*extra),
                }
            } else if dir.is_vertical()
                && obs.col == extra.col
                && start.to(extra).is_some_and(|d| d == dir)
            {
                let d_extra = extra.row.abs_diff(start.row);
                let d_obs = obs.row.abs_diff(start.row);
                match d_obs < d_extra {
                    true => Some(obs),
                    false => Some(*extra),
                }
            } else {
                Some(obs)
            }
        } else if (dir.is_horizontal() && start.row == extra.row
            || dir.is_vertical() && start.col == extra.col)
            && start.to(extra).is_some_and(|d| d == dir)
        {
            Some(*extra)
        } else {
            None
        }
    }
}

struct AoC2024_06;

impl AoC2024_06 {
    fn visited(
        &self,
        grid: &CharGrid,
        start_dir: Direction,
    ) -> IndexMap<Cell, Vec<Direction>> {
        let mut pos = grid.find_first_matching(|v| v == '^').unwrap();
        let mut dir = start_dir;
        let mut visited = IndexMap::new();
        visited.insert(pos, vec![dir]);
        loop {
            match pos.try_at(dir).filter(|cell| grid.in_bounds(cell)) {
                None => return visited,
                Some(nxt) => match grid.get(&nxt) == '#' {
                    true => dir = dir.turn(Turn::Right),
                    false => pos = nxt,
                },
            };
            visited.entry(pos).or_insert(vec![]).push(dir);
        }
    }

    fn is_loop(
        &self,
        start_pos: &Cell,
        start_dir: Direction,
        obs: &Obstacles,
        extra: &Cell,
    ) -> bool {
        #[inline]
        fn to_bits(dir: &Direction) -> u8 {
            match dir {
                Direction::Up => 1,
                Direction::Right => 2,
                Direction::Down => 4,
                Direction::Left => 8,
                _ => panic!(),
            }
        }

        let mut pos = *start_pos;
        let mut dir = start_dir;
        let mut bits = to_bits(&dir);
        let mut seen = HashMap::new();
        seen.insert(pos, bits);
        loop {
            let nxt_obs = obs.get_next(&pos, dir, extra);
            if nxt_obs.is_none() {
                return false;
            }
            pos = nxt_obs.unwrap().try_at(dir.turn(Turn::Around)).unwrap();
            dir = dir.turn(Turn::Right);
            bits = to_bits(&dir);
            if seen.get(&pos).is_some_and(|x| (x & bits) != 0) {
                return true;
            }
            seen.entry(pos).and_modify(|x| *x |= bits).or_insert(bits);
        }
    }
}

impl aoc::Puzzle for AoC2024_06 {
    type Input = CharGrid;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2024, 6);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        CharGrid::from(&lines.iter().map(AsRef::as_ref).collect::<Vec<_>>())
    }

    fn part_1(&self, grid: &Self::Input) -> Self::Output1 {
        self.visited(grid, Direction::Up).len()
    }

    fn part_2(&self, grid: &Self::Input) -> Self::Output2 {
        let obs = Obstacles::from_grid(grid);
        let visited = self.visited(grid, Direction::Up);
        let mut it = visited.into_iter();
        let mut prev = it.next().unwrap();
        let mut ans = 0;
        for curr in it {
            let start = prev.0;
            let dir = prev.1.remove(0);
            if self.is_loop(&start, dir, &obs, &curr.0) {
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
