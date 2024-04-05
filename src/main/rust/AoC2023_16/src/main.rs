#![allow(non_snake_case)]

use aoc::geometry::{Direction, Turn};
use aoc::graph::BFS;
use aoc::grid::{Cell, CharGrid, Grid};
use aoc::Puzzle;
use itertools::Itertools;

#[derive(Clone, Copy, Eq, Hash, PartialEq)]
struct Beam {
    cell: Cell,
    dir: Direction,
}

impl Beam {
    fn from<'a>(
        &'a self,
        dirs: &'a [Direction],
    ) -> impl Iterator<Item = Self> + 'a {
        dirs.iter()
            .filter_map(|dir| self.cell.try_at(*dir).map(|cell| (cell, *dir)))
            .map(|(cell, dir)| Beam { cell, dir })
    }
}

struct Contraption {
    grid: CharGrid,
}

impl Contraption {
    fn get_beams_from(&self, beam: &Beam, dirs: &[Direction]) -> Vec<Beam> {
        beam.from(dirs)
            .filter(|beam| self.grid.in_bounds(&beam.cell))
            .collect()
    }

    fn get_energised(&self, initial_beam: Beam) -> u32 {
        let adjacent: &dyn Fn(Beam) -> Vec<Beam> = &|beam: Beam| {
            let val = self.grid.get(&beam.cell);
            match (val, beam.dir.is_horizontal()) {
                ('.', true) | ('.', false) | ('|', false) | ('-', true) => {
                    self.get_beams_from(&beam, &[beam.dir])
                }
                ('/', true) | ('\\', false) => {
                    self.get_beams_from(&beam, &[beam.dir.turn(Turn::Left)])
                }
                ('/', false) | ('\\', true) => {
                    self.get_beams_from(&beam, &[beam.dir.turn(Turn::Right)])
                }
                ('|', true) => self
                    .get_beams_from(&beam, &[Direction::Up, Direction::Down]),
                ('-', false) => self.get_beams_from(
                    &beam,
                    &[Direction::Left, Direction::Right],
                ),
                _ => panic!(),
            }
        };
        BFS::flood_fill(initial_beam, adjacent)
            .iter()
            .map(|beam| beam.cell)
            .unique()
            .count() as u32
    }

    fn get_initial_energy(&self) -> u32 {
        self.get_energised(Beam {
            cell: Cell::at(0, 0),
            dir: Direction::Right,
        })
    }

    fn get_maximal_energy(&self) -> u32 {
        let it1 = (0..self.grid.height()).map(|row| Beam {
            cell: Cell::at(row, 0),
            dir: Direction::Right,
        });
        let it2 = (0..self.grid.height()).map(|row| Beam {
            cell: Cell::at(row, self.grid.width() - 1),
            dir: Direction::Left,
        });
        let it3 = (0..self.grid.width()).map(|col| Beam {
            cell: Cell::at(0, col),
            dir: Direction::Down,
        });
        let it4 = (0..self.grid.width()).map(|col| Beam {
            cell: Cell::at(self.grid.height() - 1, col),
            dir: Direction::Up,
        });
        it1.chain(it2)
            .chain(it3)
            .chain(it4)
            .map(|beam| self.get_energised(beam))
            .max()
            .unwrap()
    }
}

struct AoC2023_16;

impl AoC2023_16 {}

impl aoc::Puzzle for AoC2023_16 {
    type Input = Contraption;
    type Output1 = u32;
    type Output2 = u32;

    aoc::puzzle_year_day!(2023, 16);

    fn parse_input(&self, lines: Vec<String>) -> Contraption {
        let grid = CharGrid::from(&lines.iter().map(AsRef::as_ref).collect());
        Contraption { grid }
    }

    fn part_1(&self, contraption: &Contraption) -> u32 {
        contraption.get_initial_energy()
    }

    fn part_2(&self, contraption: &Contraption) -> u32 {
        contraption.get_maximal_energy()
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 46,
            self, part_2, TEST, 51
        };
    }
}

fn main() {
    AoC2023_16 {}.run(std::env::args());
}

const TEST: &str = "\
.|...\\....
|.-.\\.....
.....|-...
........|.
..........
.........\\
..../.\\\\..
.-.-/..|..
.|....-|.\\
..//.|....
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2023_16 {}.samples();
    }
}
