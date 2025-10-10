#![allow(non_snake_case)]

use aoc::Puzzle;
use cached::proc_macro::cached;
use lazy_static::lazy_static;

lazy_static! {
    static ref NUMERIC: Keypad = Keypad {
        layout: vec!["789", "456", "123", " 0A"]
    };
    static ref DIRECTIONAL: Keypad = Keypad {
        layout: vec![" ^A", "<v>"]
    };
}

#[derive(Eq, PartialEq)]
struct Position {
    x: i8,
    y: i8,
}

struct Keypad {
    layout: Vec<&'static str>,
}

struct AoC2024_21;

impl Keypad {
    fn paths(
        &self,
        from: &Position,
        to: &Position,
        s: &str,
    ) -> impl Iterator<Item = String> {
        let get_char = |pos: &Position| {
            self.layout[pos.y as usize]
                .chars()
                .nth(pos.x as usize)
                .unwrap()
        };

        let mut ans = vec![];
        if from == to {
            let mut ss = String::from(s);
            ss.push('A');
            ans.push(ss);
            return ans.into_iter();
        }
        let new_from = Position {
            x: from.x - 1,
            y: from.y,
        };
        if to.x < from.x && get_char(&new_from) != ' ' {
            let mut ss = String::from(s);
            ss.push('<');
            self.paths(&new_from, to, &ss).for_each(|p| ans.push(p));
        }
        let new_from = Position {
            x: from.x,
            y: from.y - 1,
        };
        if to.y < from.y && get_char(&new_from) != ' ' {
            let mut ss = String::from(s);
            ss.push('^');
            self.paths(&new_from, to, &ss).for_each(|p| ans.push(p));
        }
        let new_from = Position {
            x: from.x,
            y: from.y + 1,
        };
        if to.y > from.y && get_char(&new_from) != ' ' {
            let mut ss = String::from(s);
            ss.push('v');
            self.paths(&new_from, to, &ss).for_each(|p| ans.push(p));
        }
        let new_from = Position {
            x: from.x + 1,
            y: from.y,
        };
        if to.x > from.x && get_char(&new_from) != ' ' {
            let mut ss = String::from(s);
            ss.push('>');
            self.paths(&new_from, to, &ss).for_each(|p| ans.push(p));
        }
        ans.into_iter()
    }

    fn best_path(&self, (a, b): (char, char)) -> String {
        fn count_consecutive_same_chars(s: &str) -> usize {
            s.chars()
                .zip(s.chars().skip(1))
                .filter(|(a, b)| a == b)
                .count()
        }
        let get_position = |wanted: char| {
            for (y, row) in self.layout.iter().enumerate() {
                for (x, ch) in row.chars().enumerate() {
                    if ch == wanted {
                        return Position {
                            x: x as i8,
                            y: y as i8,
                        };
                    }
                }
            }
            unreachable!();
        };

        let from = get_position(a);
        let to = get_position(b);
        self.paths(&from, &to, "")
            .min_by_key(|p| usize::MAX - count_consecutive_same_chars(p))
            .unwrap()
    }
}

impl AoC2024_21 {
    fn solve(&self, input: &[String], levels: usize) -> u64 {
        #[cached]
        fn count(path: String, level: usize, max_level: usize) -> u64 {
            if level > max_level {
                return path.len() as u64;
            }
            let keypad: &Keypad =
                if level > 0 { &DIRECTIONAL } else { &NUMERIC };
            let mut s = String::from("A");
            s.push_str(&path);
            s.chars()
                .zip(path.chars())
                .map(|(a, b)| keypad.best_path((a, b)))
                .map(|best| count(best, level + 1, max_level))
                .sum()
        }

        input
            .iter()
            .map(|s| {
                s[..s.len() - 1].parse::<u64>().unwrap()
                    * count(s.clone(), 0, levels)
            })
            .sum()
    }
}

impl aoc::Puzzle for AoC2024_21 {
    type Input = Vec<String>;
    type Output1 = u64;
    type Output2 = u64;

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
