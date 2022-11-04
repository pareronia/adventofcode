#ifndef _AOC_HPP_
#define _AOC_HPP_

#include <bits/stdc++.h>

using sys_clock = std::chrono::system_clock;
using sys_nanoseconds = std::chrono::duration<double, std::nano>;
using namespace std;

namespace aoc {
    vector<vector<string>> toBlocks(vector<string> lines);
    vector<int> getNumbers(const string s);
    vector<string> split(const string& s);
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
