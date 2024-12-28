use core::fmt;
use core::fmt::Display;
use std::collections::HashSet;
use std::str::FromStr;

#[derive(Clone, Copy, Debug, PartialEq, Eq, Hash)]
pub struct XY {
    x: i32,
    y: i32,
}

impl XY {
    pub fn of(x: i32, y: i32) -> Self {
        Self { x, y }
    }

    pub fn x(&self) -> i32 {
        self.x
    }

    pub fn y(&self) -> i32 {
        self.y
    }

    pub fn manhattan_distance(&self, other: &XY) -> u32 {
        ((self.x - other.x).abs() + (self.y - other.y).abs()) as u32
    }
}

#[derive(Clone, Copy, Debug, Eq, Hash, PartialEq)]
pub enum Direction {
    Up,
    RightAndUp,
    Right,
    RightAndDown,
    Down,
    LeftAndDown,
    Left,
    LeftAndUp,
}

impl Direction {
    pub fn capital() -> HashSet<Direction> {
        vec![
            Direction::Up,
            Direction::Right,
            Direction::Down,
            Direction::Left,
        ]
        .into_iter()
        .collect()
    }

    pub fn octants() -> HashSet<Direction> {
        vec![
            Direction::Up,
            Direction::RightAndUp,
            Direction::Right,
            Direction::RightAndDown,
            Direction::Down,
            Direction::LeftAndDown,
            Direction::Left,
            Direction::LeftAndUp,
        ]
        .into_iter()
        .collect()
    }

    pub fn is_horizontal(&self) -> bool {
        *self == Direction::Right || *self == Direction::Left
    }

    pub fn is_vertical(&self) -> bool {
        *self == Direction::Up || *self == Direction::Down
    }

    pub fn turn(&self, turn: Turn) -> Direction {
        match self {
            Direction::Up => match turn {
                Turn::Around => Direction::Down,
                Turn::Left => Direction::Left,
                Turn::Right => Direction::Right,
            },
            Direction::Right => match turn {
                Turn::Around => Direction::Left,
                Turn::Left => Direction::Up,
                Turn::Right => Direction::Down,
            },
            Direction::Down => match turn {
                Turn::Around => Direction::Up,
                Turn::Left => Direction::Right,
                Turn::Right => Direction::Left,
            },
            Direction::Left => match turn {
                Turn::Around => Direction::Right,
                Turn::Left => Direction::Down,
                Turn::Right => Direction::Up,
            },
            _ => panic!("Invalid Direction for turn: {}", self),
        }
    }
}

impl FromStr for Direction {
    type Err = &'static str;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        match s {
            "U" | "^" => Ok(Direction::Up),
            "R" | ">" => Ok(Direction::Right),
            "D" | "v" => Ok(Direction::Down),
            "L" | "<" => Ok(Direction::Left),
            _ => panic!("Invalid Direction '{}'", s),
        }
    }
}

impl Display for Direction {
    fn fmt(&self, f: &mut fmt::Formatter) -> fmt::Result {
        fmt::Debug::fmt(self, f)
    }
}

pub enum Turn {
    Around,
    Left,
    Right,
}

impl TryFrom<Direction> for XY {
    type Error = &'static str;

    fn try_from(value: Direction) -> Result<Self, Self::Error> {
        match value {
            Direction::Up => Ok(XY::of(0, 1)),
            Direction::RightAndUp => Ok(XY::of(1, 1)),
            Direction::Right => Ok(XY::of(1, 0)),
            Direction::RightAndDown => Ok(XY::of(1, -1)),
            Direction::Down => Ok(XY::of(0, -1)),
            Direction::LeftAndDown => Ok(XY::of(-1, -1)),
            Direction::Left => Ok(XY::of(-1, 0)),
            Direction::LeftAndUp => Ok(XY::of(-1, 1)),
        }
    }
}

pub trait Translate {
    fn translate(&self, xy: &XY, amplitude: i32) -> Self;
}

impl Translate for XY {
    fn translate(&self, xy: &XY, amplitude: i32) -> XY {
        XY {
            x: self.x + xy.x * amplitude,
            y: self.y + xy.y * amplitude,
        }
    }
}
