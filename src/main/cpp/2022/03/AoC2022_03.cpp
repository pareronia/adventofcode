#include <assert.h>

#include <algorithm>
#include <iterator>
#include <set>
#include <string>
#include <vector>

#include "../../aoc/aoc.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;

int priority(const char& ch) {
    if ('a' <= ch && ch <= 'z') {
        return ch - 'a' + 1;
    } else {
        return ch - 'A' + 27;
    }
}

set<char> to_set(const string& s) { return set<char>(s.begin(), s.end()); }

set<char> intersection(const set<char>& s1, const set<char>& s2) {
    set<char> ans;
    set_intersection(s1.begin(), s1.end(), s2.begin(), s2.end(),
                     inserter(ans, ans.begin()));
    return ans;
}

int part1(const vector<string>& input) {
    int ans = 0;
    for (int i : aoc::Range::range(input.size())) {
        const string& line = input[i];
        const int cutoff = line.size() / 2;
        const set<char>& s1 = to_set(line.substr(0, cutoff));
        const set<char>& s2 = to_set(line.substr(cutoff));
        ans += priority(*intersection(s1, s2).begin());
    }
    return ans;
}

int part2(const vector<string>& input) {
    int ans = 0;
    for (int i : aoc::Range::range(0, input.size(), 3)) {
        const set<char>& s1 = to_set(input[i]);
        const set<char>& s2 = to_set(input[i + 1]);
        const set<char>& s3 = to_set(input[i + 2]);
        ans += priority(*intersection(intersection(s1, s2), s3).begin());
    }
    return ans;
}

// clang-format off
const vector<string> TEST = {
"vJrwpWtwJgWrhcsFMMfFFhFp",
"jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL",
"PmmdzqPrVvPwwTWBwg",
"wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn",
"ttgJtRGJQctTZtZT",
"CrZsJsPPZsGzwwsLwLmpwMDw"
};
// clang-format on

void samples() {
    assert(part1(TEST) == 157);
    assert(part2(TEST) == 70);
}

MAIN(2022, 3)
