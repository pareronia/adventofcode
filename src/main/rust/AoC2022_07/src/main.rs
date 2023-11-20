#![allow(non_snake_case)]

use aoc::{log, Puzzle};
use itertools::Itertools;
use std::collections::{HashMap, VecDeque};

const ROOT: &str = "<root>";

struct AoC2022_07;

impl AoC2022_07 {}

impl aoc::Puzzle for AoC2022_07 {
    type Input = HashMap<String, u32>;
    type Output1 = u32;
    type Output2 = u32;

    aoc::puzzle_year_day!(2022, 7);

    fn parse_input(&self, lines: Vec<String>) -> HashMap<String, u32> {
        let mut sizes: HashMap<String, u32> = HashMap::new();
        let mut path: VecDeque<&str> = VecDeque::new();
        lines.iter().skip(1).for_each(|line| {
            if let Some(name) = line.strip_prefix("$ cd ") {
                if name == ".." {
                    path.pop_back();
                } else {
                    path.push_back(name);
                }
            } else if !line.starts_with('$') {
                let split = line.split_whitespace().next().unwrap();
                if !split.starts_with("dir") {
                    let size = split.parse::<u32>().unwrap();
                    for i in 0..=path.len() {
                        let mut pp = vec![ROOT];
                        path.iter().take(i).for_each(|p| pp.push(p));
                        sizes
                            .entry(pp.join("/"))
                            .and_modify(|e| *e += size)
                            .or_insert(size);
                    }
                }
            }
        });
        log!(&sizes);
        sizes
    }

    fn part_1(&self, sizes: &HashMap<String, u32>) -> u32 {
        sizes.values().filter(|&v| *v <= 100_000).sum()
    }

    fn part_2(&self, sizes: &HashMap<String, u32>) -> u32 {
        let total = sizes.get(ROOT).unwrap();
        let wanted = 30_000_000 - (70_000_000 - total);
        *sizes
            .values()
            .filter(|&v| *v >= wanted)
            .sorted()
            .next()
            .unwrap()
    }

    fn samples(&self) {
        #[rustfmt::skip]
        let test =
            "$ cd /\n\
             $ ls\n\
             dir a\n\
             14848514 b.txt\n\
             8504156 c.dat\n\
             dir d\n\
             $ cd a\n\
             $ ls\n\
             dir e\n\
             29116 f\n\
             2557 g\n\
             62596 h.lst\n\
             $ cd e\n\
             $ ls\n\
             584 i\n\
             $ cd ..\n\
             $ cd ..\n\
             $ cd d\n\
             $ ls\n\
             4060174 j\n\
             8033020 d.log\n\
             5626152 d.ext\n\
             7214296 k";
        aoc::puzzle_samples! {
            self, part_1, test, 95_437,
            self, part_2, test, 24_933_642
        };
    }
}

fn main() {
    AoC2022_07 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2022_07 {}.samples();
    }
}
