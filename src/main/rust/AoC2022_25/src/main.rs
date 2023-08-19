#![allow(non_snake_case)]

use {aoc::Puzzle, std::str::FromStr};

enum Decode {
    Zero = 0,
    One = 1,
    Two = 2,
    Dash = -1,
    DoubleDash = -2,
}

impl FromStr for Decode {
    type Err = &'static str;

    fn from_str(string: &str) -> Result<Self, Self::Err> {
        match string {
            "0" => Ok(Decode::Zero),
            "1" => Ok(Decode::One),
            "2" => Ok(Decode::Two),
            "-" => Ok(Decode::Dash),
            "=" => Ok(Decode::DoubleDash),
            _ => panic!(),
        }
    }
}

struct DigitAndCarry {
    digit: char,
    carry: i64,
}

impl DigitAndCarry {
    fn from_i64(i: i64) -> DigitAndCarry {
        let (digit, carry) = match i {
            0 => ('0', 0),
            1 => ('1', 0),
            2 => ('2', 0),
            3 => ('=', 1),
            4 => ('-', 1),
            5 => ('0', 1),
            _ => panic!(),
        };
        DigitAndCarry { digit, carry }
    }
}

struct AoC2022_25;

impl AoC2022_25 {}

impl aoc::Puzzle for AoC2022_25 {
    type Input = Vec<String>;
    type Output1 = String;
    type Output2 = String;

    aoc::puzzle_year_day!(2022, 25);

    fn parse_input(&self, lines: Vec<String>) -> Vec<String> {
        lines
    }

    fn part_1(&self, input: &Vec<String>) -> String {
        let mut total: i64 = input
            .iter()
            .map(|line| {
                line.chars()
                    .rev()
                    .map(|ch| Decode::from_str(&ch.to_string()).unwrap() as i64)
                    .enumerate()
                    .map(|(exp, coeff)| coeff * 5_i64.pow(exp as u32))
                    .sum::<i64>()
            })
            .sum();
        let mut ans = String::from("");
        while total > 0 {
            let encode = DigitAndCarry::from_i64(total % 5);
            ans.push_str(&encode.digit.to_string());
            total = total / 5 + encode.carry;
        }
        ans.chars().rev().collect::<String>()
    }

    fn part_2(&self, _input: &Vec<String>) -> String {
        String::from("🎄")
    }

    fn samples(&self) {
        #[rustfmt::skip]
        let test =
            "1=-0-2\n\
             12111\n\
             2=0=\n\
             21\n\
             2=01\n\
             111\n\
             20012\n\
             112\n\
             1=-1=\n\
             1-12\n\
             12\n\
             1=\n\
             122";
        let test1 = "1=11-2";
        let test2 = "1-0---0";
        let test3 = "1121-1110-1=0";

        aoc::puzzle_samples! {
            self, part_1, test, "2=-1=0",
            self, part_1, test1, "1=11-2",
            self, part_1, test2, "1-0---0",
            self, part_1, test3, "1121-1110-1=0",
            self, part_2, test, "🎄"
        };
    }
}

fn main() {
    AoC2022_25 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2022_25 {}.samples();
    }
}
