#include <bits/stdc++.h>
#include "../../aoc/aoc.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;

template<typename Strategy>
int countJumps(const vector<string> &input, const Strategy &strategy) {
    vector<int> numbers;
    for (const string &s : input) {
        numbers.push_back(stoi(s));
    }
    uint i = 0, cnt = 0;
    while (i < numbers.size()) {
        const int jump = numbers[i];
        numbers[i] = strategy(jump);
        i += jump;
        cnt++;
    }
    return cnt;
}

int part1(const vector<string> &input) {
    return countJumps(input, [](int j) { return j + 1; });
}

int part2(const vector<string> &input) {
    return countJumps(input, [](int j) { return j >= 3 ? j - 1 : j + 1; });
}

const vector<string> TEST = { "0", "3", "0", "1", "-3" };

void samples() {
    assert(part1(TEST) == 5);
    assert(part2(TEST) == 10);
}

MAIN(2017, 5)
