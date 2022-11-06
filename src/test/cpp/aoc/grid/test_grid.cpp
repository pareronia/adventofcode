#include <bits/stdc++.h>
#include "../../../../main/cpp/aoc/grid/grid.hpp"
#include "gtest/gtest.h"
#include "gmock/gmock.h"

using namespace std;
using ::testing::Eq;
using ::testing::UnorderedElementsAre;

TEST(test_aoc_grid, constructor) {
    const Grid<int> grid = Grid<int>::from({"123", "456", "789"});
    EXPECT_THAT(grid.get(Cell::at(0, 0)), Eq(1));
    EXPECT_THAT(grid.get(Cell::at(2, 2)), Eq(9));
}

TEST(test_aoc_grid, get) {
    const vector<vector<int>> v({{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
    const Grid<int> grid(v);
    EXPECT_THAT(grid.get(Cell::at(0, 0)), Eq(1));
    EXPECT_THAT(grid.get(Cell::at(2, 2)), Eq(9));
}

TEST(test_aoc_grid, size) {
    const vector<vector<int>> v({{1, 2, 3}, {4, 5, 6}});
    const Grid<int> grid(v);
    EXPECT_THAT(grid.height(), Eq(2));
    EXPECT_THAT(grid.width(), Eq(3));
    EXPECT_THAT(grid.size(), Eq(6));
}

TEST(test_aoc_grid, iterator) {
    const vector<vector<int>> v({{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
    const Grid<int> grid(v);
    auto begin = grid.begin();
    EXPECT_THAT(*begin, Eq(Cell::at(0, 0)));
    auto end = grid.end();
    EXPECT_THAT(*end, Eq(Cell::at(3, 0)));
    EXPECT_THAT(*(++begin), Eq(Cell::at(0, 1)));
    EXPECT_THAT(*(begin++), Eq(Cell::at(0, 1)));
    int ans = 0;
    for (const Cell& cell : grid) {
        ans += grid.get(cell);
    }
    EXPECT_THAT(ans, Eq(45));
}

TEST(test_aoc_grid, countAllEqualTo) {
    const vector<vector<int>> v({{1, 2, 1}, {1, 5, 7}, {7, 8, 1}});
    const Grid<int> grid(v);
    EXPECT_THAT(grid.countAllEqualTo(1), Eq(4));
    EXPECT_THAT(grid.countAllEqualTo(7), Eq(2));
}

TEST(test_aoc_grid, capitalNeighbours) {
    const vector<vector<int>> v({{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
    const Grid<int> grid(v);
    EXPECT_THAT(
            grid.capitalNeighbours(Cell::at(1, 1)),
            UnorderedElementsAre(
                Cell::at(0, 1), Cell::at(1, 0), Cell::at(1, 2), Cell::at(2, 1)
            )
    );
    EXPECT_THAT(
            grid.capitalNeighbours(Cell::at(0, 0)),
            UnorderedElementsAre(
                Cell::at(0, 1), Cell::at(1, 0)
            )
    );
    EXPECT_THAT(
            grid.capitalNeighbours(Cell::at(2, 2)),
            UnorderedElementsAre(
                Cell::at(1, 2), Cell::at(2, 1)
            )
    );
}

TEST(test_aoc_grid, octantsNeighbours) {
    const vector<vector<int>> v({{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
    const Grid<int> grid(v);
    EXPECT_THAT(
            grid.octantsNeighbours(Cell::at(1, 1)),
            UnorderedElementsAre(
                Cell::at(0, 0), Cell::at(0, 1), Cell::at(0, 2),
                Cell::at(1, 0), Cell::at(1, 2),
                Cell::at(2, 0), Cell::at(2, 1), Cell::at(2, 2)
            )
    );
    EXPECT_THAT(
            grid.octantsNeighbours(Cell::at(0, 0)),
            UnorderedElementsAre(
                Cell::at(0, 1), Cell::at(1, 0), Cell::at(1, 1)
            )
    );
    EXPECT_THAT(
            grid.octantsNeighbours(Cell::at(2, 2)),
            UnorderedElementsAre(
                Cell::at(1, 1), Cell::at(1, 2), Cell::at(2, 1)
            )
    );
}
