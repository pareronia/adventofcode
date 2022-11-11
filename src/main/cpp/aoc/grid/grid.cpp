#include <bits/stdc++.h>
#include "grid.hpp"

Cell::Cell(const int row, const int col) {
    _row = row;
    _col = col;
}

Cell Cell::at(const int row, const int col) {
    return Cell(row, col);
}

int Cell::row() const {
    return _row;
}

int Cell::col() const {
    return _col;
}

bool Cell::operator==(const Cell& other) const {
    return _row == other.row() && _col == other.col();
}

ostream& operator<<(ostream &strm, const Cell& cell) {
    strm << "Cell[row=" << cell.row() << ", col=" << cell.col() << "]";
    return strm;
}

template<>
Grid<int> Grid<int>::from(const vector<string>& input) {
    vector<vector<int>> v;
    for (const string& line: input) {
        vector<int> vv;
        for (const char c: line) {
            vv.push_back(c - '0');
        }
        v.push_back(vv);
    }
    return Grid<int>(v);
}

template<>
Grid<char> Grid<char>::from(const vector<string>& input) {
    vector<vector<char>> v;
    for (const string& line: input) {
        vector<char> vv;
        for (const char c: line) {
            vv.push_back(c);
        }
        v.push_back(vv);
    }
    return Grid<char>(v);
}
