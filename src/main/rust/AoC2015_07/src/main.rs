#![allow(non_snake_case)]

use aoc::Puzzle;
use std::collections::{HashMap, VecDeque};
use std::str::FromStr;

#[derive(Clone, Debug, Eq, PartialEq)]
enum Op {
    Set,
    And,
    LShift,
    Not,
    Or,
    RShift,
}

impl FromStr for Op {
    type Err = &'static str;

    fn from_str(string: &str) -> Result<Self, Self::Err> {
        match string {
            "SET" => Ok(Self::Set),
            "AND" => Ok(Self::And),
            "LSHIFT" => Ok(Self::LShift),
            "NOT" => Ok(Self::Not),
            "OR" => Ok(Self::Or),
            "RSHIFT" => Ok(Self::RShift),
            _ => panic!("Invalid string for Op: {}", string),
        }
    }
}

#[derive(Clone, Debug)]
struct Gate {
    in1: String,
    op: Op,
    in2: Option<String>,
    out: String,
}

impl Gate {
    fn is_in1_numeric(&self) -> bool {
        self.in1.chars().next().unwrap().is_ascii_digit()
    }

    fn is_in2_numeric(&self) -> bool {
        self.in2
            .clone()
            .unwrap()
            .chars()
            .next()
            .unwrap()
            .is_ascii_digit()
    }

    fn get_in1(&self, wires: &HashMap<String, u16>) -> u16 {
        *wires.get(self.in1.as_str()).unwrap()
    }

    fn get_in2(&self, wires: &HashMap<String, u16>) -> u16 {
        *wires.get(self.in2.clone().unwrap().as_str()).unwrap()
    }

    fn get_in2_as_u16(&self) -> u16 {
        self.in2.clone().unwrap().parse::<u16>().unwrap()
    }

    fn execute_op(&self, wires: &mut HashMap<String, u16>) {
        match self.op {
            Op::Set => {
                wires.insert(self.out.clone(), self.get_in1(wires));
            }
            Op::And => {
                if self.is_in1_numeric() {
                    wires.insert(
                        self.out.clone(),
                        self.in1.parse::<u16>().unwrap() & self.get_in2(wires),
                    );
                } else {
                    wires.insert(
                        self.out.clone(),
                        self.get_in1(wires) & self.get_in2(wires),
                    );
                }
            }
            Op::LShift => {
                wires.insert(
                    self.out.clone(),
                    self.get_in1(wires) << self.get_in2_as_u16(),
                );
            }
            Op::Not => {
                wires.insert(self.out.clone(), !self.get_in1(wires));
            }
            Op::Or => {
                wires.insert(
                    self.out.clone(),
                    self.get_in1(wires) | self.get_in2(wires),
                );
            }
            Op::RShift => {
                wires.insert(
                    self.out.clone(),
                    self.get_in1(wires) >> self.get_in2_as_u16(),
                );
            }
        }
    }
}

struct AoC2015_07;

impl AoC2015_07 {
    fn solve(
        &self,
        wires: &mut HashMap<String, u16>,
        gates: &[Gate],
        wire: &str,
    ) -> u16 {
        let mut q: VecDeque<&Gate> = VecDeque::new();
        gates.iter().for_each(|g| q.push_back(g));
        while !q.is_empty() {
            let gate = q.pop_front().unwrap();
            if (gate.is_in1_numeric() || wires.contains_key(&gate.in1))
                && (gate.in2.is_none()
                    || gate.is_in2_numeric()
                    || wires.contains_key(&gate.in2.clone().unwrap()))
            {
                gate.execute_op(wires);
            } else {
                q.push_back(gate);
            }
        }
        *wires.get(wire).unwrap()
    }
}

impl aoc::Puzzle for AoC2015_07 {
    type Input = (HashMap<String, u16>, Vec<Gate>);
    type Output1 = u16;
    type Output2 = u16;

    aoc::puzzle_year_day!(2015, 7);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        let mut wires: HashMap<String, u16> = HashMap::new();
        let mut gates: Vec<Gate> = Vec::new();
        for line in lines {
            let (first, second) = line.split_once(" -> ").unwrap();
            let splits = first.split_whitespace().collect::<Vec<_>>();
            match splits.len() {
                1 => {
                    if splits[0].chars().next().unwrap().is_ascii_digit() {
                        wires
                            .insert(second.to_string(), first.parse().unwrap());
                    } else {
                        gates.push(Gate {
                            in1: splits[0].to_string(),
                            op: Op::Set,
                            in2: None,
                            out: second.to_string(),
                        });
                    }
                }
                2 => {
                    gates.push(Gate {
                        in1: splits[1].to_string(),
                        op: Op::Not,
                        in2: None,
                        out: second.to_string(),
                    });
                }
                _ => {
                    gates.push(Gate {
                        in1: splits[0].to_string(),
                        op: Op::from_str(splits[1]).unwrap(),
                        in2: Some(splits[2].to_string()),
                        out: second.to_string(),
                    });
                }
            };
        }
        (wires, gates)
    }

    fn part_1(&self, input: &Self::Input) -> Self::Output1 {
        let (wires, gates) = input;
        let mut wires_1: HashMap<String, u16> = HashMap::new();
        wires_1.clone_from(wires);
        self.solve(&mut wires_1, gates, "a")
    }

    fn part_2(&self, input: &Self::Input) -> Self::Output2 {
        let (wires, gates) = input;
        let mut wires_1: HashMap<String, u16> = HashMap::new();
        wires_1.clone_from(wires);
        let a = self.solve(&mut wires_1, gates, "a");
        let mut wires_2: HashMap<String, u16> = HashMap::new();
        wires_2.clone_from(wires);
        wires_2.insert("b".to_string(), a);
        self.solve(&mut wires_2, gates, "a")
    }

    fn samples(&self) {
        let (mut wires, gates) = self.parse_input(aoc::split_lines(TEST));
        assert_eq!(self.solve(&mut wires, &gates, "x"), 123);
        assert_eq!(self.solve(&mut wires, &gates, "y"), 456);
        assert_eq!(self.solve(&mut wires, &gates, "d"), 72);
        assert_eq!(self.solve(&mut wires, &gates, "e"), 507);
        assert_eq!(self.solve(&mut wires, &gates, "f"), 492);
        assert_eq!(self.solve(&mut wires, &gates, "g"), 114);
        assert_eq!(self.solve(&mut wires, &gates, "h"), 65412);
        assert_eq!(self.solve(&mut wires, &gates, "i"), 65079);
        assert_eq!(self.solve(&mut wires, &gates, "j"), 65079);
    }
}

fn main() {
    AoC2015_07 {}.run(std::env::args());
}

const TEST: &str = "\
123 -> x
456 -> y
x AND y -> d
x OR y -> e
x LSHIFT 2 -> f
y RSHIFT 2 -> g
NOT x -> h
NOT y -> i
i -> j
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2015_07 {}.samples();
    }
}
