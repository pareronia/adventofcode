#![allow(non_snake_case)]

struct AoC2022_01 {}

impl AoC2022_01 {
    fn new() -> Box<dyn aoc::Puzzle> {
        Box::new(AoC2022_01 {})
    }

    fn parse(&self, lines: &Vec<String>) -> Vec<Vec<u32>> {
        let blocks = aoc::to_blocks(lines);
        blocks
            .iter()
            .map(|block| {
                block
                    .iter()
                    .map(|line| line.parse::<u32>().unwrap())
                    .collect()
            })
            .collect()
    }

    fn solve(&self, lines: &Vec<String>, count: usize) -> u32 {
        let groups = self.parse(&lines);
        let mut sums: Vec<u32> =
            groups.iter().map(|group| group.iter().sum()).collect();
        sums.sort();
        sums.iter().rev().take(count).sum()
    }
}

impl aoc::Puzzle for AoC2022_01 {
    aoc::puzzle_year_day!(2022, 1);

    fn part_1(&self, lines: &Vec<String>) -> String {
        self.solve(&lines, 1).to_string()
    }

    fn part_2(&self, lines: &Vec<String>) -> String {
        self.solve(&lines, 3).to_string()
    }

    fn samples(&self) {
        let test = "1000\n\
             2000\n\
             3000\n\
             \n\
             4000\n\
             \n\
             5000\n\
             6000\n\
             \n\
             7000\n\
             8000\n\
             9000\n\
             \n\
             10000";
        aoc::puzzle_samples! {
            self, part_1, test, "24000",
            self, part_2, test, "45000"
        };
    }
}

fn main() {
    AoC2022_01::new().run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2022_01::new().samples();
    }
}
