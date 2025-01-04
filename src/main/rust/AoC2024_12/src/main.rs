#![allow(non_snake_case)]

use aoc::geometry::Direction;
use aoc::graph::BFS;
use aoc::grid::Cell;
use aoc::Puzzle;
use itertools::Itertools;
use lazy_static::lazy_static;
use std::collections::{HashMap, HashSet};

lazy_static! {
    static ref CORNER_DIRS: [[Direction; 3]; 4] = [
        [Direction::LeftAndUp, Direction::Left, Direction::Up],
        [Direction::RightAndUp, Direction::Right, Direction::Up],
        [Direction::RightAndDown, Direction::Right, Direction::Down],
        [Direction::LeftAndDown, Direction::Left, Direction::Down],
    ];
    static ref MATCHES: [[bool; 3]; 3] = [
        [false, false, false],
        [true, false, false],
        [false, true, true],
    ];
}

#[derive(Clone, Debug)]
struct Regions {
    plots_by_plant: HashMap<char, HashSet<Cell>>,
}

impl Regions {
    fn new() -> Self {
        Self {
            plots_by_plant: HashMap::new(),
        }
    }

    fn iter(&self) -> RegionIterator {
        RegionIterator {
            all_plots_with_plant: self.plots_by_plant.values().collect(),
            index: 0,
            seen: HashSet::new(),
        }
    }
}

impl FromIterator<(char, Cell)> for Regions {
    fn from_iter<I: IntoIterator<Item = (char, Cell)>>(iter: I) -> Self {
        let mut regions = Regions::new();
        for (ch, cell) in iter {
            regions
                .plots_by_plant
                .entry(ch)
                .and_modify(|s| {
                    s.insert(cell);
                })
                .or_insert(HashSet::from([cell]));
        }
        regions
    }
}

struct RegionIterator<'a> {
    all_plots_with_plant: Vec<&'a HashSet<Cell>>,
    index: usize,
    seen: HashSet<Cell>,
}

#[allow(clippy::needless_lifetimes)]
impl<'a> Iterator for RegionIterator<'a> {
    type Item = HashSet<Cell>;

    fn next(&mut self) -> Option<Self::Item> {
        if self.seen.len() == self.all_plots_with_plant[self.index].len() {
            if self.index + 1 == self.all_plots_with_plant.len() {
                return None;
            }
            self.index += 1;
            self.seen.clear();
        }
        let region = BFS::flood_fill(
            *self.all_plots_with_plant[self.index]
                .difference(&self.seen)
                .next()
                .unwrap(),
            |cell| {
                cell.capital_neighbours()
                    .into_iter()
                    .filter(|n| {
                        self.all_plots_with_plant[self.index].contains(n)
                            && !self.seen.contains(n)
                    })
                    .collect()
            },
        );
        region.iter().for_each(|r| {
            self.seen.insert(*r);
        });
        Some(region)
    }
}

enum Pricing {
    Perimeter,
    NumberOfSides,
}

impl Pricing {
    fn calculate(&self, plot: &Cell, region: &HashSet<Cell>) -> usize {
        match self {
            Pricing::Perimeter => {
                4 - plot
                    .capital_neighbours()
                    .iter()
                    .filter(|n| region.contains(n))
                    .count()
            }
            Pricing::NumberOfSides => CORNER_DIRS
                .iter()
                .filter(|d| {
                    let test = (0..3)
                        .map(|i| {
                            plot.try_at(d[i])
                                .is_some_and(|n| region.contains(&n))
                        })
                        .collect::<Vec<bool>>();
                    MATCHES.iter().any(|m| *m == *test)
                })
                .count(),
        }
    }
}

struct AoC2024_12;

impl AoC2024_12 {
    fn solve(&self, input: &[String], pricing: Pricing) -> usize {
        let regions: Regions = (0..input.len())
            .cartesian_product(0..input[0].len())
            .map(|(r, c)| (input[r].chars().nth(c).unwrap(), Cell::at(r, c)))
            .collect();
        regions
            .iter()
            .map(|r| {
                r.iter()
                    .map(|plot| pricing.calculate(plot, &r) * r.len())
                    .sum::<usize>()
            })
            .sum()
    }
}

impl aoc::Puzzle for AoC2024_12 {
    type Input = Vec<String>;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2024, 12);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        lines
    }

    fn part_1(&self, input: &Self::Input) -> Self::Output1 {
        self.solve(input, Pricing::Perimeter)
    }

    fn part_2(&self, input: &Self::Input) -> Self::Output2 {
        self.solve(input, Pricing::NumberOfSides)
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST1, 140,
            self, part_1, TEST2, 772,
            self, part_1, TEST3, 1930,
            self, part_2, TEST1, 80,
            self, part_2, TEST2, 436,
            self, part_2, TEST3, 1206,
            self, part_2, TEST4, 236,
            self, part_2, TEST5, 368
        };
    }
}

fn main() {
    AoC2024_12 {}.run(std::env::args());
}

const TEST1: &str = "\
AAAA
BBCD
BBCC
EEEC
";
const TEST2: &str = "\
OOOOO
OXOXO
OOOOO
OXOXO
OOOOO
";
const TEST3: &str = "\
RRRRIICCFF
RRRRIICCCF
VVRRRCCFFF
VVRCCCJFFF
VVVVCJJCFE
VVIVCCJJEE
VVIIICJJEE
MIIIIIJJEE
MIIISIJEEE
MMMISSJEEE
";
const TEST4: &str = "\
EEEEE
EXXXX
EEEEE
EXXXX
EEEEE
";
const TEST5: &str = "\
AAAAAA
AAABBA
AAABBA
ABBAAA
ABBAAA
AAAAAA
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2024_12 {}.samples();
    }

    #[test]
    pub fn regions_iterator() {
        // AABBC
        // ACCDC
        let regions: Regions = [
            ('A', Cell::at(0, 0)),
            ('A', Cell::at(0, 1)),
            ('B', Cell::at(0, 2)),
            ('B', Cell::at(0, 3)),
            ('C', Cell::at(0, 4)),
            ('A', Cell::at(1, 0)),
            ('C', Cell::at(1, 1)),
            ('C', Cell::at(1, 2)),
            ('D', Cell::at(1, 3)),
            ('C', Cell::at(1, 4)),
        ]
        .into_iter()
        .collect();
        let ans = regions.iter().collect::<Vec<_>>();
        // A
        assert!(ans.contains(&HashSet::from([
            Cell::at(0, 0),
            Cell::at(0, 1),
            Cell::at(1, 0),
        ])));
        // B
        assert!(ans.contains(&HashSet::from([Cell::at(0, 2), Cell::at(0, 3),])));
        // C
        assert!(ans.contains(&HashSet::from([Cell::at(0, 4), Cell::at(1, 4),])));
        assert!(ans.contains(&HashSet::from([Cell::at(1, 1), Cell::at(1, 2),])));
        // D
        assert!(ans.contains(&HashSet::from([Cell::at(1, 3)])));
    }
}
