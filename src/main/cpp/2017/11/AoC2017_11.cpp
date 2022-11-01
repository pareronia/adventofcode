#include <bits/stdc++.h>
#include "../../aoc/aoc.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;
using Position3D = tuple<int, int, int>;
using Vector3D = tuple<int, int, int>;

#define Position3D(x, y, z) make_tuple(x, y, z)
#define Vector3D(x, y, z) make_tuple(x, y, z)

const Position3D ORIGIN = Position3D(0, 0, 0);
const unordered_map<string, Position3D> HEADING = {
    {"n",  Vector3D(  0, -1,  1)},
    {"ne", Vector3D(  1, -1,  0)},
    {"se", Vector3D(  1,  0, -1)},
    {"s",  Vector3D(  0,  1, -1)},
    {"sw", Vector3D( -1,  1,  0)},
    {"nw", Vector3D( -1,  0,  1)}
};

vector<string> split(const string& s, const string& delim) {
    vector<string> ans;
    uint start = 0;
    int end = s.find(delim);
    while (end != -1) {
        ans.push_back(s.substr(start, end - start));
        start = end + delim.size();
        end = s.find(delim, start);
    }
    ans.push_back(s.substr(start, end - start));
    return ans;
}

vector<Vector3D> parse(const vector<string>& input) {
    assert(input.size() == 1);
    const auto& splits = split(input[0], ",");
    vector<Vector3D> path;
    transform(splits.begin(), splits.end(), back_inserter(path),
            [](const string s) { return HEADING.at(s); });
    return path;
}

Position3D translate(const Position3D& position, const Vector3D& vector) {
    return Position3D(
        get<0>(position) + get<0>(vector),
        get<1>(position) + get<1>(vector),
        get<2>(position) + get<2>(vector)
    );
}

vector<Position3D> positions(const vector<Vector3D>& path) {
    vector<Position3D> positions = {ORIGIN};
    for (const Vector3D& v : path) {
        positions.push_back(translate(positions.back(), v));
    }
    return positions;
}

int steps(const Position3D& p) {
    return (abs(get<0>(p)) + abs(get<1>(p)) + abs(get<2>(p))) / 2;
}

int part1(const vector<string>& input) {
    const vector<Vector3D>& path = parse(input);
    return steps(positions(path).back());
}

int part2(const vector<string>& input) {
    const vector<Vector3D>& path = parse(input);
    vector<int> s;
    for (const Position3D& p : positions(path)) {
        s.push_back(steps(p));
    }
    return *max_element(s.begin(), s.end());
}

void samples() {
    assert(part1({ "ne,ne,ne" }) == 3);
    assert(part1({ "ne,ne,sw,sw" }) == 0);
    assert(part1({ "ne,ne,s,s" }) == 2);
    assert(part1({ "se,sw,se,sw,sw" }) == 3);
}

MAIN(2017, 11)
