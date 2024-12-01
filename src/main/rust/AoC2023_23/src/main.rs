#![allow(non_snake_case)]

use std::{
    collections::{HashMap, HashSet, VecDeque},
    str::FromStr,
};

use aoc::{
    geometry::Direction,
    grid::{Cell, CharGrid, Grid},
    Puzzle,
};

#[derive(Debug, Eq, Hash, PartialEq)]
struct Edge {
    cell: Cell,
    distance: usize,
}

struct PathFinder<'a> {
    grid: &'a CharGrid,
    start: Cell,
    end: Cell,
}

impl<'a> PathFinder<'a> {
    fn new(grid: &'a CharGrid) -> Self {
        Self {
            grid,
            start: Cell::at(0, 1),
            end: Cell::at(grid.height() - 1, grid.width() - 2),
        }
    }

    fn find_forks(&self) -> HashSet<Cell> {
        let is_fork = |cell: &Cell| {
            cell == &self.start
                || cell == &self.end
                || self.grid.get(cell) != '#'
                    && self
                        .grid
                        .capital_neighbours(cell)
                        .iter()
                        .filter(|n| self.grid.get(n) != '#')
                        .count()
                        > 2
        };

        self.grid.cells().filter(is_fork).collect::<HashSet<Cell>>()
    }

    fn build_graph(
        &self,
        forks: &HashSet<Cell>,
        downward_slopes_only: bool,
    ) -> HashMap<Cell, HashSet<Edge>> {
        let mut graph = HashMap::<Cell, HashSet<Edge>>::new();
        for fork in forks {
            let mut q = VecDeque::<(Cell, usize)>::new();
            q.push_back((*fork, 0_usize));
            let mut seen = HashSet::<Cell>::new();
            seen.insert(*fork);
            while !q.is_empty() {
                let (cell, distance) = q.pop_front().unwrap();
                if forks.contains(&cell) && cell != *fork {
                    graph
                        .entry(*fork)
                        .and_modify(|edges| {
                            edges.insert(Edge { cell, distance });
                        })
                        .or_insert(
                            vec![Edge { cell, distance }]
                                .into_iter()
                                .collect::<HashSet<Edge>>(),
                        );
                    continue;
                }
                for d in Direction::capital() {
                    let n = match cell.try_at(d) {
                        Some(val) => val,
                        None => continue,
                    };
                    if !self.grid.in_bounds(&n) {
                        continue;
                    }
                    let val = self.grid.get(&n);
                    if val == '#'
                        || (downward_slopes_only
                            && ['v', '^', '<', '>'].contains(&val)
                            && Direction::from_str(&val.to_string()).unwrap()
                                != d)
                    {
                        continue;
                    }
                    if !seen.contains(&n) {
                        seen.insert(n);
                        q.push_back((n, distance + 1));
                    }
                }
            }
        }
        graph
    }

    fn find_longest(
        graph: &HashMap<Cell, HashSet<Edge>>,
        curr: Cell,
        end: &Cell,
        seen: &mut HashSet<Cell>,
    ) -> i32 {
        if curr == *end {
            return 0;
        }
        let mut ans: i32 = -1_000_000_000;
        seen.insert(curr);
        for Edge { cell, distance } in graph.get(&curr).unwrap().iter() {
            if seen.contains(cell) {
                continue;
            }
            ans = ans.max(
                *distance as i32 + Self::find_longest(graph, *cell, end, seen),
            );
        }
        seen.remove(&curr);
        ans
    }

    fn find_longest_hike_length_with_only_downward_slopes(&self) -> u32 {
        let forks = self.find_forks();
        let graph = self.build_graph(&forks, true);
        Self::find_longest(
            &graph,
            self.start,
            &self.end,
            &mut HashSet::<Cell>::new(),
        ) as u32
    }

    fn find_longest_hike_length(&self) -> u32 {
        let forks = self.find_forks();
        let graph = self.build_graph(&forks, false);
        let dist_from_start = graph
            .get(&self.start)
            .unwrap()
            .iter()
            .nth(0)
            .unwrap()
            .distance;
        let dist_to_end = graph
            .get(&self.end)
            .unwrap()
            .iter()
            .nth(0)
            .unwrap()
            .distance;
        let new_start = graph
            .iter()
            .filter(|(_, edges)| {
                edges.contains(&Edge {
                    cell: self.start,
                    distance: dist_from_start,
                })
            })
            .map(|(cell, _)| cell)
            .nth(0)
            .unwrap();
        let new_end = graph
            .iter()
            .filter(|(_, edges)| {
                edges.contains(&Edge {
                    cell: self.end,
                    distance: dist_to_end,
                })
            })
            .map(|(cell, _)| cell)
            .nth(0)
            .unwrap();
        dist_from_start as u32
            + dist_to_end as u32
            + Self::find_longest(
                &graph,
                *new_start,
                new_end,
                &mut HashSet::<Cell>::new(),
            ) as u32
    }
}

struct AoC2023_23;

impl AoC2023_23 {}

impl aoc::Puzzle for AoC2023_23 {
    type Input = CharGrid;
    type Output1 = u32;
    type Output2 = u32;

    aoc::puzzle_year_day!(2023, 23);

    fn parse_input(&self, lines: Vec<String>) -> CharGrid {
        CharGrid::from(&lines.iter().map(AsRef::as_ref).collect::<Vec<_>>())
    }

    fn part_1(&self, grid: &CharGrid) -> u32 {
        PathFinder::new(grid)
            .find_longest_hike_length_with_only_downward_slopes()
    }

    fn part_2(&self, grid: &CharGrid) -> u32 {
        PathFinder::new(grid).find_longest_hike_length()
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 94,
            self, part_2, TEST, 154
        };
    }
}

fn main() {
    AoC2023_23 {}.run(std::env::args());
}

const TEST: &str = "\
#.#####################
#.......#########...###
#######.#########.#.###
###.....#.>.>.###.#.###
###v#####.#v#.###.#.###
###.>...#.#.#.....#...#
###v###.#.#.#########.#
###...#.#.#.......#...#
#####.#.#.#######.#.###
#.....#.#.#.......#...#
#.#####.#.#.#########v#
#.#...#...#...###...>.#
#.#.#v#######v###.###v#
#...#.>.#...>.>.#.###.#
#####v#.#.###v#.#.###.#
#.....#...#...#.#.#...#
#.#########.###.#.#.###
#...###...#...#...#.###
###.###.#.###v#####v###
#...#...#.#.>.>.#.>.###
#.###.###.#.###.#.#v###
#.....###...###...#...#
#####################.#
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2023_23 {}.samples();
    }
}
