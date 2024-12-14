#![allow(non_snake_case)]

use aoc::geometry::{Translate, XY};
use aoc::Puzzle;

const W: usize = 101;
const H: usize = 103;
type InputType = Vec<(XY, XY)>;

struct AoC2024_14;

impl AoC2024_14 {
    fn do_move(
        &self,
        robots: &InputType,
        w: usize,
        h: usize,
        rounds: usize,
    ) -> usize {
        let moved_robots: Vec<XY> = robots
            .iter()
            .map(|(pos, vec)| pos.translate(vec, rounds as i32))
            .collect();
        let mut q: [usize; 4] = [0, 0, 0, 0];
        let (mid_x, mid_y) = (w / 2, h / 2);
        for pos in moved_robots {
            let (px, py) = (
                pos.x().rem_euclid(w as i32) as usize,
                pos.y().rem_euclid(h as i32) as usize,
            );
            if px < mid_x {
                if py < mid_y {
                    q[0] += 1;
                } else if mid_y < py && py < h {
                    q[1] += 1;
                }
            } else if mid_x < px && px < w {
                if py < mid_y {
                    q[2] += 1;
                } else if mid_y < py && py < h {
                    q[3] += 1;
                }
            }
        }
        q.iter().product()
    }

    fn solve_1(&self, input: &InputType, w: usize, h: usize) -> usize {
        self.do_move(input, w, h, 100)
    }

    fn sample_part_1(&self, input: &InputType) -> usize {
        self.solve_1(input, 11, 7)
    }
}

impl aoc::Puzzle for AoC2024_14 {
    type Input = InputType;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2024, 14);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        lines
            .iter()
            .map(|line| {
                let (p, v) = line.split_once(" ").unwrap();
                let (px, py) =
                    p.split_once("=").unwrap().1.split_once(",").unwrap();
                let (vx, vy) =
                    v.split_once("=").unwrap().1.split_once(",").unwrap();
                (
                    XY::of(
                        px.parse::<i32>().unwrap(),
                        py.parse::<i32>().unwrap(),
                    ),
                    XY::of(
                        vx.parse::<i32>().unwrap(),
                        vy.parse::<i32>().unwrap(),
                    ),
                )
            })
            .collect()
    }

    fn part_1(&self, robots: &Self::Input) -> Self::Output1 {
        self.solve_1(robots, W, H)
    }

    fn part_2(&self, robots: &Self::Input) -> Self::Output2 {
        let mut ans = 0;
        let mut best = usize::MAX;
        for round in 1..=W * H {
            let safety_factor = self.do_move(robots, W, H, round);
            if safety_factor < best {
                best = safety_factor;
                ans = round;
            }
        }
        ans
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, sample_part_1, TEST, 12
        };
    }
}

fn main() {
    AoC2024_14 {}.run(std::env::args());
}

const TEST: &str = "\
p=0,4 v=3,-3
p=6,3 v=-1,-3
p=10,3 v=-1,2
p=2,0 v=2,-1
p=0,0 v=1,3
p=3,0 v=-2,-2
p=7,6 v=-1,-3
p=3,0 v=-1,-2
p=9,3 v=2,3
p=7,3 v=-1,2
p=2,4 v=2,-3
p=9,5 v=-3,-3
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2024_14 {}.samples();
    }
}
