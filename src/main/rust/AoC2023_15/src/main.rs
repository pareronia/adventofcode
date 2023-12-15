#![allow(non_snake_case)]

use aoc::Puzzle;
use linked_hash_map::LinkedHashMap;

fn hash(s: &str) -> u32 {
    s.chars().fold(0, |acc, ch| ((acc + ch as u32) * 17) % 256)
}

#[derive(Debug)]
struct Boxes {
    boxes: Vec<LinkedHashMap<String, u32>>,
}

impl Boxes {
    fn new() -> Self {
        Self {
            boxes: vec![LinkedHashMap::new(); 256],
        }
    }

    fn add_lens(&mut self, label: &str, focal_length: u32) {
        self.boxes[hash(label) as usize]
            .entry(String::from(label))
            .and_modify(|val| *val = focal_length)
            .or_insert(focal_length);
    }

    fn remove_lens(&mut self, label: &str) {
        self.boxes[hash(label) as usize].remove(label);
    }

    fn get_total_focusing_power(&self) -> u32 {
        self.boxes
            .iter()
            .enumerate()
            .flat_map(|(b, _box)| {
                _box.values().enumerate().map(move |(i, focal_length)| {
                    (b as u32 + 1) * (i as u32 + 1) * focal_length
                })
            })
            .sum::<u32>()
    }
}

struct AoC2023_15;

impl AoC2023_15 {}

impl aoc::Puzzle for AoC2023_15 {
    type Input = Vec<String>;
    type Output1 = u32;
    type Output2 = u32;

    aoc::puzzle_year_day!(2023, 15);

    fn parse_input(&self, lines: Vec<String>) -> Vec<String> {
        lines[0].split(',').map(String::from).collect()
    }

    fn part_1(&self, steps: &Vec<String>) -> u32 {
        steps.iter().map(|step| hash(step)).sum()
    }

    fn part_2(&self, steps: &Vec<String>) -> u32 {
        let mut boxes = Boxes::new();
        for step in steps {
            if step.contains('=') {
                let (label, focal_length) = step.split_once('=').unwrap();
                boxes.add_lens(label, focal_length.parse::<u32>().unwrap());
            } else {
                let label = &step[..step.len() - 1];
                boxes.remove_lens(label);
            }
        }
        boxes.get_total_focusing_power()
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 1320,
            self, part_2, TEST, 145
        };
    }
}

fn main() {
    AoC2023_15 {}.run(std::env::args());
}

const TEST: &str = "\
rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2023_15 {}.samples();
    }
}
