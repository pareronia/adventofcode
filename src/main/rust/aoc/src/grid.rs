#[derive(Clone, Copy, Debug, PartialEq, Eq)]
pub struct Cell {
    pub row: usize,
    pub col: usize,
}

impl Cell {
    fn at(row: usize, col: usize) -> Cell {
        Cell { row, col }
    }
}

#[derive(Clone, Copy, Debug)]
pub enum Direction {
    Forward,
    North,
    East,
    South,
    West,
}

#[derive(Clone, Copy, Debug)]
pub struct GridIterator {
    width: usize,
    height: usize,
    direction: Direction,
    next: Option<Cell>,
}

impl Iterator for GridIterator {
    type Item = Cell;

    fn next(&mut self) -> Option<Self::Item> {
        match self.next {
            None => None,
            Some(val) => match self.direction {
                Direction::Forward => {
                    let n = val.row * self.width + val.col + 1;
                    self.next = match n {
                        n if n == self.height * self.width => None,
                        _ => Some(Cell::at(n / self.width, n % self.width)),
                    };
                    Some(val)
                }
                Direction::North => {
                    self.next = match val.row > 0 {
                        true => Some(Cell::at(val.row - 1, val.col)),
                        false => None,
                    };
                    self.next
                }
                Direction::East => {
                    self.next = match val.col < self.width - 1 {
                        true => Some(Cell::at(val.row, val.col + 1)),
                        false => None,
                    };
                    self.next
                }
                Direction::South => {
                    self.next = match val.row < self.height - 1 {
                        true => Some(Cell::at(val.row + 1, val.col)),
                        false => None,
                    };
                    self.next
                }
                Direction::West => {
                    self.next = match val.col > 0 {
                        true => Some(Cell::at(val.row, val.col - 1)),
                        false => None,
                    };
                    self.next
                }
            },
        }
    }
}

pub trait Grid {
    type Item: Copy;

    fn get_data(&self) -> &Vec<Vec<Self::Item>>;

    fn valid_row_index(&self, row: usize) -> bool {
        row < self.height()
    }

    fn valid_column_index(&self, col: usize) -> bool {
        col < self.width()
    }

    fn get(&self, cell: &Cell) -> Self::Item {
        assert!(
            self.valid_row_index(cell.row),
            "Invalid row index {}",
            cell.row
        );
        assert!(
            self.valid_column_index(cell.col),
            "Invalid column index {}",
            cell.col
        );
        self.get_data()[cell.row][cell.col]
    }

    fn width(&self) -> usize {
        self.get_data().iter().next().map_or(0, |row| row.len())
    }

    fn height(&self) -> usize {
        self.get_data().len()
    }

    fn size(&self) -> usize {
        self.height() * self.width()
    }

    fn cells(&self) -> GridIterator {
        GridIterator {
            width: self.width(),
            height: self.height(),
            direction: Direction::Forward,
            next: Some(Cell::at(0, 0)),
        }
    }

    fn cells_north(&self, cell: &Cell) -> GridIterator {
        GridIterator {
            width: self.width(),
            height: self.height(),
            direction: Direction::North,
            next: Some(Cell::at(cell.row, cell.col)),
        }
    }

    fn cells_east(&self, cell: &Cell) -> GridIterator {
        GridIterator {
            width: self.width(),
            height: self.height(),
            direction: Direction::East,
            next: Some(Cell::at(cell.row, cell.col)),
        }
    }

    fn cells_south(&self, cell: &Cell) -> GridIterator {
        GridIterator {
            width: self.width(),
            height: self.height(),
            direction: Direction::South,
            next: Some(Cell::at(cell.row, cell.col)),
        }
    }

    fn cells_west(&self, cell: &Cell) -> GridIterator {
        GridIterator {
            width: self.width(),
            height: self.height(),
            direction: Direction::West,
            next: Some(Cell::at(cell.row, cell.col)),
        }
    }

    // TODO return iterator
    fn cells_capital_directions(&self, cell: &Cell) -> Vec<GridIterator> {
        vec![
            self.cells_north(cell),
            self.cells_east(cell),
            self.cells_south(cell),
            self.cells_west(cell),
        ]
    }
}

pub struct IntGrid {
    data: Vec<Vec<u32>>,
}

impl IntGrid {
    pub fn from(input: &Vec<String>) -> IntGrid {
        let width = match input.len() {
            0 => panic!("Empty input to Grid"),
            _ => input[0].len(),
        };
        let mut rows = vec![];
        input.iter().for_each(|line| {
            if line.len() != width {
                panic!("Grid input should have equal widths")
            }
            let row: Vec<u32> =
                line.chars().map(|c| c.to_digit(10).unwrap()).collect();
            rows.push(row);
        });
        IntGrid { data: rows }
    }
}

impl Grid for IntGrid {
    type Item = u32;

    fn get_data(&self) -> &Vec<Vec<u32>> {
        &self.data
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    #[should_panic]
    pub fn int_empty() {
        IntGrid::from(&vec![]);
    }

    #[test]
    #[should_panic]
    pub fn int_not_equal_width() {
        IntGrid::from(&vec!["12".to_string(), "345".to_string()]);
    }

    #[test]
    pub fn int() {
        let grid = IntGrid::from(&vec!["1234".to_string(), "5678".to_string()]);
        assert_eq!(grid.width(), 4);
        assert_eq!(grid.height(), 2);
        assert_eq!(grid.size(), 8);
        assert_eq!(grid.get(&Cell::at(0, 0)), 1);
    }

    #[test]
    pub fn iterator() {
        let grid = IntGrid::from(&vec!["12".to_string(), "34".to_string()]);
        assert_eq!(
            grid.cells().collect::<Vec<Cell>>(),
            vec![
                Cell::at(0, 0),
                Cell::at(0, 1),
                Cell::at(1, 0),
                Cell::at(1, 1)
            ]
        );
    }

    #[test]
    pub fn dirs() {
        let grid = IntGrid::from(&vec![
            "12345".to_string(),
            "12345".to_string(),
            "12345".to_string(),
            "12345".to_string(),
            "12345".to_string(),
        ]);
        assert_eq!(
            grid.cells_north(&Cell::at(3, 1)).collect::<Vec<Cell>>(),
            vec![Cell::at(2, 1), Cell::at(1, 1), Cell::at(0, 1)]
        );
        assert_eq!(
            grid.cells_north(&Cell::at(0, 1)).collect::<Vec<Cell>>(),
            vec![]
        );
        assert_eq!(
            grid.cells_east(&Cell::at(2, 1)).collect::<Vec<Cell>>(),
            vec![Cell::at(2, 2), Cell::at(2, 3), Cell::at(2, 4)]
        );
        assert_eq!(
            grid.cells_south(&Cell::at(2, 1)).collect::<Vec<Cell>>(),
            vec![Cell::at(3, 1), Cell::at(4, 1)]
        );
        assert_eq!(
            grid.cells_west(&Cell::at(2, 3)).collect::<Vec<Cell>>(),
            vec![Cell::at(2, 2), Cell::at(2, 1), Cell::at(2, 0)]
        );
    }
}
