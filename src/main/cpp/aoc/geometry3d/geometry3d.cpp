#include "geometry3d.hpp"

#include <iostream>
#include <string>
#include <typeinfo>

using namespace std;

Point3D::~Point3D() {}

// Point3D Point3D::operator=(const Point3D& other) const {
//     return Point3D(other.x, other.y, other.z);
// }

bool Point3D::operator==(const Point3D& other) const {
    return this->x == other.x && this->y == other.y && this->z == other.z;
}

bool Point3D::operator!=(const Point3D& other) const {
    return !(*this == other);
}

string Point3D::toString() const {
    return string("[x=") + to_string(this->x) + string(",y=") +
           to_string(this->y) + string(",z=") + to_string(this->z) +
           string("]");
}

ostream& operator<<(ostream& strm, const Position3D& p) {
    strm << p.toString();
    return strm;
}

// Position3D Position3D::operator=(const Position3D& other) const {
//     return Position3D(other.x, other.y, other.z);
// }

string Position3D::toString() const {
    return "Position3D" + Point3D::toString();
}

Position3D Position3D::translate(const Vector3D& vector,
                                 const int amplitude) const {
    return Position3D(this->x + amplitude * vector.x,
                      this->y + amplitude * vector.y,
                      this->z + amplitude * vector.z);
}

int Position3D::manhattanDistance(const Position3D& to) const {
    return abs(this->x - to.x) + abs(this->y - to.y) + abs(this->z - to.z);
}

string Vector3D::toString() const { return "Vector3D" + Point3D::toString(); }

ostream& operator<<(ostream& strm, const Vector3D& v) {
    strm << v.toString();
    return strm;
}
