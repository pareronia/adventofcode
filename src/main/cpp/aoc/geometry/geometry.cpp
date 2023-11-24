#include "geometry.hpp"

#include <iostream>
#include <string>
#include <typeinfo>

using namespace std;

Point::~Point() {}

// Point Point::operator=(const Point& other) const {
//     return Point(other.x, other.y);
// }

bool Point::operator==(const Point& other) const {
    return this->x == other.x && this->y == other.y;
}

bool Point::operator!=(const Point& other) const { return !(*this == other); }

string Point::toString() const {
    return string("[x=") + to_string(this->x) + string(",y=") +
           to_string(this->y) + string("]");
}

ostream& operator<<(ostream& strm, const Position& p) {
    strm << p.toString();
    return strm;
}

// Position Position::operator=(const Position& other) const {
//     return Position(other.x, other.y);
// }

string Position::toString() const { return "Position" + Point::toString(); }

Position Position::translate(const Vector& vector, const int amplitude) const {
    return Position(this->x + amplitude * vector.x,
                    this->y + amplitude * vector.y);
}

string Vector::toString() const { return "Vector" + Point::toString(); }

ostream& operator<<(ostream& strm, const Vector& v) {
    strm << v.toString();
    return strm;
}
