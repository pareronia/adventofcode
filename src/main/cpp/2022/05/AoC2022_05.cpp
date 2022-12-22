#include <assert.h>

#include <deque>
#include <string>
#include <vector>

#include "../../aoc/aoc.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;
using Move = tuple<int, int, int>;

enum CrateMover { CM_9000, CM_9001 };

pair<vector<deque<char>>, vector<vector<int>>> parse(
    const vector<string>& input) {
    const auto& blocks = aoc::toBlocks(input);
    const auto& splits = aoc::split(blocks[0].back());
    int size = stoi(splits.back());
    vector<deque<char>> stacks(size, deque<char>());
    for (int i : aoc::Range::rangeClosed(blocks[0].size() - 2, 0, -1)) {
        const string& line = blocks[0][i];
        for (int j : aoc::Range::range(line.size())) {
            if (j % 4 != 1 || line[j] == ' ') {
                continue;
            }
            stacks[j / 4].push_back(line[j]);
        }
    }
    vector<vector<int>> moves;
    for (const string& line : blocks[1]) {
        moves.push_back(aoc::getNumbers(line));
    }
    return make_pair(stacks, moves);
}

string simulate(pair<vector<deque<char>>, vector<vector<int>>> procedure,
                const CrateMover crateMover) {
    auto& stacks = procedure.first;
    const auto& moves = procedure.second;
    for (const vector<int>& _move : moves) {
        const int amount = _move[0];
        const int from = _move[1] - 1;
        const int to = _move[2] - 1;
        deque<char> tmp;
        for ([[maybe_unused]] int i : aoc::Range::range(amount)) {
            const char crate = stacks[from].back();
            stacks[from].pop_back();
            if (crateMover == CM_9000) {
                tmp.push_back(crate);
            } else {
                tmp.push_front(crate);
            }
        }
        for (const char crate : tmp) {
            stacks[to].push_back(crate);
        }
    }
    vector<char> tops;
    for (const deque<char>& stack : stacks) {
        tops.push_back(stack.back());
    }
    return string(tops.begin(), tops.end());
}

string part1(const vector<string>& input) {
    return simulate(parse(input), CM_9000);
}

string part2(const vector<string>& input) {
    return simulate(parse(input), CM_9001);
}

// clang-format off
const vector<string> TEST = {
"    [D]    ",
"[N] [C]    ",
"[Z] [M] [P]",
" 1   2   3 ",
"",
"move 1 from 2 to 1",
"move 3 from 1 to 3",
"move 2 from 2 to 1",
"move 1 from 1 to 2"
};
// clang-format on

void samples() {
    assert(part1(TEST) == "CMZ");
    assert(part2(TEST) == "MCD");
}

SMAIN(2022, 5)
