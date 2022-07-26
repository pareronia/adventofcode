#include <assert.h>
#include <iostream>
#include <string>
#include <vector>
#include <bits/stdc++.h>
#include "../../aoc/aoc.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;

const vector<string> TEST1 = {
    "5 1 9 5",
    "7 5 3",
    "2 4 6 8"
};
const vector<string> TEST2 = {
    "5 9 2 8",
    "9 4 7 3",
    "3 8 6 5"
};

vector<vector<int>> parse(const vector<string> &lines) {
    vector<vector<int>> ans;
    for (const string &line : lines) {
        ans.push_back(aoc::getNumbers(line));
    }
    return ans;
}

int differenceHighestLowest(const vector<int> &line) {
    auto ans = minmax_element(line.begin(), line.end());
    return *ans.second - *ans.first;
}

int evenlyDivisibleQuotient(const vector<int> &line) {
    for (const int n1 : line) {
        for (const int n2 : line) {
            if (n1 == n2) {
                continue;
            }
            if (n1 > n2) {
                if (n1 % n2 == 0) {
                    return n1 / n2;
                }
            } else if (n2 % n1 == 0) {
                return n2 / n1;
            }
        }
    }
    throw "Illegal state";
}

template<typename Strategy>
int solve(const Strategy &strategy, const vector<string> &input) {
    int ans = 0;
    for (const vector<int> &line : parse(input)) {
        ans += strategy(line);
    }
    return ans;
}

int part1(const vector<string> &input) {
    return solve(differenceHighestLowest, input);
}

int part2(const vector<string> &input) {
    return solve(evenlyDivisibleQuotient, input);
}

void samples() {
    assert(part1(TEST1) == 18);
    assert(part2(TEST2) == 9);
}

MAIN(2017, 2)
