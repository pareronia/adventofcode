use std::fmt::{Display, Error, Formatter};

#[derive(Clone, Copy, Debug, PartialEq, Eq)]
pub struct Cell {
    pub row: usize,
    pub col: usize,
}

impl Cell {
    pub fn at(row: usize, col: usize) -> Cell {
        Cell { row, col }
    }
}

pub struct CellRange {
    from: Cell,
    to: Cell,
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
    type Item: Copy + Display + PartialEq;

    fn get_data(&self) -> &Vec<Vec<Self::Item>>;

    fn get_data_mut(&mut self) -> &mut Vec<Vec<Self::Item>>;

    fn as_string(&self) -> String {
        self.get_data()
            .iter()
            .map(|row| {
                row.iter().map(|item| item.to_string()).collect::<String>()
            })
            .collect::<Vec<String>>()
            .join("\n")
    }

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

    fn cell_range(&self, from: &Cell, to: &Cell) -> CellRange {
        match from.row <= to.row && from.col <= to.col {
            true => {
                let from_row = from.row.min(self.height());
                let to_row = to.row.min(self.height());
                let from_col = from.col.min(self.width());
                let to_col = to.col.min(self.width());
                CellRange {
                    from: Cell::at(from_row, from_col),
                    to: Cell::at(to_row, to_col),
                }
            }
            false => panic!("from should not be before to"),
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

    fn replace(&mut self, val1: Self::Item, val2: Self::Item) {
        (0..self.height()).for_each(|row| {
            (0..self.width()).for_each(|col| {
                if self.get_data()[row][col] == val1 {
                    self.get_data_mut()[row][col] = val2;
                }
            });
        });
    }

    fn get_sub_grid_data(
        &self,
        from: &Cell,
        to: &Cell,
    ) -> Vec<Vec<Self::Item>> {
        let cell_range = self.cell_range(from, to);
        let row_slice =
            &self.get_data()[cell_range.from.row..cell_range.to.row];
        row_slice
            .iter()
            .map(|row| {
                (&row[cell_range.from.col..cell_range.to.col])
                    .iter()
                    .map(|c| c.clone())
                    .collect()
            })
            .collect()
    }
}

#[derive(Debug, PartialEq)]
pub struct IntGrid {
    data: Vec<Vec<u32>>,
}

impl IntGrid {
    pub fn from(input: &Vec<&str>) -> IntGrid {
        let width = match input.len() {
            0 => panic!("Empty input to Grid"),
            _ => input[0].chars().count(),
        };
        let mut rows = vec![];
        input.iter().for_each(|line| {
            if line.chars().count() != width {
                panic!("Grid input should have equal widths")
            }
            let row: Vec<u32> =
                line.chars().map(|c| c.to_digit(10).unwrap()).collect();
            rows.push(row);
        });
        IntGrid { data: rows }
    }

    pub fn sub_grid(&self, from: &Cell, to: &Cell) -> IntGrid {
        IntGrid {
            data: self.get_sub_grid_data(from, to),
        }
    }
}

impl Grid for IntGrid {
    type Item = u32;

    fn get_data(&self) -> &Vec<Vec<u32>> {
        &self.data
    }

    fn get_data_mut(&mut self) -> &mut Vec<Vec<u32>> {
        &mut self.data
    }
}

impl Display for IntGrid {
    fn fmt(&self, f: &mut Formatter<'_>) -> Result<(), Error> {
        write!(f, "{}", self.as_string())
    }
}

#[derive(Clone, Debug, Eq, Hash, PartialEq)]
pub struct CharGrid {
    data: Vec<Vec<char>>,
}

impl CharGrid {
    pub fn from(input: &Vec<&str>) -> CharGrid {
        let width = match input.len() {
            0 => panic!("Empty input to Grid"),
            _ => input[0].chars().count(),
        };
        let mut rows = vec![];
        input.iter().for_each(|line| {
            if line.chars().count() != width {
                panic!("Grid input should have equal widths")
            }
            let row: Vec<char> = line.chars().collect();
            rows.push(row);
        });
        CharGrid { data: rows }
    }

    pub fn sub_grid(&self, from: &Cell, to: &Cell) -> CharGrid {
        CharGrid {
            data: self.get_sub_grid_data(from, to),
        }
    }
}

impl Grid for CharGrid {
    type Item = char;

    fn get_data(&self) -> &Vec<Vec<char>> {
        &self.data
    }

    fn get_data_mut(&mut self) -> &mut Vec<Vec<char>> {
        &mut self.data
    }
}

impl Display for CharGrid {
    fn fmt(&self, f: &mut Formatter<'_>) -> Result<(), Error> {
        write!(f, "{}", self.as_string())
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
        IntGrid::from(&vec!["12", "345"]);
    }

    #[test]
    pub fn int() {
        let grid = IntGrid::from(&vec!["1234", "5678"]);
        assert_eq!(grid.width(), 4);
        assert_eq!(grid.height(), 2);
        assert_eq!(grid.size(), 8);
        assert_eq!(grid.get(&Cell::at(0, 0)), 1);
    }

    #[test]
    pub fn int_sub_grid() {
        let grid = IntGrid::from(&vec!["1234", "5678"]);
        assert_eq!(
            grid.sub_grid(&Cell::at(1, 1), &Cell::at(2, 4)),
            IntGrid::from(&vec!["678"])
        );
    }

    #[test]
    pub fn int_to_string() {
        let grid = IntGrid::from(&vec!["1234", "5678"]);
        assert_eq!(grid.to_string(), "1234\n5678".to_string());
    }

    #[test]
    #[should_panic]
    pub fn char_empty() {
        CharGrid::from(&vec![]);
    }

    #[test]
    #[should_panic]
    pub fn char_not_equal_width() {
        CharGrid::from(&vec!["ab", "cde"]);
    }

    #[test]
    pub fn char() {
        let grid = CharGrid::from(&vec!["a😀😀", "def"]);
        assert_eq!(grid.width(), 3);
        assert_eq!(grid.height(), 2);
        assert_eq!(grid.size(), 6);
        assert_eq!(grid.get(&Cell::at(0, 0)), 'a');
    }

    #[test]
    pub fn char_sub_grid() {
        let grid =
            CharGrid::from(&vec!["ABCD", "EFGH", "IJKL", "MNOP", "QRST"]);
        assert_eq!(
            grid.sub_grid(&Cell::at(1, 1), &Cell::at(4, 4)),
            CharGrid::from(&vec!["FGH", "JKL", "NOP"])
        );
        assert_eq!(
            grid.sub_grid(&Cell::at(0, 0), &Cell::at(0, 0)),
            CharGrid { data: vec![] }
        );
        assert_eq!(
            grid.sub_grid(&Cell::at(1, 1), &Cell::at(5, 5)),
            CharGrid::from(&vec!["FGH", "JKL", "NOP", "RST"])
        );
    }

    #[test]
    pub fn char_to_string() {
        let grid = CharGrid::from(&vec!["ABCD", "EFGH"]);
        assert_eq!(grid.to_string(), "ABCD\nEFGH".to_string());
    }

    #[test]
    pub fn char_replace() {
        let mut grid = CharGrid::from(&vec!["XOX", "OXO", "XOX"]);
        grid.replace('X', 'Y');
        assert_eq!(grid, CharGrid::from(&vec!["YOY", "OYO", "YOY",]));
    }

    #[test]
    pub fn iterator() {
        let grid = IntGrid::from(&vec!["12", "34"]);
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
        let grid =
            IntGrid::from(&vec!["12345", "12345", "12345", "12345", "12345"]);
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
