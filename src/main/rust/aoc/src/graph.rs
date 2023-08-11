use std::{
    collections::{HashSet, VecDeque},
    hash::Hash,
};

pub struct BFS<T>(T);

#[derive(Clone)]
struct State<T> {
    node: T,
    distance: usize,
}

impl<T> BFS<T>
where
    T: Hash + Eq + Copy,
{
    pub fn execute(
        start: T,
        is_end: impl Fn(T) -> bool,
        adjacent: impl Fn(T) -> Vec<T>,
    ) -> usize {
        let mut q: VecDeque<State<T>> = VecDeque::new();
        q.push_back(State {
            node: start,
            distance: 0,
        });
        let mut seen: HashSet<T> = HashSet::new();
        seen.insert(start);
        while let Some(state) = q.pop_front() {
            if is_end(state.node.clone()) {
                return state.distance;
            }
            adjacent(state.node.clone()).iter().for_each(|n| {
                if !seen.contains(&n) {
                    seen.insert(*n);
                    q.push_back(State {
                        node: *n,
                        distance: state.distance + 1,
                    });
                }
            });
        }
        unreachable!();
    }
}

#[cfg(test)]
mod tests {
    use super::*;
    use crate::grid::{Cell, CharGrid, Grid};

    fn bfs_execute(start: Cell, end: Cell) -> usize {
        #[rustfmt::skip]
        let v = &vec![
            ".###",
            "..##",
            "#..#",
            "##..",
            "###."];
        let grid = CharGrid::from(&v);
        let adjacent = |cell| {
            grid.capital_neighbours(&cell)
                .iter()
                .filter(|n| grid.get(&n) != '#')
                .cloned()
                .collect()
        };
        BFS::execute(start, |cell| cell == end, adjacent)
    }

    #[test]
    pub fn bfs_execute_ok() {
        assert_eq!(bfs_execute(Cell::at(0, 0), Cell::at(4, 3)), 7);
        assert_eq!(bfs_execute(Cell::at(2, 2), Cell::at(0, 0)), 4);
    }

    #[test]
    #[should_panic]
    pub fn bfs_unreachable() {
        bfs_execute(Cell::at(0, 0), Cell::at(4, 0));
    }
}
