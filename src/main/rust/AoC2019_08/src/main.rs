#![allow(non_snake_case)]

use aoc::{grid::CharGrid, ocr, Puzzle};

const FILL: char = '1';
const EMPTY: char = '0';
const WIDTH: usize = 25;
const HEIGHT: usize = 6;

struct Counts {
    zeroes: usize,
    ones: usize,
    twos: usize,
}

impl Counts {
    fn new(input: &str) -> Counts {
        let mut zeroes = 0;
        let mut ones = 0;
        let mut twos = 0;
        input.chars().for_each(|ch| match ch {
            '0' => zeroes += 1,
            '1' => ones += 1,
            '2' => twos += 1,
            _ => panic!(),
        });
        Self { zeroes, ones, twos }
    }
}

struct AoC2019_08;

impl AoC2019_08 {
    fn get_layers(
        &self,
        input: &str,
        width: usize,
        height: usize,
    ) -> Vec<String> {
        let size = width * height;
        (0..input.len())
            .step_by(size)
            .map(|i| String::from(&input[i..i + size]))
            .collect()
    }

    fn get_image(&self, input: &str, width: usize, height: usize) -> String {
        let layers = self.get_layers(input, width, height);
        let mut image = String::from("");
        for i in 0..layers[0].len() {
            for lyr in layers.iter() {
                if lyr.chars().nth(i).unwrap() == '2' {
                    continue;
                }
                image.push(lyr.chars().nth(i).unwrap());
                break;
            }
        }
        image
    }

    #[cfg(debug_assertions)]
    fn print_image(&self, to_ocr: &[&str]) {
        to_ocr
            .iter()
            .map(|line| line.replace(FILL, "▒"))
            .map(|line| line.replace(EMPTY, " "))
            .for_each(|line| println!("{:?}", line));
    }
}

impl aoc::Puzzle for AoC2019_08 {
    type Input = String;
    type Output1 = usize;
    type Output2 = String;

    aoc::puzzle_year_day!(2019, 8);

    fn parse_input(&self, lines: Vec<String>) -> String {
        lines[0].to_string()
    }

    fn part_1(&self, input: &String) -> usize {
        let layers = self.get_layers(input, WIDTH, HEIGHT);
        let counts: Vec<Counts> =
            layers.iter().map(|lyr| Counts::new(lyr)).collect();
        let min_0 = counts.iter().map(|c| c.zeroes).min().unwrap();
        counts
            .iter()
            .filter(|c| c.zeroes == min_0)
            .map(|c| c.ones * c.twos)
            .nth(0)
            .unwrap()
    }

    fn part_2(&self, input: &String) -> String {
        let image = self.get_image(input, WIDTH, HEIGHT);
        let to_ocr: Vec<&str> = (0..WIDTH * HEIGHT)
            .step_by(WIDTH)
            .map(|i| &image[i..i + WIDTH])
            .collect();
        #[cfg(debug_assertions)]
        self.print_image(&to_ocr);
        ocr::convert_6(&CharGrid::from(&to_ocr), FILL, EMPTY)
    }

    fn samples(&self) {}
}

fn main() {
    AoC2019_08 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2019_08 {}.samples();
    }
}
