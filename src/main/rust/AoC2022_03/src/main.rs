#![allow(non_snake_case)]

use std::collections::HashSet;

struct AoC2022_03 {}

impl AoC2022_03 {
    fn new() -> Box<dyn aoc::Puzzle> {
        Box::new(AoC2022_03 {})
    }

    fn priority(&self, ch: char) -> u32 {
        match 'a' <= ch && ch <= 'z' {
            true => ch as u32 - 'a' as u32 + 1,
            false => ch as u32 - 'A' as u32 + 27,
        }
    }
}

impl aoc::Puzzle for AoC2022_03 {
    fn year(&self) -> u16 {
        2022
    }

    fn day(&self) -> u8 {
        3
    }

    fn part_1(&self, lines: &Vec<String>) -> String {
        (0..lines.len())
            .map(|i| {
                let line = &lines[i];
                let cutoff = line.len() / 2;
                let s1: HashSet<char> = line.chars().take(cutoff).collect();
                let s2: HashSet<char> = line.chars().skip(cutoff).collect();
                self.priority(*s1.intersection(&s2).next().unwrap())
            })
            .sum::<u32>()
            .to_string()
    }

    fn part_2(&self, lines: &Vec<String>) -> String {
        (0..lines.len())
            .step_by(3)
            .map(|i| {
                let s1: HashSet<char> = lines[i].chars().collect();
                let s2: HashSet<char> = lines[i + 1].chars().collect();
                let s3: HashSet<char> = lines[i + 2].chars().collect();
                let intersection = vec![&s2, &s3]
                    .iter()
                    .fold(s1, |i, s| i.intersection(&s).map(|x| *x).collect());
                self.priority(*intersection.iter().next().unwrap())
            })
            .sum::<u32>()
            .to_string()
    }
    
    fn samples(&self) {
        let test = aoc::split_lines(
            "vJrwpWtwJgWrhcsFMMfFFhFp\n\
             jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL\n\
             PmmdzqPrVvPwwTWBwg\n\
             wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn\n\
             ttgJtRGJQctTZtZT\n\
             CrZsJsPPZsGzwwsLwLmpwMDw",
        );
        assert_eq!(self.part_1(&test), "157");
        assert_eq!(self.part_2(&test), "70");
    }
}

fn main() {
    AoC2022_03::new().run();
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2022_03::new().samples();
    }
}
