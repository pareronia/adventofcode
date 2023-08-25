use lazy_static::lazy_static;
use regex::Regex;
use std::{
    fmt::{Display, Error, Formatter},
    fs::read_to_string,
    str::FromStr,
    time::{Duration, Instant},
};

pub mod geometry;
pub mod geometry3d;
pub mod graph;
pub mod grid;
pub mod navigation;
pub mod ocr;

static PART_NUMS: [&str; 3] = ["", "1", "2"];

lazy_static! {
    static ref REGEX_N: Regex = Regex::new(r"[0-9]+").unwrap();
    static ref REGEX_Z: Regex = Regex::new(r"[-0-9]+").unwrap();
}

#[macro_export]
macro_rules! log {
    ($($arg:tt)*) => {{
        #[cfg(debug_assertions)]
        eprintln!("{:?}", ($($arg)*));
    }};
}

#[macro_export]
macro_rules! clog {
    ($f:tt) => {{
        #[cfg(debug_assertions)]
        eprintln!("{:?}", $f());
    }};
}

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
            "\n{}({}): expected [{:?}], got [{:?}]",
            stringify!($part), $input, $expected, ans
        );
    };

    ($self:ident, $part:ident, $input:expr, $expected:expr,
     $($selfs:ident, $parts:ident, $inputs:expr, $expecteds:expr),+) => {{
        aoc::puzzle_samples!{$self, $part, $input, $expected}
        aoc::puzzle_samples!{$($selfs, $parts, $inputs, $expecteds),+}
    }};
}

#[derive(Clone, Copy)]
#[repr(usize)]
pub enum Part {
    Part1 = 1,
    Part2 = 2,
}

impl Part {
    fn from_usize(num: usize) -> Option<Part> {
        match num {
            1 => Some(Part::Part1),
            2 => Some(Part::Part2),
            _ => None,
        }
    }
}

impl Display for Part {
    fn fmt(&self, f: &mut Formatter<'_>) -> Result<(), Error> {
        write!(f, "{}", PART_NUMS[*self as usize])
    }
}

#[derive(Debug)]
pub struct ParsePartErr {}

impl FromStr for Part {
    type Err = ParsePartErr;

    fn from_str(string: &str) -> Result<Self, Self::Err> {
        match PART_NUMS
            .iter()
            .position(|&s| s == string)
            .into_iter()
            .map(|pos| Part::from_usize(pos).unwrap())
            .next()
        {
            Some(val) => Ok(val),
            None => panic!("Illegal value for part: '{}'", string),
        }
    }
}

struct Solution {
    part: Part,
    answer: Option<String>,
    duration: Duration,
}

macro_rules! solution {
    ($part:path, $self:ident, $part_method:ident, $input:expr) => {{
        let start = Instant::now();
        Solution {
            part: $part,
            answer: Some($self.$part_method($input).to_string())
                .filter(|s| !s.is_empty()),
            duration: start.elapsed(),
        }
    }};
}

impl Solution {
    fn to_json(&self) -> String {
        format!(
            "{{\"part{}\": {{\"answer\": \"{}\", \"duration\": {}}}}}",
            self.part,
            self.answer.clone().unwrap(),
            self.duration.as_nanos()
        )
    }
}

impl Display for Solution {
    fn fmt(&self, f: &mut Formatter<'_>) -> Result<(), Error> {
        write!(
            f,
            "Part {}: {}, took {:?}",
            self.part,
            self.answer.clone().unwrap_or_default(),
            self.duration
        )
    }
}

#[derive(Clone)]
struct PartCheck {
    part: Part,
    actual: Option<String>,
    expected: Option<String>,
}

impl PartCheck {
    fn is_ok(&self) -> bool {
        self.actual.is_none()
            || self.expected.is_none()
            || self.actual.clone().unwrap() == self.expected.clone().unwrap()
    }
}

impl Display for PartCheck {
    fn fmt(&self, f: &mut Formatter<'_>) -> Result<(), Error> {
        match self.is_ok() {
            true => Ok(()),
            false => write!(
                f,
                "Part {}: Expected: '{}', got '{}'",
                self.part,
                self.expected.clone().unwrap(),
                self.actual.clone().unwrap()
            ),
        }
    }
}

#[derive(Clone)]
struct Check {
    part1: PartCheck,
    part2: PartCheck,
}

impl Check {
    fn is_ok(&self) -> bool {
        self.part1.is_ok() && self.part2.is_ok()
    }
}

impl FromIterator<PartCheck> for Check {
    fn from_iter<I: IntoIterator<Item = PartCheck>>(iter: I) -> Self {
        let mut c: Vec<PartCheck> = Vec::new();
        for i in iter {
            c.push(i);
        }
        Check {
            part1: c[0].clone(),
            part2: c[1].clone(),
        }
    }
}

impl Display for Check {
    fn fmt(&self, f: &mut Formatter<'_>) -> Result<(), Error> {
        match self.is_ok() {
            true => Ok(()),
            false => write!(
                f,
                "\n\n==================================================\n\
                CHECK FAILED !!\n{}\n{}\n\
                ==================================================\n\n",
                self.part1, self.part2
            ),
        }
    }
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
    fn get_answer(&self, part: Part) -> Option<String> {
        aocd::answer(self.year(), self.day(), part as usize)
    }
    fn parse_input(&self, lines: Vec<String>) -> Self::Input;
    fn samples(&self) {}
    fn part_1(&self, input: &Self::Input) -> Self::Output1;
    fn part_2(&self, input: &Self::Input) -> Self::Output2;
    fn run(&self, args: std::env::Args) {
        let argv: Vec<String> = args.collect();
        if argv.len() == 3 {
            let part: Part = argv[1].parse().unwrap();
            let lines: Vec<String> = read_to_string(&argv[2])
                .unwrap()
                .lines()
                .map(String::from)
                .collect();
            let input = self.parse_input(lines);
            let solution = match part {
                Part::Part1 => solution!(Part::Part1, self, part_1, &input),
                Part::Part2 => solution!(Part::Part2, self, part_2, &input),
            };
            println!("{}", solution.to_json());
        } else {
            if cfg!(debug_assertions) {
                self.samples();
            }
            let input = self.parse_input(self.get_input_data());
            println!();
            let solution1 = solution!(Part::Part1, self, part_1, &input);
            println!("{}", solution1);
            let solution2 = solution!(Part::Part2, self, part_2, &input);
            println!("{}", solution2);
            println!();
            let check: Check = vec![solution1, solution2]
                .iter()
                .map(|solution| PartCheck {
                    part: solution.part,
                    actual: solution.answer.clone(),
                    expected: self.get_answer(solution.part),
                })
                .collect();
            if !check.is_ok() {
                panic!("{}", check);
            }
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

pub fn ints_with_check(line: &str, expected_count: usize) -> Vec<i32> {
    ints(line, Some(expected_count))
}

pub fn uints_with_check(line: &str, expected_count: usize) -> Vec<u32> {
    uints(line, Some(expected_count))
}

pub fn uints_no_check(line: &str) -> Vec<u32> {
    uints(line, None)
}

pub fn uints(line: &str, expected_count: Option<usize>) -> Vec<u32> {
    let ans = REGEX_N
        .find_iter(line)
        .map(|mat| mat.as_str())
        .map(|s| s.parse::<u32>().unwrap())
        .collect::<Vec<u32>>();
    match expected_count {
        None => ans,
        Some(ec) => {
            assert_eq!(ans.len(), ec, "Expected {} unsigned ints", ec);
            ans
        }
    }
}

pub fn ints(line: &str, expected_count: Option<usize>) -> Vec<i32> {
    let ans = REGEX_Z
        .find_iter(line)
        .map(|mat| mat.as_str())
        .map(|s| s.parse::<i32>().unwrap())
        .collect::<Vec<i32>>();
    match expected_count {
        None => ans,
        Some(ec) => {
            assert_eq!(ans.len(), ec, "Expected {} signed ints", ec);
            ans
        }
    }
}
