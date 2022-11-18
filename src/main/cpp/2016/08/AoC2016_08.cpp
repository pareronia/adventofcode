#include <assert.h>

#include <string>
#include <unordered_set>
#include <vector>

#include "../../aoc/aoc.hpp"
#include "../../aoc/grid/grid.hpp"
#include "../../aoc/ocr/ocr.hpp"

using namespace std;

const char ON = '#';
const char OFF = '.';

Grid<char> solve(const vector<string>& input, const uint rows,
                 const uint cols) {
    vector<string> lines;
    for (uint rr = 0; rr < rows; rr++) {
        lines.push_back(string(cols, OFF));
    }
    Grid<char> grid = Grid<char>::from(lines);
    for (const string& line : input) {
        const string rect = "rect ";
        const string rotate_row = "rotate row ";
        const string rotate_col = "rotate column ";
        if (line.find(rect, 0) == 0) {
            const vector<string>& coords =
                aoc::split(line.substr(rect.size()), "x");
            unordered_set<Cell> cells;
            for (uint rr : aoc::Range::range(stoi(coords[1]))) {
                for (uint cc : aoc::Range::range(stoi(coords[0]))) {
                    cells.insert(Cell::at(rr, cc));
                }
            }
            grid = grid.update(cells, ON);
        } else if (line.find(rotate_row, 0) == 0) {
            const vector<string>& coords =
                aoc::split(line.substr(rotate_row.size()), " by ");
            const int rr = stoi(coords[0].substr(string("y=").size()));
            const int amount = stoi(coords[1]);
            grid = grid.rollRow(rr, amount);
        } else if (line.find(rotate_col, 0) == 0) {
            const vector<string>& coords =
                aoc::split(line.substr(rotate_col.size()), " by ");
            const int cc = stoi(coords[0].substr(string("x=").size()));
            const int amount = stoi(coords[1]);
            grid = grid.rollColumn(cc, amount);
        } else {
            throw "Invalid input";
        }
    }
    return grid;
}

string part1(const vector<string>& input) {
    return to_string(solve(input, 6, 50).countAllEqualTo(ON));
}

string part2(const vector<string>& input) {
    const Grid<char>& grid = solve(input, 6, 50);
    DEBUG(grid.replace(OFF, ' ').toString());
    return ocr::convert6(grid, ON, OFF);
}

// clang-format off
const vector<string> TEST = {
  "rect 3x2",
  "rotate column x=1 by 1",
  "rotate row y=0 by 4",
  "rotate column x=1 by 1"
};
// clang-format on

void samples() { assert(solve(TEST, 3, 7).countAllEqualTo(ON) == 6); }

SMAIN(2016, 8)
