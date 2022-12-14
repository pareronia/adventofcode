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

tuple<unordered_set<Position>, int> parse(const vector<string>& input) {
    unordered_set<Position> rocks;
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
                    maxY = max(maxY, y);
                }
            }
        }
    }
    return make_tuple(rocks, maxY);
}

Position drop(const unordered_set<Position>& rocks,
              const unordered_set<Position>& sand, const int maxY) {
    int currX = SOURCE.x;
    int currY = SOURCE.y;
    while (true) {
        const Position p(currX, currY);
        const Position down(currX, currY + 1);
        const Position downLeft(currX - 1, currY + 1);
        const Position downRight(currX + 1, currY + 1);
        const vector<Position> tests({down, downLeft, downRight});
        for (const Position& test : tests) {
            if (rocks.find(test) != rocks.end()) {
                continue;
            }
            if (sand.find(test) != sand.end()) {
                continue;
            }
            currX = test.x;
            currY = test.y;
            break;
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

int solve(const unordered_set<Position>& rocks, const int maxY) {
    unordered_set<Position> sand;
    while (true) {
        const Position& p = drop(rocks, sand, maxY);
        if (p == STOP) {
            break;
        }
        sand.insert(p);
        if (p == SOURCE) {
            break;
        }
    }
    return sand.size();
}

int part1(const vector<string>& input) {
    unordered_set<Position> rocks;
    int maxY;
    tie(rocks, maxY) = parse(input);
    return solve(rocks, maxY);
}

int part2(const vector<string>& input) {
    unordered_set<Position> rocks;
    int maxY;
    tie(rocks, maxY) = parse(input);
    const auto [minX, maxX] = minmax_element(
        begin(rocks), end(rocks),
        [](const Position& a, const Position& b) { return a.x < b.x; });
    const int max = maxY + 2;
    for (int x : aoc::Range::rangeClosed(minX->x - max, maxX->x + max)) {
        rocks.insert(Position(x, max));
    }
    return solve(rocks, max);
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
