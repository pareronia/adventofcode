// #![allow(non_snake_case)]

use aoc::Puzzle;
use std::collections::{HashSet, VecDeque};

#[derive(Clone, Copy, Debug)]
#[repr(u8)]
enum OpCode {
    Adv = 0,
    Bxl = 1,
    Bst = 2,
    Jnz = 3,
    Bxc = 4,
    Out = 5,
    Bdv = 6,
    Cdv = 7,
}

impl TryFrom<u64> for OpCode {
    type Error = &'static str;

    fn try_from(value: u64) -> Result<Self, Self::Error> {
        match value {
            0 => Ok(OpCode::Adv),
            1 => Ok(OpCode::Bxl),
            2 => Ok(OpCode::Bst),
            3 => Ok(OpCode::Jnz),
            4 => Ok(OpCode::Bxc),
            5 => Ok(OpCode::Out),
            6 => Ok(OpCode::Bdv),
            7 => Ok(OpCode::Cdv),
            _ => Err(""),
        }
    }
}

#[derive(Clone, Copy, Debug)]
struct Instruction {
    opcode: OpCode,
    operand: u64,
}

impl TryFrom<(u64, u64)> for Instruction {
    type Error = &'static str;

    fn try_from(value: (u64, u64)) -> Result<Self, Self::Error> {
        let (opcode, operand) = value;
        Ok(Self {
            opcode: OpCode::try_from(opcode)?,
            operand,
        })
    }
}

struct Program {
    a: u64,
    b: u64,
    c: u64,
    ins: Vec<Instruction>,
    ip: usize,
}

impl TryFrom<&Vec<u64>> for Program {
    type Error = &'static str;

    fn try_from(value: &Vec<u64>) -> Result<Self, Self::Error> {
        let ins = (0..value.len())
            .step_by(2)
            .map(|i| Instruction::try_from((value[i], value[i + 1])).unwrap())
            .collect();
        Ok(Self {
            a: 0,
            b: 0,
            c: 0,
            ins,
            ip: 0,
        })
    }
}

impl Program {
    fn combo(&self, operand: u64) -> u64 {
        match operand {
            0..=3 => operand,
            4 => self.a,
            5 => self.b,
            6 => self.c,
            _ => panic!(),
        }
    }

    fn op(&mut self, ins: &Instruction) -> Option<u64> {
        match ins.opcode {
            OpCode::Adv => {
                self.a >>= self.combo(ins.operand);
                self.ip += 1;
            }
            OpCode::Bxl => {
                self.b ^= ins.operand;
                self.ip += 1;
            }
            OpCode::Bst => {
                self.b = self.combo(ins.operand) & 7;
                self.ip += 1;
            }
            OpCode::Jnz => match self.a == 0 {
                false => self.ip = ins.operand as usize,
                true => self.ip += 1,
            },
            OpCode::Bxc => {
                self.b ^= self.c;
                self.ip += 1;
            }
            OpCode::Out => {
                let out = self.combo(ins.operand) & 7;
                self.ip += 1;
                return Some(out);
            }
            OpCode::Bdv => {
                self.b = self.a >> self.combo(ins.operand);
                self.ip += 1;
            }
            OpCode::Cdv => {
                self.c = self.a >> self.combo(ins.operand);
                self.ip += 1;
            }
        };
        None
    }

    fn run(&mut self, a: u64, b: u64, c: u64) -> Vec<u64> {
        self.a = a;
        self.b = b;
        self.c = c;
        self.ip = 0;
        let mut ans = vec![];
        while self.ip < self.ins.len() {
            let ins = self.ins[self.ip];
            if let Some(output) = self.op(&ins) {
                ans.push(output);
            }
        }
        ans
    }
}

struct AoC2024_17;

impl AoC2024_17 {}

impl aoc::Puzzle for AoC2024_17 {
    type Input = (u64, u64, u64, Vec<u64>);
    type Output1 = String;
    type Output2 = u64;

    aoc::puzzle_year_day!(2024, 17);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        let abc: Vec<u64> = (0..3)
            .map(|i| lines[i][12..].parse::<u64>().unwrap())
            .collect();
        let ops = lines[4][9..]
            .split(",")
            .map(|s| s.parse::<u64>().unwrap())
            .collect();
        (abc[0], abc[1], abc[2], ops)
    }

    fn part_1(&self, input: &Self::Input) -> Self::Output1 {
        let (a, b, c, ops) = input;
        let mut program = Program::try_from(ops).unwrap();
        program
            .run(*a, *b, *c)
            .into_iter()
            .map(|n| n.to_string())
            .collect::<Vec<String>>()
            .join(",")
    }

    fn part_2(&self, input: &Self::Input) -> Self::Output2 {
        let (_, b, c, ops) = input;
        let mut program = Program::try_from(ops).unwrap();
        let mut seen: HashSet<u64> = HashSet::from([0]);
        let mut q: VecDeque<u64> = VecDeque::from([0]);
        while !q.is_empty() {
            let cand_a = q.pop_front().unwrap() * 8;
            for i in 0..8 {
                let na = cand_a + i;
                let res = program.run(na, *b, *c);
                if res == *ops {
                    return na;
                }
                if res == ops[ops.len() - res.len()..] && !seen.contains(&na) {
                    seen.insert(na);
                    q.push_back(na);
                }
            }
        }
        unreachable!();
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST1, "4,6,3,5,6,3,5,2,1,0",
            self, part_2, TEST2, 117440
        };
    }
}

fn main() {
    AoC2024_17 {}.run(std::env::args());
}

const TEST1: &str = "\
Register A: 729
Register B: 0
Register C: 0

Program: 0,1,5,4,3,0
";
const TEST2: &str = "\
Register A: 2024
Register B: 0
Register C: 0

Program: 0,3,5,4,3,0
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2024_17 {}.samples();
    }
}
