#![allow(non_snake_case)]

use aoc::Puzzle;
use num_integer::lcm;
use std::collections::HashMap;

struct Map {
    instructions: String,
    network: HashMap<String, (String, String)>,
}

impl Map {
    fn from_input(lines: &[String]) -> Self {
        let blocks = aoc::to_blocks(lines);
        let instructions = String::from(blocks[0][0]);
        let mut network = HashMap::new();
        for line in &blocks[1] {
            let (key, vals) = line.split_once(" = ").unwrap();
            network.insert(
                String::from(key),
                (String::from(&vals[1..4]), String::from(&vals[6..9])),
            );
        }
        Map {
            instructions,
            network,
        }
    }

    fn steps(&self, start_key: &str) -> u64 {
        let mut inss = self.instructions.chars().cycle();
        let mut node = self.network.get(start_key).unwrap();
        let mut ans = 0;
        let mut key = start_key;
        while !key.ends_with('Z') {
            key = match inss.next().unwrap() {
                'L' => &node.0,
                'R' => &node.1,
                _ => panic!(),
            };
            node = self.network.get(key).unwrap();
            ans += 1;
        }
        ans
    }
}

struct AoC2023_08;

impl AoC2023_08 {}

impl aoc::Puzzle for AoC2023_08 {
    type Input = Map;
    type Output1 = u64;
    type Output2 = u64;

    aoc::puzzle_year_day!(2023, 8);

    fn parse_input(&self, lines: Vec<String>) -> Map {
        Map::from_input(&lines)
    }

    fn part_1(&self, map: &Map) -> u64 {
        map.steps("AAA")
    }

    fn part_2(&self, map: &Map) -> u64 {
        map.network
            .keys()
            .filter(|k| k.ends_with('A'))
            .map(|k| map.steps(k))
            .reduce(lcm)
            .unwrap()
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST1, 2,
            self, part_1, TEST2, 6,
            self, part_2, TEST3, 6
        };
    }
}

fn main() {
    AoC2023_08 {}.run(std::env::args());
}

const TEST1: &str = "\
RL

AAA = (BBB, CCC)
BBB = (DDD, EEE)
CCC = (ZZZ, GGG)
DDD = (DDD, DDD)
EEE = (EEE, EEE)
GGG = (GGG, GGG)
ZZZ = (ZZZ, ZZZ)
";

const TEST2: &str = "\
LLR

AAA = (BBB, BBB)
BBB = (AAA, ZZZ)
ZZZ = (ZZZ, ZZZ)
";

const TEST3: &str = "\
LR

11A = (11B, XXX)
11B = (XXX, 11Z)
11Z = (11B, XXX)
22A = (22B, XXX)
22B = (22C, 22C)
22C = (22Z, 22Z)
22Z = (22B, 22B)
XXX = (XXX, XXX)
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2023_08 {}.samples();
    }
}
