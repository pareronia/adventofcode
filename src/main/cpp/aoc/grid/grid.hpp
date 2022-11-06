#ifndef _AOC_GRID_HPP_
#define _AOC_GRID_HPP_

#include <bits/stdc++.h>
#include "../aoc.hpp"

using namespace std;

class Cell {
    int _row;
    int _col;
public:
    Cell(const int row, const int col);
    static Cell at(const int row, const int col);
    int row() const;
    int col() const;
    bool operator==(const Cell& other) const;
    friend ostream& operator <<(ostream &strm, const Cell& cell);
};

template<typename T>
class Grid {
    vector<vector<T>> _cells;
public:
    Grid(const vector<vector<T>>& cells);
    static Grid<int> from(const vector<string>& input);
    T get(const Cell& cell) const;
    int width() const;
    int height() const;
    long size() const;
    long countAllEqualTo(T value) const;
    unordered_set<Cell> capitalNeighbours(const Cell& cell) const;
    unordered_set<Cell> octantsNeighbours(const Cell& cell) const;
    class iterator: public std::iterator<
                    std::input_iterator_tag,   // iterator_category
                    Cell,                      // value_type
                    Cell,                      // difference_type
                    const Cell*,               // pointer
                    Cell                       // reference
                    >
    {
        const Grid& grid;
        Cell cell;
    public:
        explicit iterator(const Grid& _grid, Cell _cell = Cell::at(0, 0))
            : grid(_grid), cell(_cell.row(), _cell.col()) {}
        iterator& operator++() {
            long n = cell.row() * grid.width() + cell.col() + 1;
            cell = Cell(n / grid.width(), n % grid.width());
            return *this;
        }
        iterator operator++(int) {
            iterator retval = *this;
            ++(*this);
            return retval;
        }
        bool operator==(iterator other) const { return cell == other.cell; }
        bool operator!=(iterator other) const { return !(*this == other); }
        reference operator*() const { return cell; }
    };
    iterator begin() const { return iterator(*this, Cell::at(0, 0)); }
    iterator end() const { return iterator(*this, Cell::at(height(), 0)); }
protected:
    aoc::Range rowIndices() const;
    aoc::Range colIndices() const;
    unordered_set<Cell>
        neighbours(const Cell& cell, const set<pair<int, int>> deltas) const;
};

template<typename T>
Grid<T>::Grid(const vector<vector<T>>& cells) {
    _cells = cells;
}

template<typename T>
T Grid<T>::get(const Cell& cell) const {
    return _cells[cell.row()][cell.col()];
}

template<typename T>
int Grid<T>::width() const {
    return _cells[0].size();
}

template<typename T>
int Grid<T>::height() const {
    return _cells.size();
}

template<typename T>
long Grid<T>::size() const {
    return width() * height();
}

template<typename T>
aoc::Range Grid<T>::rowIndices() const {
    return aoc::Range(0, height() - 1);
}

template<typename T>
aoc::Range Grid<T>::colIndices() const {
    return aoc::Range(0, width() - 1);
}

template<typename T>
long Grid<T>::countAllEqualTo(const T value) const {
    long ans = 0;
    for (const int row : rowIndices()) {
        for (const int col : colIndices()) {
            if (_cells[row][col] == value) {
                ans++;
            }
        }
    }
    return ans;
}

template<>
struct std::hash<Cell> {
    std::size_t operator()(Cell const& cell) const noexcept {
        std::size_t h1 = std::hash<int>{}(cell.row());
        std::size_t h2 = std::hash<int>{}(cell.col());
        return h1 ^ (h2 << 1);
    }
};

template<typename T>
unordered_set<Cell> Grid<T>::neighbours(
        const Cell& cell, const set<pair<int, int>> deltas) const {
    unordered_set<Cell> n;
    for (const auto& delta : deltas) {
        int rr = cell.row() + delta.first;
        int cc = cell.col() + delta.second;
        if (rr >= 0 && rr < height() && cc >= 0 && cc < width()) {
            n.insert(Cell::at(rr, cc));
        }
    }
    return n;
}

template<typename T>
unordered_set<Cell> Grid<T>::capitalNeighbours(const Cell& cell) const {
    const set<pair<int, int>> deltas = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    return neighbours(cell, deltas);
}

template<typename T>
unordered_set<Cell> Grid<T>::octantsNeighbours(const Cell& cell) const {
    const set<pair<int, int>> deltas = {
        { 0, 1}, {1, 0}, {0, -1}, {-1,  0},
        {-1, 1}, {1, 1}, {1, -1}, {-1, -1}
    };
    return neighbours(cell, deltas);
}
#endif
