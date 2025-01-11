#include <assert.h>

#include <numeric>
#include <regex>
#include <string>
#include <vector>

#include "../../aoc/aoc.hpp"
#include "../../aoc/grid/grid.hpp"

using namespace std;

const string REGEX = "([ a-z]+) ([0-9]+),([0-9]+) through ([0-9]+),([0-9]+)";

void forEachCell(int (&grid)[1'000'000], const Cell& start, const Cell& end,
                 const function<int(int)>& f) {
    for (int r = start.row() * 1000; r <= end.row() * 1000; r += 1000) {
        for (int i = r + start.col(); i <= r + end.col(); i++) {
            grid[i] = f(grid[i]);
        }
    }
}

int solve(
        const vector<string>& input,
        const function<int(int)>& turn_on,
        const function<int(int)>& turn_off,
        const function<int(int)>& toggle
) {
    int grid[1'000'000] = {};
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
    return accumulate(begin(grid), end(grid), 0, plus<int>());
}

int part1(const vector<string>& input) {
    return solve(
        input,
        [](int v) { return 1; },
        [](int v) { return 0; },
        [](int v) { return v == 0 ? 1 : 0; }
    );
}

int part2(const vector<string>& input) {
    return solve(
        input,
        [](int v) { return v + 1; },
        [](int v) { return v == 0 ? 0 : v - 1; },
        [](int v) { return v + 2; }
    );
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
