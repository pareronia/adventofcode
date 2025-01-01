#![allow(non_snake_case)]

use aoc::Puzzle;
use std::cmp::Reverse;
use std::collections::BinaryHeap;

const TRIANGLE: [u64; 10] = [0, 0, 1, 3, 6, 10, 15, 21, 28, 36];

enum Mode {
    Mode1,
    Mode2,
}

#[derive(Debug)]
struct File {
    id: u16,
    pos: u32,
    sz: u8,
}

impl Mode {
    fn create_files(&self, id: u16, pos: u32, sz: u8) -> Vec<File> {
        match self {
            Mode::Mode1 => (0..sz)
                .map(|i| File {
                    id,
                    pos: pos + i as u32,
                    sz: 1,
                })
                .collect(),
            Mode::Mode2 => vec![File { id, pos, sz }],
        }
    }

    fn checksum(&self, f: &File) -> u64 {
        match self {
            Mode::Mode1 => f.id as u64 * f.pos as u64,
            Mode::Mode2 => {
                f.id as u64
                    * (f.pos as u64 * f.sz as u64 + TRIANGLE[f.sz as usize])
            }
        }
    }
}

struct AoC2024_09;

impl AoC2024_09 {
    fn solve(&self, input: &Vec<u8>, mode: Mode) -> u64 {
        let mut files: Vec<File> = Vec::with_capacity(input.len() / 2);
        let mut free_by_sz = [const { BinaryHeap::<Reverse<u32>>::new() }; 10];
        let (mut is_free, mut id, mut pos) = (false, 0, 0);
        for n in input {
            match is_free {
                true => {
                    free_by_sz.get_mut(*n as usize).unwrap().push(Reverse(pos))
                }
                false => {
                    files.append(&mut mode.create_files(id, pos, *n));
                    id += 1;
                }
            };
            pos += *n as u32;
            is_free = !is_free;
        }
        let mut ans = 0_u64;
        for file in files.iter_mut().rev() {
            let earliest = free_by_sz[(file.sz as usize)..]
                .iter_mut()
                .enumerate()
                .filter(|e| !e.1.is_empty())
                .min_by(|e1, e2| {
                    e1.1.peek().unwrap().0.cmp(&e2.1.peek().unwrap().0)
                });
            if let Some(e) = earliest {
                let free_sz = e.0 as u8 + file.sz;
                let free = e.1;
                let free_pos = free.peek().unwrap().0;
                if free_pos < file.pos {
                    free.pop();
                    file.pos = free_pos;
                    if file.sz < free_sz {
                        free_by_sz[(free_sz - file.sz) as usize]
                            .push(Reverse(file.pos + file.sz as u32));
                    }
                }
            }
            ans += mode.checksum(file);
        }
        ans
    }
}

impl aoc::Puzzle for AoC2024_09 {
    type Input = Vec<u8>;
    type Output1 = u64;
    type Output2 = u64;

    aoc::puzzle_year_day!(2024, 9);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        lines[0]
            .chars()
            .map(|ch| ch.to_digit(10).unwrap() as u8)
            .collect()
    }

    fn part_1(&self, input: &Self::Input) -> Self::Output1 {
        self.solve(input, Mode::Mode1)
    }

    fn part_2(&self, input: &Self::Input) -> Self::Output2 {
        self.solve(input, Mode::Mode2)
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 1928,
            self, part_2, TEST, 2858
        };
    }
}

fn main() {
    AoC2024_09 {}.run(std::env::args());
}

const TEST: &str = "\
2333133121414131402
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2024_09 {}.samples();
    }
}
