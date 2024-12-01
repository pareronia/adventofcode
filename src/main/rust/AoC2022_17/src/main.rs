#![allow(non_snake_case)]

use aoc::geometry::{Direction, Translate, XY};
use aoc::Puzzle;
use lazy_static::lazy_static;
use std::{
    collections::{HashMap, HashSet},
    str::FromStr,
};

const WIDTH: usize = 7;
const OFFSET_X: i32 = 2;
const OFFSET_Y: i32 = 3;
const KEEP_ROWS: usize = 55;
const LOOP_TRESHOLD: usize = 3_000;

lazy_static! {
    static ref FLOOR: HashSet<XY> =
        (0..WIDTH).map(|x| XY::of(x as i32, -1)).collect();
    static ref SHAPES: Vec<Vec<XY>> =
        vec![
            // 🔲🔲🔲🔲
            vec![XY::of(0, 0), XY::of(1, 0), XY::of(2, 0), XY::of(3, 0)],
            //   🔲
            // 🔲🔲🔲
            //   🔲
            vec![
                XY::of(0, 1),
                XY::of(1, 0),
                XY::of(1, 1),
                XY::of(1, 2),
                XY::of(2, 1),
            ],
            //     🔲
            //     🔲
            // 🔲🔲🔲
            vec![
                XY::of(0, 0),
                XY::of(1, 0),
                XY::of(2, 0),
                XY::of(2, 1),
                XY::of(2, 2),
            ],
            // 🔲
            // 🔲
            // 🔲
            // 🔲
            vec![XY::of(0, 0), XY::of(0, 1), XY::of(0, 2), XY::of(0, 3)],
            // 🔲🔲
            // 🔲🔲
            vec![XY::of(0, 0), XY::of(0, 1), XY::of(1, 0), XY::of(1, 1)],
        ];
}

#[derive(Clone, Copy)]
struct Cycle {
    cycle: usize,
    top: i32,
}

#[derive(Clone, Copy, Debug, Eq, Hash, PartialEq)]
struct State {
    shape: usize,
    tops: [i32; WIDTH],
    jet: Direction,
}

struct Shape {
    idx: usize,
    shape: Vec<XY>,
}

#[derive(Debug)]
struct Rock {
    idx: usize,
    shape: HashSet<XY>,
}

struct ShapesIterator {
    idx: usize,
}

struct JetIterator {
    idx: usize,
    jets: Vec<Direction>,
}

#[derive(Debug)]
struct Stack {
    positions: HashSet<XY>,
    tops: HashMap<usize, i32>,
    top: i32,
}

struct AoC2022_17;

impl Rock {
    fn move_(&self, vector: &XY) -> Self {
        let new_shape = self
            .shape
            .iter()
            .map(|s| s.translate(vector, 1))
            .collect::<HashSet<_>>();
        Rock {
            idx: self.idx,
            shape: new_shape,
        }
    }

    fn inside_x(&self, start_inclusive: i32, end_exclusive: i32) -> bool {
        self.shape
            .iter()
            .all(|p| start_inclusive <= p.x() && p.x() < end_exclusive)
    }
}

impl ShapesIterator {
    fn new() -> Self {
        Self { idx: 0 }
    }
}

impl JetIterator {
    fn new(jets: &[Direction]) -> Self {
        Self {
            idx: 0,
            jets: jets.to_vec(),
        }
    }
}

impl Iterator for ShapesIterator {
    type Item = Shape;

    fn next(&mut self) -> Option<Self::Item> {
        let i = self.idx % SHAPES.len();
        self.idx += 1;
        Some(Shape {
            idx: i,
            shape: SHAPES[i].clone(),
        })
    }
}

impl Iterator for JetIterator {
    type Item = Direction;

    fn next(&mut self) -> Option<Self::Item> {
        let i = self.idx % self.jets.len();
        self.idx += 1;
        Some(self.jets[i])
    }
}

impl Stack {
    fn new(positions: &HashSet<XY>) -> Self {
        Self {
            positions: positions.clone(),
            tops: Stack::get_tops(positions),
            top: 0,
        }
    }

    fn get_tops(positions: &HashSet<XY>) -> HashMap<usize, i32> {
        let mut tops: HashMap<usize, i32> = HashMap::new();
        positions
            .iter()
            .map(|p| (p.x() as usize, p.y()))
            .for_each(|(x, y)| {
                match tops.get(&x) {
                    Some(val) => tops.insert(x, *val.max(&y)),
                    None => tops.insert(x, y),
                };
            });
        tops
    }

    fn get_tops_normalized(&self) -> [i32; WIDTH] {
        let mut ans = [0_i32; WIDTH];
        (0..WIDTH)
            .for_each(|i| ans[i] = self.top - *self.tops.get(&i).unwrap());
        ans
    }

    fn overlapped_by(&self, rock: &Rock) -> bool {
        rock.shape.iter().any(|p| self.positions.contains(p))
    }

    fn add(&mut self, rock: &Rock) {
        rock.shape.iter().for_each(|p| {
            let val = *self.tops.get(&(p.x() as usize)).unwrap();
            self.tops.insert(p.x() as usize, val.max(p.y()));
            self.positions.insert(*p);
            self.top = self.top.max(p.y() + 1);
        });
        self.positions = self.get_top_rows(KEEP_ROWS);
    }

    fn get_top_rows(&self, n: usize) -> HashSet<XY> {
        self.positions
            .clone()
            .into_iter()
            .filter(|p| p.y() > self.top - n as i32)
            .collect()
    }
}

impl AoC2022_17 {
    fn solve(&self, jets: &[Direction], requested_drops: usize) -> usize {
        fn drop(
            drop_idx: usize,
            states: &mut HashMap<State, Vec<Cycle>>,
            stack: &mut Stack,
            shapes: &mut ShapesIterator,
            jets: &mut JetIterator,
        ) -> State {
            let mut cnt = 0;
            let shape = shapes.next().unwrap();
            let start = Rock {
                idx: shape.idx,
                shape: shape.shape.into_iter().collect(),
            }
            .move_(&XY::of(OFFSET_X, stack.top + OFFSET_Y));
            let mut rock = start;
            loop {
                assert!(cnt < 10_000, "infinite loop");
                let jet = jets.next().unwrap();
                let state = State {
                    shape: rock.idx,
                    tops: stack.get_tops_normalized(),
                    jet,
                };
                if cnt == 1 {
                    let cycle = Cycle {
                        cycle: drop_idx,
                        top: stack.top,
                    };
                    states
                        .entry(state)
                        .and_modify(|cycles| cycles.push(cycle))
                        .or_insert(vec![cycle]);
                }
                cnt += 1;
                let mut moved = rock.move_(&jet.try_into().unwrap());
                if moved.inside_x(0, WIDTH as i32)
                    && !stack.overlapped_by(&moved)
                {
                    rock = moved;
                }
                moved = rock.move_(&Direction::Down.try_into().unwrap());
                if stack.overlapped_by(&moved) {
                    stack.add(&rock);
                    return state;
                }
                rock = moved;
            }
        }

        let mut stack = Stack::new(&FLOOR);
        let mut states: HashMap<State, Vec<Cycle>> = HashMap::new();
        let mut shapes_iterator = ShapesIterator::new();
        let mut jet_iterator = JetIterator::new(jets);
        let mut drops = 0;
        loop {
            let state = drop(
                drops,
                &mut states,
                &mut stack,
                &mut shapes_iterator,
                &mut jet_iterator,
            );
            drops += 1;
            if drops == requested_drops {
                return stack.top as usize;
            }
            if drops >= LOOP_TRESHOLD
                && states.get(&state).unwrap_or(&vec![]).len() > 1
            {
                let cycles = states.get(&state).unwrap();
                let loop_size = cycles[1].cycle - cycles[0].cycle;
                let diff = cycles[1].top - cycles[0].top;
                let loops = (requested_drops - drops) / loop_size;
                let left = requested_drops - (drops + loops * loop_size);
                (0..left).for_each(|_| {
                    drop(
                        drops,
                        &mut states,
                        &mut stack,
                        &mut shapes_iterator,
                        &mut jet_iterator,
                    );
                    drops += 1;
                });
                return stack.top as usize + loops * diff as usize;
            }
        }
    }
}

impl aoc::Puzzle for AoC2022_17 {
    type Input = Vec<Direction>;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2022, 17);

    fn parse_input(&self, lines: Vec<String>) -> Vec<Direction> {
        lines[0]
            .chars()
            .map(|ch| Direction::from_str(&ch.to_string()).unwrap())
            .collect()
    }

    fn part_1(&self, jets: &Vec<Direction>) -> usize {
        self.solve(jets, 2022)
    }

    fn part_2(&self, jets: &Vec<Direction>) -> usize {
        self.solve(jets, 1_000_000_000_000)
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 3_068,
            self, part_2, TEST, 1_514_285_714_288_usize
        };
    }
}

fn main() {
    AoC2022_17 {}.run(std::env::args());
}

const TEST: &str = "\
>>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn jets() {
        let mut jets = JetIterator::new(
            &AoC2022_17 {}.parse_input(vec![String::from("<>><")]),
        );
        assert!(jets.next().unwrap() == Direction::Left);
        assert!(jets.next().unwrap() == Direction::Right);
        assert!(jets.next().unwrap() == Direction::Right);
        assert!(jets.next().unwrap() == Direction::Left);
        assert!(jets.next().unwrap() == Direction::Left);
        assert!(jets.next().unwrap() == Direction::Right);
        assert!(jets.next().unwrap() == Direction::Right);
        assert!(jets.next().unwrap() == Direction::Left);
    }

    #[test]
    pub fn samples() {
        AoC2022_17 {}.samples();
    }
}
