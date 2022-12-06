#include <assert.h>

#include <string>
#include <unordered_set>
#include <vector>

#include "../../aoc/aoc.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;
using namespace aoc;

int solve(const string& buffer, const size_t size) {
    for (int i : aoc::Range::range(size, buffer.size())) {
        const string& sub = buffer.substr(i - size, size);
        const unordered_set<char> s(sub.begin(), sub.end());
        if (s.size() == size) {
            return i;
        }
    };
    throw "unsolvable";
}

int part1(const vector<string>& input) { return solve(input[0], 4); }

int part2(const vector<string>& input) { return solve(input[0], 14); }

// clang-format off
const vector<string> TEST1 = {"mjqjpqmgbljsphdztnvjfqwrcgsmlb"};
const vector<string> TEST2 = {"bvwbjplbgvbhsrlpgdmjqwftvncz"};
const vector<string> TEST3 = {"nppdvjthqldpwncqszvftbrmjlhg"};
const vector<string> TEST4 = {"nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg"};
const vector<string> TEST5 = {"zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw"};
// clang-format on

void samples() {
    assert(part1(TEST1) == 7);
    assert(part1(TEST2) == 5);
    assert(part1(TEST3) == 6);
    assert(part1(TEST4) == 10);
    assert(part1(TEST5) == 11);
    assert(part2(TEST1) == 19);
    assert(part2(TEST2) == 23);
    assert(part2(TEST3) == 23);
    assert(part2(TEST4) == 29);
    assert(part2(TEST5) == 26);
}

MAIN(2022, 6)
