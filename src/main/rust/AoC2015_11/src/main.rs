#![allow(non_snake_case)]
use aoc::Puzzle;

struct AoC2015_11 {}

impl AoC2015_11 {
    fn is_ok(&self, password: &[u8]) -> bool {
        let mut pairs = (None, None);
        let mut trio = false;
        for i in 0..password.len() {
            let ch = password[i] as char;
            if ch == 'i' || ch == 'o' || ch == 'l' {
                return false;
            }
            if i > 0 && password[i] == password[i - 1] {
                match pairs.0 {
                    None => pairs.0 = Some(i),
                    _ => pairs.1 = Some(i),
                }
            }
            trio = trio
                || i < password.len() - 3
                    && password[i] == password[i + 1] - 1
                    && password[i + 1] == password[i + 2] - 1;
        }
        trio && pairs.0.is_some()
            && pairs.1.is_some()
            && password[pairs.0.unwrap()] != password[pairs.1.unwrap()]
    }

    fn generate_from(&self, input: &String) -> String {
        fn increment(password: &mut [u8], i: usize) {
            password[i] = 'a' as u8 + (password[i] - 'a' as u8 + 1) % 26;
            if password[i] as char == 'a' {
                increment(password, i - 1);
            }
        }

        let len = input.len();
        let mut password = input.bytes().collect::<Vec<u8>>();
        loop {
            increment(&mut password, len - 1);
            if self.is_ok(&password) {
                break;
            }
        }
        String::from_utf8(password).unwrap()
    }
}

impl aoc::Puzzle for AoC2015_11 {
    type Input = String;
    type Output1 = String;
    type Output2 = String;

    aoc::puzzle_year_day!(2015, 11);

    fn parse_input(&self, lines: Vec<String>) -> String {
        lines[0].clone()
    }

    fn part_1(&self, input: &String) -> String {
        self.generate_from(input)
    }

    fn part_2(&self, input: &String) -> String {
        self.generate_from(&self.generate_from(input))
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, "abcdefgh", "abcdffaa",
            self, part_1, "ghijklmn", "ghjaabcc"
        };
    }
}

fn main() {
    AoC2015_11 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2015_11 {}.samples();
    }

    #[test]
    pub fn is_ok() {
        let puzzle = AoC2015_11 {};
        assert!(puzzle.is_ok("heqaabcc".as_bytes()));
    }
}
