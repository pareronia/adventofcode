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
        cout << "Part 1: " << part1(input) << endl;                         \
        cout << "Part 2: " << part2(input) << endl;                         \
    }                                                                       \
    return 0;                                                               \
}

#endif
