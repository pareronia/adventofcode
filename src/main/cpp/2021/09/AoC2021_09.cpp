#include <bits/stdc++.h>
#include "../../aoc/aoc.hpp"
#include "../../aoc/grid/grid.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;

unordered_set<Cell> findLows(const Grid<int>& grid) {
    unordered_set<Cell> lows;
    for (const Cell& cell : grid) {
        const auto& n = grid.capitalNeighbours(cell);
        const bool isLow = all_of(
                n.begin(), n.end(),
                [&grid, &cell](const Cell& nn) {
                    return grid.get(nn) > grid.get(cell);
                }
        );
        if (isLow) {
            lows.insert(cell);
        }
    }
    return lows;
}

uint sizeOfBasinAroundLow(const Grid<int>& grid, const Cell& low) {
    unordered_set<Cell> seen;
    deque<Cell> q({low});
    uint cnt = 0;
    while (!q.empty()) {
        const Cell& cell = q.front();
        q.pop_front();
        for (const Cell& n : grid.capitalNeighbours(cell)) {
            auto it = seen.find(n);
            if (it != seen.end() || grid.get(n) == 9) {
                continue;
            }
            seen.insert(n);
            q.push_back(n);
            cnt++;
        }
    }
    return cnt;
}

int part1(const vector<string>& input) {
    const Grid<int>& grid = Grid<int>::from(input);
    const auto& lows = findLows(grid);
    return accumulate(
            lows.begin(), lows.end(),
            0,
            [&grid](const int x, const Cell& cell) {
                return x + grid.get(cell) + 1;
            }
    );
}

int part2(const vector<string>& input) {
    const Grid<int>& grid = Grid<int>::from(input);
    vector<uint> sizes;
    for (const Cell& low : findLows(grid)) {
        sizes.push_back(sizeOfBasinAroundLow(grid, low));
    }
    sort(sizes.begin(), sizes.end());
    uint ans = 1, i = 0;
    for (auto it = sizes.rbegin(); i < 3; i++, ++it) {
        ans *= *it;
    }
    return ans;
}

const vector<string> TEST = {
"2199943210",
"3987894921",
"9856789892",
"8767896789",
"9899965678"
};

void samples() {
    assert(part1(TEST) == 15);
    assert(part2(TEST) == 1134);
}

MAIN(2021, 9)
