#include <bits/stdc++.h>
#include "../../../main/cpp/aoc/aoc.hpp"
#include "gtest/gtest.h"
#include "gmock/gmock.h"

using namespace std;
using namespace aoc;
using ::testing::Eq;
using ::testing::IsEmpty;
using ::testing::MatchesRegex;

TEST(test_aoc, getNumbers) {
    vector<int> expected = {1, 2, 3, 4, 5, 67};
    EXPECT_THAT(getNumbers("abc1-2 ooo3ooo 4 -> 5__$67"), Eq(expected));
}

TEST(test_aoc, getNumbersReturnsEmptyWhenNoNumbers) {
    EXPECT_THAT(getNumbers("abc oooooo -> __$"), IsEmpty());
}

TEST(test_aoc, toBlocks) {
    vector<string> input = {"1", "2", "", "3", "4", "5", "", "6"};
    vector<vector<string>> expected = {{"1", "2"}, {"3", "4", "5"}, {"6"}};
    EXPECT_THAT(toBlocks(input), Eq(expected));
}

TEST(test_aoc, toBlocksReturnOneBlockIfNoEmptyLines) {
    vector<string> input = {"1", "2", "3", "4", "5", "6"};
    vector<vector<string>> expected = {{"1", "2", "3", "4", "5", "6"}};
    EXPECT_THAT(toBlocks(input), Eq(expected));
}

TEST(test_aoc, toBlocksReturnsEmptyWhenNoLines) {
    vector<string> input = {};
    EXPECT_THAT(toBlocks(input), IsEmpty());
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

TEST(test_aoc, splitOnSpace) {
    EXPECT_THAT(split(""), Eq(vector<string>({})));
    EXPECT_THAT(split("a b"), Eq(vector<string>({"a", "b"})));
    EXPECT_THAT(split(" a b "), Eq(vector<string>({"a", "b"})));
    EXPECT_THAT(split("a   b"), Eq(vector<string>({"a", "b"})));
    EXPECT_THAT(split("a1 b 2"), Eq(vector<string>({"a1", "b", "2"})));
}

TEST(test_aoc, splitOnToken) {
    EXPECT_THAT(split("a-b", "-"), Eq(vector<string>({"a", "b"})));
    EXPECT_THAT(split("a- b", "-"), Eq(vector<string>({"a", " b"})));
    EXPECT_THAT(split("a--b", "-"), Eq(vector<string>({"a", "", "b"})));
    EXPECT_THAT(split("a-b+c", "-"), Eq(vector<string>({"a", "b+c"})));
    EXPECT_THAT(split("a1-->b-->2c$3", "-->"), Eq(vector<string>({"a1", "b", "2c$3"})));
    EXPECT_THAT(split("a", "-->"), Eq(vector<string>({"a"})));
    EXPECT_THAT(split("", "-->"), Eq(vector<string>({""})));
    EXPECT_ANY_THROW(split("aa", ""));
}
vector<int> collect(const Range& range) {
    vector<int> ans;
    for (int n : range) {
        ans.push_back(n);
    }
    return ans;
}

TEST(test_aoc, Range) {
    EXPECT_THAT(collect(Range(0, 5)), Eq(vector<int>({0, 1, 2, 3, 4, 5})));
    EXPECT_THAT(collect(Range(1, 6)), Eq(vector<int>({1, 2, 3, 4, 5, 6})));
}
