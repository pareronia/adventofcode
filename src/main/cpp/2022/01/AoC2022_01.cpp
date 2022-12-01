#include <assert.h>

#include <algorithm>
#include <numeric>
#include <string>
#include <vector>

#include "../../aoc/aoc.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;

int solve(const vector<string>& input, const int count) {
    vector<int> sums;
    for (const vector<string>& block : aoc::toBlocks(input)) {
        const int sum = accumulate(
            block.begin(), block.end(), 0,
            [](const int a, const string& line) { return a + stoi(line); });
        sums.push_back(sum);
    }
    sort(sums.rbegin(), sums.rend());
    return accumulate(sums.begin(), sums.begin() + count, 0,
                      [](const int a, const int b) { return a + b; });
}

int part1(const vector<string>& input) { return solve(input, 1); }

int part2(const vector<string>& input) { return solve(input, 3); }

// clang-format off
const vector<string> TEST = {
"1000",
"2000",
"3000",
"",
"4000",
"",
"5000",
"6000",
"",
"7000",
"8000",
"9000",
"",
"10000"
};
// clang-format on

void samples() {
    assert(part1(TEST) == 24000);
    assert(part2(TEST) == 45000);
}

MAIN(2022, 1)
