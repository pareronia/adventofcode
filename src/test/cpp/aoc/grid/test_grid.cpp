#include "../../../../main/cpp/aoc/grid/grid.hpp"
#include "gtest/gtest.h"
#include "gmock/gmock.h"

using namespace std;
using ::testing::Eq;
using ::testing::Ne;
using ::testing::UnorderedElementsAre;

TEST(test_aoc_grid, constructor) {
    const Grid<int> grid = Grid<int>::from({"123", "456", "789"});
    EXPECT_THAT(grid.get(Cell::at(0, 0)), Eq(1));
    EXPECT_THAT(grid.get(Cell::at(2, 2)), Eq(9));
}

TEST(test_aoc_grid, equals) {
    const Grid<int> grid1 = Grid<int>::from({"123", "456", "789"});
    const Grid<int> grid2 = Grid<int>::from({"123", "456", "789"});
    const Grid<int> grid3 = Grid<int>::from({"123", "456", "999"});
    const Grid<int> grid4 = Grid<int>::from({"123", "456", "789", "000"});
    EXPECT_THAT(grid1 == grid2, Eq(true));
    EXPECT_THAT(grid1 == grid3, Eq(false));
    EXPECT_THAT(grid1 == grid4, Eq(false));
}

TEST(test_aoc_grid, get) {
    const vector<vector<int>> v({{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
    const Grid<int> grid(v);
    EXPECT_THAT(grid.get(Cell::at(0, 0)), Eq(1));
    EXPECT_THAT(grid.get(Cell::at(2, 2)), Eq(9));
}

TEST(test_aoc_grid, size) {
    const vector<vector<int>> e;
    const Grid<int> empty(e);
    EXPECT_THAT(empty.height(), Eq(0));
    EXPECT_THAT(empty.width(), Eq(0));
    EXPECT_THAT(empty.size(), Eq(0));
    EXPECT_THAT(empty.isSquare(), Eq(true));
    const vector<vector<int>> v({{1, 2, 3}, {4, 5, 6}});
    const Grid<int> grid(v);
    EXPECT_THAT(grid.height(), Eq(2));
    EXPECT_THAT(grid.width(), Eq(3));
    EXPECT_THAT(grid.size(), Eq(6));
    EXPECT_THAT(grid.isSquare(), Eq(false));
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

TEST(test_aoc_grid, setValue) {
    const vector<vector<int>> v({{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
    Grid<int> grid(v);
    grid.setValue(Cell::at(2, 2), -1);
    EXPECT_THAT(grid.get(Cell::at(2, 2)), Eq(-1));
}

TEST(test_aoc_grid, increment) {
    const vector<vector<int>> v({{1, 2, 3}, {4, 5, 6}, {7, 8, 9}});
    Grid<int> grid(v);
    grid.increment(Cell::at(2, 2), -1);
    EXPECT_THAT(grid.get(Cell::at(2, 2)), Eq(8));
    grid.increment(Cell::at(2, 2));
    EXPECT_THAT(grid.get(Cell::at(2, 2)), Eq(9));
}

TEST(test_aoc_grid, subGrid) {
    const vector<string> v({"ABCD", "EFGH", "IJKL", "MNOP", "QRST"});
    const Grid<char> grid = Grid<char>::from(v);
    const Grid<char> result1 = grid.subGrid(Cell::at(1, 1), Cell::at(4, 4));
    EXPECT_THAT(result1, Eq(Grid<char>::from({"FGH", "JKL", "NOP"})));
    const Grid<char> result2 = grid.subGrid(Cell::at(0, 0), Cell::at(0, 0));
    EXPECT_THAT(result2.size(), Eq(0));
    const Grid<char> result3 = grid.subGrid(Cell::at(1, 1), Cell::at(5, 5));
    EXPECT_THAT(result3, Eq(Grid<char>::from({"FGH", "JKL", "NOP", "RST"})));
}

TEST(test_aoc_grid, divide) {
    const Grid<char> grid = Grid<char>::from({"ABCD", "EFGH", "IJKL", "MNOP"});
    const vector<vector<Grid<char>>> subGrids = grid.divide(2);
    EXPECT_THAT(subGrids.size(), Eq(2));
    EXPECT_THAT(subGrids[0].size(), Eq(2));
    EXPECT_THAT(subGrids[1].size(), Eq(2));
    EXPECT_THAT(subGrids[0][0], Eq(Grid<char>::from({"AB", "EF"})));
    EXPECT_THAT(subGrids[0][1], Eq(Grid<char>::from({"CD", "GH"})));
    EXPECT_THAT(subGrids[1][0], Eq(Grid<char>::from({"IJ", "MN"})));
    EXPECT_THAT(subGrids[1][1], Eq(Grid<char>::from({"KL", "OP"})));
}

TEST(test_aoc_grid, merge) {
    const Grid<char> grid1 = Grid<char>::from({"AB", "EF"});
    const Grid<char> grid2 = Grid<char>::from({"CD", "GH"});
    const Grid<char> grid3 = Grid<char>::from({"IJ", "MN"});
    const Grid<char> grid4 = Grid<char>::from({"KL", "OP"});
    const Grid<char> grid5 = Grid<char>::from({"XXX", "XXX"});
    EXPECT_ANY_THROW(Grid<char>::merge({{grid1, grid2}, {grid3, grid5}}));
    EXPECT_THAT(
            Grid<char>::merge({{grid1, grid2}, {grid3, grid4}}),
            Eq(Grid<char>::from({"ABCD", "EFGH", "IJKL", "MNOP"})));
}

TEST(test_aoc_grid, flipHorizontally) {
    const Grid<char> grid = Grid<char>::from({"123", "456", "789"});
    EXPECT_THAT(
            grid.flipHorizontally(),
            Eq(Grid<char>::from({"789", "456", "123"})));
}

TEST(test_aoc_grid, rotate) {
    const Grid<char> grid = Grid<char>::from({"123", "456", "789"});
    EXPECT_THAT(
            grid.rotate(),
            Eq(Grid<char>::from({"741", "852", "963"})));
}

TEST(test_aoc_grid, permutations) {
    const Grid<char> grid = Grid<char>::from({"123", "456", "789"});
    auto perms = grid.permutations_begin();
    auto end = grid.permutations_end();
    // Permutation 0 : original
    EXPECT_THAT(perms, Ne(end));
    EXPECT_THAT(
            *perms,
            Eq(Grid<char>::from({"123", "456", "789"})));
    // Permutation 1 : rotated 90
    perms++;
    EXPECT_THAT(perms, Ne(end));
    EXPECT_THAT(
            *perms,
            Eq(Grid<char>::from({"741", "852", "963"})));
    // Permutation 2 : rotated 180
    perms++;
    EXPECT_THAT(perms, Ne(end));
    EXPECT_THAT(
            *perms,
            Eq(Grid<char>::from({"987", "654", "321"})));
    // Permutation 3 : rotated 270
    perms++;
    EXPECT_THAT(perms, Ne(end));
    EXPECT_THAT(
            *perms,
            Eq(Grid<char>::from({"369", "258", "147"})));
    // Permutation 4 : diagonal TB
    perms++;
    EXPECT_THAT(perms, Ne(end));
    EXPECT_THAT(
            *perms,
            Eq(Grid<char>::from({"147", "258", "369"})));
    // Permutation 5 : Vertical flip
    perms++;
    EXPECT_THAT(perms, Ne(end));
    EXPECT_THAT(
            *perms,
            Eq(Grid<char>::from({"321", "654", "987"})));
    // Permutation 6 : diagonal BT
    perms++;
    EXPECT_THAT(perms, Ne(end));
    EXPECT_THAT(
            *perms,
            Eq(Grid<char>::from({"963", "852", "741"})));
    // Permutation 7 : horizontal flip
    perms++;
    EXPECT_THAT(perms, Ne(end));
    EXPECT_THAT(
            *perms,
            Eq(Grid<char>::from({"789", "456", "123"})));
    // end
    perms++;
    EXPECT_THAT(perms, Eq(end));

    uint cnt = 0;
    auto perm = grid.permutations_begin();
    while (perm != grid.permutations_end()) {
        perm++;
        cnt++;
    }
    EXPECT_THAT(cnt, Eq(8));
}

TEST(test_aoc_grid, replace) {
   const Grid<char> grid = Grid<char>::from({"XOX", "OXO", "XOX"});
   EXPECT_THAT(grid.replace('X', 'Y'),
               Eq(Grid<char>::from({"YOY", "OYO", "YOY"})));
}

TEST(test_aoc_grid, toString) {
   const Grid<char> grid = Grid<char>::from({"XOX", "OXO", "XOX"});
   EXPECT_THAT(grid.toString(), Eq("XOX\nOXO\nXOX"));
}
