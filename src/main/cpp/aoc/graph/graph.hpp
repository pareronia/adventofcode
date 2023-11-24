#ifndef _AOC_GRAPH_HPP_
#define _AOC_GRAPH_HPP_

#include <assert.h>

#include <deque>
#include <functional>
#include <queue>
#include <unordered_map>
#include <unordered_set>

#include "../../aoc/aoc.hpp"

using namespace std;
template <typename T>
using Distances = unordered_map<T, ulong>;
template <typename T>
using Paths = unordered_map<T, T>;

template <typename T>
class State {
    friend class AStar;

   public:
    State(const T _node, const ulong _cost)
        : node(std::move(_node)), cost(_cost) {}
    bool operator>(const State<T>& other) const { return compare(other) > 0; }
    bool operator==(const State<T>& other) const {
        return this->compare(other) == 0;
    }
    ostream& operator<<(ostream& strm);

   private:
    T node;
    ulong cost;
    int compare(const State<T>& other) const;
};

template <typename T>
struct std::hash<State<T>> {
    std::size_t operator()(const State<T>& state) const noexcept {
        return std::hash<T>{}(state.node);
    }
};

template <typename T>
int State<T>::compare(const State<T>& other) const {
    if (cost == other.cost) {
        return 0;
    } else if (cost > other.cost) {
        return 1;
    } else {
        return -1;
    }
}

template <typename T>
ostream& State<T>::operator<<(ostream& strm) {
    strm << "State[node: " << node << ", cost: " << cost << "]";
    return strm;
}

class AStar {
   public:
    template <typename T>
    class Result {
       public:
        Result(const T& source_, const Distances<T>& distances_,
               const Paths<T>& paths_)
            : source(source_), distances(distances_), paths(paths_) {}
        Distances<T> getDistances() const { return distances; }
        Paths<T> getPaths() const { return paths; }
        vector<T> getPath(const T& t) const;

       private:
        const T source;
        const Distances<T> distances;
        const Paths<T> paths;
    };
    template <typename T>
    static Result<T> execute(const T& start, const function<bool(T)> end,
                             const function<unordered_set<T>(T)> adjacent,
                             const function<ulong(T)> cost);

   private:
    template <typename T>
    static ulong getOrDefault(const Distances<T>& map, const T& node,
                              const ulong defValue);
};

template <typename T>
vector<T> AStar::Result<T>::getPath(const T& t) const {
    vector<T> p;
    T parent = t;
    if (t != source) {
        while (parent != source) {
            p.insert(p.begin(), parent);
            parent = paths[parent];
        }
        p.insert(p.begin(), source);
    } else {
        p.push_back(source);
    }
    return p;
}

template <typename T>
AStar::Result<T> AStar::execute(const T& start, const function<bool(T)> end,
                                const function<unordered_set<T>(T)> adjacent,
                                const function<ulong(T)> cost) {
    priority_queue<State<T>, deque<State<T>>, greater<State<T>>> q;
    q.push(State<T>(start, 0));
    Distances<T> best({{start, 0}});
    Paths<T> parent;
    while (!q.empty()) {
        const State<T> state = q.top();
        q.pop();
        if (end(state.node)) {
            break;
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
    return Result<T>(start, best, parent);
}

template <typename T>
ulong AStar::getOrDefault(const Distances<T>& map, const T& node,
                          const ulong defValue) {
    auto it = map.find(node);
    return it == map.end() ? defValue : it->second;
}

#endif  //_AOC_GRAPH_HPP_
