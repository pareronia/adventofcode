#include <assert.h>

#include <algorithm>
#include <climits>
#include <functional>
#include <iterator>
#include <string>
#include <unordered_set>
#include <vector>

#include "../../aoc/aoc.hpp"
#include "../../aoc/grid/grid.hpp"
#include "../../aoc/ocr/ocr.hpp"

using namespace std;

const char FILL = '#';
const char EMPTY = ' ';

struct Position {
    Position(const int x_, const int y_) : x(x_), y(y_) {}
    Position operator=(const Position& other) const {
        return Position(other.x, other.y);
    }
    bool operator==(const Position& other) const {
        return x == other.x && y == other.y;
    }

    const int x;
    const int y;
};

template <>
struct std::hash<Position> {
    std::size_t operator()(const Position& p) const {
        return p.x + 31 * (p.y << 1);
    }
};

using Heading = Position;

const Heading WEST = Heading(-1, 0);
const Heading SOUTH = Heading(0, -1);

class Fold {
   public:
    Fold(const bool xAxis_, const int value_) : xAxis(xAxis_), value(value_) {}
    unordered_set<Position> applyTo(
        const unordered_set<Position>& positions) const;

   private:
    int amplitude(const Position& p, const function<int(Position)>& dim) const;
    unordered_set<Position> apply(const unordered_set<Position>& positions,
                                  const Heading& vector,
                                  const function<int(Position)>& dim) const;

    const bool xAxis;
    const int value;
};

struct Paper {
    Paper(const unordered_set<Position> positions_, const vector<Fold> folds_)
        : positions(positions_), folds(folds_) {}
    const unordered_set<Position> positions;
    const vector<Fold> folds;
};

int Fold::amplitude(const Position& p,
                    const function<int(Position)>& dim) const {
    const int value = dim(p);
    return value > this->value ? 2 * (value - this->value) : 0;
}

unordered_set<Position> Fold::apply(const unordered_set<Position>& positions,
                                    const Heading& vector,
                                    const function<int(Position)>& dim) const {
    unordered_set<Position> np;
    for (const Position& p : positions) {
        const int a = amplitude(p, dim);
        np.insert(Position(p.x + a * vector.x, p.y + a * vector.y));
    }
    return np;
}

unordered_set<Position> Fold::applyTo(
    const unordered_set<Position>& positions) const {
    if (this->xAxis) {
        return apply(positions, WEST, [](const Position& p) { return p.x; });
    } else {
        return apply(positions, SOUTH, [](const Position& p) { return p.y; });
    }
}

Paper parse(const vector<string>& input) {
    const vector<vector<string>> blocks = aoc::toBlocks(input);
    unordered_set<Position> positions;
    for (const string& line : blocks[0]) {
        const vector<string>& splits = aoc::split(line, ",");
        positions.insert(Position(stoi(splits[0]), stoi(splits[1])));
    }
    vector<Fold> folds;
    for (const string& line : blocks[1]) {
        const vector<string>& splits =
            aoc::split(line.substr(string("fold along ").size()), "=");
        folds.push_back(Fold(splits[0] == "x", stoi(splits[1])));
    }
    return Paper(positions, folds);
}

vector<string> draw(const unordered_set<Position>& positions, const char fill,
                    const char empty) {
    int maxX = -INT_MAX;
    int maxY = -INT_MAX;
    for (const Position& p : positions) {
        maxX = max(maxX, p.x);
        maxY = max(maxY, p.y);
    }
    vector<string> lines;
    for (const int y : aoc::Range::rangeClosed(maxY)) {
        vector<char> line;
        for (const int x : aoc::Range::range(maxX + 2)) {
            if (positions.find(Position(x, y)) != positions.end()) {
                line.push_back(fill);
            } else {
                line.push_back(empty);
            }
        }
        lines.push_back(string(line.begin(), line.end()));
    }
    return lines;
}

vector<string> solve2(const vector<string>& input) {
    const Paper& paper = parse(input);
    unordered_set<Position> positions = paper.positions;
    for (const Fold& fold : paper.folds) {
        positions = fold.applyTo(positions);
    }
    return draw(positions, FILL, EMPTY);
}

string part1(const vector<string>& input) {
    const Paper& paper = parse(input);
    return to_string(paper.folds[0].applyTo(paper.positions).size());
}

string part2(const vector<string>& input) {
    const Grid<char> drawing = Grid<char>::from(solve2(input));
    DEBUG(drawing.toString());
    return ocr::convert6(drawing, FILL, EMPTY);
}

// clang-format off
const vector<string> TEST = {
"6,10",
"0,14",
"9,10",
"0,3",
"10,4",
"4,11",
"6,0",
"6,12",
"4,1",
"0,13",
"10,12",
"3,4",
"3,0",
"8,4",
"1,10",
"2,14",
"8,10",
"9,0",
"",
"fold along y=7",
"fold along x=5"
};
const vector<string> RESULT =  {
"##### ",
"#   # ",
"#   # ",
"#   # ",
"##### "
};
// clang-format on

void samples() {
    assert(part1(TEST) == "17");
    assert(solve2(TEST) == RESULT);
}

SMAIN(2021, 13)
