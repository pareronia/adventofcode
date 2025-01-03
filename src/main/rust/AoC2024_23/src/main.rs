#![allow(non_snake_case)]

use aoc::Puzzle;
use std::collections::{HashMap, HashSet};

struct AoC2024_23;

impl AoC2024_23 {}

impl aoc::Puzzle for AoC2024_23 {
    type Input = HashMap<String, HashSet<String>>;
    type Output1 = usize;
    type Output2 = String;

    aoc::puzzle_year_day!(2024, 23);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        let mut edges: HashMap<String, HashSet<String>> = HashMap::new();
        lines.iter().for_each(|line| {
            let (node_1, node_2) = line.split_once("-").unwrap();
            edges
                .entry(String::from(node_1))
                .or_default()
                .insert(String::from(node_2));
            edges
                .entry(String::from(node_2))
                .or_default()
                .insert(String::from(node_1));
        });
        edges
    }

    fn part_1(&self, edges: &Self::Input) -> Self::Output1 {
        let mut triangles: HashSet<(&str, &str, &str)> = HashSet::new();
        for t in edges.keys().filter(|node| node.starts_with("t")) {
            for neighbour_1 in edges.get(t).unwrap() {
                for neighbour_2 in edges.get(neighbour_1).unwrap() {
                    if edges.get(neighbour_2).unwrap().contains(t) {
                        let mut triangle = [t, neighbour_1, neighbour_2];
                        triangle.sort_unstable();
                        triangles.insert((
                            triangle[0],
                            triangle[1],
                            triangle[2],
                        ));
                    }
                }
            }
        }
        triangles.len()
    }

    fn part_2(&self, edges: &Self::Input) -> Self::Output2 {
        let mut clique = vec![];
        let mut largest = vec![];
        let mut seen: HashSet<&String> = HashSet::new();
        for (n1, neighbours) in edges {
            if seen.contains(n1) {
                continue;
            }
            clique.clear();
            clique.push(n1);
            for n2 in neighbours {
                let nn = edges.get(n2).unwrap();
                if clique.iter().all(|&c| nn.contains(c)) {
                    seen.insert(n2);
                    clique.push(n2);
                }
            }
            if clique.len() > largest.len() {
                largest.clone_from(&clique);
            }
        }
        largest.sort_unstable();
        largest
            .iter()
            .copied()
            .map(String::as_str)
            .collect::<Vec<_>>()
            .join(",")
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 7,
            self, part_2, TEST, "co,de,ka,ta"
        };
    }
}

fn main() {
    AoC2024_23 {}.run(std::env::args());
}

const TEST: &str = "\
kh-tc
qp-kh
de-cg
ka-co
yn-aq
qp-ub
cg-tb
vc-aq
tb-ka
wh-tc
yn-cg
kh-ub
ta-co
de-co
tc-td
tb-wq
wh-td
ta-ka
td-qp
aq-cg
wq-ub
ub-vc
de-ta
wq-aq
wq-vc
wh-yn
ka-de
kh-ta
co-tc
wh-qp
tb-vc
td-yn
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2024_23 {}.samples();
    }
}
