#include "../../../../main/cpp/aoc/geometry3d/geometry3d.hpp"
#include "gmock/gmock.h"
#include "gtest/gtest.h"

using namespace std;
using ::testing::Eq;
using ::testing::Ne;

TEST(test_geometry, Vector3D) {
    const Vector3D v1(2, 3, 4);
    EXPECT_THAT(v1.x, Eq(2));
    EXPECT_THAT(v1.y, Eq(3));
    EXPECT_THAT(v1.z, Eq(4));
    const Vector3D v2(2, 3, 4);
    EXPECT_THAT(v2.x, Eq(2));
    EXPECT_THAT(v2.y, Eq(3));
    EXPECT_THAT(v2.z, Eq(4));
    Vector3D v3(3, 3, 4);
    EXPECT_THAT(v3.x, Eq(3));
    EXPECT_THAT(v3.y, Eq(3));
    EXPECT_THAT(v3.z, Eq(4));
    EXPECT_THAT(v2, Eq(v1));
    EXPECT_THAT(v3, Ne(v1));
    // v3 = v1;
    // EXPECT_THAT(v3, Eq(v1));
    EXPECT_THAT(v1.toString(), Eq("Vector3D[x=2,y=3,z=4]"));
    EXPECT_THAT(hash<Vector3D>{}(v1), Eq(hash<Vector3D>{}(v2)));
}

TEST(test_geometry, Position3D) {
    const Position3D p1(2, 3, 4);
    EXPECT_THAT(p1.x, Eq(2));
    EXPECT_THAT(p1.y, Eq(3));
    EXPECT_THAT(p1.z, Eq(4));
    const Position3D p2(2, 3, 4);
    EXPECT_THAT(p2.x, Eq(2));
    EXPECT_THAT(p2.y, Eq(3));
    EXPECT_THAT(p2.z, Eq(4));
    Position3D p3(3, 3, 4);
    EXPECT_THAT(p3.x, Eq(3));
    EXPECT_THAT(p3.y, Eq(3));
    EXPECT_THAT(p3.z, Eq(4));
    EXPECT_THAT(p2, Eq(p1));
    EXPECT_THAT(p3, Ne(p1));
    // p3 = p1;
    // EXPECT_THAT(p3, Eq(p1));
    EXPECT_THAT(p1.toString(), Eq("Position3D[x=2,y=3,z=4]"));
    EXPECT_THAT(hash<Position3D>{}(p1), Eq(hash<Position3D>{}(p2)));
}

TEST(test_geometry, translate) {
    const Vector3D v1(-2, -2, -2);
    EXPECT_THAT(Position3D(2, 4, 6).translate(v1), Eq(Position3D(0, 2, 4)));
    EXPECT_THAT(Position3D(2, 4, 6).translate(v1, 2), Eq(Position3D(-2, 0, 2)));
}

TEST(test_geometry, manhattanDistance) {
    const Position3D p(1, 2, 3);
    EXPECT_THAT(p.manhattanDistance(), Eq(6));
    EXPECT_THAT(p.manhattanDistance(Position3D(5, -5, 3)), Eq(11));
}
