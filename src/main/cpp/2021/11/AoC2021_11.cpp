#include <bits/stdc++.h>
#include "../../aoc/aoc.hpp"
#include "../../aoc/grid/grid.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;

void flash(Grid<int>& grid, const Cell& cell, uint& flashes) {
    grid.setValue(cell, 0);
    flashes++;
    for (const Cell& n : grid.octantsNeighbours(cell)) {
        if (grid.get(n) == 0) {
            continue;
        }
        grid.increment(n);
        if (grid.get(n) > 9) {
            flash(grid, n, flashes);
        }
    }
}

uint cycle(Grid<int>& grid) {
    for (const Cell& cell : grid) {
        grid.increment(cell);
    }
    uint flashes = 0;
    for (const Cell& cell : grid) {
        if (grid.get(cell) > 9) {
            flash(grid, cell, flashes);
        }
    }
    return flashes;
}

int part1(const vector<string>& input) {
    Grid<int> grid = Grid<int>::from(input);
    uint flashes = 0;
    for (int i = 0; i < 100; i++) {
        flashes += cycle(grid);
    }
    return flashes;
}

int part2(const vector<string>& input) {
    Grid<int> grid = Grid<int>::from(input);
    int ans = 1;
    while (true) {
        if (cycle(grid) == grid.size()) {
            break;
        }
        ans++;
    }
    return ans;
}

const vector<string> TEST = {
"5483143223",
"2745854711",
"5264556173",
"6141336146",
"6357385478",
"4167524645",
"2176841721",
"6882881134",
"4846848554",
"5283751526"
};

void samples() {
    assert(part1(TEST) == 1656);
    assert(part2(TEST) == 195);
}

MAIN(2021, 11)
