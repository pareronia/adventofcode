#include <bits/stdc++.h>
#include "game_of_life.hpp"

InfiniteGrid::InfiniteGrid(const uint dim) {
    m_dim = dim;
    m_deltas = product(
            Range(-1, 1), Range(-1, 1),
            Range(-1, 1), Range(-1, 1));
}

Range InfiniteGrid::expand(const set<Cell>& alive, const uint idx) {
    set<int> values;
    for (const Cell& cell : alive) {
        switch(idx) {
        case 0:
            values.insert(get<0>(cell));
            break;
        case 1:
            values.insert(get<1>(cell));
            break;
        case 2:
            values.insert(get<2>(cell));
            break;
        case 3:
            values.insert(get<3>(cell));
            break;
        }
    }
    auto minmax = minmax_element(values.begin(), values.end());
    auto ans = Range(*minmax.first - 1, *minmax.second + 1);
    return ans;
}

vector<Cell> InfiniteGrid::product(
        const Range& range1,
        const Range& range2,
        const Range& range3,
        const Range& range4
) {
    vector<Cell> cells;
    for (int x = range1.first; x <= range1.second; x++) {
        for (int y = range2.first; y <= range2.second; y++) {
            for (int z = range3.first; z <= range3.second; z++) {
                for (int w = range4.first; w <= range4.second; w++) {
                    cells.push_back(Cell(x, y, z, w));
                }
            }
        }
    }
    return cells;
}

vector<Cell> InfiniteGrid::cells(const set<Cell>& alive) {
    return product(
        expand(alive, 0),
        expand(alive, 1),
        m_dim >= 3 ? expand(alive, 2) : Range(0, 0),
        m_dim >= 4 ? expand(alive, 3) : Range(0, 0));
}

uint InfiniteGrid::getNeighbourCount(const Cell& cell, const set<Cell>& alive) {
    uint cnt = 0;
    int x, y, z, w;
    tie(x, y, z, w) = cell;
    const Cell zeroes = Cell(0, 0, 0, 0);
    for (const Cell& delta : m_deltas) {
        int dx, dy, dz, dw;
        if (delta == zeroes) {
            continue;
        }
        tie(dx, dy, dz, dw) = delta;
        const Cell& n = Cell(x + dx, y + dy, z + dz, w + dw);
        if (alive.find(n) != alive.end()) {
            cnt++;
        }
    }
    return cnt;
}

GameOfLife::GameOfLife(const set<Cell>& alive, Type* type, Rules* rules) {
    m_alive = alive;
    m_type = type;
    m_rules = rules;
}

set<Cell> GameOfLife::getAlive() const {
    return m_alive;
}

GameOfLife GameOfLife::next() {
    set<Cell> newAlive;
    for (const Cell& cell : m_type->cells(m_alive)) {
        if (isAlive(cell)) {
            newAlive.insert(cell);
        }
    }
    return GameOfLife(newAlive, m_type, m_rules);
}

bool GameOfLife::isAlive(const Cell& cell) {
    const uint count = m_type->getNeighbourCount(cell, m_alive);
    return m_rules->alive(cell, count, m_alive);
}
