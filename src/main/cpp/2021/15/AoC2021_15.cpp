#include "../../aoc/graph/graph.hpp"
#include "../../aoc/grid/grid.hpp"

using namespace std;

const vector<int> DR = {-1, 0, 1, 0};
const vector<int> DC = {0, 1, 0, -1};
const Cell START = Cell::at(0, 0);

uint getRisk(const Grid<int>& grid, const Cell& cell) {
    const Cell actual =
        Cell(cell.row() % grid.height(), cell.col() % grid.width());
    int value = grid.get(actual) + cell.row() / grid.height() +
                cell.col() / grid.width();
    while (value > 9) {
        value -= 9;
    }
    return value;
}

unordered_set<Cell> findNeighbours(const Grid<int>& grid, const Cell& cell,
                                   const uint tiles) {
    unordered_set<Cell> neighbours;
    for (uint i = 0; i < 4; i++) {
        const uint rr = cell.row() + DR[i];
        const uint cc = cell.col() + DC[i];
        if (rr >= 0 && rr < tiles * grid.height() && cc >= 0 &&
            cc < tiles * grid.width()) {
            neighbours.insert(Cell::at(rr, cc));
        }
    }
    return neighbours;
}

int solve(const vector<string>& input, const uint tiles) {
    const Grid<int> grid = Grid<int>::from(input);
    const Cell end = Cell(tiles * grid.height() - 1, tiles * grid.width() - 1);
    const AStar::Result<Cell> result = AStar::execute<Cell>(
        START, [&end](const Cell& cell) { return cell == end; },
        [&grid, &tiles](const Cell& cell) {
            return findNeighbours(grid, cell, tiles);
        },
        [&grid](const Cell& cell) { return getRisk(grid, cell); });
    return result.getDistances()[end];
}

int part1(const vector<string>& input) { return solve(input, 1); }

int part2(const vector<string>& input) { return solve(input, 5); }

// clang-format off
const vector<string> TEST = {
    "1163751742",
    "1381373672",
    "2136511328",
    "3694931569",
    "7463417111",
    "1319128137",
    "1359912421",
    "3125421639",
    "1293138521",
    "2311944581"
};
// clang-format on

void samples() {
    assert(part1(TEST) == 40);
    assert(part2(TEST) == 315);
}

MAIN(2021, 15)
