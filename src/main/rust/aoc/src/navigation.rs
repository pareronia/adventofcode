use crate::geometry::{Direction, Translate, XY};

#[derive(Clone, Copy)]
pub enum Heading {
    North,
    East,
    South,
    West,
}

impl From<Heading> for Direction {
    fn from(heading: Heading) -> Self {
        match heading {
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
            _ => panic!("Direction not supported: {}", direction),
        }
    }
}

pub struct NavigationWithHeading<'a> {
    position: XY,
    heading: Heading,
    visited: Vec<XY>,
    in_bounds: Box<dyn FnMut(&XY) -> bool + 'a>,
}

impl<'a> NavigationWithHeading<'a> {
    pub fn new(start: XY, heading: Heading) -> Self {
        let mut navigation = Self {
            position: start,
            heading,
            visited: vec![],
            in_bounds: Box::new(|_| true),
        };
        navigation.remember_visited_position(navigation.position);
        navigation
    }

    pub fn new_with_bounds(
        start: XY,
        heading: Heading,
        in_bounds: Box<dyn FnMut(&XY) -> bool + 'a>,
    ) -> Self {
        let mut navigation = Self {
            position: start,
            heading,
            visited: vec![],
            in_bounds,
        };
        navigation.remember_visited_position(navigation.position);
        navigation
    }

    fn remember_visited_position(&mut self, xy: XY) {
        self.visited.push(xy);
    }

    pub fn get_position(&self) -> &XY {
        &self.position
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
        let mut new_position = self.position;
        (0..amount).for_each(|_| {
            new_position =
                new_position.translate(&XY::try_from(direction).unwrap(), 1);
            if (self.in_bounds)(&new_position) {
                self.position = new_position;
            }
        });
        self.remember_visited_position(self.position);
    }
}
