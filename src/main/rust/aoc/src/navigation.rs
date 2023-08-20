use crate::geometry::{Direction, Translate, XY};

#[derive(Clone, Copy)]
pub enum Heading {
    North,
    East,
    South,
    West,
}

impl Into<Direction> for Heading {
    fn into(self) -> Direction {
        match self {
            Heading::North => Direction::Up,
            Heading::East => Direction::Right,
            Heading::South => Direction::Down,
            Heading::West => Direction::Left,
        }
    }
}

impl From<Direction> for Heading {
    fn from(direction: Direction) -> Self {
        match direction {
            Direction::Up => Heading::North,
            Direction::Right => Heading::East,
            Direction::Down => Heading::South,
            Direction::Left => Heading::West,
        }
    }
}

pub struct NavigationWithHeading {
    position: XY,
    heading: Heading,
    visited: Vec<XY>,
}

impl NavigationWithHeading {
    pub fn new(start: XY, heading: Heading) -> Self {
        let mut navigation = Self {
            position: start,
            heading,
            visited: vec![],
        };
        navigation.remember_visited_position(navigation.position);
        navigation
    }

    fn remember_visited_position(&mut self, xy: XY) {
        self.visited.push(xy);
    }

    pub fn get_visited_positions(
        &self,
        include_start_position: bool,
    ) -> impl Iterator<Item = &XY> {
        let skip = match include_start_position {
            true => 0,
            false => 1,
        };
        self.visited.iter().skip(skip)
    }

    pub fn navigate(&mut self, heading: Heading, amount: i32) {
        self.heading = heading;
        let direction: Direction = self.heading.into();
        self.position = self
            .position
            .translate(&XY::try_from(direction).unwrap(), amount);
        self.remember_visited_position(self.position.clone());
    }
}
