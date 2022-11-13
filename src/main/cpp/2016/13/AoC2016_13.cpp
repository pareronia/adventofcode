#include <algorithm>
#include <bitset>

#include "../../aoc/graph/graph.hpp"
#include "../../aoc/grid/grid.hpp"

using namespace std;

const vector<int> DR = {-1, 0, 1, 0};
const vector<int> DC = {0, 1, 0, -1};
const Cell START = Cell::at(1, 1);

bool isOpenSpace(const uint input, const Cell& cell) {
    const int x = cell.row();
    const int y = cell.col();
    const int t = input + x * x + 3 * x + 2 * x * y + y + y * y;
    return bitset<32>(t).count() % 2 == 0;
}

unordered_set<Cell> findNeighbours(const uint input, const Cell& cell) {
    unordered_set<Cell> neighbours;
    for (uint i = 0; i < 4; i++) {
        const int rr = cell.row() + DR[i];
        const int cc = cell.col() + DC[i];
        const Cell n = Cell::at(rr, cc);
        if (rr >= 0 && cc >= 0 && isOpenSpace(input, n)) {
            neighbours.insert(n);
        }
    }
    return neighbours;
}

AStar::Result<Cell> runAStar(const vector<string>& input) {
    return AStar::execute<Cell>(
        START, [](const Cell& cell) { return false; },
        [&input](const Cell& cell) {
            return findNeighbours(stoi(input[0]), cell);
        },
        [](const Cell& cell) { return 1; });
}

int getDistance(const vector<string>& input, const Cell& end) {
    return runAStar(input).getDistances()[end];
}

int part1(const vector<string>& input) {
    return getDistance(input, Cell::at(31, 39));
}

int part2(const vector<string>& input) {
    const auto& dist = runAStar(input).getDistances();
    return count_if(dist.begin(), dist.end(),
                    [](const auto& item) { return item.second <= 50; });
}

const vector<string> TEST = {"10"};

void samples() {
    assert(getDistance(TEST, Cell::at(1, 1)) == 0);
    assert(getDistance(TEST, Cell::at(7, 4)) == 11);
}

MAIN(2016, 13)
