#include <bits/stdc++.h>
#include "../../aoc/aoc.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;

const char ESCAPE = '!';
const char OPEN_GROUP = '{';
const char CLOSE_GROUP = '}';
const char OPEN_GARBAGE = '<';
const char CLOSE_GARBAGE = '>';

pair<int, int> solve(const vector<string>& input) {
    assert(input.size() == 1);
    int open = 0;
    int cnt = 0;
    vector<int> scores;
    bool inGarbage = false;
    bool escaped = false;
    char prev = ' ';
    for (const char c : input[0]) {
        if (prev == ESCAPE) {
            escaped = true;
        }
        if (!escaped && !inGarbage && c == OPEN_GROUP) {
            open++;
        } else if (!escaped && !inGarbage && c == CLOSE_GROUP) {
            scores.push_back(open);
            open--;
        } else if (!escaped && c == OPEN_GARBAGE && !inGarbage) {
            inGarbage = true;
        } else if (!escaped && c == CLOSE_GARBAGE && inGarbage) {
            inGarbage = false;
        } else if (!escaped && inGarbage && c != ESCAPE) {
            cnt++;
        }
        if (escaped && c == ESCAPE) {
            prev = ' ';
        } else {
            prev = c;
        }
        escaped = false;
    }
    const int totalScore = accumulate(scores.begin(), scores.end(), 0);
    return make_pair(totalScore, cnt);
}

int part1(const vector<string>& input) {
    return solve(input).first;
}

int part2(const vector<string>& input) {
    return solve(input).second;
}

void samples() {
    assert(part1({"{}"}) == 1);
    assert(part1({"{{{}}}"}) == 6);
    assert(part1({"{{},{}}"}) == 5);
    assert(part1({"{{{},{},{{}}}}"}) == 16);
    assert(part1({"{<a>,<a>,<a>,<a>}"}) == 1);
    assert(part1({"{{<ab>},{<ab>},{<ab>},{<ab>}}"}) == 9);
    assert(part1({"{{<!!>},{<!!>},{<!!>},{<!!>}}"}) == 9);
    assert(part1({"{{<a!>},{<a!>},{<a!>},{<ab>}}"}) == 3);
    assert(part2({"<>"}) == 0);
    assert(part2({"<random characters>"}) == 17);
    assert(part2({"<{!>}>"}) == 2);
    assert(part2({"<!!>"}) == 0);
    assert(part2({"<!!!>>"}) == 0);
    assert(part2({"<{o\"i!a,<{i<a>"}) == 10);
}

MAIN(2017, 9)
