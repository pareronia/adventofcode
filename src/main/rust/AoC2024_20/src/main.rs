#![allow(non_snake_case)]

use aoc::geometry::{Direction, Turn};
use aoc::grid::{Cell, CharGrid, Grid};
use aoc::Puzzle;

struct AoC2024_20;

impl AoC2024_20 {
    fn solve(&self, grid: &CharGrid, cheat_len: usize, target: usize) -> usize {
        let (h, w) = (grid.height(), grid.width());
        let start = grid.find_first_matching(|ch| ch == 'S').unwrap();
        let end = grid.find_first_matching(|ch| ch == 'E').unwrap();
        let mut dir = Direction::capital()
            .into_iter()
            .find(|d| start.try_at(*d).is_some_and(|n| grid.get(&n) != '#'))
            .unwrap();
        let mut pos = start;
        let mut dist = 0;
        let mut track: Vec<Cell> = Vec::new();
        let mut d: Vec<usize> = Vec::with_capacity(h * w);
        (0..h).for_each(|_| {
            (0..w).for_each(|_| {
                d.push(usize::MAX);
            });
        });
        loop {
            d[pos.row * h + pos.col] = dist;
            track.push(pos);
            if pos == end {
                break;
            }
            dir = [dir, dir.turn(Turn::Right), dir.turn(Turn::Left)]
                .into_iter()
                .find(|d| pos.try_at(*d).is_some_and(|n| grid.get(&n) != '#'))
                .unwrap();
            pos = pos.try_at(dir).unwrap();
            dist += 1;
        }
        let mut ans = 0;
        for cell in track {
            let d_cell = d[cell.row * h + cell.col];
            for md in 2..cheat_len + 1 {
                let min_req = target + md;
                for n in cell.get_all_at_manhattan_distance(md) {
                    if !(0..h).contains(&n.row) || !(0..w).contains(&n.col) {
                        continue;
                    }
                    let d_n = d[n.row * h + n.col];
                    if d_n == usize::MAX || d_n < d_cell {
                        continue;
                    }
                    if d_n - d_cell >= min_req {
                        ans += 1;
                    }
                }
            }
        }
        ans
    }
    fn sample_part_1(&self, grid: &CharGrid) -> usize {
        self.solve(grid, 2, 2)
    }

    fn sample_part_2(&self, grid: &CharGrid) -> usize {
        self.solve(grid, 20, 50)
    }
}

impl aoc::Puzzle for AoC2024_20 {
    type Input = CharGrid;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2024, 20);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        CharGrid::from(&lines.iter().map(AsRef::as_ref).collect::<Vec<_>>())
    }

    fn part_1(&self, grid: &Self::Input) -> Self::Output1 {
        self.solve(grid, 2, 100)
    }

    fn part_2(&self, grid: &Self::Input) -> Self::Output2 {
        self.solve(grid, 20, 100)
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, sample_part_1, TEST, 44,
            self, sample_part_2, TEST, 285
        };
    }
}

fn main() {
    AoC2024_20 {}.run(std::env::args());
}

const TEST: &str = "\
###############
#...#...#.....#
#.#.#.#.#.###.#
#S#...#.#.#...#
#######.#.#.###
#######.#.#...#
#######.#.###.#
###..E#...#...#
###.#######.###
#...###...#...#
#.#####.#.###.#
#.#...#.#.#...#
#.#.#.#.#.#.###
#...#...#...###
###############
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2024_20 {}.samples();
    }
}
