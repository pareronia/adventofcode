#include "grid.hpp"

#include <numeric>

Cell::Cell(const int row, const int col) {
    _row = row;
    _col = col;
}

Cell Cell::at(const int row, const int col) { return Cell(row, col); }

int Cell::row() const { return _row; }

int Cell::col() const { return _col; }

bool Cell::operator==(const Cell& other) const {
    return _row == other.row() && _col == other.col();
}

ostream& operator<<(ostream& strm, const Cell& cell) {
    strm << "Cell[row=" << cell.row() << ", col=" << cell.col() << "]";
    return strm;
}

template <>
Grid<int> Grid<int>::from(const vector<string>& input) {
    vector<vector<int>> v;
    for (const string& line : input) {
        vector<int> vv;
        for (const char c : line) {
            vv.push_back(c - '0');
        }
        v.push_back(vv);
    }
    return Grid<int>(v);
}

template <>
Grid<char> Grid<char>::from(const vector<string>& input) {
    vector<vector<char>> v;
    for (const string& line : input) {
        vector<char> vv;
        for (const char c : line) {
            vv.push_back(c);
        }
        v.push_back(vv);
    }
    return Grid<char>(v);
}

template <>
string Grid<char>::toString() const {
    return accumulate(next(_cells.begin()), _cells.end(),
                      string(_cells[0].begin(), _cells[0].end()),
                      [](const string& a, const vector<char>& b) {
                          return a + "\n" + string(b.begin(), b.end());
                      });
}

ostream& operator<<(ostream& strm, const Grid<char>& grid) {
    strm << endl << grid.toString() << endl;
    return strm;
}
