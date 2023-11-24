#include <assert.h>

#include <string>
#include <unordered_map>
#include <utility>
#include <vector>

#include "../../aoc/aoc.hpp"
#include "../../aoc/geometry/geometry.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;
using Heading = pair<int, int>;

const Heading NW = Heading(-1, -1);
const Heading N = Heading(-1, 0);
const Heading NE = Heading(-1, 1);
const Heading W = Heading(0, -1);
const Heading E = Heading(0, 1);
const Heading SW = Heading(1, -1);
const Heading S = Heading(1, 0);
const Heading SE = Heading(1, 1);

const vector<Heading> OCTANTS = {NW, N, NE, W, E, SE, S, SW};

class HeadingsAndPeriods {
   private:
    const vector<Heading> directions = {E, N, W, S};
    vector<uint> periods = {1, 1, 2, 2};

   public:
    pair<Heading, uint> apply(uint t) {
        const int idx = t % 4;
        const uint period = periods[idx];
        periods[idx] = period + 2;
        const Heading heading = directions[idx];
        return make_pair(heading, period);
    }
};

class CoordinatesSupplier {
   private:
    int x = 0;
    int y = 0;
    uint j = 0;
    uint k = 0;
    HeadingsAndPeriods headingAndPeriods;
    pair<Heading, uint> headingAndPeriod = headingAndPeriods.apply(k);

   public:
    Position get() {
        if (j == headingAndPeriod.second) {
            k++;
            headingAndPeriod = headingAndPeriods.apply(k);
            j = 0;
        }
        x += headingAndPeriod.first.first;
        y += headingAndPeriod.first.second;
        j++;
        return Position(x, y);
    }
};

int part1(const vector<string>& input) {
    const uint n = stoi(input[0]);
    if (n == 1) {
        return 0;
    }
    CoordinatesSupplier coords;
    uint i = 1;
    while (i < n) {
        i++;
        const Position& position = coords.get();
        if (i == n) {
            return abs(position.x) + abs(position.y);
        }
    }
    throw "unsolvable";
}

int part2(const vector<string>& input) {
    const uint n = stoi(input[0]);
    if (n == 1) {
        return 1;
    }
    unordered_map<Position, uint> squares;
    squares[Position(0, 0)] = 1;
    CoordinatesSupplier coords;
    while (true) {
        const Position& position = coords.get();
        uint value = 0;
        for (const Heading& heading : OCTANTS) {
            const Position neighbour = Position(position.x + heading.first,
                                                position.y + heading.second);
            if (squares.find(neighbour) != squares.end()) {
                value += squares[neighbour];
            }
        }
        squares[position] = value;
        if (value > n) {
            return value;
        }
    }
}

const vector<string> TEST1 = {"1"};
const vector<string> TEST2 = {"12"};
const vector<string> TEST3 = {"23"};
const vector<string> TEST4 = {"1024"};
const vector<string> TEST5 = {"1"};
const vector<string> TEST6 = {"2"};
const vector<string> TEST7 = {"3"};
const vector<string> TEST8 = {"4"};
const vector<string> TEST9 = {"5"};

void samples() {
    assert(part1(TEST1) == 0);
    assert(part1(TEST2) == 3);
    assert(part1(TEST3) == 2);
    assert(part1(TEST4) == 31);
    assert(part2(TEST5) == 1);
    assert(part2(TEST6) == 4);
    assert(part2(TEST7) == 4);
    assert(part2(TEST8) == 5);
    assert(part2(TEST9) == 10);
}

MAIN(2017, 3)
