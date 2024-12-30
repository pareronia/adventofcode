#![allow(non_snake_case)]

use aoc::Puzzle;
use lazy_static::lazy_static;
use cached::proc_macro::cached;

lazy_static! {
    static ref NUMERIC: Keypad = Keypad {
        layout: vec!["789", "456", "123", " 0A"]
    };
    static ref DIRECTIONAL: Keypad = Keypad {
        layout: vec![" ^A", "<v>"]
    };
}

struct Keypad {
    layout: Vec<&'static str>,
}

impl Keypad {
    fn get_position(&self, wanted: char) -> (i8, i8) {
        for (y, row) in self.layout.iter().enumerate() {
            for (x, ch) in row.chars().enumerate() {
                if ch == wanted {
                    return (x as i8, y as i8);
                }
            }
        }
        unreachable!();
    }

    fn get_char(&self, pos: (i8, i8)) -> char {
        let (x, y) = pos;
        self.layout[y as usize].chars().nth(x as usize).unwrap()
    }
}

struct AoC2024_21;

impl AoC2024_21 {
    fn solve(&self, input: &[String], levels: u8) -> usize {
        #[cached]
        fn count(path: String, level: u8, max_level: u8) -> usize {
            fn paths(
                keypad: &Keypad,
                from: (i8, i8),
                to: (i8, i8),
                s: String,
            ) -> Vec<String> {
                if from == to {
                    let mut ss = s.clone();
                    ss.push('A');
                    return vec![ss];
                }
                let mut ans: Vec<String> = vec![];
                let mut new_from = (from.0 - 1, from.1);
                if to.0 < from.0 && keypad.get_char(new_from) != ' ' {
                    let mut ss = s.clone();
                    ss.push('<');
                    ans.extend_from_slice(&paths(keypad, new_from, to,  ss));
                }
                new_from = (from.0, from.1 - 1);
                if to.1 < from.1 && keypad.get_char(new_from) != ' ' {
                    let mut ss = s.clone();
                    ss.push('^');
                    ans.extend_from_slice(&paths(keypad, new_from, to,  ss));
                }
                new_from = (from.0, from.1 + 1);
                if to.1 > from.1 && keypad.get_char(new_from) != ' ' {
                    let mut ss = s.clone();
                    ss.push('v');
                    ans.extend_from_slice(&paths(keypad, new_from, to,  ss));
                }
                new_from = (from.0 + 1, from.1);
                if to.0 > from.0 && keypad.get_char(new_from) != ' ' {
                    let mut ss = s.clone();
                    ss.push('>');
                    ans.extend_from_slice(&paths(keypad, new_from, to,  ss));
                }
                ans
            }

            if level > max_level {
                return path.len();
            }
            let keypad: &Keypad =
                if level > 0 { &DIRECTIONAL } else { &NUMERIC };
            let count_consecutive_same_chars = |s: &&String| {
                let ans = s.chars()
                    .zip(s[1..].chars())
                    .filter(|(a, b)| *a == *b)
                    .count();
                ans
            };
            let mut x = String::from("A");
            x.push_str(&path);
            x.chars()
                .zip(path.chars())
                .map(|(first, second)| {
                    let from = keypad.get_position(first);
                    let to = keypad.get_position(second);
                    let p = paths(keypad, from, to, String::from(""));
                    println!("{:?}", (&first, &second));
                    println!("{:?}", &p);
                    p
                        .iter()
                        .max_by_key(count_consecutive_same_chars)
                        .unwrap()
                        .to_owned()
                })
                .map(|best| count(best, level + 1, max_level))
                .sum::<usize>()
        }

        input
            .iter()
            .map(|combo| {
                combo[0..combo.len() - 1].parse::<usize>().unwrap()
                    * count(combo.clone(), 0, levels)
            })
            .sum()
    }
}

impl aoc::Puzzle for AoC2024_21 {
    type Input = Vec<String>;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2024, 21);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        lines
    }

    fn part_1(&self, input: &Self::Input) -> Self::Output1 {
        self.solve(input, 2)
    }

    fn part_2(&self, input: &Self::Input) -> Self::Output2 {
        self.solve(input, 25)
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 126384
        };
    }
}

fn main() {
    AoC2024_21 {}.run(std::env::args());
}

const TEST: &str = "\
029A
980A
179A
456A
379A
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2024_21 {}.samples();
    }
}
