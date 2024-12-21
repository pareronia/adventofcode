#![allow(non_snake_case)]

use aoc::graph::BFS;
use aoc::grid::{CharGrid, Grid};
use aoc::Puzzle;

struct AoC2024_20;

impl AoC2024_20 {
    fn solve(&self, grid: &CharGrid, cheat_len: usize, target: usize) -> usize {
        let start = grid.find_first_matching(|ch| ch == 'S').unwrap();
        let distances = BFS::execute_full(
            start,
            |cell| grid.get(&cell) != '#',
            |cell| {
                grid.capital_neighbours(&cell)
                    .into_iter()
                    .filter(|n| grid.get(n) != '#')
                    .collect()
            },
        );
        let mut ans = 0;
        for cell in distances.keys() {
            for md in 2..cheat_len + 1 {
                for n in cell.get_all_at_manhattan_distance(md) {
                    if !distances.contains_key(&n) {
                        continue;
                    }
                    if distances[&n] < distances[cell] {
                        continue;
                    }
                    if distances[&n] - distances[cell] >= target + md {
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
