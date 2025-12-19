#![allow(non_snake_case)]

use aoc::Puzzle;
use cached::proc_macro::cached;
use itertools::Itertools;
use std::collections::{HashMap, HashSet};

#[derive(Debug)]
struct Graph {
    edges: HashMap<String, HashSet<String>>,
}

impl Graph {
    fn from_input(input: &[String]) -> Self {
        let mut edges = HashMap::new();
        for line in input {
            let (src, dsts) = line.split_once(": ").unwrap();
            edges.insert(
                src.to_string(),
                HashSet::from_iter(
                    dsts.split_whitespace().map(|sp| sp.to_string()),
                ),
            );
        }
        Graph { edges }
    }

    fn count_all_paths_along(&self, nodes: &[&str]) -> usize {
        self.count_all_paths_along_x(nodes, 0)
    }

    fn count_all_paths_along_x(&self, nodes: &[&str], id: u8) -> usize {
        fn count_all_paths(
            id: u8,
            start: &str,
            end: &str,
            edges: &HashMap<String, HashSet<String>>,
        ) -> usize {
            #[cached(
                key = "String",
                convert = r#"{format!("{}-{}-{}", id, node, end)}"#
            )]
            fn dfs(
                id: u8,
                node: &str,
                end: &str,
                edges: &HashMap<String, HashSet<String>>,
            ) -> usize {
                if node == end {
                    return 1;
                }
                edges
                    .get(node)
                    .unwrap_or(&HashSet::new())
                    .iter()
                    .map(|d| dfs(id, d, end, edges))
                    .sum()
            }

            dfs(id, start, end, edges)
        }

        nodes
            .iter()
            .tuple_windows()
            .map(|(a, b)| count_all_paths(id, a, b, &self.edges))
            .product()
    }
}

struct AoC2025_11;

impl AoC2025_11 {
    fn sample_part_1(&self, graph: &<Self as Puzzle>::Input) -> usize {
        graph.count_all_paths_along_x(&["you", "out"], 1)
    }

    fn sample_part_2(&self, graph: &<Self as Puzzle>::Input) -> usize {
        graph.count_all_paths_along_x(&["svr", "dac", "fft", "out"], 2)
            + graph.count_all_paths_along_x(&["svr", "fft", "dac", "out"], 2)
    }
}

impl aoc::Puzzle for AoC2025_11 {
    type Input = Graph;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2025, 11);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        Graph::from_input(&lines)
    }

    fn part_1(&self, graph: &Self::Input) -> Self::Output1 {
        graph.count_all_paths_along(&["you", "out"])
    }

    fn part_2(&self, graph: &Self::Input) -> Self::Output2 {
        graph.count_all_paths_along(&["svr", "dac", "fft", "out"])
            + graph.count_all_paths_along(&["svr", "fft", "dac", "out"])
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, sample_part_1, TEST1, 5,
            self, sample_part_2, TEST2, 2
        };
    }
}

fn main() {
    AoC2025_11 {}.run(std::env::args());
}

const TEST1: &str = "\
aaa: you hhh
you: bbb ccc
bbb: ddd eee
ccc: ddd eee fff
ddd: ggg
eee: out
fff: out
ggg: out
hhh: ccc fff iii
iii: out
";
const TEST2: &str = "\
svr: aaa bbb
aaa: fft
fft: ccc
bbb: tty
tty: ccc
ccc: ddd eee
ddd: hub
hub: fff
eee: dac
dac: fff
fff: ggg hhh
ggg: out
hhh: out
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2025_11 {}.samples();
    }
}
