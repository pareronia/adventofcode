use itertools::Itertools;
use std::cmp::Ordering;
use std::ops::RangeInclusive;

pub trait IsOverlappedBy<Idx> {
    fn is_overlapped_by(&self, other: &RangeInclusive<Idx>) -> bool;
}

impl IsOverlappedBy<isize> for RangeInclusive<isize> {
    fn is_overlapped_by(&self, other: &RangeInclusive<isize>) -> bool {
        other.contains(self.start())
            || other.contains(self.end())
            || self.contains(other.start())
    }
}

pub fn merge(ranges: &[RangeInclusive<isize>]) -> Vec<RangeInclusive<isize>> {
    let mut merged: Vec<RangeInclusive<isize>> = Vec::new();
    ranges
        .iter()
        .sorted_by(|s, o| {
            let ans = s.start().cmp(o.start());
            match ans {
                Ordering::Equal => s.end().cmp(o.end()),
                _ => ans,
            }
        })
        .for_each(|range| {
            if merged.is_empty() {
                merged.push(range.clone());
            } else {
                let last = merged.last().unwrap().clone();
                if last.is_overlapped_by(range) {
                    merged.pop();
                    merged.push(*last.start()..=*last.end().max(range.end()));
                } else {
                    merged.push(range.clone());
                }
            }
        });
    merged
}
