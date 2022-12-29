#include <assert.h>

#include <algorithm>
#include <deque>
#include <numeric>
#include <string>
#include <unordered_map>
#include <unordered_set>
#include <vector>

#include "../../aoc/aoc.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;
using Position = pair<int, int>;
using Direction = pair<int, int>;
using State = tuple<int, int, int>;
using Blizzards = unordered_map<int, unordered_set<Position>>;

#define Position(x, y) make_pair(x, y)
#define Direction(x, y) make_pair(x, y)
#define State(position, time) make_tuple(position.first, position.second, time)

const Direction N = Direction(0, 1);
const Direction S = Direction(0, -1);
const Direction W = Direction(-1, 0);
const Direction E = Direction(1, 0);
const unordered_set<Direction> ALL_DIRS = {N, E, S, W, Direction(0, 0)};
const unordered_map<char, Direction> BLIZZARD_DIRS = {
    {'^', N}, {'>', E}, {'v', S}, {'<', W}};

struct Blizzard {
    const Position position;
    const Direction direction;

    Blizzard(const Position& position, const Direction& direction)
        : position(position), direction(direction) {}
    Position at(const int time, const int width, const int height) const {
        int amount;
        if (this->direction == W || this->direction == E) {
            amount = time % width;
        } else if (this->direction == N || this->direction == S) {
            amount = time % height;
        } else {
            assert(false);
        }
        const Position newPosition(
            this->position.first + amount * this->direction.first,
            this->position.second + amount * this->direction.second);
        const Position pp((width + newPosition.first) % width,
                          (height + newPosition.second) % height);
        assert(0 <= pp.first && pp.first < width && 0 <= pp.second &&
               pp.second < height);
        return pp;
    }
};

struct Valley {
    const int height;
    const int width;
    const int period;
    const Position start;
    const Position end;
    const Blizzards blizzards;

    Valley(const int height, const int width, const int period,
           const Blizzards blizzards)
        : height(height),
          width(width),
          period(period),
          start(Position(0, height)),
          end(Position(width - 1, -1)),
          blizzards(blizzards) {}
    bool isBlizzard(const Position& position, const int time) const {
        const unordered_set<Position>& blizzardsAtTime =
            this->blizzards.at(time % this->period);
        return blizzardsAtTime.find(position) != blizzardsAtTime.end();
    }
    bool inBounds(const Position& position) const {
        return 0 <= position.first && position.first < this->width &&
               0 <= position.second && position.second < this->height;
    }
};

Valley parse(const vector<string>& input) {
    const int height = input.size() - 2;
    const int width = input[0].size() - 2;
    const int period = lcm(height, width);
    vector<Blizzard> blizzards;
    for (uint y = 0; y < input.size(); y++) {
        for (uint x = 0; x < input[y].size(); x++) {
            const char ch = input[y][x];
            if (ch != '.' && ch != '#') {
                const Position position(x - 1, height - y);
                blizzards.push_back(Blizzard(position, BLIZZARD_DIRS.at(ch)));
            }
        }
    }
    Blizzards blizzardsByTime;
    for (int i = 0; i < period; i++) {
        blizzardsByTime.try_emplace(i, unordered_set<Position>());
        for (const Blizzard& blizzard : blizzards) {
            blizzardsByTime.at(i).insert(blizzard.at(i, width, height));
        }
    }
    Valley valley(height, width, period, blizzardsByTime);
    return valley;
}

template <>
struct std::hash<State> {
    size_t operator()(const State& p) const {
        return get<0>(p) + 31 * (get<1>(p) << 1) + 73 * (get<2>(p) << 3);
    }
};

int solve(const Valley& valley, const Position& start, const Position& end,
          const int startTime) {
    deque<State> q;
    q.push_back(State(start, startTime));
    unordered_set<State> seen;
    while (q.size() > 0) {
        const State& state = q.front();
        q.pop_front();
        const int x = get<0>(state);
        const int y = get<1>(state);
        const int time = get<2>(state);
        if (x == end.first && y == end.second) {
            return time;
        }
        const int nextTime = time + 1;
        for (const Direction& d : ALL_DIRS) {
            const Position n(x + d.first, y + d.second);
            if (n == start || n == end ||
                (valley.inBounds(n) && !valley.isBlizzard(n, nextTime))) {
                const State newState = State(n, nextTime);
                if (seen.find(newState) == seen.end()) {
                    seen.insert(newState);
                    q.push_back(newState);
                }
            }
        }
    }
    throw "Unsolvable";
}

int part1(const vector<string>& input) {
    const Valley& valley = parse(input);
    return solve(valley, valley.start, valley.end, 0);
}

int part2(const vector<string>& input) {
    const Valley& valley = parse(input);
    const int there = solve(valley, valley.start, valley.end, 0);
    const int thereAndBack = solve(valley, valley.end, valley.start, there);
    const int thereAndBackAndThereAgain =
        solve(valley, valley.start, valley.end, thereAndBack);
    return thereAndBackAndThereAgain;
}

// clang-format off
const vector<string> TEST = {
"#.######",
"#>>.<^<#",
"#.<..<<#",
"#>v.><>#",
"#<^v^^>#",
"######.#"
};
// clang-format on

void samples() {
    assert(part1(TEST) == 18);
    assert(part2(TEST) == 54);
}

MAIN(2022, 24)
