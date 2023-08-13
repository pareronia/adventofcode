use std::{
    collections::{HashMap, HashSet, VecDeque},
    hash::Hash,
};

#[derive(Clone)]
struct State<T> {
    node: T,
    distance: usize,
}

pub struct BFS<T>(T);

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

    pub fn flood_fill(start: T, adjacent: impl Fn(T) -> Vec<T>) -> HashSet<T> {
        let mut q: VecDeque<T> = VecDeque::new();
        q.push_back(start);
        let mut seen: HashSet<T> = HashSet::new();
        seen.insert(start);
        while let Some(node) = q.pop_front() {
            adjacent(node.clone()).iter().for_each(|n| {
                if !seen.contains(&n) {
                    seen.insert(*n);
                    q.push_back(*n);
                }
            });
        }
        seen
    }
}

pub struct AStar<T>(T);

pub struct Result<T> {
    start: T,
    distances: HashMap<T, usize>,
    paths: HashMap<T, T>,
}

impl<T> Result<T>
where
    T: Hash + Eq + Copy,
{
    pub fn get_distance(&self, t: &T) -> Option<usize> {
        self.distances.get(t).cloned()
    }

    pub fn get_distances(&self) -> &HashMap<T, usize> {
        &self.distances
    }

    pub fn get_path(&self, t: &T) -> Option<Vec<T>> {
        match self.get_distance(t) {
            None => None,
            Some(_) => {
                let mut path = vec![];
                let mut parent = Some(t.clone());
                let source = self.start.clone();
                if *t != self.start {
                    while parent.is_some() && parent.unwrap() != self.start {
                        let p = parent.unwrap().clone();
                        path.insert(0, p);
                        parent = self.paths.get(&p).cloned()
                    }
                    path.insert(0, source);
                } else {
                    path.push(source);
                }
                Some(path)
            }
        }
    }
}

impl<T> AStar<T>
where
    T: Hash + Eq + Copy,
{
    pub fn execute(
        start: T,
        is_end: impl Fn(T) -> bool,
        adjacent: impl Fn(T) -> Vec<T>,
        cost: impl Fn(T) -> usize,
    ) -> Result<T> {
        let mut q: VecDeque<State<T>> = VecDeque::new();
        q.push_back(State {
            node: start,
            distance: 0,
        });
        let mut distances: HashMap<T, usize> = HashMap::new();
        distances.insert(start, 0);
        let mut paths: HashMap<T, T> = HashMap::new();
        while let Some(state) = q.pop_front() {
            if is_end(state.node.clone()) {
                break;
            }
            let total =
                distances.get(&state.node).unwrap_or(&usize::MAX).clone();
            adjacent(state.node.clone()).iter().for_each(|n| {
                let risk = total + cost(*n);
                if risk < *distances.get(n).unwrap_or(&usize::MAX) {
                    distances.insert(*n, risk);
                    paths.insert(*n, state.node);
                    q.push_back(State {
                        node: *n,
                        distance: risk,
                    });
                }
            });
        }
        Result {
            start,
            distances,
            paths,
        }
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

    #[test]
    pub fn bfs_flood_fill() {
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
        assert_eq!(
            BFS::flood_fill(Cell::at(2, 2), adjacent),
            HashSet::from([
                Cell::at(0, 0),
                Cell::at(1, 0),
                Cell::at(1, 1),
                Cell::at(2, 1),
                Cell::at(2, 2),
                Cell::at(3, 2),
                Cell::at(3, 3),
                Cell::at(4, 3),
            ])
        );
    }

    #[test]
    pub fn astar_execute_ok() {
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
        let result = AStar::execute(
            Cell::at(0, 0),
            |cell| cell == Cell::at(4, 3),
            adjacent,
            |_| 1,
        );
        assert_eq!(result.get_distance(&Cell::at(2, 2)).unwrap(), 4);
        assert_eq!(result.get_distance(&Cell::at(4, 3)).unwrap(), 7);
        assert_eq!(
            result.get_path(&Cell::at(2, 2)).unwrap(),
            vec![
                Cell::at(0, 0),
                Cell::at(1, 0),
                Cell::at(1, 1),
                Cell::at(2, 1),
                Cell::at(2, 2)
            ]
        );
        assert!(result.get_distance(&Cell::at(4, 0)).is_none());
        assert!(result.get_path(&Cell::at(4, 0)).is_none());
    }
}
