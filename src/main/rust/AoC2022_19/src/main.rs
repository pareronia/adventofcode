#![allow(non_snake_case)]

use aoc::{self, graph::BFS};
use rayon::prelude::*;

#[derive(Debug)]
struct Blueprint {
    id: u8,
    ore_cost: u8,
    clay_cost: u8,
    obsidian_ore_cost: u8,
    obsidian_clay_cost: u8,
    geode_ore_cost: u8,
    geode_obsidian_cost: u8,
    max_ore: u8,
}

impl Blueprint {
    fn from_input(line: &String) -> Self {
        let nums = aoc::uints_with_check(&line, 7);
        let id = nums[0] as u8;
        let ore_cost = nums[1] as u8;
        let clay_cost = nums[2] as u8;
        let obsidian_ore_cost = nums[3] as u8;
        let obsidian_clay_cost = nums[4] as u8;
        let geode_ore_cost = nums[5] as u8;
        let geode_obsidian_cost = nums[6] as u8;
        let max_ore =
            *vec![ore_cost, clay_cost, obsidian_ore_cost, geode_ore_cost]
                .iter()
                .max()
                .unwrap() as u8;
        Self {
            id,
            ore_cost,
            clay_cost,
            obsidian_ore_cost,
            obsidian_clay_cost,
            geode_ore_cost,
            geode_obsidian_cost,
            max_ore,
        }
    }
}

#[derive(Clone, Copy, Default, Eq, Hash, PartialEq)]
struct State {
    time: u8,
    ore_store: u8,
    ore_robot: u8,
    clay_store: u8,
    clay_robot: u8,
    obsidian_store: u8,
    obsidian_robot: u8,
    geode_store: u8,
    geode_robot: u8,
}

impl State {
    fn max_possible_geodes(&self, max_time: u8) -> u8 {
        self.geode_store + (self.geode_robot + 1) * (max_time - self.time)
    }

    fn can_build_geode_robot(&self, blueprint: &Blueprint) -> bool {
        self.ore_store >= blueprint.geode_ore_cost
            && self.obsidian_store >= blueprint.geode_obsidian_cost
    }

    fn need_obsidian_robot(&self, blueprint: &Blueprint) -> bool {
        self.obsidian_robot < blueprint.geode_obsidian_cost
    }

    fn can_build_obsidian_robot(&self, blueprint: &Blueprint) -> bool {
        self.ore_store >= blueprint.obsidian_ore_cost
            && self.clay_store >= blueprint.obsidian_clay_cost
    }

    fn need_clay_robot(&self, blueprint: &Blueprint) -> bool {
        self.clay_robot < blueprint.obsidian_clay_cost
    }

    fn can_build_clay_robot(&self, blueprint: &Blueprint) -> bool {
        self.ore_store >= blueprint.clay_cost
    }

    fn need_ore_robot(&self, blueprint: &Blueprint) -> bool {
        self.ore_robot < blueprint.max_ore
    }

    fn can_build_ore_robot(&self, blueprint: &Blueprint) -> bool {
        self.ore_store >= blueprint.ore_cost
    }

    fn build_geode_robot(&self, blueprint: &Blueprint) -> Self {
        Self {
            time: self.time + 1,
            ore_store: self.ore_store + self.ore_robot
                - blueprint.geode_ore_cost,
            clay_store: self.clay_store + self.clay_robot,
            clay_robot: self.clay_robot,
            obsidian_store: self.obsidian_store + self.obsidian_robot
                - blueprint.geode_obsidian_cost,
            geode_store: self.geode_store + self.geode_robot,
            geode_robot: self.geode_robot + 1,
            ..*self
        }
    }

    fn build_ore_robot(&self, blueprint: &Blueprint) -> Self {
        self.build_next(
            blueprint,
            blueprint.max_ore,
            self.ore_store + self.ore_robot - blueprint.ore_cost,
            self.ore_robot + 1,
            self.clay_store + self.clay_robot,
            self.clay_robot,
            self.obsidian_store + self.obsidian_robot,
            self.obsidian_robot,
            self.geode_store + self.geode_robot,
            self.geode_robot,
        )
    }

    fn build_clay_robot(&self, blueprint: &Blueprint) -> Self {
        self.build_next(
            blueprint,
            blueprint.max_ore,
            self.ore_store + self.ore_robot - blueprint.clay_cost,
            self.ore_robot,
            self.clay_store + self.clay_robot,
            self.clay_robot + 1,
            self.obsidian_store + self.obsidian_robot,
            self.obsidian_robot,
            self.geode_store + self.geode_robot,
            self.geode_robot,
        )
    }

    fn build_obsidian_robot(&self, blueprint: &Blueprint) -> Self {
        self.build_next(
            blueprint,
            blueprint.max_ore,
            self.ore_store + self.ore_robot - blueprint.obsidian_ore_cost,
            self.ore_robot,
            self.clay_store + self.clay_robot - blueprint.obsidian_clay_cost,
            self.clay_robot,
            self.obsidian_store + self.obsidian_robot,
            self.obsidian_robot + 1,
            self.geode_store + self.geode_robot,
            self.geode_robot,
        )
    }

    fn no_build(&self, blueprint: &Blueprint) -> Self {
        self.build_next(
            blueprint,
            blueprint.max_ore,
            self.ore_store + self.ore_robot,
            self.ore_robot,
            self.clay_store + self.clay_robot,
            self.clay_robot,
            self.obsidian_store + self.obsidian_robot,
            self.obsidian_robot,
            self.geode_store + self.geode_robot,
            self.geode_robot,
        )
    }

    fn build_next(
        &self,
        blueprint: &Blueprint,
        max_ore: u8,
        ore_store: u8,
        ore_robot: u8,
        clay_store: u8,
        clay_robot: u8,
        obsidian_store: u8,
        obsidian_robot: u8,
        geode_store: u8,
        geode_robot: u8,
    ) -> Self {
        Self {
            time: self.time + 1,
            ore_store: ore_store.min(self.cushion(max_ore)),
            ore_robot,
            clay_store: clay_store
                .min(self.cushion(blueprint.obsidian_clay_cost)),
            clay_robot,
            obsidian_store: obsidian_store
                .min(self.cushion(blueprint.geode_obsidian_cost)),
            obsidian_robot,
            geode_store,
            geode_robot,
        }
    }

    fn cushion(&self, val: u8) -> u8 {
        (val as f32 * 1.5_f32).ceil() as u8
    }
}

use aoc::Puzzle;

struct AoC2022_19;

impl AoC2022_19 {
    fn solve(&self, blueprint: &Blueprint, max_time: u8) -> u32 {
        let start = State {
            ore_robot: 1,
            ..State::default()
        };
        let mut best = 0;
        let adjacent = |state: State| {
            if state.time > max_time
                || best >= state.max_possible_geodes(max_time)
            {
                return vec![];
            }
            best = best.max(state.geode_store);
            let mut next: Vec<State> = vec![];
            if state.can_build_geode_robot(blueprint) {
                next.push(state.build_geode_robot(blueprint));
                return next;
            }
            if state.need_obsidian_robot(blueprint)
                && state.can_build_obsidian_robot(blueprint)
            {
                next.push(state.build_obsidian_robot(blueprint));
            }
            if state.need_clay_robot(blueprint)
                && state.can_build_clay_robot(blueprint)
            {
                next.push(state.build_clay_robot(blueprint));
            }
            if state.need_ore_robot(blueprint)
                && state.can_build_ore_robot(blueprint)
            {
                next.push(state.build_ore_robot(blueprint));
            }
            next.push(state.no_build(blueprint));
            next
        };
        BFS::flood_fill_mut(start, adjacent);
        best as u32
    }
}

impl aoc::Puzzle for AoC2022_19 {
    type Input = Vec<Blueprint>;
    type Output1 = u32;
    type Output2 = u32;

    aoc::puzzle_year_day!(2022, 19);

    fn parse_input(&self, lines: Vec<String>) -> Vec<Blueprint> {
        lines
            .iter()
            .map(|line| Blueprint::from_input(line))
            .collect()
    }

    fn part_1(&self, blueprints: &Vec<Blueprint>) -> u32 {
        blueprints
            .par_iter()
            .map(|blueprint| blueprint.id as u32 * self.solve(blueprint, 24))
            .sum()
    }

    fn part_2(&self, blueprints: &Vec<Blueprint>) -> u32 {
        blueprints
            .par_iter()
            .take(3)
            .map(|blueprint| self.solve(blueprint, 32))
            .product()
    }

    fn samples(&self) {
        #[rustfmt::skip]
        let test =
            "Blueprint 1: \
             Each ore robot costs 4 ore. \
             Each clay robot costs 2 ore. \
             Each obsidian robot costs 3 ore and 14 clay. \
             Each geode robot costs 2 ore and 7 obsidian.\n\
             Blueprint 2: \
             Each ore robot costs 2 ore. \
             Each clay robot costs 3 ore. \
             Each obsidian robot costs 3 ore and 8 clay. \
             Each geode robot costs 3 ore and 12 obsidian.";
        aoc::puzzle_samples! {
            self, part_1, test, 33,
            self, part_2, test, 56 * 62
        };
    }
}

fn main() {
    AoC2022_19 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2022_19 {}.samples();
    }
}
