use std::env::Args;
use std::fs::read_to_string;
use std::time::Instant;

#[macro_export]
macro_rules! puzzle_year_day {
    ($year:expr, $day:expr) => {
        fn year(&self) -> u16 {
            $year
        }

        fn day(&self) -> u8 {
            $day
        }
    };
}

pub trait Puzzle {
    fn year(&self) -> u16;
    fn day(&self) -> u8;
    fn get_input_data(&self) -> Vec<String> {
        aocd::get_input_data(self.year(), self.day())
    }
    fn samples(&self) {}
    fn part_1(&self, lines: &Vec<String>) -> String;
    fn part_2(&self, lines: &Vec<String>) -> String;
    fn run(&self, args: Args) {
        let argv: Vec<String> = args.collect();
        if argv.len() == 3 {
            let part: usize = argv[1].parse().unwrap();
            let lines: Vec<String> = read_to_string(&argv[2])
                .unwrap()
                .lines()
                .map(String::from)
                .collect();
            let start = Instant::now();
            let (answer, duration) = match part {
                1 => (self.part_1(&lines), start.elapsed()),
                2 => (self.part_2(&lines), start.elapsed()),
                _ => panic!("part should be '1' or '2'"),
            };
            println!(
                "{{\"part{}\": {{\"answer\": \"{}\", \"duration\": {}}}}}",
                part,
                answer,
                duration.as_nanos()
            );
        } else {
            if cfg!(debug_assertions) {
                self.samples();
            }
            let lines = self.get_input_data();
            println!("Part 1: {}", self.part_1(&lines));
            println!("Part 2: {}", self.part_2(&lines));
        }
    }
}

pub fn to_blocks(lines: &Vec<String>) -> Vec<Vec<&String>> {
    let mut blocks = vec![];
    let mut idx: usize = 0;
    blocks.push(vec![]);
    lines.iter().for_each(|line| {
        if line.len() == 0 {
            blocks.push(vec![]);
            idx += 1;
        } else {
            blocks[idx].push(line);
        }
    });
    blocks
}

pub fn split_lines(s: &str) -> Vec<String> {
    s.lines().map(|line| String::from(line)).collect()
}
