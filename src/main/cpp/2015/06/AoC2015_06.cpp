#include <assert.h>

#include <numeric>
#include <regex>
#include <string>
#include <vector>

#include "../../aoc/aoc.hpp"
#include "../../aoc/grid/grid.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;

const string REGEX = "([ a-z]+) ([0-9]+),([0-9]+) through ([0-9]+),([0-9]+)";

void forEachCell(Grid<int>& grid, const Cell& start, const Cell& end,
                 const function<void(Grid<int>&, const Cell&)>& f) {
    for (int rr : aoc::Range::rangeClosed(start.row(), end.row())) {
        for (int cc : aoc::Range::rangeClosed(start.col(), end.col())) {
            f(grid, Cell::at(rr, cc));
        }
    }
}

int solve(const vector<string>& input,
          const function<void(Grid<int>&, const Cell&)>& turn_on,
          const function<void(Grid<int>&, const Cell&)>& turn_off,
          const function<void(Grid<int>&, const Cell&)>& toggle) {
    vector<string> cells;
    for (int i = 0; i < 1000; ++i) {
        cells.push_back(string(1000, '0'));
    }
    Grid<int> grid = Grid<int>::from(cells);
    const regex re(REGEX);
    for (const string& line : input) {
        smatch sm;
        regex_search(line, sm, re);
        const string command = sm.str(1);
        const Cell start = Cell::at(stoi(sm.str(2)), stoi(sm.str(3)));
        const Cell end = Cell::at(stoi(sm.str(4)), stoi(sm.str(5)));
        if (command == "turn on") {
            forEachCell(grid, start, end, turn_on);
        } else if (command == "turn off") {
            forEachCell(grid, start, end, turn_off);
        } else if (command == "toggle") {
            forEachCell(grid, start, end, toggle);
        }
    }
    return accumulate(
        grid.begin(), grid.end(), 0,
        [&grid](const int a, const Cell& b) { return a + grid.get(b); });
}

int part1(const vector<string>& input) {
    return solve(
        input,
        [](Grid<int>& grid, const Cell& cell) { grid.setValue(cell, 1); },
        [](Grid<int>& grid, const Cell& cell) { grid.setValue(cell, 0); },
        [](Grid<int>& grid, const Cell& cell) {
            grid.setValue(cell, grid.get(cell) == 1 ? 0 : 1);
        });
}

int part2(const vector<string>& input) {
    return solve(
        input,
        [](Grid<int>& grid, const Cell& cell) {
            grid.setValue(cell, grid.get(cell) + 1);
        },
        [](Grid<int>& grid, const Cell& cell) {
            grid.setValue(cell, max(grid.get(cell) - 1, 0));
        },
        [](Grid<int>& grid, const Cell& cell) {
            grid.setValue(cell, grid.get(cell) + 2);
        });
}

const vector<string> TEST1 = {"turn on 0,0 through 999,999"};
const vector<string> TEST2 = {"toggle 0,0 through 999,0"};
const vector<string> TEST3 = {"turn off 499,499 through 500,500"};
const vector<string> TEST4 = {"turn on 0,0 through 0,0"};
const vector<string> TEST5 = {"toggle 0,0 through 999,999"};

void samples() {
    assert(part1(TEST1) == 1000000);
    assert(part1(TEST2) == 1000);
    assert(part1(TEST3) == 0);
    assert(part2(TEST4) == 1);
    assert(part2(TEST5) == 2000000);
}

MAIN(2015, 6)
