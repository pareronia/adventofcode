#ifndef _AOC_GEOMETRY_HPP_
#define _AOC_GEOMETRY_HPP_

#include <functional>
#include <iostream>
#include <string>

struct Point {
   protected:
    Point(const int x_, const int y_) : x(x_), y(y_) {}
    virtual ~Point() = 0 ;

   public:
    // Point operator=(const Point& other) const;
    bool operator==(const Point& other) const;
    bool operator!=(const Point& other) const;
    virtual std::string toString() const;

    const int x;
    const int y;
};

template <>
struct std::hash<Point> {
    std::size_t operator()(const Point& p) const {
        return p.x + 31 * (p.y << 1);
    }
};

class Vector : public Point {
   public:
    Vector(const int x_, const int y_) : Point(x_, y_) {}
    virtual std::string toString() const;
    friend std::ostream& operator<<(std::ostream& strm, const Vector& v);
};

template <>
struct std::hash<Vector> {
    std::size_t operator()(const Vector& p) const {
        return std::hash<Point>{}(p);
    }
};

class Position : public Point {
   public:
    Position(const int x_, const int y_) : Point(x_, y_) {}
    // Position operator=(const Position& other) const;
    Position translate(const Vector& vector, const int amplitude = 1) const;
    virtual std::string toString() const;
    friend std::ostream& operator<<(std::ostream& strm, const Position& p);
};

template <>
struct std::hash<Position> {
    std::size_t operator()(const Position& p) const {
        return std::hash<Point>{}(p);
    }
};

#endif  //_AOC_GEOMETRY_HPP_
