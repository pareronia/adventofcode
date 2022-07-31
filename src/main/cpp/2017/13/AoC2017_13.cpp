#include <bits/stdc++.h>
#include "../../aoc/aoc.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;

struct Layer {
    int depth;
    int range;

    Layer(int d, int r) {
        depth = d;
        range = r;
    }
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

vector<Layer> parse(const vector<string>& input) {
    vector<Layer> ans;
    for (const auto& line : input) {
        const auto& splits = split(line, ": ");
        ans.push_back(Layer(stoi(splits[0]), stoi(splits[1])));
    }
    return ans;
}

bool caught(const Layer& layer, const uint delay) {
    return (delay + layer.depth) % ((layer.range - 1) * 2) == 0;
}

int part1(const vector<string>& input) {
    uint ans = 0;
    for (const auto& layer : parse(input)) {
        if (caught(layer, 0)) {
            ans += layer.depth * layer.range;
        }
    }
    return ans;
}

int part2(const vector<string>& input) {
    const auto& layers = parse(input);
    uint delay = 0;
    while (any_of(begin(layers), end(layers),
                  [delay](const auto& lyr) { return caught(lyr, delay); })) {
        ++delay;
    }
    return delay;
}

const vector<string> TEST = {
    "0: 3",
    "1: 2",
    "4: 4",
    "6: 4"
};

void samples() {
    assert(part1(TEST) == 24);
    assert(part2(TEST) == 10);
}

MAIN(2017, 13)
