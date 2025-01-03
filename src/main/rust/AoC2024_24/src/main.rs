#![allow(non_snake_case)]

use aoc::Puzzle;
use std::collections::{HashMap, VecDeque};
use std::str::FromStr;

#[derive(Debug, Eq, PartialEq)]
enum Op {
    Or,
    And,
    Xor,
}

impl Op {
    fn execute(&self, b1: bool, b2: bool) -> bool {
        match self {
            Self::Or => b1 | b2,
            Self::And => b1 & b2,
            Self::Xor => b1 ^ b2,
        }
    }
}

impl FromStr for Op {
    type Err = &'static str;

    fn from_str(string: &str) -> Result<Self, Self::Err> {
        match string {
            "OR" => Ok(Self::Or),
            "AND" => Ok(Self::And),
            "XOR" => Ok(Self::Xor),
            _ => panic!("Invalid string for Op: {}", string),
        }
    }
}

#[derive(Debug)]
struct Gate {
    in1: String,
    op: Op,
    in2: String,
    out: String,
}

struct AoC2024_24;

impl AoC2024_24 {}

impl aoc::Puzzle for AoC2024_24 {
    type Input = (HashMap<String, bool>, Vec<Gate>);
    type Output1 = u64;
    type Output2 = String;

    aoc::puzzle_year_day!(2024, 24);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        let mut wires: HashMap<String, bool> = HashMap::new();
        let mut gates: Vec<Gate> = Vec::new();
        lines.iter().for_each(|line| {
            if line.contains(": ") {
                let (name, val) = line.split_once(": ").unwrap();
                wires.insert(name.to_string(), val == "1");
            };
            if line.contains(" -> ") {
                let split: Vec<String> = line
                    .split_whitespace()
                    .take(5)
                    .map(|s| s.to_string())
                    .collect();
                gates.push(Gate {
                    in1: split[0].clone(),
                    op: Op::from_str(&split[1]).unwrap(),
                    in2: split[2].clone(),
                    out: split[4].clone(),
                });
            }
        });
        (wires, gates)
    }

    fn part_1(&self, input: &Self::Input) -> Self::Output1 {
        let (wires_in, gates) = input;
        let mut wires = wires_in.clone();
        let mut q: VecDeque<&Gate> = VecDeque::new();
        gates.iter().for_each(|g| q.push_back(g));
        while !q.is_empty() {
            let gate = q.pop_front().unwrap();
            if wires.contains_key(&gate.in1) && wires.contains_key(&gate.in2) {
                let b = gate.op.execute(wires[&gate.in1], wires[&gate.in2]);
                wires
                    .entry(gate.out.to_string())
                    .and_modify(|e| *e = b)
                    .or_insert(b);
            } else {
                q.push_back(gate);
            }
        }
        let max_z = gates
            .iter()
            .filter(|g| g.out.starts_with("z"))
            .map(|g| g.out[1..].parse::<u8>().unwrap())
            .max()
            .unwrap();
        (0..=max_z)
            .filter(|i| *wires.get(&format!("z{:02}", i)).unwrap())
            .map(|i| 1_u64 << i)
            .sum()
    }

    fn part_2(&self, input: &Self::Input) -> Self::Output2 {
        fn is_swapped(gate: &Gate, gates: &[Gate]) -> bool {
            fn outputs_to_z_except_first_one_and_not_xor(gate: &Gate) -> bool {
                gate.out.starts_with('z')
                    && gate.op != Op::Xor
                    && gate.out != "z45"
            }
            fn is_xor_not_connected_to_x_or_y_or_z(gate: &Gate) -> bool {
                gate.op == Op::Xor
                    && !(gate.in1.starts_with('x')
                        || gate.in1.starts_with('y')
                        || gate.in1.starts_with('z')
                        || gate.in2.starts_with('x')
                        || gate.in2.starts_with('y')
                        || gate.in2.starts_with('z')
                        || gate.out.starts_with('x')
                        || gate.out.starts_with('y')
                        || gate.out.starts_with('z'))
            }
            fn is_and_except_last_with_output_not_to_or(
                gate: &Gate,
                others: &[Gate],
            ) -> bool {
                gate.op == Op::And
                    && !(gate.in1 == "x00" || gate.in2 == "x00")
                    && others.iter().any(|other| {
                        other.op != Op::Or
                            && (other.in1 == gate.out || other.in2 == gate.out)
                    })
            }
            fn is_xor_with_output_to_or(gate: &Gate, others: &[Gate]) -> bool {
                gate.op == Op::Xor
                    && !(gate.in1 == "x00" || gate.in2 == "x00")
                    && others.iter().any(|other| {
                        other.op == Op::Or
                            && (other.in1 == gate.out || other.in2 == gate.out)
                    })
            }

            outputs_to_z_except_first_one_and_not_xor(gate)
                || is_xor_not_connected_to_x_or_y_or_z(gate)
                || is_and_except_last_with_output_not_to_or(gate, gates)
                || is_xor_with_output_to_or(gate, gates)
        }

        let (_, gates) = input;
        let mut swapped = gates
            .iter()
            .filter(|g| is_swapped(g, gates))
            .map(|g| g.out.to_string())
            .collect::<Vec<_>>();
        swapped.sort_unstable();
        swapped.join(",")
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST1, 4,
            self, part_1, TEST2, 2024
        };
    }
}

fn main() {
    AoC2024_24 {}.run(std::env::args());
}

const TEST1: &str = "\
x00: 1
x01: 1
x02: 1
y00: 0
y01: 1
y02: 0

x00 AND y00 -> z00
x01 XOR y01 -> z01
x02 OR y02 -> z02
";
const TEST2: &str = "\
x00: 1
x01: 0
x02: 1
x03: 1
x04: 0
y00: 1
y01: 1
y02: 1
y03: 1
y04: 1

ntg XOR fgs -> mjb
y02 OR x01 -> tnw
kwq OR kpj -> z05
x00 OR x03 -> fst
tgd XOR rvg -> z01
vdt OR tnw -> bfw
bfw AND frj -> z10
ffh OR nrd -> bqk
y00 AND y03 -> djm
y03 OR y00 -> psh
bqk OR frj -> z08
tnw OR fst -> frj
gnj AND tgd -> z11
bfw XOR mjb -> z00
x03 OR x00 -> vdt
gnj AND wpb -> z02
x04 AND y00 -> kjc
djm OR pbm -> qhw
nrd AND vdt -> hwm
kjc AND fst -> rvg
y04 OR y02 -> fgs
y01 AND x02 -> pbm
ntg OR kjc -> kwq
psh XOR fgs -> tgd
qhw XOR tgd -> z09
pbm OR djm -> kpj
x03 XOR y03 -> ffh
x00 XOR y04 -> ntg
bfw OR bqk -> z06
nrd XOR fgs -> wpb
frj XOR qhw -> z04
bqk OR frj -> z07
y03 OR x01 -> nrd
hwm AND bqk -> z03
tgd XOR rvg -> z12
tnw OR pbm -> gnj
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2024_24 {}.samples();
    }
}
