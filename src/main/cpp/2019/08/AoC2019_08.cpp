#include <assert.h>

#include <algorithm>
#include <string>
#include <vector>

#include "../../aoc/aoc.hpp"
#include "../../aoc/grid/grid.hpp"
#include "../../aoc/ocr/ocr.hpp"

using namespace std;

const int WIDTH = 25;
const int HEIGHT = 6;
const char BLACK = '0';
const char WHITE = '1';
const char TRANSPARENT = '2';

Grid<char> createGrid(const string& input, const int width) {
    vector<string> lines;
    for (int i : aoc::Range::range(0, input.size(), width)) {
        lines.push_back(input.substr(i, width));
    }
    return Grid<char>::from(lines);
}

string getImage(const string& input, const int width, const int height) {
    const Grid<char>& grid = createGrid(input, width * height);
    vector<char> decoded;
    for (int col : grid.colIndices()) {
        const vector<char>& column = grid.getColumn(col);
        for (const char ch : column) {
            if (ch != '2') {
                decoded.push_back(ch);
                break;
            }
        }
    }
    return string(decoded.begin(), decoded.end());
}

inline int countMatches(const string& s, const char ch) {
    return count(s.begin(), s.end(), ch);
}

string part1(const vector<string>& input) {
    const Grid<char>& grid = createGrid(input[0], WIDTH * HEIGHT);
    vector<string> rows;
    for (int row : grid.rowIndices()) {
        const vector<char>& line = grid.getRow(row);
        rows.push_back(string(line.begin(), line.end()));
    }
    sort(rows.begin(), rows.end(), [](const string& s1, const string& s2) {
        return countMatches(s1, BLACK) < countMatches(s2, BLACK);
    });
    return to_string(countMatches(rows[0], WHITE) *
                     countMatches(rows[0], TRANSPARENT));
}

string part2(const vector<string>& input) {
    const Grid<char>& image =
        createGrid(getImage(input[0], WIDTH, HEIGHT), WIDTH);
    DEBUG(image.replace(BLACK, ' ').replace(WHITE, '#').toString());
    return ocr::convert6(image, WHITE, BLACK);
}

const vector<string> TEST = {"0222112222120000"};

void samples() { assert(getImage(TEST[0], 2, 2) == "0110"); }

SMAIN(2019, 8)
