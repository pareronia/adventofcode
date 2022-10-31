#include <bits/stdc++.h>
#include "../../aoc/aoc.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;
using Quartet = tuple<int, int, int, int>;
using Cell = tuple<int, int>;

#define Quartet(x1, y1, x2, y2) make_tuple(x1, y1, x2, y2)
#define Cell(x, y) make_tuple(x, y)

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

vector<Quartet> parse(const vector<string>& input) {
    vector<Quartet> ans;
    for (const string& line : input) {
        const vector<string>& splits = split(line, " -> ");
        const vector<string>& splits1 = split(splits[0], ",");
        const vector<string>& splits2 = split(splits[1], ",");
        ans.push_back(Quartet(stoi(splits1[0]), stoi(splits1[1]),
                              stoi(splits2[0]), stoi(splits2[1])));
    }
    return ans;
}

struct ihash : unary_function<Cell, uint> {
    uint operator()(const Cell& cell) const {
        return 31 ^ get<0>(cell) ^ get<1>(cell);
    }
};

struct iequal_to : binary_function<Cell, Cell, bool> {
    bool operator()(const Cell& cell, const Cell& other) const {
        return get<0>(cell) == get<0>(other)
            && get<1>(cell) == get<1>(other);
    }
};

uint solve(const vector<string>& input, const bool diag) {
    unordered_map<Cell, uint, ihash, iequal_to> map;
    for (const Quartet& quartet: parse(input)) {
        int x1, y1, x2, y2;
        tie(x1, y1, x2, y2) = quartet;
        const int mx = x1 == x2 ? 0 : (x1 < x2 ? 1 : -1);
        const int my = y1 == y2 ? 0 : (y1 < y2 ? 1 : -1);
        if (!diag && mx != 0 && my != 0) {
            continue;
        }
        const uint len = max(abs(x1 - x2), abs(y1 - y2));
        for (uint i = 0; i <= len; i++) {
            map[Cell(x1 + mx * i, y1 + my * i)]++;
        }
    }
    return count_if(
            map.begin(), map.end(),
            [](const auto& item) { return item.second > 1; });
}

int part1(const vector<string>& input) {
    return solve(input, false);
}

int part2(const vector<string>& input) {
    return solve(input, true);
}

const vector<string> TEST = {
    "0,9 -> 5,9",
    "8,0 -> 0,8",
    "9,4 -> 3,4",
    "2,2 -> 2,1",
    "7,0 -> 7,4",
    "6,4 -> 2,0",
    "0,9 -> 2,9",
    "3,4 -> 1,4",
    "0,0 -> 8,8",
    "5,5 -> 8,2",
};

void samples() {
    assert(part1(TEST) == 5);
    assert(part2(TEST) == 12);
}

MAIN(2021, 5)
