#![allow(non_snake_case)]

use {
    aoc::Puzzle,
    serde_json::{json, Value},
    std::cmp::Ordering,
};

#[derive(Clone, Eq, PartialEq)]
struct Packet {
    value: Value,
}

impl Ord for Packet {
    fn cmp(&self, other: &Self) -> Ordering {
        match (&self.value, &other.value) {
            (lhs, rhs) if lhs.is_number() && rhs.is_number() => {
                lhs.as_u64().cmp(&rhs.as_u64())
            }
            (lhs, rhs) if lhs.is_array() && rhs.is_number() => {
                let packet = Packet {
                    value: json!([rhs.as_u64()]),
                };
                self.cmp(&packet)
            }
            (lhs, rhs) if lhs.is_number() && rhs.is_array() => {
                let packet = Packet {
                    value: json!([lhs.as_u64()]),
                };
                packet.cmp(other)
            }
            (lhs, rhs) if lhs.is_array() && rhs.is_array() => {
                let a1 = lhs.as_array().unwrap();
                let a2 = rhs.as_array().unwrap();
                for (i, n1) in a1.iter().enumerate() {
                    match a2.get(i) {
                        Some(n2) => {
                            let p1 = Packet { value: json!(n1) };
                            let p2 = Packet { value: json!(n2) };
                            match p1.cmp(&p2) {
                                Ordering::Equal => continue,
                                val => return val,
                            }
                        }
                        None => return Ordering::Greater,
                    }
                }
                a1.len().cmp(&a2.len())
            }
            _ => unreachable!(),
        }
    }
}

impl PartialOrd for Packet {
    fn partial_cmp(&self, other: &Self) -> Option<Ordering> {
        Some(self.cmp(other))
    }
}

struct AoC2022_13;

impl AoC2022_13 {}

impl aoc::Puzzle for AoC2022_13 {
    type Input = Vec<Packet>;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2022, 13);

    fn parse_input(&self, lines: Vec<String>) -> Vec<Packet> {
        lines
            .iter()
            .filter(|line| !line.is_empty())
            .map(|line| serde_json::from_str::<Value>(line).unwrap())
            .map(|value| Packet { value })
            .collect()
    }

    fn part_1(&self, packets: &Vec<Packet>) -> usize {
        (0..packets.len())
            .step_by(2)
            .filter(|i| packets[*i] <= packets[*i + 1])
            .map(|i| i / 2 + 1)
            .sum()
    }

    fn part_2(&self, packets: &Vec<Packet>) -> usize {
        let mut packets =
            packets.iter().map(Packet::clone).collect::<Vec<Packet>>();
        let dividers = vec![
            Packet {
                value: json!([[2]]),
            },
            Packet {
                value: json!([[6]]),
            },
        ];
        dividers.iter().for_each(|d| packets.push(d.clone()));
        packets.sort_unstable();
        (1..=packets.len())
            .filter(|i| dividers.contains(&packets[i - 1]))
            .product()
    }

    fn samples(&self) {
        #[rustfmt::skip]
        let test =
            "[1,1,3,1,1]\n\
            [1,1,5,1,1]\n\
            \n\
            [[1],[2,3,4]]\n\
            [[1],4]\n\
            \n\
            [9]\n\
            [[8,7,6]]\n\
            \n\
            [[4,4],4,4]\n\
            [[4,4],4,4,4]\n\
            \n\
            [7,7,7,7]\n\
            [7,7,7]\n\
            \n\
            []\n\
            [3]\n\
            \n\
            [[[]]]\n\
            [[]]\n\
            \n\
            [1,[2,[3,[4,[5,6,7]]]],8,9]\n\
            [1,[2,[3,[4,[5,6,0]]]],8,9]";
        aoc::puzzle_samples! {
            self, part_1, test, 13,
            self, part_2, test, 140
        };
    }
}

fn main() {
    AoC2022_13 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2022_13 {}.samples();
    }
}
