#include "assert.h"
#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
#include "../../aocd/aocd.hpp"

using namespace std;

const vector<string> TEST = {
    "NNCB",
    "",
    "CH -> B",
    "HH -> N",
    "CB -> H",
    "NH -> C",
    "HB -> C",
    "HC -> B",
    "HN -> C",
    "NN -> C",
    "BH -> H",
    "NC -> B",
    "NB -> B",
    "BN -> B",
    "BB -> N",
    "BC -> B",
    "CC -> N",
    "CN -> C"
};

int64_t solve(const vector<string> &input, const int cycles) {
    const string start = input[0];
    map<pair<char, char>, char> rules;
    for (unsigned int i = 2; i < input.size(); i++) {
        rules[make_pair(input[i][0], input[i][1])] = input[i].back();
    }

    map<pair<char, char>, int64_t> pairCounters;
    map<char, int64_t> elemCounters;
    for (unsigned int i = 0; i < start.size(); i++) {
        ++elemCounters[start[i]];
        if (i == 0) {
            continue;
        }
        ++pairCounters[make_pair(start[i - 1], start[i])];
    }

    for (int i = 0; i < cycles; i++) {
        map<pair<char, char>, int64_t> pairCounters2;
        for (auto const [pair, count] : pairCounters) {
            const char elem = rules[pair];
            elemCounters[elem] += count;
            pairCounters2[make_pair(pair.first, elem)] += count;
            pairCounters2[make_pair(elem, pair.second)] += count;
        }
        pairCounters = pairCounters2;
    }

    auto [min_elem, max_elem] = minmax_element(
            elemCounters.begin(), elemCounters.end(),
            [](const auto l, const auto r) {
                return l.second < r.second;
            });
    return max_elem->second - min_elem->second;
}

int64_t part1(const vector<string> &input) {
    return solve(input, 10);
}

int64_t part2(const vector<string> &input) {
    return solve(input, 40);
}

int main() {
    assert(part1(TEST) == 1588);
    assert(part2(TEST) == 2188189693529);

    const vector<string> input = aocd::puzzle::getInputData(2021, 14);
    cout << "Part 1: " << part1(input) << endl;
    cout << "Part 2: " << part2(input) << endl;

    return 0;
}
