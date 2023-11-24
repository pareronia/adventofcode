#include <assert.h>

#include <algorithm>
#include <climits>
#include <string>
#include <unordered_map>
#include <vector>

#include "../../aoc/aoc.hpp"
#include "../../aoc/geometry3d/geometry3d.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;

const Position3D ORIGIN = Position3D(0, 0, 0);
// clang-format off
const unordered_map<string, Vector3D> HEADING = {
    {"n",  Vector3D(  0, -1,  1)},
    {"ne", Vector3D(  1, -1,  0)},
    {"se", Vector3D(  1,  0, -1)},
    {"s",  Vector3D(  0,  1, -1)},
    {"sw", Vector3D( -1,  1,  0)},
    {"nw", Vector3D( -1,  0,  1)}
};
// clang-format on

vector<Vector3D> parse(const vector<string>& input) {
    assert(input.size() == 1);
    const auto& splits = aoc::split(input[0], ",");
    vector<Vector3D> path;
    transform(splits.begin(), splits.end(), back_inserter(path),
              [](const string s) { return HEADING.at(s); });
    return path;
}

vector<Position3D> positions(const vector<Vector3D>& path) {
    vector<Position3D> positions = {ORIGIN};
    for (const Vector3D& v : path) {
        positions.push_back(positions.back().translate(v));
    }
    return positions;
}

inline int steps(const Position3D& p) {
    return p.manhattanDistance(ORIGIN) / 2;
}

int part1(const vector<string>& input) {
    const vector<Vector3D>& path = parse(input);
    return steps(positions(path).back());
}

int part2(const vector<string>& input) {
    const vector<Vector3D>& path = parse(input);
    int ans = -INT_MAX;
    for (const Position3D& p : positions(path)) {
        ans = max(ans, steps(p));
    }
    return ans;
}

void samples() {
    assert(part1({"ne,ne,ne"}) == 3);
    assert(part1({"ne,ne,sw,sw"}) == 0);
    assert(part1({"ne,ne,s,s"}) == 2);
    assert(part1({"se,sw,se,sw,sw"}) == 3);
}

MAIN(2017, 11)
