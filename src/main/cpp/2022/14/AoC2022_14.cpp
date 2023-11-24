#include <assert.h>

#include <string>
#include <unordered_set>
#include <vector>

#include "../../aoc/aoc.hpp"
#include "../../aoc/geometry/geometry.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;

const Position SOURCE(500, 0);
const Position STOP(-1, -1);

tuple<vector<vector<bool>>, int> parse(const vector<string>& input) {
    unordered_set<Position> rocks;
    int maxX = 0;
    int maxY = 0;
    for (const string& line : input) {
        auto splits = aoc::split(line, " -> ");
        for (int i : aoc::Range::range(1, splits.size())) {
            auto splits1 = aoc::split(splits[i - 1], ",");
            auto splits2 = aoc::split(splits[i], ",");
            vector<int> xs = {stoi(splits1[0]), stoi(splits2[0])};
            vector<int> ys = {stoi(splits1[1]), stoi(splits2[1])};
            sort(xs.begin(), xs.end());
            sort(ys.begin(), ys.end());
            for (int x : aoc::Range::rangeClosed(xs[0], xs[1])) {
                for (int y : aoc::Range::rangeClosed(ys[0], ys[1])) {
                    rocks.insert(Position(x, y));
                    maxX = max(maxX, x);
                    maxY = max(maxY, y);
                }
            }
        }
    }
    vector<vector<bool>> occupied;
    for (int i = 0; i < maxY + 2; i++) {
        occupied.push_back(vector<bool>(maxX + 150, false));
    }
    for (const Position& p : rocks) {
        occupied[p.y][p.x] = true;
    }
    return make_tuple(occupied, maxY);
}

Position drop(const vector<vector<bool>>& occupied, const int maxY) {
    int currX = SOURCE.x;
    int currY = SOURCE.y;
    while (true) {
        const Position p(currX, currY);
        const int y = currY + 1;
        for (const int x : vector<int>({0, -1, 1})) {
            try {
                if (!occupied.at(y).at(currX + x)) {
                    currX = currX + x;
                    currY = y;
                    break;
                }
            } catch (const std::out_of_range& e) {
            }
        }
        if (p.x == currX && p.y == currY) {
            return Position(currX, currY);
        }
        if (currY > maxY) {
            return STOP;
        }
    }
    return Position(0, 0);
}

int solve(vector<vector<bool>>& occupied, const int maxY) {
    int cnt = 0;
    while (true) {
        const Position& p = drop(occupied, maxY);
        if (p == STOP) {
            break;
        }
        occupied[p.y][p.x] = true;
        cnt++;
        if (p == SOURCE) {
            break;
        }
    }
    return cnt;
}

int part1(const vector<string>& input) {
    vector<vector<bool>> occupied;
    int maxY;
    tie(occupied, maxY) = parse(input);
    return solve(occupied, maxY);
}

int part2(const vector<string>& input) {
    vector<vector<bool>> occupied;
    int maxY;
    tie(occupied, maxY) = parse(input);
    return solve(occupied, maxY + 2);
}

// clang-format off
const vector<string> TEST1 = {
"498,4 -> 498,6 -> 496,6",
"503,4 -> 502,4 -> 502,9 -> 494,9"
};
// clang-format on

void samples() {
    assert(part1(TEST1) == 24);
    assert(part2(TEST1) == 93);
}

MAIN(2022, 14)
