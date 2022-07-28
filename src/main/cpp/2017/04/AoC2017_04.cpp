#include <bits/stdc++.h>
#include "../../aoc/aoc.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;

const vector<string> TEST1 = { "aa bb cc dd ee" };
const vector<string> TEST2 = { "aa bb cc dd aa" };
const vector<string> TEST3 = { "aa bb cc dd aaa" };
const vector<string> TEST4 = { "abcde fghij" };
const vector<string> TEST5 = { "abcde xyz ecdab" };
const vector<string> TEST6 = { "a ab abc abd abf abj" };
const vector<string> TEST7 = { "iiii oiii ooii oooi oooo" };
const vector<string> TEST8 = { "oiii ioii iioi iiio" };

vector<string> split(const string& s) {
    istringstream iss(s);
    return {istream_iterator<string>{iss}, istream_iterator<string>{}};
}

bool hasNoDuplicateWords(const string &s) {
    map<string, uint> m;
    for (const string &token : split(s)) {
        m[token]++;
    }
    return count_if(m.begin(), m.end(),
                    [](pair<string, int> pair) { return pair.second > 1; })
            == 0;
}

bool hasNoAnagrams(const string &s) {
    set<map<char, uint>> letterCounts;
    uint wordCount = 0;
    for (const string &token : split(s)) {
        wordCount++;
        map<char, uint> m;
        for (auto i = token.begin(); i != token.end(); ++i) {
            m[*i]++;
        }
        letterCounts.insert(m);
    }
    return letterCounts.size() == wordCount;
}

template<typename Strategy>
int solve(const Strategy &strategy, const vector<string> &input) {
    return count_if(input.begin(), input.end(), strategy);
}

int part1(const vector<string> &input) {
    return solve(hasNoDuplicateWords, input);
}

int part2(const vector<string> &input) {
    return solve(hasNoAnagrams, input);
}

void samples() {
    assert(part1(TEST1) == 1);
    assert(part1(TEST2) == 0);
    assert(part1(TEST3) == 1);
    assert(part2(TEST4) == 1);
    assert(part2(TEST5) == 0);
    assert(part2(TEST6) == 1);
    assert(part2(TEST7) == 1);
    assert(part2(TEST8) == 0);
}

MAIN(2017, 4)
