#![allow(non_snake_case)]

use itertools::Itertools;
use std::collections::HashMap;

#[derive(Debug)]
struct Distances {
    matrix: Vec<Vec<u16>>,
}

impl Distances {
    fn from_input(input: &[String]) -> Self {
        let mut map: HashMap<String, usize> = HashMap::new();
        let mut cnt = 0;
        let values: HashMap<(usize, usize), u16> = input
            .iter()
            .map(|line| line.split_whitespace().collect::<Vec<&str>>())
            .flat_map(|splits| {
                let idx1 =
                    *map.entry(splits[0].to_string()).or_insert_with(|| {
                        let x = cnt;
                        cnt += 1;
                        x
                    });
                let idx2 =
                    *map.entry(splits[2].to_string()).or_insert_with(|| {
                        let x = cnt;
                        cnt += 1;
                        x
                    });
                let val = splits[4].parse::<u16>().unwrap();
                [((idx1, idx2), val), ((idx2, idx1), val)]
            })
            .collect();
        let mut matrix = vec![vec![0_u16; map.len()]; map.len()];
        values
            .iter()
            .for_each(|(key, val)| matrix[key.0][key.1] = *val);
        Self { matrix }
    }

    fn get_distances_of_complete_routes(
        &self,
    ) -> impl Iterator<Item = u16> + '_ {
        let len = self.matrix.len();
        (0..len)
            .permutations(len)
            .map(|p| (1..p.len()).map(|i| self.matrix[p[i - 1]][p[i]]).sum())
    }
}

use aoc::Puzzle;

struct AoC2015_09;

impl AoC2015_09 {}

impl aoc::Puzzle for AoC2015_09 {
    type Input = Distances;
    type Output1 = u16;
    type Output2 = u16;

    aoc::puzzle_year_day!(2015, 9);

    fn parse_input(&self, lines: Vec<String>) -> Distances {
        Distances::from_input(&lines)
    }

    fn part_1(&self, distances: &Distances) -> u16 {
        distances.get_distances_of_complete_routes().min().unwrap()
    }

    fn part_2(&self, distances: &Distances) -> u16 {
        distances.get_distances_of_complete_routes().max().unwrap()
    }

    fn samples(&self) {
        #[rustfmt::skip]
        let test =
            "London to Dublin = 464\n\
             London to Belfast = 518\n\
             Dublin to Belfast = 141";

        aoc::puzzle_samples! {
            self, part_1, test, 605,
            self, part_2, test, 982
        };
    }
}

fn main() {
    AoC2015_09 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2015_09 {}.samples();
    }
}
