#![allow(non_snake_case)]

use aoc::Puzzle;

#[derive(Debug)]
struct Machine {
    ax: i64,
    ay: i64,
    bx: i64,
    by: i64,
    px: i64,
    py: i64,
}

impl Machine {
    fn from_input(block: &[&String]) -> Self {
        let sp: Vec<&str> = block[2].split(", ").collect();
        Self {
            ax: block[0][12..14].parse::<i64>().unwrap(),
            ay: block[0][18..20].parse::<i64>().unwrap(),
            bx: block[1][12..14].parse::<i64>().unwrap(),
            by: block[1][18..20].parse::<i64>().unwrap(),
            px: sp[0].split_once("=").unwrap().1.parse::<i64>().unwrap(),
            py: sp[1][2..].parse::<i64>().unwrap(),
        }
    }
}

struct AoC2024_13;

impl AoC2024_13 {
    fn solve(&self, machines: &[Machine], offset: i64) -> u64 {
        fn calc_tokens(machine: &Machine, offset: i64) -> Option<u64> {
            let (px, py) = (machine.px + offset, machine.py + offset);
            let div =
                (machine.bx * machine.ay - machine.ax * machine.by) as f64;
            let ans_a = (py * machine.bx - px * machine.by) as f64 / div;
            let ans_b = (px * machine.ay - py * machine.ax) as f64 / div;
            match ans_a.fract() == 0.0 && ans_b.fract() == 0.0 {
                true => Some(ans_a as u64 * 3 + ans_b as u64),
                false => None,
            }
        }

        machines.iter().filter_map(|m| calc_tokens(m, offset)).sum()
    }
}

impl aoc::Puzzle for AoC2024_13 {
    type Input = Vec<Machine>;
    type Output1 = u64;
    type Output2 = u64;

    aoc::puzzle_year_day!(2024, 13);

    fn parse_input(&self, lines: Vec<String>) -> Self::Input {
        aoc::to_blocks(&lines)
            .iter()
            .map(|block| Machine::from_input(block))
            .collect()
    }

    fn part_1(&self, machines: &Self::Input) -> Self::Output1 {
        self.solve(machines, 0)
    }

    fn part_2(&self, machines: &Self::Input) -> Self::Output2 {
        self.solve(machines, 10_000_000_000_000)
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 480
        };
    }
}

fn main() {
    AoC2024_13 {}.run(std::env::args());
}

const TEST: &str = "\
Button A: X+94, Y+34
Button B: X+22, Y+67
Prize: X=8400, Y=5400

Button A: X+26, Y+66
Button B: X+67, Y+21
Prize: X=12748, Y=12176

Button A: X+17, Y+86
Button B: X+84, Y+37
Prize: X=7870, Y=6450

Button A: X+69, Y+23
Button B: X+27, Y+71
Prize: X=18641, Y=10279
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2024_13 {}.samples();
    }
}
