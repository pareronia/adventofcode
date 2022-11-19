#include "../../../../main/cpp/aoc/geometry/geometry.hpp"
#include "gmock/gmock.h"
#include "gtest/gtest.h"

using namespace std;
using ::testing::Eq;
using ::testing::Ne;

TEST(test_geometry, Vector) {
    const Vector v1(2, 3);
    EXPECT_THAT(v1.x, Eq(2));
    EXPECT_THAT(v1.y, Eq(3));
    const Vector v2(2, 3);
    EXPECT_THAT(v2.x, Eq(2));
    EXPECT_THAT(v2.y, Eq(3));
    Vector v3(3, 3);
    EXPECT_THAT(v3.x, Eq(3));
    EXPECT_THAT(v3.y, Eq(3));
    EXPECT_THAT(v2, Eq(v1));
    EXPECT_THAT(v3, Ne(v1));
    // v3 = v1;
    // EXPECT_THAT(v3, Eq(v1));
    EXPECT_THAT(v1.toString(), Eq("Vector[x=2,y=3]"));
    EXPECT_THAT(hash<Vector>{}(v1), Eq(hash<Vector>{}(v2)));
}

TEST(test_geometry, Position) {
    const Position p1(2, 3);
    EXPECT_THAT(p1.x, Eq(2));
    EXPECT_THAT(p1.y, Eq(3));
    const Position p2(2, 3);
    EXPECT_THAT(p2.x, Eq(2));
    EXPECT_THAT(p2.y, Eq(3));
    Position p3(3, 3);
    EXPECT_THAT(p3.x, Eq(3));
    EXPECT_THAT(p3.y, Eq(3));
    EXPECT_THAT(p2, Eq(p1));
    EXPECT_THAT(p3, Ne(p1));
    // p3 = p1;
    // EXPECT_THAT(p3, Eq(p1));
    EXPECT_THAT(p1.toString(), Eq("Position[x=2,y=3]"));
    EXPECT_THAT(hash<Position>{}(p1), Eq(hash<Position>{}(p2)));
}

TEST(test_geometry, translate) {
    const Vector v1(2, -2);
    EXPECT_THAT(Position(4, 5).translate(v1), Eq(Position(6, 3)));
    EXPECT_THAT(Position(4, 5).translate(v1, 2), Eq(Position(8, 1)));
}

