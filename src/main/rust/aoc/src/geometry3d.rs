#![allow(unused)]

use std::ops::RangeInclusive;

#[derive(Clone, Copy, Debug, PartialEq, Eq, Hash)]
pub struct XYZ {
    x: i32,
    y: i32,
    z: i32,
}

impl XYZ {
    pub fn of(x: i32, y: i32, z: i32) -> Self {
        Self { x, y, z }
    }

    pub fn x(&self) -> i32 {
        self.x
    }

    pub fn y(&self) -> i32 {
        self.y
    }

    pub fn z(&self) -> i32 {
        self.z
    }

    pub fn capital_neighbours(&self) -> impl Iterator<Item = XYZ> + '_ {
        vec![
            Direction3D::Up,
            Direction3D::Right,
            Direction3D::Down,
            Direction3D::Left,
            Direction3D::Forward,
            Direction3D::Backward,
        ]
        .into_iter()
        .map(|h| XYZ::try_from(h).unwrap())
        .map(|xyz| self.translate(&xyz, 1))
    }

    pub fn squared_distance(&self, other: &XYZ) -> u64 {
        ((self.x - other.x) as i64 * (self.x - other.x) as i64) as u64
            + ((self.y - other.y) as i64 * (self.y - other.y) as i64) as u64
            + ((self.z - other.z) as i64 * (self.z - other.z) as i64) as u64
    }
}

impl FromIterator<u32> for XYZ {
    fn from_iter<I: IntoIterator<Item = u32>>(iter: I) -> Self {
        let mut v = vec![];
        for i in iter {
            v.push(i);
        }
        XYZ::of(v[0] as i32, v[1] as i32, v[2] as i32)
    }
}

#[derive(Clone, Copy, Debug)]
pub enum Direction3D {
    Up,
    Right,
    Down,
    Left,
    Forward,
    Backward,
}

impl TryFrom<Direction3D> for XYZ {
    type Error = &'static str;

    fn try_from(value: Direction3D) -> Result<Self, Self::Error> {
        match value {
            Direction3D::Up => Ok(XYZ::of(0, 1, 0)),
            Direction3D::Right => Ok(XYZ::of(1, 0, 0)),
            Direction3D::Down => Ok(XYZ::of(0, -1, 0)),
            Direction3D::Left => Ok(XYZ::of(-1, 0, 0)),
            Direction3D::Forward => Ok(XYZ::of(0, 0, 1)),
            Direction3D::Backward => Ok(XYZ::of(0, 0, -1)),
        }
    }
}

pub struct Cuboid {
    x: RangeInclusive<i32>,
    y: RangeInclusive<i32>,
    z: RangeInclusive<i32>,
}

impl Cuboid {
    pub fn of(
        x: RangeInclusive<i32>,
        y: RangeInclusive<i32>,
        z: RangeInclusive<i32>,
    ) -> Self {
        Self { x, y, z }
    }

    pub fn get_points(&self) -> impl Iterator<Item = XYZ> + '_ {
        self.x.clone().flat_map(move |x| {
            self.y.clone().flat_map(move |y| {
                self.z.clone().map(move |z| XYZ::of(x, y, z))
            })
        })
    }

    pub fn contains(&self, xyz: &XYZ) -> bool {
        self.x.contains(&xyz.x())
            && self.y.contains(&xyz.y())
            && self.z.contains(&xyz.z())
    }
}

pub trait Translate {
    fn translate(&self, xyz: &XYZ, amplitude: i32) -> Self;
}

impl Translate for XYZ {
    fn translate(&self, xyz: &XYZ, amplitude: i32) -> XYZ {
        XYZ {
            x: self.x + xyz.x * amplitude,
            y: self.y + xyz.y * amplitude,
            z: self.z + xyz.z * amplitude,
        }
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn cuboid_get_points() {
        let c = Cuboid {
            x: 0..=3,
            y: 0..=3,
            z: 0..=3,
        };
        assert_eq!(c.get_points().count(), 64);
        assert_eq!(c.get_points().next().unwrap(), XYZ::of(0, 0, 0));
        assert_eq!(c.get_points().nth(63).unwrap(), XYZ::of(3, 3, 3));

        let c = Cuboid {
            x: 0..=2,
            y: 0..=1,
            z: 0..=1,
        };
        assert_eq!(
            c.get_points().collect::<Vec<XYZ>>(),
            vec![
                XYZ::of(0, 0, 0),
                XYZ::of(0, 0, 1),
                XYZ::of(0, 1, 0),
                XYZ::of(0, 1, 1),
                XYZ::of(1, 0, 0),
                XYZ::of(1, 0, 1),
                XYZ::of(1, 1, 0),
                XYZ::of(1, 1, 1),
                XYZ::of(2, 0, 0),
                XYZ::of(2, 0, 1),
                XYZ::of(2, 1, 0),
                XYZ::of(2, 1, 1)
            ]
        );
    }

    #[test]
    pub fn cuboid_contains() {
        let c = Cuboid {
            x: 0..=2,
            y: 0..=2,
            z: 0..=2,
        };
        assert!(!c.contains(&XYZ::of(-1, 0, 0)));
        assert!(c.contains(&XYZ::of(0, 0, 0)));
        assert!(c.contains(&XYZ::of(0, 1, 1)));
        assert!(c.contains(&XYZ::of(2, 1, 1)));
        assert!(!c.contains(&XYZ::of(2, 3, 3)));
    }
}
