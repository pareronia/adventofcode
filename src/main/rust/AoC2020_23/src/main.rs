#![allow(non_snake_case)]

use aoc::Puzzle;

struct AoC2020_23;

impl AoC2020_23 {
    fn prepare_cups(&self, labels: &[u32]) -> Vec<u32> {
        let mut cups = [0].repeat(labels.len() + 1);
        for i in 0..labels.len() {
            cups[labels[i] as usize] = labels[(i + 1) % labels.len()]
        }
        cups
    }

    fn do_move(
        &self,
        cups: &mut [u32],
        current: usize,
        min: u32,
        max: u32,
    ) -> usize {
        let c = current;
        let p1 = cups[c];
        let p2 = cups[p1 as usize];
        let p3 = cups[p2 as usize];
        cups[c] = cups[p3 as usize];
        let pickup = [p1, p2, p3];
        let mut d = c as u32 - 1;
        if d < min {
            d = max;
        }
        while pickup.contains(&d) {
            d -= 1;
            if d < min {
                d = max;
            }
        }
        cups[p3 as usize] = cups[d as usize];
        cups[d as usize] = p1;
        cups[c] as usize
    }
}

impl aoc::Puzzle for AoC2020_23 {
    type Input = Vec<u32>;
    type Output1 = String;
    type Output2 = u64;

    aoc::puzzle_year_day!(2020, 23);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        lines[0]
            .chars()
            .map(|ch| ch.to_digit(10).unwrap())
            .collect()
    }

    fn part_1(&self, labels: &Self::Input) -> Self::Output1 {
        let mut cups = self.prepare_cups(labels);
        let mut current = labels[0] as usize;
        for _ in 0..100 {
            current = self.do_move(&mut cups, current, 1, 9);
        }
        let mut cup = 1;
        std::iter::from_fn(|| {
            cup = cups[cup as usize];
            Some(cup)
        })
        .take_while(|cup| *cup != 1)
        .map(|cup| cup.to_string())
        .collect::<String>()
    }

    fn part_2(&self, labels_in: &Self::Input) -> Self::Output2 {
        let mut labels = labels_in.clone();
        labels.extend(10..=1_000_000);
        let mut cups = self.prepare_cups(&labels);
        let mut current = labels[0] as usize;
        for _ in 0..10_000_000 {
            current = self.do_move(&mut cups, current, 1, 1_000_000);
        }
        cups[1] as u64 * cups[cups[1] as usize] as u64
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, "67384529",
            self, part_2, TEST, 149245887792_u64
        };
    }
}

fn main() {
    AoC2020_23 {}.run(std::env::args());
}

const TEST: &str = "\
389125467
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2020_23 {}.samples();
    }
}
