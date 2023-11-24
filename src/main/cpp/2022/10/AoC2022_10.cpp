#include <assert.h>

#include <numeric>
#include <string>
#include <vector>

#include "../../aoc/aoc.hpp"
#include "../../aoc/grid/grid.hpp"
#include "../../aoc/ocr/ocr.hpp"

using namespace std;

const char FILL = '#';
const char EMPTY = ' ';

vector<pair<int, int>> program(const vector<string>& input) {
    int x = 1;
    int cycles = 0;
    vector<pair<int, int>> v;
    for (const string& line : input) {
        const auto splits = aoc::split(line);
        if (splits[0] == "noop") {
            v.push_back(make_pair(cycles, x));
            cycles++;
        } else if (splits[0] == "addx") {
            v.push_back(make_pair(cycles, x));
            cycles++;
            v.push_back(make_pair(cycles, x));
            cycles++;
            x += stoi(splits[1]);
        }
    }
    return v;
}

inline int check(const int cycles, const int x) {
    return cycles % 40 == 20 ? x * cycles : 0;
}

inline char draw(const int cycles, const int x) {
    return abs(x - cycles % 40) <= 1 ? FILL : EMPTY;
}

vector<string> getPixels(const vector<string>& input) {
    const auto& p = program(input);
    vector<char> pixels;
    for (const pair<int, int>& state : p) {
        pixels.push_back(draw(state.first, state.second));
    }
    vector<string> grid;
    for (int i : aoc::Range::range(0, 240, 40)) {
        grid.push_back(string(pixels.begin() + i, pixels.begin() + i + 40));
    }
    return grid;
}

string part1(const vector<string>& input) {
    const auto& p = program(input);
    int ans = accumulate(p.begin(), p.end(), 0,
                         [](const int a, const pair<int, int>& p) {
                             return a + check(p.first + 1, p.second);
                         });
    return to_string(ans);
}

string part2(const vector<string>& input) {
    const Grid<char> drawing = Grid<char>::from(getPixels(input));
    DEBUG(drawing.toString());
    return ocr::convert6(drawing, FILL, EMPTY);
}

// clang-format off
const vector<string> TEST = {
    "addx 15",
    "addx -11",
    "addx 6",
    "addx -3",
    "addx 5",
    "addx -1",
    "addx -8",
    "addx 13",
    "addx 4",
    "noop",
    "addx -1",
    "addx 5",
    "addx -1",
    "addx 5",
    "addx -1",
    "addx 5",
    "addx -1",
    "addx 5",
    "addx -1",
    "addx -35",
    "addx 1",
    "addx 24",
    "addx -19",
    "addx 1",
    "addx 16",
    "addx -11",
    "noop",
    "noop",
    "addx 21",
    "addx -15",
    "noop",
    "noop",
    "addx -3",
    "addx 9",
    "addx 1",
    "addx -3",
    "addx 8",
    "addx 1",
    "addx 5",
    "noop",
    "noop",
    "noop",
    "noop",
    "noop",
    "addx -36",
    "noop",
    "addx 1",
    "addx 7",
    "noop",
    "noop",
    "noop",
    "addx 2",
    "addx 6",
    "noop",
    "noop",
    "noop",
    "noop",
    "noop",
    "addx 1",
    "noop",
    "noop",
    "addx 7",
    "addx 1",
    "noop",
    "addx -13",
    "addx 13",
    "addx 7",
    "noop",
    "addx 1",
    "addx -33",
    "noop",
    "noop",
    "noop",
    "addx 2",
    "noop",
    "noop",
    "noop",
    "addx 8",
    "noop",
    "addx -1",
    "addx 2",
    "addx 1",
    "noop",
    "addx 17",
    "addx -9",
    "addx 1",
    "addx 1",
    "addx -3",
    "addx 11",
    "noop",
    "noop",
    "addx 1",
    "noop",
    "addx 1",
    "noop",
    "noop",
    "addx -13",
    "addx -19",
    "addx 1",
    "addx 3",
    "addx 26",
    "addx -30",
    "addx 12",
    "addx -1",
    "addx 3",
    "addx 1",
    "noop",
    "noop",
    "noop",
    "addx -9",
    "addx 18",
    "addx 1",
    "addx 2",
    "noop",
    "noop",
    "addx 9",
    "noop",
    "noop",
    "noop",
    "addx -1",
    "addx 2",
    "addx -37",
    "addx 1",
    "addx 3",
    "noop",
    "addx 15",
    "addx -21",
    "addx 22",
    "addx -6",
    "addx 1",
    "noop",
    "addx 2",
    "addx 1",
    "noop",
    "addx -10",
    "noop",
    "noop",
    "addx 20",
    "addx 1",
    "addx 2",
    "addx 2",
    "addx -6",
    "addx -11",
    "noop",
    "noop",
    "noop",
};
const vector<string> RESULT =  {
    "##  ##  ##  ##  ##  ##  ##  ##  ##  ##  ",
    "###   ###   ###   ###   ###   ###   ### ",
    "####    ####    ####    ####    ####    ",
    "#####     #####     #####     #####     ",
    "######      ######      ######      ####",
    "#######       #######       #######     ",
};
// clang-format on

void samples() {
    assert(part1(TEST) == "13140");
    assert(getPixels(TEST) == RESULT);
}

SMAIN(2022, 10)
