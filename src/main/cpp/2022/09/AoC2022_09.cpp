#include <assert.h>

#include <string>
#include <unordered_map>
#include <unordered_set>
#include <vector>

#include "../../aoc/aoc.hpp"
#include "../../aoc/geometry/geometry.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;
using Heading = Vector;

const unordered_map<string, Heading> MOVES = {{"U", Heading(0, 1)},
                                              {"D", Heading(0, -1)},
                                              {"L", Heading(-1, 0)},
                                              {"R", Heading(1, 0)}};

vector<Heading> parse(const vector<string>& input) {
    vector<Heading> moves;
    for (const string& line : input) {
        auto splits = aoc::split(line);
        for ([[maybe_unused]] int i = 0; i < stoi(splits[1]); ++i) {
            moves.push_back(MOVES.at(splits[0]));
        }
    }
    return moves;
}

Position catchup(const Position& head, const Position& tail) {
    const int dx = head.x - tail.x;
    const int dy = head.y - tail.y;
    if (abs(dx) > 1 || abs(dy) > 1) {
        auto vx = dx < 0 ? -1 : dx > 0 ? 1 : 0;
        auto vy = dy < 0 ? -1 : dy > 0 ? 1 : 0;
        return tail.translate(Vector(vx, vy));
    }
    return tail;
}

void moveRope(vector<Position>& rope, const Heading& move) {
    vector<Position> v;
    v.push_back(rope[0].translate(move));
    for (int i : aoc::Range::range(1, rope.size())) {
        v.push_back(catchup(v.back(), rope[i]));
    }
    rope = std::move(v);
}

int solve(const vector<string>& input, const size_t size) {
    vector<Position> rope(size, Position(0, 0));
    unordered_set<Position> seen;
    const auto moves = parse(input);
    for (const auto& move : moves) {
        moveRope(rope, move);
        seen.insert(rope.back());
    }
    return seen.size();
}

int part1(const vector<string>& input) { return solve(input, 2); }

int part2(const vector<string>& input) { return solve(input, 10); }

// clang-format off
const vector<string> TEST1 = {
"R 4",
"U 4",
"L 3",
"D 1",
"R 4",
"D 1",
"L 5",
"R 2"
};
const vector<string> TEST2 = {
"R 5",
"U 8",
"L 8",
"D 3",
"R 17",
"D 10",
"L 25",
"U 20"
};
// clang-format on

void samples() {
    assert(part1(TEST1) == 13);
    assert(part2(TEST1) == 1);
    assert(part2(TEST2) == 36);
}

MAIN(2022, 9)
