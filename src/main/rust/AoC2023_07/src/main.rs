#![allow(non_snake_case)]

use aoc::Puzzle;
use counter::Counter;
use itertools::Itertools;
use std::{cmp::Ordering, str::FromStr};

#[derive(Debug, Default)]
struct Hand {
    bid: u32,
    value: u32,
    strength: String,
    value_with_jokers: u32,
    strength_with_jokers: String,
}

impl FromStr for Hand {
    type Err = &'static str;

    fn from_str(s: &str) -> Result<Self, Self::Err> {
        fn get_value(cards: &str) -> u32 {
            let mc =
                cards.chars().collect::<Counter<_>>().most_common_ordered();
            match mc.len() > 1 {
                true => 2 * mc[0].1 as u32 + mc[1].1 as u32,
                false => 2 * mc[0].1 as u32,
            }
        }

        fn with_jokers(cards: &str) -> String {
            let c = cards
                .chars()
                .filter(|ch| ch != &'J')
                .collect::<Counter<_>>();
            if c.total::<usize>() == 0 {
                return String::from("AAAAA");
            }
            let best = c.most_common_ordered()[0].0;
            cards
                .chars()
                .map(|ch| match ch {
                    'J' => best,
                    _ => ch,
                })
                .collect()
        }

        let (s_value, s_bid) = s.split_once(' ').unwrap();
        let bid: u32 = s_bid.parse().unwrap();
        let value = get_value(s_value);
        let value_with_jokers = get_value(&with_jokers(s_value));
        let strength: String = s_value
            .chars()
            .map(|ch| match ch {
                'T' => 'B',
                'J' => 'C',
                'Q' => 'D',
                'K' => 'E',
                'A' => 'F',
                _ => ch,
            })
            .collect();
        let strength_with_jokers: String = s_value
            .chars()
            .map(|ch| match ch {
                'T' => 'B',
                'J' => '0',
                'Q' => 'D',
                'K' => 'E',
                'A' => 'F',
                _ => ch,
            })
            .collect();
        Ok(Hand {
            bid,
            value,
            strength,
            value_with_jokers,
            strength_with_jokers,
        })
    }
}

impl Hand {
    fn compare(self: &&Hand, other: &&Hand) -> Ordering {
        if self.value == other.value {
            self.strength.cmp(&other.strength)
        } else {
            self.value.cmp(&other.value)
        }
    }

    fn compare_with_jokers(self: &&Hand, other: &&Hand) -> Ordering {
        if self.value_with_jokers == other.value_with_jokers {
            self.strength_with_jokers.cmp(&other.strength_with_jokers)
        } else {
            self.value_with_jokers.cmp(&other.value_with_jokers)
        }
    }
}

struct AoC2023_07;

impl AoC2023_07 {
    fn solve(
        &self,
        hands: &[Hand],
        compare: impl FnMut(&&Hand, &&Hand) -> Ordering,
    ) -> u32 {
        hands
            .iter()
            .sorted_by(compare)
            .enumerate()
            .map(|(i, hand)| (i as u32 + 1) * hand.bid)
            .sum()
    }
}

impl aoc::Puzzle for AoC2023_07 {
    type Input = Vec<Hand>;
    type Output1 = u32;
    type Output2 = u32;

    aoc::puzzle_year_day!(2023, 7);

    fn parse_input(&self, lines: Vec<String>) -> Vec<Hand> {
        lines
            .into_iter()
            .map(|line| Hand::from_str(&line).unwrap())
            .collect()
    }

    fn part_1(&self, hands: &Vec<Hand>) -> u32 {
        self.solve(hands, Hand::compare)
    }

    fn part_2(&self, hands: &Vec<Hand>) -> u32 {
        self.solve(hands, Hand::compare_with_jokers)
    }

    fn samples(&self) {
        aoc::puzzle_samples! {
            self, part_1, TEST, 6440,
            self, part_2, TEST, 5905
        };
    }
}

fn main() {
    AoC2023_07 {}.run(std::env::args());
}

const TEST: &str = "\
32T3K 765
T55J5 684
KK677 28
KTJJT 220
QQQJA 483
";

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2023_07 {}.samples();
    }
}
