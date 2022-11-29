#include <assert.h>

#include <regex>
#include <string>
#include <vector>

#include "../../aoc/aoc.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;

const string VOWEL = "(a|e|i|o|u)";
const string TWIN = "([a-z])\\1";
const string BAD_PAIR = "(ab|cd|pq|xy)";
const string TWO_TWINS = "([a-z]{2})[a-z]*\\1";
const string THREE_LETTER_PALINDROME = "([a-z])[a-z]\\1";

int countMatches(const string& pattern, const string& s) {
    const regex re(pattern);
    auto begin = sregex_iterator(s.begin(), s.end(), re);
    auto end = sregex_iterator();
    return distance(begin, end);
}

int part1(const vector<string>& input) {
    return count_if(input.begin(), input.end(), [](const string& line) {
        return countMatches(VOWEL, line) >= 3 &&
               countMatches(TWIN, line) >= 1 &&
               countMatches(BAD_PAIR, line) == 0;
    });
}

int part2(const vector<string>& input) {
    return count_if(input.begin(), input.end(), [](const string& line) {
        return countMatches(TWO_TWINS, line) >= 1 &&
               countMatches(THREE_LETTER_PALINDROME, line) >= 1;
    });
}

const vector<string> TEST1 = {"ugknbfddgicrmopn"};
const vector<string> TEST2 = {"aaa"};
const vector<string> TEST3 = {"jchzalrnumimnmhp"};
const vector<string> TEST4 = {"haegwjzuvuyypxyu"};
const vector<string> TEST5 = {"dvszwmarrgswjxmb"};
const vector<string> TEST6 = {"qjhvhtzxzqqjkmpb"};
const vector<string> TEST7 = {"xxyxx"};
const vector<string> TEST8 = {"uurcxstgmygtbstg"};
const vector<string> TEST9 = {"ieodomkazucvgmuy"};
const vector<string> TEST10 = {"xyxy"};

void samples() {
    assert(part1(TEST1) == 1);
    assert(part1(TEST2) == 1);
    assert(part1(TEST3) == 0);
    assert(part1(TEST4) == 0);
    assert(part1(TEST5) == 0);
    assert(part2(TEST6) == 1);
    assert(part2(TEST7) == 1);
    assert(part2(TEST8) == 0);
    assert(part2(TEST9) == 0);
    assert(part2(TEST10) == 1);
}

MAIN(2015, 5)
