#include <assert.h>
#include <iostream>
#include <string>
#include <vector>
#include "../../aoc/aoc.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;

const vector<string> TEST1 = { "1122" };
const vector<string> TEST2 = { "1111" };
const vector<string> TEST3 = { "1234" };
const vector<string> TEST4 = { "91212129" };
const vector<string> TEST5 = { "1212" };
const vector<string> TEST6 = { "1221" };
const vector<string> TEST7 = { "123425" };
const vector<string> TEST8 = { "123123" };
const vector<string> TEST9 = { "12131415" };

int sumSameCharsAt(const string input, const int distance) {
    const string test = input + input.substr(0, distance);
    unsigned ans = 0;
    for (unsigned i = 0; i < input.size(); i++) {
        if (test[i] == test[i + distance]) {
            ans += stoi(test.substr(i, 1));
        }
    }
    return ans;
}

int part1(const vector<string> &input) {
    return sumSameCharsAt(input[0], 1);
}

int part2(const vector<string> &input) {
    return sumSameCharsAt(input[0], input[0].size() / 2);
}

void samples() {
    assert(part1(TEST1) == 3);
    assert(part1(TEST2) == 4);
    assert(part1(TEST3) == 0);
    assert(part1(TEST4) == 9);
    assert(part2(TEST5) == 6);
    assert(part2(TEST6) == 0);
    assert(part2(TEST7) == 4);
    assert(part2(TEST8) == 12);
    assert(part2(TEST9) == 4);
}

MAIN(2017, 1)
