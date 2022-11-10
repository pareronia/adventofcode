#ifndef _AOC_HPP_
#define _AOC_HPP_

#include <bits/stdc++.h>

template<typename T1, typename T2>
struct std::hash<std::pair<T1, T2>> {
    std::size_t operator()(std::pair<T1, T2> const &p) const {
        std::size_t h1 = std::hash<T1>{}(p.first);
        std::size_t h2 = std::hash<T2>{}(p.second);
        return h1 ^ (h2 << 1);
    }
};

using sys_clock = std::chrono::system_clock;
using sys_nanoseconds = std::chrono::duration<double, std::nano>;
using namespace std;

namespace aoc {
    vector<vector<string>> toBlocks(vector<string> lines);
    vector<int> getNumbers(const string s);
    vector<string> split(const string& s);
    vector<string> split(const string& s, const string& delim);

    class Range {
        int from;
        int to;
    public:
        Range(const int _from, const int _to) {
            assert(_from <= _to);
            from = _from;
            to = _to;
        }
        // member typedefs provided through inheriting from std::iterator
        class iterator: public std::iterator<
                        std::input_iterator_tag,   // iterator_category
                        int,                      // value_type
                        int,                      // difference_type
                        const int*,               // pointer
                        int                       // reference
                        >
        {
            int num;
        public:
            explicit iterator(int _num = 0) : num(_num) {}
            iterator& operator++() {num = num + 1; return *this;}
            iterator operator++(int) {iterator retval = *this; ++(*this); return retval;}
            bool operator==(iterator other) const {return num == other.num;}
            bool operator!=(iterator other) const {return !(*this == other);}
            reference operator*() const {return num;}
        };
        iterator begin() {return iterator(from);}
        iterator end() {return iterator(to >= from ? to + 1 : to - 1);}
    };
}

#define DEBUG(Message) \
if (getenv("NDEBUG") == NULL) { \
    cerr << Message << endl; \
}

#define MAIN(Year, Day)                                                        \
pair<string, double> main_part(const uint part, const vector<string>& input) { \
    const auto start = sys_clock::now();                                       \
    const string answer = to_string(part == 1 ? part1(input) : part2(input));  \
    const sys_nanoseconds duration = sys_clock::now() - start;                 \
    return make_pair(answer, duration.count());                                \
}                                                                              \
                                                                               \
int main(int argc, char **argv) {                                              \
    if (argc > 1) {                                                            \
        const uint part = stoi(argv[1]);                                       \
        vector<string> input;                                                  \
        aocd::sys::getFileContent(argv[2], input);                             \
        pair<string, double> response = main_part(part, input);                \
        cout << "{\"part" << part << "\":";                                    \
        cout << "{\"answer\":\"" << response.first << "\",";                   \
        cout << "\"duration\":" << response.second << "}}" << endl;            \
    } else {                                                                   \
        samples();                                                             \
                                                                               \
        const vector<string>& input = aocd::puzzle::getInputData(Year, Day);   \
        pair<string, double> part_1 = main_part(1, input);                     \
        cout << "Part 1: " << part_1.first;                                    \
        cout << ", took: " << part_1.second / 1000000 << " ms" << endl;        \
        pair<string, double> part_2 = main_part(2, input);                     \
        cout << "Part 2: " << part_2.first;                                    \
        cout << ", took: " << part_2.second / 1000000 << " ms" << endl;        \
        return aocd::puzzle::check(Year, Day, part_1.first, part_2.first);     \
    }                                                                          \
    return 0;                                                                  \
}

#endif
