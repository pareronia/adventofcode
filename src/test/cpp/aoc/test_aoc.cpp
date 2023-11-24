#include <string>
#include <vector>

#include "../../../main/cpp/aoc/aoc.hpp"
#include "gmock/gmock.h"
#include "gtest/gtest.h"

using namespace std;
using namespace aoc;
using ::testing::Eq;
using ::testing::IsEmpty;
using ::testing::IsFalse;
using ::testing::IsTrue;
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
    EXPECT_THAT(split("a1-->b-->2c$3", "-->"),
                Eq(vector<string>({"a1", "b", "2c$3"})));
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

TEST(test_aoc, RangeCannotHaveZeroStep) {
    EXPECT_ANY_THROW(Range::range(10, 20, 0));
}

TEST(test_aoc, RangeCannotHaveAscendingRangeToNegative) {
    EXPECT_ANY_THROW(Range::range(-10));
    EXPECT_ANY_THROW(Range::rangeClosed(-10));
}

TEST(test_aoc, RangeCannotHaveNegativeStepOnAscendingRange) {
    EXPECT_ANY_THROW(Range::range(10, 20, -1));
}

TEST(test_aoc, RangeCannotHavePositiveStepOnDescendingRange) {
    EXPECT_ANY_THROW(Range::range(20, 10, 1));
}

TEST(test_aoc, Range) {
    EXPECT_THAT(collect(Range::range(5)), Eq(vector<int>({0, 1, 2, 3, 4})));
    EXPECT_THAT(collect(Range::range(5, 5, 1)), IsEmpty());
    EXPECT_THAT(collect(Range::range(5, 5, -1)), IsEmpty());
    EXPECT_THAT(collect(Range::range(5, 6, 1)), Eq(vector<int>({5})));
    EXPECT_THAT(collect(Range::range(5, 6, 2)), Eq(vector<int>({5})));
    EXPECT_THAT(collect(Range::range(5, 7, 2)), Eq(vector<int>({5})));
    EXPECT_THAT(collect(Range::range(5, 8, 2)), Eq(vector<int>({5, 7})));
    EXPECT_THAT(collect(Range::range(1, 6)), Eq(vector<int>({1, 2, 3, 4, 5})));
    EXPECT_THAT(collect(Range::range(4, 15, 2)),
                Eq(vector<int>({4, 6, 8, 10, 12, 14})));
    EXPECT_THAT(collect(Range::range(4, 16, 2)),
                Eq(vector<int>({4, 6, 8, 10, 12, 14})));
    EXPECT_THAT(collect(Range::range(4, -1, -1)),
                Eq(vector<int>({4, 3, 2, 1, 0})));
    EXPECT_THAT(collect(Range::rangeClosed(4, 0, -1)),
                Eq(vector<int>({4, 3, 2, 1, 0})));
    EXPECT_THAT(collect(Range::rangeClosed(4, -1, -1)),
                Eq(vector<int>({4, 3, 2, 1, 0, -1})));
    EXPECT_THAT(collect(Range::rangeClosed(7)),
                Eq(vector<int>({0, 1, 2, 3, 4, 5, 6, 7})));
    EXPECT_THAT(collect(Range::rangeClosed(4, 13, 2)),
                Eq(vector<int>({4, 6, 8, 10, 12})));
    EXPECT_THAT(collect(Range::rangeClosed(4, 12, 2)),
                Eq(vector<int>({4, 6, 8, 10, 12})));
    EXPECT_THAT(Range::range(0, 5).contains(0), IsTrue());
    EXPECT_THAT(Range::range(0, 5).contains(5), IsFalse());
    EXPECT_THAT(Range::rangeClosed(0, 5).contains(5), IsTrue());
    EXPECT_THAT(collect(Range::range(0, 0)), Eq(vector<int>({})));
    EXPECT_THAT(collect(Range::range(0, 1)), Eq(vector<int>({0})));
    EXPECT_THAT(collect(Range::rangeClosed(0, 0)), Eq(vector<int>({0})));
    EXPECT_THAT(Range::rangeClosed(1, 4).contains(Range::rangeClosed(1, 4)),
                IsTrue());
    EXPECT_THAT(Range::rangeClosed(1, 4).contains(Range::rangeClosed(0, 4)),
                IsFalse());
    EXPECT_THAT(Range::rangeClosed(1, 4).contains(Range::rangeClosed(2, 3)),
                IsTrue());
    EXPECT_THAT(
        Range::rangeClosed(1, 4).isOverlappedBy(Range::rangeClosed(0, 4)),
        IsTrue());
    EXPECT_THAT(
        Range::rangeClosed(1, 4).isOverlappedBy(Range::rangeClosed(4, 5)),
        IsTrue());
    EXPECT_THAT(
        Range::rangeClosed(1, 4).isOverlappedBy(Range::rangeClosed(6, 8)),
        IsFalse());
}
