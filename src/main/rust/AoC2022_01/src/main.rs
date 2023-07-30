#![allow(non_snake_case)]

struct AoC2022_01 {}

impl AoC2022_01 {
    fn new() -> Box<dyn aoc::Puzzle> {
        Box::new(AoC2022_01 {})
    }

    fn solve(&self, lines: &Vec<String>, count: usize) -> u32 {
        let blocks = aoc::to_blocks(lines);
        let mut sums: Vec<u32> = blocks
            .iter()
            .map(|block| {
                block
                    .iter()
                    .map(|line| line.parse::<u32>().unwrap())
                    .sum::<u32>()
            })
            .collect();
        sums.sort();
        sums.iter().rev().take(count).sum()
    }
}

impl aoc::Puzzle for AoC2022_01 {
    fn year(&self) -> u16 {
        2022
    }

    fn day(&self) -> u8 {
        1
    }

    fn part_1(&self, lines: &Vec<String>) -> String {
        self.solve(&lines, 1).to_string()
    }

    fn part_2(&self, lines: &Vec<String>) -> String {
        self.solve(&lines, 3).to_string()
    }
}

fn main() {
    let puzzle = AoC2022_01::new();
    let lines = puzzle.get_input_data();

    println!("{}", puzzle.part_1(&lines));
    println!("{}", puzzle.part_2(&lines));
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        let puzzle = AoC2022_01::new();
        let test = aoc::split_lines(
            "1000\n\
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
             10000",
        );
        assert_eq!(puzzle.part_1(&test), "24000");
        assert_eq!(puzzle.part_2(&test), "45000");
    }
}
