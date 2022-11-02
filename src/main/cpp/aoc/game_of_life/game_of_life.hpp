#ifndef _AOC_GAME_OF_LIFE_HPP_
#define _AOC_GAME_OF_LIFE_HPP_

#include <bits/stdc++.h>

using namespace std;
using Cell = tuple<int, int, int, int>;
using Range = pair<int, int>;

class Type {
    public:
        virtual vector<Cell> cells(const set<Cell>& alive) = 0;
        virtual uint getNeighbourCount(const Cell& cell, const set<Cell>& alive) = 0;
};

class InfiniteGrid : public Type {
    private:
        uint m_dim;
        vector<Cell> m_deltas;

    public:
        InfiniteGrid(const uint dim);
        virtual vector<Cell> cells(const set<Cell>& alive);
        virtual uint getNeighbourCount(const Cell& cell, const set<Cell>& alive);

    private:
        vector<Cell> product(
                const Range& range1,
                const Range& range2,
                const Range& range3,
                const Range& range4);
        Range expand(const set<Cell>& alive, const uint idx);
};

class Rules {
    public:
        virtual bool alive(const Cell& cell, const uint count, const set<Cell>& alive) = 0;
};

class ClassicRules : public Rules {
    public:
        virtual bool alive(const Cell& cell, const uint count, const set<Cell>& alive) {
            return count == 3 || (count == 2 && alive.find(cell) != alive.end());
        }
};

class GameOfLife {
    private:
        set<Cell> m_alive;
        Type* m_type;
        Rules* m_rules;

    public:
        GameOfLife(const set<Cell>& alive, Type* type, Rules* rules);
        GameOfLife next();
        set<Cell> getAlive() const;

    private:
        bool isAlive(const Cell& cell);
};

#endif
