#include <assert.h>
#include <iostream>
#include <string>
#include <vector>
#include <array>
#include <algorithm>
#include <optional>
#include <queue>
#include "../../aocd/aocd.hpp"

using namespace std;

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

const vector<int> DR = { -1, 0, 1, 0 };
const vector<int> DC = { 0, 1, 0, -1 };

class Cell {
    public:
        Cell() {
        }

        Cell(const int row, const int col) {
            this->row = row;
            this->col = col;
        }

        int row;
        int col;
};

class IntGrid {
    public:
        static IntGrid from(const vector<string> &input) {
            vector<vector<int>> rows;
            for (const string line : input) {
                vector<int> row;
                for (const char ch : line) {
                    row.push_back(ch - '0');
                }
                rows.push_back(row);
            }
            return IntGrid(rows);
        }

        IntGrid(const vector<vector<int>> &values) {
            this->values = values;
        }

        void print() const {
            for (const vector<int> row : values) {
                for (const int col : row) {
                    cout << col;
                }
                cout << endl;
            }
        }

        int getValue(const Cell cell) const {
            return this->values[cell.row][cell.col];
        }

        int getHeight() const {
            return this->values.size();
        }

        int getWidth() const {
            return this->values[0].size();
        }

    private:
        vector<vector<int>> values;
};

class State {
    public:
        State(Cell cell, const int risk) {
            this->cell = cell;
            this->risk = risk;
        }

        bool operator <(const State &other) {
            return compare(other) < 0;
        }

        bool operator ==(const State &other) {
            return this->compare(other) == 0;
        }

        Cell cell;
        int risk;

    private:
        int compare(const State &other) {
            if (risk == other.risk) {
                return 0;
            } else if (risk > other.risk) {
                return 1;
            } else {
                return -1;
            }
        }
};

int getRisk(const IntGrid &grid, const Cell cell) {
    const Cell actual = Cell(cell.row % grid.getHeight(),
                             cell.col % grid.getWidth());
    int value = grid.getValue(actual)
                    + cell.row / grid.getHeight()
                    + cell.col / grid.getWidth();
    while (value > 9) {
        value -= 9;
    }
    return value;
}

vector<Cell> findNeighbours(const IntGrid &grid, const Cell cell, const int tiles) {
    vector<Cell> neighbours;
    for (int i = 0; i < 4; i++) {
        const int rr = cell.row + DR[i];
        const int cc = cell.col + DC[i];
        if (rr >= 0 && rr < tiles * grid.getHeight()
                && cc >= 0 && cc < tiles * grid.getWidth()) {
            neighbours.push_back(Cell(rr, cc));
        }
    }
    return neighbours;
}

vector<Cell> findLeastRiskPath(const IntGrid grid, const int tiles) {
    const Cell start = Cell(0, 0);
    const Cell end = Cell(tiles * grid.getHeight() - 1,
                          tiles * grid.getWidth() - 1);
    priority_queue<State> q;
    q.push(State(start, 0));
    map<Cell, int> best;
    best[start] = 0;
    map<Cell, Cell> parent;
    while (!q.empty()) {
    }
    throw "Unsolvable";
}

int solve(const vector<string> &input, const int tiles) {
    const IntGrid grid = IntGrid::from(input);
    return 0;
}

int part1(const vector<string> &input) {
    return solve(input, 1);
}

int part2(const vector<string> &input) {
    return solve(input, 5);
}

int main() {
    const vector<string> input = aocd::puzzle::getInputData(2021, 15);

    assert(part1(TEST) == 40);

    cout << "Part 1: " << part1(input) << endl;
    cout << "Part 2: " << part2(input) << endl;

    return 0;
}
