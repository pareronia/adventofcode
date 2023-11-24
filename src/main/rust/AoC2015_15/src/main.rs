#![allow(non_snake_case)]

use aoc::Puzzle;
use strum::{EnumCount, EnumIter, IntoEnumIterator};

#[derive(Clone, Copy, EnumCount, EnumIter, PartialEq)]
enum Property {
    Capacity,
    Durability,
    Flavor,
    Texture,
    Calories,
}

#[derive(Debug)]
struct Ingredients {
    ingredients: Vec<Vec<i32>>,
}

impl Ingredients {
    fn from_input(lines: &Vec<String>) -> Self {
        let ingredients: Vec<Vec<i32>> = lines
            .iter()
            .map(|line| aoc::ints_with_check(&line, Property::COUNT))
            .collect();
        Self { ingredients }
    }

    fn generate_measures(&self) -> Vec<Vec<i32>> {
        let mut measures: Vec<Vec<i32>> = Vec::new();
        for i in 0..=100 {
            if self.ingredients.len() == 1 {
                measures.push(vec![i, 100 - i]);
                continue;
            }
            for j in 0..=100 - i {
                for k in 0..=100 - i - j {
                    measures.push(vec![i, j, k, 100 - i - j - k]);
                }
            }
        }
        measures
    }

    fn get_property_score(&self, measures: &Vec<i32>, p: Property) -> i32 {
        (0..self.ingredients.len())
            .map(|i| self.ingredients[i][p as usize] * measures[i])
            .sum()
    }

    fn calculate_score(
        &self,
        measures: &Vec<i32>,
        calories_target: Option<i32>,
    ) -> i32 {
        match calories_target {
            Some(val)
                if self.get_property_score(measures, Property::Calories)
                    != val =>
            {
                0
            }
            _ => Property::iter()
                .filter(|p| *p != Property::Calories)
                .map(|p| 0.max(self.get_property_score(measures, p)))
                .product(),
        }
    }

    fn get_maximum_score(&self, limit: Option<i32>) -> i32 {
        self.generate_measures()
            .iter()
            .map(|m| self.calculate_score(m, limit))
            .max()
            .unwrap()
    }

    fn get_highest_score(&self) -> i32 {
        self.get_maximum_score(None)
    }

    fn get_highest_score_with_calorie_limit(&self, limit: i32) -> i32 {
        self.get_maximum_score(Some(limit))
    }
}

struct AoC2015_15;

impl AoC2015_15 {}

impl aoc::Puzzle for AoC2015_15 {
    type Input = Ingredients;
    type Output1 = i32;
    type Output2 = i32;

    aoc::puzzle_year_day!(2015, 15);

    fn parse_input(&self, lines: Vec<String>) -> Ingredients {
        Ingredients::from_input(&lines)
    }

    fn part_1(&self, ingredients: &Ingredients) -> i32 {
        ingredients.get_highest_score()
    }

    fn part_2(&self, ingredients: &Ingredients) -> i32 {
        ingredients.get_highest_score_with_calorie_limit(500)
    }

    fn samples(&self) {
        #[rustfmt::skip]
        let test =
            "Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8\n\
             Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3";
        aoc::puzzle_samples! {
            self, part_1, test, 62_842_880,
            self, part_2, test, 57_600_000
        };
    }
}

fn main() {
    AoC2015_15 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2015_15 {}.samples();
    }
}
