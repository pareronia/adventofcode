#![allow(non_snake_case)]

use aoc::Puzzle;

struct Range {
    start: u32,
    end: u32,
}

impl Range {
    fn between(start: u32, end: u32) -> Range {
        Range { start, end }
    }
}

trait Contains {
    fn contains(&self, other: u32) -> bool;
}

impl Contains for Range {
    fn contains(&self, other: u32) -> bool {
        self.start <= other && other <= self.end
    }
}

trait ContainsRange {
    fn contains_range(&self, other: &Range) -> bool;
}

impl ContainsRange for Range {
    fn contains_range(&self, other: &Range) -> bool {
        self.contains(other.start) && self.contains(other.end)
    }
}

trait IsOverlappedBy {
    fn is_overlapped_by(&self, other: &Range) -> bool;
}

impl IsOverlappedBy for Range {
    fn is_overlapped_by(&self, other: &Range) -> bool {
        other.contains(self.start)
            || other.contains(self.end)
            || self.contains(other.start)
    }
}

struct AoC2022_04 {}

impl AoC2022_04 {
    fn solve2<F>(
        &self,
        input: &Vec<((u32, u32), (u32, u32))>,
        mut method: F,
    ) -> usize
    where
        F: FnMut(&Range, &Range) -> bool,
    {
        input
            .iter()
            .filter(|(t1, t2)| {
                let r1: Range = Range::between(t1.0, t1.1);
                let r2: Range = Range::between(t2.0, t2.1);
                method(&r1, &r2)
            })
            .count()
    }
}

impl aoc::Puzzle for AoC2022_04 {
    type Input = Vec<((u32, u32), (u32, u32))>;
    type Output1 = usize;
    type Output2 = usize;

    aoc::puzzle_year_day!(2022, 4);

    fn parse_input(&self, lines: Vec<String>) -> Vec<((u32, u32), (u32, u32))> {
        lines
            .iter()
            .map(|line| aoc::uints_with_check(line, 4))
            .map(|v| ((v[0], v[1]), (v[2], v[3])))
            .collect()
    }

    fn part_1(&self, input: &Vec<((u32, u32), (u32, u32))>) -> usize {
        self.solve2(input, |r1, r2| {
            r1.contains_range(r2) || r2.contains_range(r1)
        })
    }

    fn part_2(&self, input: &Vec<((u32, u32), (u32, u32))>) -> usize {
        self.solve2(input, |r1, r2| r1.is_overlapped_by(r2))
    }

    fn samples(&self) {
        #[rustfmt::skip]
        let test =
            "2-4,6-8\n\
             2-3,4-5\n\
             5-7,7-9\n\
             2-8,3,7\n\
             6-6,4-6\n\
             2-6,4-8";
        aoc::puzzle_samples! {
            self, part_1, test, 2,
            self, part_2, test, 4
        };
    }
}

fn main() {
    AoC2022_04 {}.run(std::env::args());
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn samples() {
        AoC2022_04 {}.samples();
    }
}
