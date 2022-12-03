#include <assert.h>

#include <numeric>
#include <string>
#include <unordered_map>
#include <vector>

#include "../../aoc/aoc.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;

const int WIN = 6;
const int DRAW = 3;
const int LOSS = 0;

int solve(const vector<string>& input,
          const unordered_map<pair<string, string>, int>& vals) {
    return accumulate(input.begin(), input.end(), 0,
                      [&vals](const int a, const string& line) {
                          auto splits = aoc::split(line);
                          return a + vals.at({splits[0], splits[1]});
                      });
}

int part1(const vector<string>& input) {
    // clang-format off
    const unordered_map<pair<string, string>, int> vals = {
        {{"A", "X"}, DRAW + 1},
        {{"A", "Y"}, WIN + 2},
        {{"A", "Z"}, LOSS + 3},
        {{"B", "X"}, LOSS + 1},
        {{"B", "Y"}, DRAW + 2},
        {{"B", "Z"}, WIN + 3},
        {{"C", "X"}, WIN + 1},
        {{"C", "Y"}, LOSS + 2},
        {{"C", "Z"}, DRAW + 3},
    };
    // clang-format on
    return solve(input, vals);
}

int part2(const vector<string>& input) {
    // clang-format off
    const unordered_map<pair<string, string>, int> vals = {
        {{"A", "X"}, LOSS + 3},
        {{"A", "Y"}, DRAW + 1},
        {{"A", "Z"}, WIN + 2},
        {{"B", "X"}, LOSS + 1},
        {{"B", "Y"}, DRAW + 2},
        {{"B", "Z"}, WIN + 3},
        {{"C", "X"}, LOSS + 2},
        {{"C", "Y"}, DRAW + 3},
        {{"C", "Z"}, WIN + 1},
    };
    // clang-format on
    return solve(input, vals);
}

// clang-format off
const vector<string> TEST = {
"A Y",
"B X",
"C Z",
};
// clang-format on

void samples() {
    assert(part1(TEST) == 15);
    assert(part2(TEST) == 12);
}

MAIN(2022, 2)
