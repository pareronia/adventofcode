#include <bits/stdc++.h>
#include "../../aoc/aoc.hpp"
#include "../../aoc/grid/grid.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;

const vector<int> DR = { -1, 0, 1, 0 };
const vector<int> DC = { 0, 1, 0, -1 };
const Cell START = Cell::at(0, 0);

class State {
    public:
        State(const Cell _cell, const uint _risk)
            : cell(_cell.row(), _cell.col()), risk(_risk) {}

        bool operator >(const State& other) const {
            return compare(other) > 0;
        }

        bool operator ==(const State& other) const {
            return this->compare(other) == 0;
        }

        friend ostream& operator <<(ostream& strm, const State& state);
        Cell cell;
        uint risk;

    private:
        int compare(const State& other) const {
            if (risk == other.risk) {
                return 0;
            } else if (risk > other.risk) {
                return 1;
            } else {
                return -1;
            }
        }
};

template<>
struct std::hash<State> {
    std::size_t operator()(const State& state) const noexcept {
        std::size_t h1 = std::hash<Cell>{}(state.cell);
        std::size_t h2 = std::hash<int>{}(state.risk);
        return h1 ^ (h2 << 1);
    }
};

ostream& operator <<(ostream& strm, const State& state) {
    strm << "State[cell: " << state.cell << ", risk: " << state.risk << "]";
    return strm;
}

uint getRisk(const Grid<int>& grid, const Cell& cell) {
    const Cell actual = Cell(cell.row() % grid.height(),
                             cell.col() % grid.width());
    int value = grid.get(actual)
                    + cell.row() / grid.height()
                    + cell.col() / grid.width();
    while (value > 9) {
        value -= 9;
    }
    return value;
}

unordered_set<Cell> findNeighbours(
        const Grid<int>& grid,
        const Cell& cell,
        const uint tiles
) {
    unordered_set<Cell> neighbours;
    for (uint i = 0; i < 4; i++) {
        const uint rr = cell.row() + DR[i];
        const uint cc = cell.col() + DC[i];
        if (rr >= 0 && rr < tiles * grid.height()
                && cc >= 0 && cc < tiles * grid.width()) {
            neighbours.insert(Cell::at(rr, cc));
        }
    }
    return neighbours;
}

uint getOrDefault(
        const unordered_map<Cell, uint>& map,
        const Cell& cell,
        const uint defValue
) {
    auto it = map.find(cell);
    return it == map.end() ? defValue : it->second;
}

vector<Cell> findLeastRiskPath(const Grid<int>& grid, const uint tiles) {
    const Cell end = Cell(tiles * grid.height() - 1,
                          tiles * grid.width() - 1);
    priority_queue<State, deque<State>, greater<State>> q;
    q.push(State(START, 0));
    unordered_map<Cell, uint> best({{START, 0}});
    unordered_map<Cell, Cell> parent;
    while (!q.empty()) {
        const State state = q.top();
        q.pop();
        if (state.cell == end) {
            vector<Cell> path;
            path.push_back(end);
            Cell curr = end;
            while (parent.find(curr) != parent.end()) {
                curr = parent.at(curr);
                path.push_back(curr);
            }
            return path;
        }
        const uint total = getOrDefault(best, state.cell, 1e9);
        for (const Cell& n : findNeighbours(grid, state.cell, tiles)) {
            const uint newRisk = total + getRisk(grid, n);
            if (newRisk < getOrDefault(best, n, 1e9)) {
                best[n] = newRisk;
                parent.insert({n, state.cell});
                q.push(State(n, newRisk));
            }
        }
    }
    throw "Unsolvable";
}

int solve(const vector<string>& input, const uint tiles) {
    const Grid<int> grid = Grid<int>::from(input);
    uint ans = 0;
    for (const Cell& cell : findLeastRiskPath(grid, tiles)) {
        if (cell == START) {
            continue;
        }
        ans += getRisk(grid, cell);
    }
    return ans;
}

int part1(const vector<string>& input) {
    return solve(input, 1);
}

int part2(const vector<string>& input) {
    return solve(input, 5);
}

const vector<string> TEST = {
    "1163751742",
    "1381373672",
    "2136511328",
    "3694931569",
    "7463417111",
    "1319128137",
    "1359912421",
    "3125421639",
    "1293138521",
    "2311944581"
};

 void samples() {
    assert(part1(TEST) == 40);
    assert(part2(TEST) == 315);
}

MAIN(2021, 15)
