#include <assert.h>
#include <iostream>
#include <string>
#include <vector>
#include "../../aoc/aoc.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;

const vector<string> TEST = {
    "199",
    "200",
    "208",
    "210",
    "200",
    "207",
    "240",
    "269",
    "260",
    "263"
};

int countIncreases(const vector<string> &input, const int window) {
    int ans = 0;
    for (unsigned i = window; i < input.size(); i++) {
        if (stoi(input[i]) > stoi(input[i - window])) {
            ans++;
        }
    }
    return ans;
}

int part1(const vector<string> &input) {
    return countIncreases(input, 1);
}

int part2(const vector<string> &input) {
    return countIncreases(input, 3);
}

void samples() {
    assert(part1(TEST) == 7);
    assert(part2(TEST) == 5);
}

MAIN(2021, 1)
