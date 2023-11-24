#![allow(non_snake_case)]
#![allow(unused)]
#![allow(clippy::upper_case_acronyms)]

use aoc::{
    graph::{AStar, Result},
    log, Puzzle,
};
use itertools::Itertools;
use std::collections::{HashMap, HashSet};

#[derive(Debug)]
struct Cave {
    start: usize,
    valves: Vec<String>,
    rates: Vec<usize>,
    tunnels: Vec<HashSet<usize>>,
}

impl Cave {
    fn get_distances(&self) -> Vec<Vec<usize>> {
        let relevant_valves = (0..self.valves.len())
            .filter(|&i| self.rates[i] > 0 || i == self.start)
            .collect_vec();
        let size = self.valves.len();
        let mut distances: Vec<Vec<usize>> = vec![vec![0; size]; size];
        for i in relevant_valves.iter().copied() {
            let result = AStar::execute(
                i,
                |v| false,
                |v| self.tunnels[v].iter().copied().collect_vec(),
                |v| 1,
            );
            for j in relevant_valves.iter().copied() {
                distances[i][j] = result.get_distance(j).unwrap();
            }
        }
        distances
    }
}

#[derive(Default)]
struct DFS {
    max_time: usize,
    best_per_used: HashMap<u64, usize>,
    used: u64,
    max_flow: usize,
    sample: bool,
}

impl DFS {
    fn new(max_time: usize, sample: bool) -> Self {
        Self {
            max_time,
            sample,
            ..Self::default()
        }
    }

    fn dfs(
        &mut self,
        cave: &Cave,
        distances: &Vec<Vec<usize>>,
        start: usize,
        time: usize,
    ) {
        for i in 0..cave.valves.len() {
            let idx: u64 = 1 << i;
            if cave.rates[i] == 0 || (self.used & idx) != 0 {
                continue;
            }
            let new_time = time + 1 + distances[start][i];
            if new_time >= self.max_time {
                continue;
            }
            let flow = cave.rates[i] * (self.max_time - new_time);
            if !self.sample
                && self.max_flow + flow
                    < *self.best_per_used.get(&(self.used + idx)).unwrap_or(&0)
            {
                continue;
            }
            self.max_flow += flow;
            self.used += idx;
            self.dfs(cave, distances, i, new_time);
            self.max_flow -= flow;
            self.used -= idx;
        }
        self.best_per_used
            .entry(self.used)
            .and_modify(|e| *e = *e.max(&mut self.max_flow))
            .or_insert(self.max_flow);
    }
}

struct AoC2022_16;

impl AoC2022_16 {
    fn solve_1(&self, cave: &Cave) -> usize {
        let distances = cave.get_distances();
        let mut dfs = DFS::new(30, false);
        dfs.dfs(cave, &distances, cave.start, 0);
        *dfs.best_per_used.values().max().unwrap()
    }

    fn solve_2(&self, cave: &Cave, sample: bool) -> usize {
        let distances = cave.get_distances();
        let mut dfs = DFS::new(26, sample);
        dfs.dfs(cave, &distances, cave.start, 0);
        dfs.best_per_used
            .iter()
            .flat_map(|(&used1, v1)| {
                dfs.best_per_used
                    .iter()
                    .filter(move |(&used2, _)| used1 & used2 == 0)
                    .map(move |(_, v2)| v1 + v2)
            })
            .max()
            .unwrap()
    }

    fn sample_part_2(&self, cave: &Cave) -> usize {
        self.solve_2(cave, true)
    }
}

impl aoc::Puzzle for AoC2022_16 {
    type Input = Cave;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2022, 16);

    fn parse_input(&self, lines: Vec<String>) -> Cave {
        let mut start = usize::MAX;
        let mut valves = vec![String::new(); lines.len()];
        let mut map: HashMap<String, usize> = HashMap::new();
        let mut rates = vec![usize::MAX; lines.len()];
        let mut edges: HashMap<String, HashSet<String>> = HashMap::new();
        let mut tunnels = vec![HashSet::<usize>::new(); lines.len()];
        lines
            .iter()
            .map(|line| line.replace(',', ""))
            .map(|line| line.replace(';', ""))
            .enumerate()
            .for_each(|(i, line)| {
                let splits = line.split_ascii_whitespace().collect_vec();
                let name = splits[1];
                if name == "AA" {
                    start = i;
                }
                valves[i] = String::from(name);
                map.insert(String::from(name), i);
                rates[i] = splits[4]
                    .split('=')
                    .nth(1)
                    .unwrap()
                    .parse::<usize>()
                    .unwrap();
                splits.as_slice()[9..].iter().for_each(|&split| {
                    edges
                        .entry(String::from(name))
                        .and_modify(|mut s| {
                            s.insert(String::from(split));
                        })
                        .or_insert(HashSet::from([String::from(split)]));
                });
            });
        edges.iter().for_each(|(from, to)| {
            let k = *map.get(from).unwrap();
            let v: HashSet<usize> =
                to.iter().map(|name| *map.get(name).unwrap()).collect();
            tunnels[k] = v;
        });
        Cave {
            start,
            valves,
            rates,
            tunnels,
        }
    }

    fn part_1(&self, cave: &Cave) -> usize {
        self.solve_1(cave)
    }

    fn part_2(&self, cave: &Cave) -> usize {
        self.solve_2(cave, false)
    }

    fn samples(&self) {
        #[rustfmt::skip]
        let test =
            "Valve AA has flow rate=0; tunnels lead to valves DD, II, BB\n\
             Valve BB has flow rate=13; tunnels lead to valves CC, AA\n\
             Valve CC has flow rate=2; tunnels lead to valves DD, BB\n\
             Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE\n\
             Valve EE has flow rate=3; tunnels lead to valves FF, DD\n\
             Valve FF has flow rate=0; tunnels lead to valves EE, GG\n\
             Valve GG has flow rate=0; tunnels lead to valves FF, HH\n\
             Valve HH has flow rate=22; tunnel leads to valve GG\n\
             Valve II has flow rate=0; tunnels lead to valves AA, JJ\n\
             Valve JJ has flow rate=21; tunnel leads to valve II";

        aoc::puzzle_samples! {
            self, part_1, test, 1651,
            self, sample_part_2, test, 1707
        };
    }
}

fn main() {
    AoC2022_16 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2022_16 {}.samples();
    }
}
