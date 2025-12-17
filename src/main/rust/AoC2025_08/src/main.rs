#![allow(non_snake_case)]

use aoc::geometry3d::XYZ;
use aoc::Puzzle;
use itertools::Itertools;

#[derive(Clone)]
struct Box {
    idx: usize,
    position: XYZ,
}

struct Pair {
    first: Box,
    second: Box,
    distance: u64,
}

struct UnionFind {
    sz: Vec<usize>,
    ids: Vec<usize>,
    components: usize,
}

impl UnionFind {
    fn new(size: usize) -> Self {
        UnionFind {
            sz: vec![1; size],
            ids: (0..size).collect::<Vec<_>>(),
            components: size,
        }
    }

    fn find(&self, p: usize) -> usize {
        let mut root = p;
        while root != self.ids[root] {
            root = self.ids[root];
        }
        root
    }

    fn unify(&mut self, p: usize, q: usize) {
        let root_p = self.find(p);
        let root_q = self.find(q);
        if root_p != root_q {
            if self.sz[root_p] < self.sz[root_q] {
                self.sz[root_q] += self.sz[root_p];
                self.ids[root_p] = root_q;
                self.sz[root_p] = 0;
            } else {
                self.sz[root_p] += self.sz[root_q];
                self.ids[root_q] = root_p;
                self.sz[root_q] = 0;
            }
            self.components -= 1;
        }
    }
}

struct AoC2025_08;

impl AoC2025_08 {
    fn solve_1(
        &self,
        num_boxes: usize,
        pairs: &[Pair],
        size: usize,
    ) -> <Self as Puzzle>::Output1 {
        let mut dsu = UnionFind::new(num_boxes);
        pairs
            .iter()
            .take(size)
            .for_each(|pair| dsu.unify(pair.first.idx, pair.second.idx));
        dsu.sz.iter().sorted().rev().take(3).product()
    }

    fn sample_part_1(
        &self,
        input: &<Self as Puzzle>::Input,
    ) -> <Self as Puzzle>::Output1 {
        self.solve_1(input.0, &input.1, 10)
    }
}

impl aoc::Puzzle for AoC2025_08 {
    type Input = (usize, Vec<Pair>);
    type Output1 = usize;
    type Output2 = u64;

    aoc::puzzle_year_day!(2025, 8);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        let boxes = lines
            .iter()
            .enumerate()
            .map(|(idx, line)| Box {
                idx,
                position: XYZ::from_iter(
                    line.split(',').map(|sp| sp.parse::<u32>().unwrap()),
                ),
            })
            .collect::<Vec<_>>();
        let pairs = boxes
            .iter()
            .enumerate()
            .flat_map(|(i, r#box)| {
                (i + 1..boxes.len()).map(|j| Pair {
                    first: r#box.clone(),
                    second: boxes[j].clone(),
                    distance: r#box
                        .position
                        .squared_distance(&boxes[j].position),
                })
            })
            .sorted_by(|p1, p2| Ord::cmp(&p1.distance, &p2.distance))
            .collect::<Vec<_>>();
        (boxes.len(), pairs)
    }

    fn part_1(&self, input: &Self::Input) -> Self::Output1 {
        self.solve_1(input.0, &input.1, 1000)
    }

    fn part_2(&self, input: &Self::Input) -> Self::Output2 {
        let mut dsu = UnionFind::new(input.0);
        for pair in input.1.iter() {
            dsu.unify(pair.first.idx, pair.second.idx);
            if dsu.components == 1 && dsu.sz[dsu.find(0)] == input.0 {
                return pair.first.position.x() as u64
                    * pair.second.position.x() as u64;
            }
        }
        unreachable!();
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, sample_part_1, TEST, 40,
            self, part_2, TEST, 25272
        };
    }
}

fn main() {
    AoC2025_08 {}.run(std::env::args());
}

const TEST: &str = "\
162,817,812
57,618,57
906,360,560
592,479,940
352,342,300
466,668,158
542,29,236
431,825,988
739,650,466
52,470,668
216,146,977
819,987,18
117,168,530
805,96,715
346,949,466
970,615,88
941,993,340
862,61,35
984,92,344
425,690,689
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2025_08 {}.samples();
    }
}
