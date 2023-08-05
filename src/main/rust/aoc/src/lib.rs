use regex::Regex;
use std::fmt::Display;
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

#[macro_export]
macro_rules! puzzle_samples {
    ($self:ident, $part:ident, $input:expr, $expected:expr) => {
        let ans = $self.$part(&$self.parse_input(aoc::split_lines($input)));
        assert_eq!(
            ans,
            $expected,
            "\n{}({}): expected [{}], got [{}]",
            stringify!($part), $input, $expected, ans
        );
    };

    ($self:ident, $part:ident, $input:expr, $expected:expr, $($selfs:ident, $parts:ident, $inputs:expr, $expecteds:expr),+) => {{
        aoc::puzzle_samples!{$self, $part, $input, $expected}
        aoc::puzzle_samples!{$($selfs, $parts, $inputs, $expecteds),+}
    }};
}

pub trait Puzzle {
    type Input;
    type Output1: Display;
    type Output2: Display;

    fn year(&self) -> u16;
    fn day(&self) -> u8;
    fn get_input_data(&self) -> Vec<String> {
        aocd::get_input_data(self.year(), self.day())
    }
    fn parse_input(&self, lines: Vec<String>) -> Self::Input;
    fn samples(&self) {}
    fn part_1(&self, input: &Self::Input) -> Self::Output1;
    fn part_2(&self, input: &Self::Input) -> Self::Output2;
    fn run(&self, args: std::env::Args) {
        let argv: Vec<String> = args.collect();
        if argv.len() == 3 {
            let part: usize = argv[1].parse().unwrap();
            let lines: Vec<String> = read_to_string(&argv[2])
                .unwrap()
                .lines()
                .map(String::from)
                .collect();
            let input = self.parse_input(lines);
            let start = Instant::now();
            let answer1;
            let answer2;
            let (answer, duration) = match part {
                1 => {
                    answer1 = self.part_1(&input);
                    (&answer1 as &dyn Display, start.elapsed())
                }
                2 => {
                    answer2 = self.part_2(&input);
                    (&answer2 as &dyn Display, start.elapsed())
                }
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
            let input = self.parse_input(self.get_input_data());
            let start1 = Instant::now();
            let answer1 = self.part_1(&input);
            println!("Part 1: {}, took {:?}", answer1, start1.elapsed());
            let start2 = Instant::now();
            let answer2 = self.part_2(&input);
            println!("Part 2: {}, took {:?}", answer2, start2.elapsed());
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

pub fn uints(line: &str, expected_count: usize) -> Vec<u32> {
    let ans = Regex::new(r"[0-9]+")
        .unwrap()
        .find_iter(line)
        .map(|mat| mat.as_str())
        .map(|s| s.parse::<u32>().unwrap())
        .collect::<Vec<u32>>();
    assert_eq!(
        ans.len(),
        expected_count,
        "Expected {} unsigned ints",
        expected_count
    );
    ans
}
