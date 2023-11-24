#![allow(non_snake_case)]

use aoc::log;
use aoc::Puzzle;
use std::collections::VecDeque;

struct AoC2022_05 {}

type Stack = VecDeque<char>;

#[derive(Clone, Debug)]
struct Move {
    quantity: usize,
    from: usize,
    to: usize,
}

struct Rearrangement {
    stacks: Vec<Stack>,
    moves: Vec<Move>,
}

enum CrateMover {
    CM9000,
    CM9001,
}

trait DoMove {
    fn do_move(&self, crate_: char, to: &mut Stack);
}

impl DoMove for CrateMover {
    fn do_move(&self, crate_: char, to: &mut Stack) {
        match self {
            CrateMover::CM9000 => {
                to.push_back(crate_);
            }
            CrateMover::CM9001 => {
                to.push_front(crate_);
            }
        };
    }
}

impl AoC2022_05 {
    fn simulate(&self, input: &Rearrangement, cm: CrateMover) -> String {
        let mut stacks = input.stacks.clone();
        for move_ in &input.moves {
            let from = move_.from - 1;
            let to = move_.to - 1;
            let mut tmp: VecDeque<char> = VecDeque::new();
            (0..move_.quantity).for_each(|_| {
                let crate_ = stacks[from].pop_back().unwrap();
                cm.do_move(crate_, &mut tmp);
            });
            tmp.iter().for_each(|c| stacks[to].push_back(*c));
        }
        stacks
            .iter()
            .map(|stack| stack.back().unwrap())
            .collect::<String>()
    }
}

impl aoc::Puzzle for AoC2022_05 {
    type Input = Rearrangement;
    type Output1 = String;
    type Output2 = String;

    aoc::puzzle_year_day!(2022, 5);

    fn parse_input(&self, lines: Vec<String>) -> Rearrangement {
        let mut stacks = vec![];
        let blocks = aoc::to_blocks(&lines);
        let size = aoc::uints_no_check(blocks[0].last().unwrap()).len();
        log!("size: {}", size);
        (0..size).for_each(|_| stacks.push(Stack::new()));
        (0..=blocks[0].len() - 2).rev().for_each(|i| {
            let line = blocks[0][i];
            (0..line.len())
                .filter(|j| j % 4 == 1)
                .filter(|j| line.chars().nth(*j).unwrap() != ' ')
                .for_each(|j| {
                    stacks[j / 4].push_back(line.chars().nth(j).unwrap())
                });
        });
        let moves = blocks[1]
            .iter()
            .map(|line| aoc::uints_with_check(line, 3))
            .map(|v| Move {
                quantity: v[0] as usize,
                from: v[1] as usize,
                to: v[2] as usize,
            })
            .collect();
        log!("stacks: {:?}", &stacks);
        log!("moves: {:?}", &moves);
        Rearrangement { stacks, moves }
    }

    fn part_1(&self, input: &Rearrangement) -> String {
        self.simulate(input, CrateMover::CM9000)
    }

    fn part_2(&self, input: &Rearrangement) -> String {
        self.simulate(input, CrateMover::CM9001)
    }

    fn samples(&self) {
        #[rustfmt::skip]
        let test =
           "    [D]    \n\
            [N] [C]    \n\
            [Z] [M] [P]\n\
             1   2   3 \n\
            \n\
            move 1 from 2 to 1\n\
            move 3 from 1 to 3\n\
            move 2 from 2 to 1\n\
            move 1 from 1 to 2";
        aoc::puzzle_samples! {
            self, part_1, test, "CMZ",
            self, part_2, test, "MCD"
        };
    }
}

fn main() {
    AoC2022_05 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2022_05 {}.samples();
    }
}
