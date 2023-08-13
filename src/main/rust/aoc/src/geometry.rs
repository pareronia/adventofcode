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
pub enum Heading {
    North,
    East,
    South,
    West,
}

#[derive(Debug)]
pub struct HeadingFromStrErr;

impl FromStr for Heading {
    type Err = HeadingFromStrErr;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        match s {
            "U" => Ok(Heading::North),
            "R" => Ok(Heading::East),
            "D" => Ok(Heading::South),
            "L" => Ok(Heading::West),
            _ => panic!("Invalid Heading '{}'", s),
        }
    }
}

impl TryFrom<Heading> for XY {
    type Error = &'static str;

    fn try_from(value: Heading) -> Result<Self, Self::Error> {
        match value {
            Heading::North => Ok(XY::of(0, 1)),
            Heading::East => Ok(XY::of(1, 0)),
            Heading::South => Ok(XY::of(0, -1)),
            Heading::West => Ok(XY::of(-1, 0)),
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
