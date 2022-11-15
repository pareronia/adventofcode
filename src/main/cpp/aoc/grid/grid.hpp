#ifndef _AOC_GRID_HPP_
#define _AOC_GRID_HPP_

#include <algorithm>
#include <set>
#include <string>
#include <unordered_set>
#include <vector>

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
    friend ostream& operator<<(ostream& strm, const Cell& cell);
};

template <typename T>
class Grid {
    vector<vector<T>> _cells;

   public:
    Grid(const vector<vector<T>>& cells);
    static Grid<T> from(const vector<string>& input);
    bool operator==(const Grid<T>& other) const;
    string toString() const;
    T get(const Cell& cell) const;
    void setValue(const Cell& cell, T value);
    Grid<T> replace(const T& from, const T& to) const;
    void increment(const Cell& cell, uint amount = 1);
    int width() const;
    int height() const;
    long size() const;
    bool isSquare() const;
    long countAllEqualTo(T value) const;
    unordered_set<Cell> capitalNeighbours(const Cell& cell) const;
    unordered_set<Cell> octantsNeighbours(const Cell& cell) const;
    Grid<T> subGrid(const Cell& from, const Cell& to) const;
    vector<vector<Grid<T>>> divide(const uint partSize) const;
    static Grid<T> merge(const vector<vector<Grid<T>>>& grids);
    Grid<T> flipHorizontally() const;
    Grid<T> rotate() const;
    class iterator
        : public std::iterator<std::input_iterator_tag,  // iterator_category
                               Cell,                     // value_type
                               Cell,                     // difference_type
                               const Cell*,              // pointer
                               Cell                      // reference
                               > {
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
    class permutations
        : public std::iterator<std::input_iterator_tag,  // iterator_category
                               Grid<T>,                  // value_type
                               Grid<T>,                  // difference_type
                               const Grid<T>*,           // pointer
                               Grid<T>                   // reference
                               > {
        Grid<T> grid;
        uint cnt;

       public:
        explicit permutations(const Grid<T> _grid, uint _cnt)
            : grid(_grid), cnt(_cnt) {}
        permutations& operator++() {
            cnt++;
            if (cnt == 4) {
                grid = grid.flipHorizontally();
            } else {
                grid = grid.rotate();
            }
            return *this;
        }
        permutations operator++(int) {
            permutations retval = *this;
            ++(*this);
            return retval;
        }
        bool operator==(permutations other) const { return cnt == other.cnt; }
        bool operator!=(permutations other) const { return !(*this == other); }
        Grid<T> operator*() const { return grid; }
    };
    permutations permutations_begin() const {
        validateIsSquare();
        return permutations(*this, 0);
    }
    permutations permutations_end() const { return permutations(*this, 8); }

   protected:
    aoc::Range rowIndices() const;
    aoc::Range rowIndicesReversed() const;
    aoc::Range colIndices() const;
    aoc::Range colIndicesReversed() const;
    unordered_set<Cell> neighbours(const Cell& cell,
                                   const set<pair<int, int>> deltas) const;
    void validateIsSquare() const;
};

template <typename T>
Grid<T>::Grid(const vector<vector<T>>& cells) {
    _cells = cells;
}

template <typename T>
bool Grid<T>::operator==(const Grid<T>& other) const {
    if (height() != other.height() || width() != other.width()) {
        return false;
    }
    for (const int row : rowIndices()) {
        for (const int col : colIndices()) {
            if (_cells[row][col] != other.get(Cell::at(row, col))) {
                return false;
            }
        }
    }
    return true;
}

template <typename T>
struct std::hash<Grid<T>> {
    std::size_t operator()(Grid<T> const& grid) const noexcept {
        int result = 1;
        for (const Cell& cell : grid) {
            result = 31 * result + std::hash<T>{}(grid.get(cell));
        }
        return result;
    }
};

template <typename T>
T Grid<T>::get(const Cell& cell) const {
    return _cells[cell.row()][cell.col()];
}

template <typename T>
void Grid<T>::increment(const Cell& cell, uint amount) {
    _cells[cell.row()][cell.col()] += amount;
}

template <typename T>
void Grid<T>::setValue(const Cell& cell, T value) {
    _cells[cell.row()][cell.col()] = value;
}

template <typename T>
Grid<T> Grid<T>::replace(const T& from, const T& to) const {
    vector<vector<T>> cells;
    for (int rr : rowIndices()) {
        vector<T> row;
        transform(_cells[rr].begin(), _cells[rr].end(), back_inserter(row),
                  [&from, &to](const T& value) {
                      return value == from ? to : value;
                  });
        cells.push_back(row);
    }
    return Grid(cells);
}

template <typename T>
int Grid<T>::width() const {
    return height() == 0 ? 0 : _cells[0].size();
}

template <typename T>
int Grid<T>::height() const {
    return _cells.size();
}

template <typename T>
long Grid<T>::size() const {
    return width() * height();
}

template <typename T>
bool Grid<T>::isSquare() const {
    return width() == height();
}

template <typename T>
void Grid<T>::validateIsSquare() const {
    if (!isSquare()) {
        throw "Grid should be square.";
    }
}

template <typename T>
aoc::Range Grid<T>::rowIndices() const {
    return aoc::Range::range(height());
}

template <typename T>
aoc::Range Grid<T>::rowIndicesReversed() const {
    return aoc::Range::rangeClosed(height() - 1, 0, -1);
}

template <typename T>
aoc::Range Grid<T>::colIndices() const {
    return aoc::Range::range(width());
}

template <typename T>
aoc::Range Grid<T>::colIndicesReversed() const {
    return aoc::Range::rangeClosed(width() - 1, 0, -1);
}

template <typename T>
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

template <>
struct std::hash<Cell> {
    std::size_t operator()(Cell const& cell) const noexcept {
        std::size_t h1 = std::hash<int>{}(cell.row());
        std::size_t h2 = std::hash<int>{}(cell.col());
        return h1 ^ (h2 << 1);
    }
};

template <typename T>
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

template <typename T>
unordered_set<Cell> Grid<T>::capitalNeighbours(const Cell& cell) const {
    const set<pair<int, int>> deltas = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    return neighbours(cell, deltas);
}

template <typename T>
unordered_set<Cell> Grid<T>::octantsNeighbours(const Cell& cell) const {
    const set<pair<int, int>> deltas = {{0, 1},  {1, 0}, {0, -1}, {-1, 0},
                                        {-1, 1}, {1, 1}, {1, -1}, {-1, -1}};
    return neighbours(cell, deltas);
}

template <typename T>
Grid<T> Grid<T>::subGrid(const Cell& from, const Cell& to) const {
    if (from.row() > to.row() || from.col() > to.col()) {
        throw "Invalid coordinates";
    }
    const int endRow = min(to.row(), height());
    const int endCol = min(to.col(), width());
    const int height = endRow - from.row();
    const int width = endCol - from.col();
    vector<vector<T>> cells;
    for (int r = from.row(), i = 0; r < endRow && i < height; r++, i++) {
        vector<T> row;
        for (int c = from.col(), j = 0; c < endCol && j < width; c++, j++) {
            row.push_back(_cells[r][c]);
        }
        cells.push_back(row);
    }
    return Grid(cells);
}

template <typename T>
vector<vector<Grid<T>>> Grid<T>::divide(const uint partSize) const {
    validateIsSquare();
    const uint parts = height() / partSize;
    vector<vector<Grid<T>>> subGrids;
    for (uint r : aoc::Range::range(parts)) {
        vector<Grid<T>> row;
        for (uint c : aoc::Range::range(parts)) {
            const Grid<T> sub =
                subGrid(Cell::at(r * partSize, c * partSize),
                        Cell::at((r + 1) * partSize, (c + 1) * partSize));
            row.push_back(sub);
        }
        subGrids.push_back(row);
    }
    return subGrids;
}

template <typename T>
Grid<T> Grid<T>::merge(const vector<vector<Grid<T>>>& grids) {
    unordered_set<uint> heights, widths;
    for (const vector<Grid<T>>& row : grids) {
        for (const Grid<T>& grid : row) {
            heights.insert(grid.height());
            widths.insert(grid.width());
        }
    }
    if (heights.size() > 1 || widths.size() > 1) {
        throw "Grids should be same size";
    }
    vector<vector<T>> cells;
    const uint fullHeight = grids.size() * grids[0][0].height();
    const uint fullWidth = grids[0].size() * grids[0][0].width();
    const uint height = grids[0][0].height();
    const uint width = grids[0][0].width();
    for (uint r : aoc::Range::range(fullHeight)) {
        const auto rDiv = div((int)r, (int)height);
        const int RR = rDiv.quot;
        const int rr = rDiv.rem;
        vector<T> row;
        for (uint c : aoc::Range::range(fullWidth)) {
            const auto cDiv = div((int)c, (int)width);
            const int CC = cDiv.quot;
            const int cc = cDiv.rem;
            row.push_back(grids[RR][CC].get(Cell::at(rr, cc)));
        }
        cells.push_back(row);
    }
    return Grid(cells);
}

template <typename T>
Grid<T> Grid<T>::flipHorizontally() const {
    vector<vector<T>> cells;
    for (int rr : rowIndicesReversed()) {
        vector<T> row;
        copy(_cells.at(rr).begin(), _cells.at(rr).end(), back_inserter(row));
        cells.push_back(row);
    }
    return Grid(cells);
}

template <typename T>
Grid<T> Grid<T>::rotate() const {
    vector<vector<T>> cells;
    for (int cc : colIndices()) {
        vector<T> row;
        for (int rr : rowIndicesReversed()) {
            row.push_back(get(Cell::at(rr, cc)));
        }
        cells.push_back(row);
    }
    return Grid(cells);
}

#endif
