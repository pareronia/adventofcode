#![allow(non_snake_case)]

use aoc::Puzzle;

struct AoC2024_02;

impl AoC2024_02 {
    fn safe(&self, levels: &[u32]) -> bool {
        let diffs: Vec<i32> = levels
            .windows(2)
            .map(|window| window[1] as i32 - window[0] as i32)
            .collect();
        diffs.iter().all(|diff| 1 <= *diff && *diff <= 3)
            || diffs.iter().all(|diff| -1 >= *diff && *diff >= -3)
    }
}

impl aoc::Puzzle for AoC2024_02 {
    type Input = Vec<Vec<u32>>;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2024, 2);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        lines
            .iter()
            .map(|line| {
                line.split_whitespace()
                    .map(|s| s.parse::<u32>().unwrap())
                    .collect()
            })
            .collect()
    }

    fn part_1(&self, reports: &Self::Input) -> Self::Output1 {
        reports.iter().filter(|report| self.safe(report)).count()
    }

    fn part_2(&self, reports: &Self::Input) -> Self::Output2 {
        reports
            .iter()
            .filter(|report| {
                (0..report.len()).any(|i| {
                    let mut new_report: Vec<u32> = report.to_vec();
                    new_report.remove(i);
                    self.safe(&new_report)
                })
            })
            .count()
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 2,
            self, part_2, TEST, 4
        };
    }
}

fn main() {
    AoC2024_02 {}.run(std::env::args());
}

const TEST: &str = "\
7 6 4 2 1
1 2 7 8 9
9 7 6 2 1
1 3 2 4 5
8 6 4 4 1
1 3 6 7 9
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2024_02 {}.samples();
    }
}
