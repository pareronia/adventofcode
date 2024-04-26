#![allow(non_snake_case)]

use aoc::{
    geometry::Direction,
    graph::BFS,
    grid::{Cell, CharGrid, Grid},
    Puzzle,
};
use itertools::Itertools;
use std::collections::{HashMap, HashSet, VecDeque};

struct LoopFinder {
    tiles: HashMap<Direction, HashMap<char, Direction>>,
}

#[derive(Clone, Copy, Eq, Hash, PartialEq)]
struct Node {
    cell: Cell,
    direction: Direction,
}

struct State {
    distance: usize,
    node: Node,
}

struct EnlargeGridInsideFinder {
    xgrids: HashMap<char, CharGrid>,
}

struct AoC2023_10;

impl LoopFinder {
    fn new() -> Self {
        let mut tiles = HashMap::new();
        tiles.insert(
            Direction::Up,
            HashMap::from([
                ('|', Direction::Up),
                ('7', Direction::Left),
                ('F', Direction::Right),
            ]),
        );
        tiles.insert(
            Direction::Right,
            HashMap::from([
                ('-', Direction::Right),
                ('J', Direction::Up),
                ('7', Direction::Down),
            ]),
        );
        tiles.insert(
            Direction::Down,
            HashMap::from([
                ('|', Direction::Down),
                ('J', Direction::Left),
                ('L', Direction::Right),
            ]),
        );
        tiles.insert(
            Direction::Left,
            HashMap::from([
                ('-', Direction::Left),
                ('L', Direction::Up),
                ('F', Direction::Down),
            ]),
        );
        LoopFinder { tiles }
    }

    fn find_loop(&self, grid: &CharGrid) -> HashSet<Cell> {
        let start = grid.find_first_matching(|val| val == 'S').unwrap();
        let mut q: VecDeque<State> = VecDeque::new();
        let mut seen: HashSet<Node> = HashSet::new();
        let mut parent: HashMap<Node, Node> = HashMap::new();
        Direction::capital().iter().for_each(|d| {
            q.push_back(State {
                distance: 0,
                node: Node {
                    cell: start,
                    direction: *d,
                },
            })
        });
        while !q.is_empty() {
            let state = q.pop_front().unwrap();
            let curr = state.node.cell;
            let direction = state.node.direction;
            match curr.try_at(direction) {
                None => continue,
                Some(n) if n == start => {
                    let mut path = HashSet::new();
                    path.insert(curr);
                    let mut c = &Node {
                        cell: curr,
                        direction,
                    };
                    while parent.contains_key(c) {
                        c = parent.get(c).unwrap();
                        path.insert(c.cell);
                    }
                    return path;
                }
                Some(n) => {
                    let val = grid.get(&n);
                    if self.tiles.get(&direction).unwrap().contains_key(&val) {
                        let new_direction = self
                            .tiles
                            .get(&direction)
                            .unwrap()
                            .get(&val)
                            .unwrap();
                        let next = Node {
                            cell: n,
                            direction: *new_direction,
                        };
                        if seen.contains(&next) {
                            continue;
                        }
                        seen.insert(next);
                        parent.insert(next, state.node);
                        q.push_back(State {
                            distance: state.distance + 1,
                            node: next,
                        });
                    }
                }
            };
        }
        unreachable!()
    }
}

impl EnlargeGridInsideFinder {
    fn new() -> Self {
        let xgrids = HashMap::from([
            ('|', CharGrid::from(&[".#.", ".#.", ".#."])),
            ('-', CharGrid::from(&["...", "###", "..."])),
            ('L', CharGrid::from(&[".#.", ".##", "..."])),
            ('J', CharGrid::from(&[".#.", "##.", "..."])),
            ('7', CharGrid::from(&["...", "##.", ".#."])),
            ('F', CharGrid::from(&["...", ".##", ".#."])),
            ('.', CharGrid::from(&["...", "...", "..."])),
            ('S', CharGrid::from(&[".S.", "SSS", ".S."])),
        ]);
        Self { xgrids }
    }

    fn count_inside(&self, grid: &CharGrid, loop_: &HashSet<Cell>) -> usize {
        let grids: Vec<Vec<&CharGrid>> = (0..grid.height())
            .map(|r| {
                (0..grid.width())
                    .map(|c| Cell::at(r, c))
                    .map(|cell| {
                        if loop_.contains(&cell) {
                            self.xgrids.get(&grid.get(&cell)).unwrap()
                        } else {
                            self.xgrids.get(&'.').unwrap()
                        }
                    })
                    .collect()
            })
            .collect();
        let xgrid = CharGrid::merge(&grids);
        let new_loop = xgrid
            .cells()
            .filter(|cell| xgrid.get(&cell) == 'S' || xgrid.get(&cell) == '#')
            .collect::<HashSet<_>>();
        let adjacent = |cell: Cell| {
            xgrid
                .capital_neighbours(&cell)
                .into_iter()
                .filter(|c| !new_loop.contains(&c))
                .collect()
        };
        let outside = BFS::flood_fill(Cell::at(0, 0), adjacent);
        let cells = xgrid.cells().collect::<HashSet<_>>();
        let inside = &(&cells - &outside) - &new_loop;
        grid.cells()
            .filter(|cell| {
                (0..3)
                    .cartesian_product(0..3)
                    .map(|(r, c)| Cell::at(3 * cell.row + r, 3 * cell.col + c))
                    .all(|test| inside.contains(&test))
            })
            .count()
    }
}

impl AoC2023_10 {}

impl aoc::Puzzle for AoC2023_10 {
    type Input = CharGrid;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2023, 10);

    fn parse_input(&self, lines: Vec<String>) -> CharGrid {
        CharGrid::from(&lines.iter().map(AsRef::as_ref).collect::<Vec<_>>())
    }

    fn part_1(&self, grid: &CharGrid) -> usize {
        LoopFinder::new().find_loop(&grid).len() / 2
    }

    fn part_2(&self, grid: &CharGrid) -> usize {
        let loop_ = LoopFinder::new().find_loop(&grid);
        EnlargeGridInsideFinder::new().count_inside(&grid, &loop_)
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST1, 4,
            self, part_1, TEST2, 8,
            self, part_2, TEST1, 1,
            self, part_2, TEST2, 1,
            self, part_2, TEST3, 4,
            self, part_2, TEST4, 8,
            self, part_2, TEST5, 10
        };
    }
}

fn main() {
    AoC2023_10 {}.run(std::env::args());
}

const TEST1: &str = "\
-L|F7
7S-7|
L|7||
-L-J|
L|-JF
";
const TEST2: &str = "\
7-F7-
.FJ|7
SJLL7
|F--J
LJ.LJ
";
const TEST3: &str = "\
...........
.S-------7.
.|F-----7|.
.||.....||.
.||.....||.
.|L-7.F-J|.
.|..|.|..|.
.L--J.L--J.
...........
";
const TEST4: &str = "\
.F----7F7F7F7F-7....
.|F--7||||||||FJ....
.||.FJ||||||||L7....
FJL7L7LJLJ||LJ.L-7..
L--J.L7...LJS7F-7L7.
....F-J..F7FJ|L7L7L7
....L7.F7||L7|.L7L7|
.....|FJLJ|FJ|F7|.LJ
....FJL-7.||.||||...
....L---J.LJ.LJLJ...
";
const TEST5: &str = "\
FF7FSF7F7F7F7F7F---7
L|LJ||||||||||||F--J
FL-7LJLJ||||||LJL-77
F--JF--7||LJLJ7F7FJ-
L---JF-JLJ.||-FJLJJ7
|F|F-JF---7F7-L7L|7|
|FFJF7L7F-JF7|JL---7
7-L-JL7||F7|L7F-7F7|
L.L7LFJ|||||FJL7||LJ
L7JLJL-JLJLJL--JLJ.L
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2023_10 {}.samples();
    }
}
