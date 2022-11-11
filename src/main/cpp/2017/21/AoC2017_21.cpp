#include <bits/stdc++.h>
#include "../../aoc/aoc.hpp"
#include "../../aoc/grid/grid.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;

const Grid<char> START = Grid<char>::from({".#.", "..#", "###"});

struct Rule {
    Rule(const vector<Grid<char>>& from_, const Grid<char>& to_)
        : from(from_), to(to_) {};

    const vector<Grid<char>> from;
    const Grid<char> to;
};

Rule parseRule(const string& input) {
    const auto& splits = aoc::split(input, " => ");
    const Grid<char> from = Grid<char>::from(aoc::split(splits[0], "/"));
    const Grid<char> to = Grid<char>::from(aoc::split(splits[1], "/"));
    vector<Grid<char>> froms;
    transform(
            from.permutations_begin(), from.permutations_end(),
            back_inserter(froms),
            [](const Grid<char>& g) { return g; });
    return Rule(froms, to);
}

vector<Rule> parse(const vector<string>& input) {
    vector<Rule> rules;
    transform(input.begin(), input.end(),
            back_inserter(rules),
            [](const string& line) { return parseRule(line); });
    return rules;
}

Grid<char> enhance(const vector<Rule>& rules, const Grid<char>& grid) {
    for (const Rule& rule : rules) {
        if (find(rule.from.begin(), rule.from.end(), grid) == rule.from.end()) {
            continue;
        }
        return rule.to;
    }
    throw "No rule matched";
}

long solve(const vector<string>& input, const uint iterations) {
    const vector<Rule>& rules = parse(input);
    Grid<char> grid = START;
    for (uint i = 0; i < iterations; i++) {
        const uint size = grid.height();
        const vector<vector<Grid<char>>>& subGrids
            = grid.divide(size % 2 == 0 ? 2 : 3);
        vector<vector<Grid<char>>> enhanced;
        for (const vector<Grid<char>>& v : subGrids) {
            vector<Grid<char>> row;
            for (const Grid<char>& g : v) {
                row.push_back(enhance(rules, g));
            }
            enhanced.push_back(row);
        }
        grid = Grid<char>::merge(enhanced);
    }
    return grid.countAllEqualTo('#');
}

long part1(const vector<string>& input) {
    return solve(input, 5);
}

long part2(const vector<string>& input) {
    return solve(input, 18);
}

const vector<string> TEST = {
"../.# => ##./#../...",
".#./..#/### => #..#/..../..../#..#"
};

void samples() {
    assert(solve(TEST, 2) == 12);
}

MAIN(2017, 21)
