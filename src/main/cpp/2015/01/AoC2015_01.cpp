#include <assert.h>

#include <algorithm>
#include <string>
#include <vector>

#include "../../aoc/aoc.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;

int part1(const vector<string>& input) {
    const string& s = input[0];
    return s.size() - 2 * count(s.begin(), s.end(), ')');
}

int part2(const vector<string>& input) {
    int sum = 0;
    for (int i : aoc::Range::range(input[0].size())) {
        sum += (input[0][i] == '(' ? 1 : -1);
        if (sum == -1) {
            return i + 1;
        }
    }
    throw "unsolvable";
}

const vector<string> TEST1 = {"(())"};
const vector<string> TEST2 = {"()()"};
const vector<string> TEST3 = {"((("};
const vector<string> TEST4 = {"(()(()("};
const vector<string> TEST5 = {"))((((("};
const vector<string> TEST6 = {"())"};
const vector<string> TEST7 = {"))("};
const vector<string> TEST8 = {")))"};
const vector<string> TEST9 = {")())())"};
const vector<string> TEST10 = {")"};
const vector<string> TEST11 = {"()())"};

void samples() {
    assert(part1(TEST1) == 0);
    assert(part1(TEST2) == 0);
    assert(part1(TEST3) == 3);
    assert(part1(TEST4) == 3);
    assert(part1(TEST5) == 3);
    assert(part1(TEST6) == -1);
    assert(part1(TEST7) == -1);
    assert(part1(TEST8) == -3);
    assert(part1(TEST9) == -3);
    assert(part2(TEST10) == 1);
    assert(part2(TEST11) == 5);
}

MAIN(2015, 1)
