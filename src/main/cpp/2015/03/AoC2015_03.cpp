#include <assert.h>

#include <string>
#include <unordered_set>
#include <vector>

#include "../../aoc/aoc.hpp"
#include "../../aoc/grid/grid.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;

Cell move(const Cell& curr, const char& ch) {
    if (ch == '^') {
        return Cell::at(curr.row() - 1, curr.col()); 
    } else if (ch == '>') {
        return Cell::at(curr.row(), curr.col() + 1); 
    } else if (ch == 'v') {
        return Cell::at(curr.row() + 1, curr.col()); 
    } else if (ch == '<') {
        return Cell::at(curr.row(), curr.col() - 1); 
    } else {
        throw "Invalid input";
    }
}

int part1(const vector<string>& input) {
    Cell curr = Cell::at(0, 0);
    unordered_set<Cell> visited({curr});
    for (const char& ch : input [0]) {
        curr = move(curr, ch);
        visited.insert(curr);
    }
    return visited.size();
}

int part2(const vector<string>& input) {
    Cell currS = Cell::at(0, 0);
    Cell currR = Cell::at(0, 0);
    unordered_set<Cell> visited({currS, currR});
    for (const int i : aoc::Range::range(input[0].size())) {
        const char& ch = input[0][i];
        if (i % 2== 0) {
            currS = move(currS, ch);
            visited.insert(currS);
        } else {
            currR = move(currR, ch);
            visited.insert(currR);
        }
    }
    return visited.size();
}

const vector<string> TEST1 = {">"};
const vector<string> TEST2 = {"^>v<"};
const vector<string> TEST3 = {"^v^v^v^v^v"};
const vector<string> TEST4 = {"^v"};

void samples() {
    assert(part1(TEST1) == 2);
    assert(part1(TEST2) == 4);
    assert(part1(TEST3) == 2);
    assert(part2(TEST4) == 3);
    assert(part2(TEST2) == 3);
    assert(part2(TEST3) == 11);
}

MAIN(2015, 3)
