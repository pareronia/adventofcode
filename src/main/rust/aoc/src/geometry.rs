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
}

#[derive(Clone, Copy, Debug, Eq, Hash, PartialEq)]
pub enum Direction {
    Up,
    Right,
    Down,
    Left,
}

impl Direction {
    pub fn is_horizontal(&self) -> bool {
        *self == Direction::Right || *self == Direction::Left
    }

    pub fn turn(&self, turn: Turn) -> Direction {
        match self {
            Direction::Up => match turn {
                Turn::Around => Direction::Down,
                Turn::Left => Direction::Left,
                Turn::Right => Direction::Right,
            },
            Direction::Right => match turn{
                Turn::Around => Direction::Left,
                Turn::Left => Direction::Up,
                Turn::Right => Direction::Down,
            },
            Direction::Down => match turn {
                Turn::Around => Direction::Up,
                Turn::Left => Direction::Right,
                Turn::Right => Direction::Left,
            } ,
            Direction::Left => match turn {
                Turn::Around => Direction::Right,
                Turn::Left => Direction::Down,
                Turn::Right => Direction::Up,
            },
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
            Direction::Right => Ok(XY::of(1, 0)),
            Direction::Down => Ok(XY::of(0, -1)),
            Direction::Left => Ok(XY::of(-1, 0)),
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
