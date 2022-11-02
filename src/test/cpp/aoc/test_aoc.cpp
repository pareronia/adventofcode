#include <bits/stdc++.h>
#include "../../../main/cpp/aoc/aoc.hpp"
#include "gtest/gtest.h"
#include "gmock/gmock.h"

using namespace std;
using ::testing::Eq;
using ::testing::IsEmpty;
using ::testing::MatchesRegex;

TEST(test_aoc, getNumbers) {
    vector<int> expected = {1, 2, 3, 4, 5, 67};
    EXPECT_THAT(aoc::getNumbers("abc1-2 ooo3ooo 4 -> 5__$67"), Eq(expected));
}

TEST(test_aoc, getNumbersReturnsEmptyWhenNoNumbers) {
    EXPECT_THAT(aoc::getNumbers("abc oooooo -> __$"), IsEmpty());
}

TEST(test_aoc, toBlocks) {
    vector<string> input = {"1", "2", "", "3", "4", "5", "", "6"};
    vector<vector<string>> expected = {{"1", "2"}, {"3", "4", "5"}, {"6"}};
    EXPECT_THAT(aoc::toBlocks(input), Eq(expected));
}

TEST(test_aoc, toBlocksReturnOneBlockIfNoEmptyLines) {
    vector<string> input = {"1", "2", "3", "4", "5", "6"};
    vector<vector<string>> expected = {{"1", "2", "3", "4", "5", "6"}};
    EXPECT_THAT(aoc::toBlocks(input), Eq(expected));
}

TEST(test_aoc, toBlocksReturnsEmptyWhenNoLines) {
    vector<string> input = {};
    EXPECT_THAT(aoc::toBlocks(input), IsEmpty());
}

TEST(test_aoc, DEBUG) {
    ::testing::internal::CaptureStderr();
    DEBUG("test");
    const string& captured = ::testing::internal::GetCapturedStderr().c_str();
    EXPECT_THAT(captured, MatchesRegex("test\r?\n"));
}

TEST(test_aoc, DEBUGDoesNothingWhenNDEBUG) {
    ::testing::internal::CaptureStderr();
    setenv("NDEBUG", "", 1);
    DEBUG("test");
    const string& captured = ::testing::internal::GetCapturedStderr().c_str();
    EXPECT_THAT(captured, IsEmpty());
}
