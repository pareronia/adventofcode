#ifndef _AOC_GEOMETRY3D_HPP_
#define _AOC_GEOMETRY3D_HPP_

#include <functional>
#include <iostream>
#include <string>

struct Point3D {
   protected:
    Point3D(const int x, const int y, const int z) : x(x), y(y), z(z) {}
    virtual ~Point3D() = 0;

   public:
    // Point3D operator=(const Point3D& other) const;
    bool operator==(const Point3D& other) const;
    bool operator!=(const Point3D& other) const;
    virtual std::string toString() const;

    const int x;
    const int y;
    const int z;
};

template <>
struct std::hash<Point3D> {
    std::size_t operator()(const Point3D& p) const {
        return p.x + 31 * (p.y << 1) + 37 * (p.z << 2);
    }
};

class Vector3D : public Point3D {
   public:
    Vector3D(const int x, const int y, const int z) : Point3D(x, y, z) {}
    virtual std::string toString() const;
    friend std::ostream& operator<<(std::ostream& strm, const Vector3D& v);
};

template <>
struct std::hash<Vector3D> {
    std::size_t operator()(const Vector3D& p) const {
        return std::hash<Point3D>{}(p);
    }
};

class Position3D : public Point3D {
   public:
    Position3D(const int x, const int y, const int z) : Point3D(x, y, z) {}
    // Position3D operator=(const Position3D& other) const;
    Position3D translate(const Vector3D& vector, const int amplitude = 1) const;
    int manhattanDistance(const Position3D& to = Position3D(0, 0, 0)) const ;
    virtual std::string toString() const;
    friend std::ostream& operator<<(std::ostream& strm, const Position3D& p);
};

template <>
struct std::hash<Position3D> {
    std::size_t operator()(const Position3D& p) const {
        return std::hash<Point3D>{}(p);
    }
};

#endif  //_AOC_GEOMETRY3D_HPP_
