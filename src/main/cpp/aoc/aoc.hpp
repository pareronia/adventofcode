#ifndef _AOC_HPP_
#define _AOC_HPP_

#include <chrono>
#include <climits>
#include <iostream>
#include <iterator>
#include <string>
#include <vector>

#include "../aocd/aocd.hpp"

template <typename T1, typename T2>
struct std::hash<std::pair<T1, T2>> {
    std::size_t operator()(std::pair<T1, T2> const& p) const {
        std::size_t h1 = std::hash<T1>{}(p.first);
        std::size_t h2 = std::hash<T2>{}(p.second);
        return h1 ^ (h2 << 1);
    }
};

using sys_clock = std::chrono::system_clock;
using sys_nanoseconds = std::chrono::duration<double, std::nano>;
using namespace std;

namespace aoc {
void printResult(const uint part, const pair<string, double>& result);
void jsonResult(const uint part, const pair<string, double>& result);
vector<vector<string>> toBlocks(vector<string> lines);
vector<int> getNumbers(const string s);
vector<string> split(const string& s);
vector<string> split(const string& s, const string& delim);

class Range {
    Range(const int from_, const int to_, const int step_)
        : from(from_), to(to_), step(step_) {
        if (step == 0) {
            throw "step should be != 0";
        }
        if (from < to && step < 0) {
            throw "step should be > 0";
        }
        if (from > to && step > 0) {
            throw "step should be < 0";
        }
    }
    const int from;
    const int to;
    const int step;

   public:
    static Range range(const int to) { return Range(0, to, 1); }
    static Range rangeClosed(const int to) { return Range(0, to + 1, 1); }
    static Range range(const int from, const int to, const int step = 1) {
        return Range(from, to, step);
    }
    static Range rangeClosed(const int from, const int to, const int step = 1) {
        return Range(from, from <= to ? to + 1 : to - 1, step);
    }
    // member typedefs provided through inheriting from std::iterator
    class iterator
        : public std::iterator<std::input_iterator_tag,  // iterator_category
                               int,                      // value_type
                               int,                      // difference_type
                               const int*,               // pointer
                               int                       // reference
                               > {
        const int _step;
        const int _to;
        int num;

       public:
        explicit iterator(int step_, int to_, int num_)
            : _step(step_), _to(to_), num(num_) {}
        iterator& operator++() {
            num += _step;
            if (num >= _to && _step > 0) {
                num = -INT_MAX;
            } else if (num <= _to && _step < 0) {
                num = INT_MAX;
            }
            return *this;
        }
        iterator operator++(int) {
            iterator retval = *this;
            ++(*this);
            return retval;
        }
        bool operator==(iterator other) const { return num == other.num; }
        bool operator!=(iterator other) const { return !(*this == other); }
        reference operator*() const { return num; }
    };
    iterator begin() const {
        if (from == to) {
            return end();
        }
        return iterator(step, to, from);
    }
    iterator end() const {
        return iterator(step, to, step > 0 ? -INT_MAX : INT_MAX);
    }
    bool contains(const int value) const { return from <= value && value < to; }
    bool contains(const Range& other) const {
        return contains(other.from) && contains(other.to - 1);
    }
    bool isOverlappedBy(const Range& other) const {
        return other.contains(from) || other.contains(to - 1) ||
               contains(other.from);
    }
};
}  // namespace aoc

#define DEBUG(Message)              \
    if (getenv("NDEBUG") == NULL) { \
        cerr << Message << endl;    \
    }

#define MAIN(Year, Day)                                                        \
    pair<string, double> main_part(const uint part,                            \
                                   const vector<string>& input) {              \
        const auto start = sys_clock::now();                                   \
        const string answer =                                                  \
            to_string(part == 1 ? part1(input) : part2(input));                \
        const sys_nanoseconds duration = sys_clock::now() - start;             \
        return make_pair(answer, duration.count());                            \
    }                                                                          \
                                                                               \
    int main(int argc, char** argv) {                                          \
        if (argc > 1) {                                                        \
            const uint part = stoi(argv[1]);                                   \
            vector<string> input;                                              \
            aocd::sys::getFileContent(argv[2], input);                         \
            aoc::jsonResult(part, main_part(part, input));                     \
        } else {                                                               \
            samples();                                                         \
                                                                               \
            const vector<string>& input =                                      \
                aocd::puzzle::getInputData(Year, Day);                         \
            const pair<string, double> part_1 = main_part(1, input);           \
            aoc::printResult(1, part_1);                                       \
            const pair<string, double> part_2 = main_part(2, input);           \
            aoc::printResult(2, part_2);                                       \
            return aocd::puzzle::check(Year, Day, part_1.first, part_2.first); \
        }                                                                      \
        return 0;                                                              \
    }

#define SMAIN(Year, Day)                                                       \
    pair<string, double> main_part(const uint part,                            \
                                   const vector<string>& input) {              \
        const auto start = sys_clock::now();                                   \
        const string answer = part == 1 ? part1(input) : part2(input);         \
        const sys_nanoseconds duration = sys_clock::now() - start;             \
        return make_pair(answer, duration.count());                            \
    }                                                                          \
                                                                               \
    int main(int argc, char** argv) {                                          \
        if (argc > 1) {                                                        \
            const uint part = stoi(argv[1]);                                   \
            vector<string> input;                                              \
            aocd::sys::getFileContent(argv[2], input);                         \
            aoc::jsonResult(part, main_part(part, input));                     \
        } else {                                                               \
            samples();                                                         \
                                                                               \
            const vector<string>& input =                                      \
                aocd::puzzle::getInputData(Year, Day);                         \
            const pair<string, double> part_1 = main_part(1, input);           \
            aoc::printResult(1, part_1);                                       \
            const pair<string, double> part_2 = main_part(2, input);           \
            aoc::printResult(2, part_2);                                       \
            return aocd::puzzle::check(Year, Day, part_1.first, part_2.first); \
        }                                                                      \
        return 0;                                                              \
    }

#endif
