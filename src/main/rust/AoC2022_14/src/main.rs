#![allow(non_snake_case)]

use {aoc::Puzzle, lazy_static::lazy_static, std::collections::HashSet};

lazy_static! {
    static ref SOURCE: (usize, usize) = (500, 0);
}

#[derive(Clone, Debug)]
struct Cave {
    occupied: Vec<Vec<bool>>,
    max_y: usize,
}

struct AoC2022_14;

impl AoC2022_14 {
    fn solve(&self, cave: &mut Cave, max_y: usize) -> usize {
        fn drop(cave: &Cave, max_y: usize) -> Option<(usize, usize)> {
            let (mut curr_x, mut curr_y) = *SOURCE;
            loop {
                let x = curr_x;
                let y = curr_y;
                let yy = y + 1;
                for xx in [x, x - 1, x + 1] {
                    match cave.occupied.get(yy) {
                        None => continue,
                        Some(row) => match row.get(xx) {
                            Some(val) if !(*val) => {
                                (curr_x, curr_y) = (xx, yy);
                                break;
                            }
                            _ => continue,
                        },
                    }
                }
                if curr_x == x && curr_y == y {
                    return Some((curr_x, curr_y));
                }
                if curr_y == max_y {
                    return None;
                }
            }
        }

        let mut cnt = 0;
        loop {
            match drop(cave, max_y) {
                None => break,
                Some(val) => {
                    cave.occupied[val.1][val.0] = true;
                    cnt += 1;
                    if val == *SOURCE {
                        break;
                    }
                }
            }
        }
        cnt
    }
}

impl aoc::Puzzle for AoC2022_14 {
    type Input = Cave;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2022, 14);

    fn parse_input(&self, lines: Vec<String>) -> Cave {
        let mut rocks: HashSet<(usize, usize)> = HashSet::new();
        let mut max_x = usize::MIN;
        let mut max_y = usize::MIN;
        lines.iter().for_each(|line| {
            let mut c = line
                .split(" -> ")
                .map(|split| {
                    split
                        .split(',')
                        .map(|s| s.parse::<usize>().unwrap())
                        .collect::<Vec<usize>>()
                })
                .collect::<Vec<Vec<usize>>>();
            let mut c1 = c.clone();
            for (v1, v2) in c.iter_mut().zip(c1[1..].iter_mut()) {
                let mut xs = [v1[0], v2[0]];
                let mut ys = [v1[1], v2[1]];
                xs.sort_unstable();
                ys.sort_unstable();
                for x in xs[0]..=xs[1] {
                    for y in ys[0]..=ys[1] {
                        rocks.insert((x, y));
                        max_x = max_x.max(x);
                        max_y = max_y.max(y);
                    }
                }
            }
        });
        let size = max_y + 2;
        let mut occupied: Vec<Vec<bool>> = Vec::with_capacity(size);
        (0..size).for_each(|_| occupied.push(vec![false; max_x + 150]));
        rocks.iter().for_each(|&(x, y)| occupied[y][x] = true);
        Cave { occupied, max_y }
    }

    fn part_1(&self, input: &Cave) -> usize {
        let max_y = input.max_y;
        let mut cave = input.clone();
        self.solve(&mut cave, max_y)
    }

    fn part_2(&self, input: &Cave) -> usize {
        let max_y = input.max_y + 2;
        let mut cave = input.clone();
        self.solve(&mut cave, max_y)
    }

    fn samples(&self) {
        #[rustfmt::skip]
        let test =
            "498,4 -> 498,6 -> 496,6\n\
             503,4 -> 502,4 -> 502,9 -> 494,9";
        aoc::puzzle_samples! {
            self, part_1, test, 24,
            self, part_2, test, 93
        };
    }
}

fn main() {
    AoC2022_14 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2022_14 {}.samples();
    }
}
