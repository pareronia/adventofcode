#include <bits/stdc++.h>
#include "../../aoc/aoc.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;

vector<int> parse(const vector<string>& input) {
    assert(input.size() == 1);
    return aoc::getNumbers(input[0]);
}

long solve(const vector<string>& input, const uint days) {
    deque<long> fishies(9);
    fill(fishies.begin(), fishies.end(), 0L);
    for (uint i : parse(input)) {
        fishies[i]++;
    }
    for (uint i = 0; i < days; i++) {
        long zeroes = fishies.front();
        fishies.pop_front();
        fishies.emplace_back(zeroes);
        fishies[6] += zeroes;
    }
    return accumulate(fishies.begin(), fishies.end(), 0L);
}

long part1(const vector<string>& input) {
    return solve(input, 80);
}

long part2(const vector<string>& input) {
    return solve(input, 256);
}

const vector<string> TEST = {"3,4,3,1,2"};

void samples() {
    assert(part1(TEST) == 5934);
    assert(part2(TEST) == 26984457539L);
}

MAIN(2021, 6)
