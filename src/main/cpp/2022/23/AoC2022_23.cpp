#include <assert.h>

#include <algorithm>
#include <deque>
#include <string>
#include <unordered_map>
#include <unordered_set>
#include <vector>

#include "../../aoc/aoc.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;
using Tile = pair<int, int>;
using Direction = pair<int, int>;
using Bounds = tuple<int, int, int, int>;

#define Tile(r, c) make_pair(r, c)
#define Direction(r, c) make_pair(r, c)
#define Bounds(min_r, max_r, min_c, max_c) \
    make_tuple(min_r, max_r, min_c, max_c)

const char ELF = '#';
const char GROUND = '.';
const Direction N = Direction(-1, 0);
const Direction NW = Direction(-1, -1);
const Direction NE = Direction(-1, 1);
const Direction S = Direction(1, 0);
const Direction SW = Direction(1, -1);
const Direction SE = Direction(1, 1);
const Direction W = Direction(0, -1);
const Direction E = Direction(0, 1);
const unordered_set<Direction> ALL_DIRS = {N, NE, E, SE, S, SW, W, NW};
const unordered_map<Direction, unordered_set<Direction>> DIRS = {
    {N, {N, NE, NW}}, {S, {S, SE, SW}}, {W, {W, NW, SW}}, {E, {E, NE, SE}}};

unordered_set<Tile> parse(const vector<string>& input) {
    unordered_set<Tile> elves;
    for (int r : aoc::Range::range(input.size())) {
        for (int c : aoc::Range::range(input[r].size())) {
            if (input[r][c] == ELF) {
                elves.insert(Tile(r, c));
            }
        }
    }
    return elves;
}

Bounds getBounds(const unordered_set<Tile>& elves) {
    const auto [min_r, max_r] = minmax_element(
        elves.begin(), elves.end(),
        [](const Tile& a, const Tile& b) { return a.first < b.first; });
    const auto [min_c, max_c] = minmax_element(
        elves.begin(), elves.end(),
        [](const Tile& a, const Tile& b) { return a.second < b.second; });
    return Bounds((*min_r).first, (*max_r).first, (*min_c).second,
                  (*max_c).second);
}

void draw(const unordered_set<Tile>& elves) {
    if (getenv("NDEBUG") != NULL) {
        return;
    }
    const auto& bounds = getBounds(elves);
    for (int r : aoc::Range::rangeClosed(get<0>(bounds), get<1>(bounds))) {
        vector<char> chars;
        for (int c : aoc::Range::rangeClosed(get<2>(bounds), get<3>(bounds))) {
            if (elves.find(Tile(r, c)) == elves.end()) {
                chars.push_back(GROUND);
            } else {
                chars.push_back(ELF);
            }
        }
        DEBUG(string(chars.begin(), chars.end()));
    }
}

inline Tile add(const Tile& tile, const Direction& direction) {
    return Tile(tile.first + direction.first, tile.second + direction.second);
}

bool allNotOccupied(const unordered_set<Tile>& elves, const Tile& elf,
                    const unordered_set<Direction>& directions) {
    return none_of(directions.begin(), directions.end(),
                   [&elves, &elf](const auto& direction) {
                       return elves.find(add(elf, direction)) != elves.end();
                   });
}

unordered_map<Tile, vector<Tile>> calculateMoves(
    const unordered_set<Tile>& elves, const deque<Direction>& order) {
    unordered_map<Tile, vector<Tile>> moves;
    for (const Tile& elf : elves) {
        if (allNotOccupied(elves, elf, ALL_DIRS)) {
            continue;
        }
        for (const Direction& d : order) {
            if (allNotOccupied(elves, elf, DIRS.at(d))) {
                const Tile& n = add(elf, d);
                moves.try_emplace(n, vector<Tile>());
                moves[n].push_back(elf);
                break;
            }
        }
    }
    return moves;
}

void executeMoves(unordered_set<Tile>& elves,
                  const unordered_map<Tile, vector<Tile>>& moves) {
    for (const auto& move : moves) {
        if (move.second.size() > 1) {
            continue;
        }
        assert(elves.find(move.second[0]) != elves.end());
        elves.erase(move.second[0]);
        elves.insert(move.first);
    }
}

int part1(const vector<string>& input) {
    unordered_set<Tile> elves = parse(input);
    deque<Direction> order = {N, S, W, E};
    for (const Tile& elf : elves) {
        DEBUG(to_string(elf.first) + "," + to_string(elf.second));
    }
    for (int i : aoc::Range::rangeClosed(1, 10)) {
        DEBUG("Round " + to_string(i));
        const auto& moves = calculateMoves(elves, order);
        executeMoves(elves, moves);
        draw(elves);
        order.push_back(order.front());
        order.pop_front();
    }
    const auto& bounds = getBounds(elves);
    return (get<1>(bounds) - get<0>(bounds) + 1) *
               (get<3>(bounds) - get<2>(bounds) + 1) -
           elves.size();
}

int part2(const vector<string>& input) {
    unordered_set<Tile> elves = parse(input);
    deque<Direction> order = {N, S, W, E};
    int i = 1;
    while (true) {
        DEBUG("Round " + to_string(i));
        const auto& moves = calculateMoves(elves, order);
        if (moves.empty()) {
            return i;
        }
        executeMoves(elves, moves);
        order.push_back(order.front());
        order.pop_front();
        i++;
    }
}

// clang-format off
const vector<string> TEST = {
"....#..",
"..###.#",
"#...#.#",
".#...##",
"#.###..",
"##.#.##",
".#..#.."
};
// clang-format on

void samples() {
    assert(part1(TEST) == 110);
    assert(part2(TEST) == 20);
}

MAIN(2022, 23)
