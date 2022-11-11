#ifndef _AOC_GRAPH_HPP_
#define _AOC_GRAPH_HPP_

#include <bits/stdc++.h>

using namespace std;

template<typename T>
class State {
    public:
        State(const T _node, const ulong _cost)
            : node(std::move(_node)), cost(_cost) {}
        bool operator >(const State<T>& other) const {
            return compare(other) > 0;
        }
        bool operator ==(const State<T>& other) const {
            return this->compare(other) == 0;
        }
        ostream& operator <<(ostream& strm) {
            strm << "State[node: " << node << ", cost: " << cost << "]";
            return strm;
        }
        T node;
        ulong cost;
    private:
        int compare(const State<T>& other) const {
            if (cost == other.cost) {
                return 0;
            } else if (cost > other.cost) {
                return 1;
            } else {
                return -1;
            }
        }
};

template<typename T>
struct std::hash<State<T>> {
    std::size_t operator ()(const State<T>& state) const noexcept {
        return std::hash<T>{}(state.node);
    }
};

class AStar {
private:
    template<typename T>
    static ulong getOrDefault(
            const unordered_map<T, ulong>& map,
            const T& node,
            const ulong defValue
    ) {
        auto it = map.find(node);
        return it == map.end() ? defValue : it->second;
    }

    template<typename T>
    static vector<T> createPath(
            const unordered_map<T, T>& parent,
            const T& end
    ) {
        T curr = end;
        vector<T> path({end});
        while (parent.find(curr) != parent.end()) {
            curr = parent.at(curr);
            path.push_back(curr);
        }
        return path;
    }

public:
    template<typename T>
    struct Result {
        Result(vector<T> path_, ulong cost_) {
            path = path_;
            cost = cost_;
        }
        vector<T> path;
        ulong cost;
    };

    template<typename T>
    static Result<T> execute(
            const T& start,
            const function<bool(T)> end,
            const function<unordered_set<T>(T)> adjacent,
            const function<ulong(T)> cost
    ) {
        priority_queue<State<T>, deque<State<T>>, greater<State<T>>> q;
        q.push(State<T>(start, 0));
        unordered_map<T, ulong> best({{start, 0}});
        unordered_map<T, T> parent;
        while (!q.empty()) {
            const State<T> state = q.top();
            q.pop();
            if (end(state.node)) {
                return Result<T>(createPath(parent, state.node), state.cost);
            }
            const ulong total = getOrDefault(best, state.node, LONG_MAX);
            for (const T& n : adjacent(state.node)) {
                const ulong newRisk = total + cost(n);
                if (newRisk < getOrDefault(best, n, LONG_MAX)) {
                    best[n] = newRisk;
                    parent.insert({n, state.node});
                    q.push(State<T>(n, newRisk));
                }
            }
        }
        throw "Unsolvable";
    }
};
#endif //_AOC_GRAPH_HPP_
