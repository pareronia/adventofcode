#include <bits/stdc++.h>
#include "../../aoc/aoc.hpp"
#include "../../aoc/game_of_life/game_of_life.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;

const char ON = '#';
const uint GENERATIONS = 6;

GameOfLife parse(const vector<string>& input, const uint dim) {
    set<Cell> alive;
    for (uint i = 0; i < input.size(); i++) {
        const string& line = input[i];
        for (uint j = 0; j < line.size(); j++) {
            if (line[j] == ON) {
                alive.insert(Cell(i, j, 0, 0));
            }
        }
    }
    return GameOfLife(alive, new InfiniteGrid(dim), new ClassicRules());
}

long part1(const vector<string>& input) {
    GameOfLife gol = parse(input, 3);
    for (uint i = 0; i < GENERATIONS; i++) {
        gol = gol.next();
    }
    return gol.getAlive().size();
}

int part2(const vector<string>& input) {
    GameOfLife gol = parse(input, 4);
    for (uint i = 0; i < GENERATIONS; i++) {
        gol = gol.next();
    }
    return gol.getAlive().size();
}

const vector<string> TEST = {
    ".#.",
    "..#",
    "###"
};

void samples() {
    assert(part1(TEST) == 112);
    assert(part2(TEST) == 848);
}

MAIN(2020, 17)
