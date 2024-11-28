#![allow(non_snake_case)]

use aoc::Puzzle;
use std::collections::{HashMap, VecDeque};

#[derive(Clone, Copy)]
enum Operation {
    Square,
    Add,
    Multiply,
}

#[derive(Clone)]
struct Monkey {
    items: VecDeque<u64>,
    operation: Operation,
    operand: Option<u64>,
    test: u64,
    throw_true: usize,
    throw_false: usize,
}

struct AoC2022_11;

impl AoC2022_11 {
    fn solve(
        &self,
        monkeys: &mut [Monkey],
        rounds: usize,
        divider: u64,
        modulus: Option<u64>,
    ) -> u64 {
        let mut counter: HashMap<usize, usize> = HashMap::new();
        let calc_level = |item: u64, operation, operand: Option<u64>| {
            let mut level = match operation {
                Operation::Square => item * item,
                Operation::Add => item + operand.unwrap(),
                Operation::Multiply => item * operand.unwrap(),
            };
            level /= divider;
            modulus.map_or(level, |mo| level % mo)
        };
        let mut round = || {
            (0..monkeys.len()).for_each(|i| {
                let len = monkeys[i].items.len();
                (0..len).for_each(|_| {
                    let level = calc_level(
                        monkeys[i].items.pop_front().unwrap(),
                        monkeys[i].operation,
                        monkeys[i].operand,
                    );
                    let to = match level % monkeys[i].test {
                        0 => monkeys[i].throw_true,
                        _ => monkeys[i].throw_false,
                    };
                    monkeys[to].items.push_back(level);
                });
                counter.entry(i).and_modify(|e| *e += len).or_insert(len);
            })
        };

        (0..rounds).for_each(|_| round());
        let mut values: Vec<usize> = counter.values().copied().collect();
        values.sort();
        values
            .iter()
            .rev()
            .take(2)
            .fold(1_u64, |acc, x| acc * *x as u64)
    }
}

impl aoc::Puzzle for AoC2022_11 {
    type Input = Vec<Monkey>;
    type Output1 = u64;
    type Output2 = u64;

    aoc::puzzle_year_day!(2022, 11);

    fn parse_input(&self, lines: Vec<String>) -> Vec<Monkey> {
        fn parse_last_word(line: &str) -> u32 {
            line.split_whitespace().last().unwrap().parse().unwrap()
        }

        aoc::to_blocks(&lines)
            .iter()
            .map(|block| {
                let items: VecDeque<u64> = aoc::uints_no_check(block[1])
                    .iter()
                    .map(|u| *u as u64)
                    .collect();
                let splits: Vec<&str> = block[2]
                    .split(" = ")
                    .nth(1)
                    .unwrap()
                    .split_whitespace()
                    .collect();
                let (operation, operand) = match splits[2] {
                    "old" => (Operation::Square, None),
                    _ => {
                        let a = splits[2].parse::<u64>().ok();
                        match splits[1] {
                            "+" => (Operation::Add, a),
                            _ => (Operation::Multiply, a),
                        }
                    }
                };
                let test = parse_last_word(block[3]) as u64;
                let throw_true = parse_last_word(block[4]) as usize;
                let throw_false = parse_last_word(block[5]) as usize;
                Monkey {
                    items,
                    operation,
                    operand,
                    test,
                    throw_true,
                    throw_false,
                }
            })
            .collect::<Vec<Monkey>>()
    }

    fn part_1(&self, input: &Vec<Monkey>) -> u64 {
        let mut monkeys =
            input.iter().map(Monkey::clone).collect::<Vec<Monkey>>();
        self.solve(&mut monkeys, 20, 3, None)
    }

    fn part_2(&self, input: &Vec<Monkey>) -> u64 {
        let mut monkeys: Vec<Monkey> =
            input.iter().map(Monkey::clone).collect();
        let modulus: u64 = monkeys.iter().map(|m| m.test).product();
        self.solve(&mut monkeys, 10_000, 1, Some(modulus))
    }

    fn samples(&self) {
        #[rustfmt::skip]
        let test =
            "Monkey 0:\n\
              Starting items: 79, 98\n\
              Operation: new = old * 19\n\
              Test: divisible by 23\n\
                If true: throw to monkey 2\n\
                If false: throw to monkey 3\n\
            \n\
            Monkey 1:\n\
              Starting items: 54, 65, 75, 74\n\
              Operation: new = old + 6\n\
              Test: divisible by 19\n\
                If true: throw to monkey 2\n\
                If false: throw to monkey 0\n\
            \n\
            Monkey 2:\n\
              Starting items: 79, 60, 97\n\
              Operation: new = old * old\n\
              Test: divisible by 13\n\
                If true: throw to monkey 1\n\
                If false: throw to monkey 3\n\
            \n\
            Monkey 3:\n\
              Starting items: 74\n\
              Operation: new = old + 3\n\
              Test: divisible by 17\n\
                If true: throw to monkey 0\n\
                If false: throw to monkey 1";
        aoc::puzzle_samples! {
            self, part_1, test, 10_605,
            self, part_2, test, 2_713_310_158_u64
        };
    }
}

fn main() {
    AoC2022_11 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2022_11 {}.samples();
    }
}
