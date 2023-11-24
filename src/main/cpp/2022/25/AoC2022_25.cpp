#include <assert.h>

#include <algorithm>
#include <cmath>
#include <string>
#include <unordered_map>
#include <vector>

#include "../../aoc/aoc.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;

const unordered_map<char, int> DECODE = {
    {'0', 0}, {'1', 1}, {'2', 2}, {'-', -1}, {'=', -2}};
const unordered_map<long, tuple<char, int>> ENCODE = {
    {0, {'0', 0}}, {1, {'1', 0}}, {2, {'2', 0}},
    {3, {'=', 1}}, {4, {'-', 1}}, {5, {'0', 1}}};

string part1(const vector<string>& input) {
    long total = 0;
    for (const string& line : input) {
        for (const int i : aoc::Range::range(0, line.size())) {
            const int coefficient = DECODE.at(line[i]);
            const int exponent = line.size() - 1 - i;
            total += coefficient * pow(5, exponent);
        }
    }
    string ans = "";
    char digit;
    int carry;
    while (total > 0) {
        tie(digit, carry) = ENCODE.at(total % 5);
        ans += digit;
        total = total / 5 + carry;
    }
    reverse(ans.begin(), ans.end());
    return ans;
}

string part2(const vector<string>& input) { return ""; }

// clang-format off
const vector<string> TEST = {
"1=-0-2",
"12111",
"2=0=",
"21",
"2=01",
"111",
"20012",
"112",
"1=-1=",
"1-12",
"12",
"1=",
"122"
};
const vector<string> TEST1 = {"1=11-2"};
const vector<string> TEST2 = {"1-0---0"};
const vector<string> TEST3 = {"1121-1110-1=0"};
// clang-format on

void samples() {
    assert(part1(TEST) == "2=-1=0");
    assert(part1(TEST1) == "1=11-2");
    assert(part1(TEST2) == "1-0---0");
    assert(part1(TEST3) == "1121-1110-1=0");
    assert(part2(TEST) == "");
}

SMAIN(2022, 25)
