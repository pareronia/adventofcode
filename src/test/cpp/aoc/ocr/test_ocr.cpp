#include "../../../../main/cpp/aoc/ocr/ocr.hpp"
#include "gtest/gtest.h"
#include "gmock/gmock.h"

using namespace std;
using ::testing::Eq;

TEST(test_aoc_ocr, convert6) {
//clang-format off
    const Grid<char> grid = Grid<char>::from({
        " **  ***   **  **** ****  **  *  *  ***   ** *  * *     **  ***  ***   *** *  * *   ***** ",
        "*  * *  * *  * *    *    *  * *  *   *     * * *  *    *  * *  * *  * *    *  * *   *   * ",
        "*  * ***  *    ***  ***  *    ****   *     * **   *    *  * *  * *  * *    *  *  * *   *  ",
        "**** *  * *    *    *    * ** *  *   *     * * *  *    *  * ***  ***   **  *  *   *   *   ",
        "*  * *  * *  * *    *    *  * *  *   *  *  * * *  *    *  * *    * *     * *  *   *  *    ",
        "*  * ***   **  **** *     *** *  *  ***  **  *  * ****  **  *    *  * ***   **    *  **** "
     });
//clang-format on
    const string& result = ocr::convert6(grid, '#', ' ');
    EXPECT_THAT(result, Eq("ABCEFGHIJKLOPRSUYZ"));
}
