#![allow(non_snake_case)]

use aoc::grid::*;
use aoc::Puzzle;

struct AoC2022_08;

impl AoC2022_08 {
    fn ignoring_borders(&self, grid: &IntGrid) -> Vec<Cell> {
        grid.cells()
            .filter(|cell| 1 <= cell.row)
            .filter(|cell| cell.row < grid.height() - 1)
            .filter(|cell| 1 <= cell.col)
            .filter(|cell| cell.col < grid.width() - 1)
            .collect()
    }

    fn visible_from_outside(&self, grid: &IntGrid, cell: &Cell) -> bool {
        let val = grid.get(cell);
        grid.cells_capital_directions(cell)
            .iter_mut()
            .any(|dir| dir.all(|cell| grid.get(&cell) < val))
    }

    fn viewing_distance(
        &self,
        grid: &IntGrid,
        direction: &GridIterator,
        val: u32,
    ) -> u32 {
        let mut n = 0;
        let mut stop = false;
        for cell in direction.collect::<Vec<Cell>>() {
            if stop {
                break;
            }
            n += 1;
            stop = grid.get(&cell) >= val;
        }
        n
    }

    fn scenic_score(&self, grid: &IntGrid, cell: &Cell) -> u32 {
        grid.cells_capital_directions(cell)
            .iter()
            .map(|dir| self.viewing_distance(grid, dir, grid.get(cell)))
            .product()
    }
}

impl aoc::Puzzle for AoC2022_08 {
    type Input = IntGrid;
    type Output1 = usize;
    type Output2 = u32;

    aoc::puzzle_year_day!(2022, 8);

    fn parse_input(&self, lines: Vec<String>) -> IntGrid {
        IntGrid::from(&lines.iter().map(AsRef::as_ref).collect::<Vec<_>>())
    }

    fn part_1(&self, grid: &IntGrid) -> usize {
        let ans = 2 * (grid.height() + grid.width()) - 4;
        ans + self
            .ignoring_borders(grid)
            .iter()
            .filter(|cell| self.visible_from_outside(grid, cell))
            .count()
    }

    fn part_2(&self, grid: &IntGrid) -> u32 {
        self.ignoring_borders(grid)
            .iter()
            .map(|cell| self.scenic_score(grid, cell))
            .max()
            .unwrap()
    }

    fn samples(&self) {
        #[rustfmt::skip]
        let test =
            "30373\n\
             25512\n\
             65332\n\
             33549\n\
             35390";
        aoc::puzzle_samples! {
            self, part_1, test, 21,
            self, part_2, test, 8
        };
    }
}

fn main() {
    AoC2022_08 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2022_08 {}.samples();
    }
}
