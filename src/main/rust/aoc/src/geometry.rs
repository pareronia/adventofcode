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

#[derive(Clone, Copy, Debug)]
pub enum Direction {
    Up,
    Right,
    Down,
    Left,
}

impl FromStr for Direction {
    type Err = &'static str;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        match s {
            "U" => Ok(Direction::Up),
            "R" => Ok(Direction::Right),
            "D" => Ok(Direction::Down),
            "L" => Ok(Direction::Left),
            _ => panic!("Invalid Direction '{}'", s),
        }
    }
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
