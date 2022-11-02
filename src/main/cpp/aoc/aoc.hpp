#ifndef _AOC_HPP_
#define _AOC_HPP_

#include <string>
#include <vector>

using namespace std;

namespace aoc {
    vector<vector<string>> toBlocks(vector<string> lines);
    vector<int> getNumbers(const string s);
}

#define DEBUG(Message) \
if (getenv("NDEBUG") == NULL) { \
    cerr << Message << endl; \
}

#define MAIN(Year, Day)                                                     \
int main(int argc, char **argv) {                                           \
    if (argc > 1) {                                                         \
        const uint part = stoi(argv[1]);                                    \
        vector<string> input;                                               \
        aocd::sys::getFileContent(argv[2], input);                          \
        if (part == 1) {                                                    \
            cout << part1(input) << endl;                                   \
        } else {                                                            \
            cout << part2(input) << endl;                                   \
        }                                                                   \
    } else {                                                                \
        samples();                                                          \
                                                                            \
        const vector<string> input = aocd::puzzle::getInputData(Year, Day); \
        const string part_1 = to_string(part1(input));                      \
        cout << "Part 1: " << part_1 << endl;                               \
        const string part_2 = to_string(part2(input));                      \
        cout << "Part 2: " << part_2 << endl;                               \
        return aocd::puzzle::check(Year, Day, part_1, part_2);              \
    }                                                                       \
    return 0;                                                               \
}

#endif
