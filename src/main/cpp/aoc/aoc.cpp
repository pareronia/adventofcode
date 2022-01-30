#include "aoc.hpp"
#include <string>
#include <vector>
#include <regex>

using namespace std;

vector<vector<string>> aoc::toBlocks(vector<string> lines) {
    vector<vector<string>> splits;
    vector<string> current_split;
    for (string& str : lines) {
        if (!str.empty()) {
            current_split.emplace_back(move(str));
            continue;
        }

        if (current_split.empty()) {
            continue;
        }

        splits.emplace_back(move(current_split));
        current_split.clear();
    }
    if (!current_split.empty()) {
        splits.emplace_back(move(current_split));
    }
    return splits;
}

vector<int> aoc::getNumbers(const string s) {
    regex re("[0-9]+");
    vector<int> numbers;
    for (sregex_iterator j = sregex_iterator(s.begin(), s.end(), re);
            j != sregex_iterator();
            ++j)
    {
        smatch m = *j;
        numbers.push_back(stoi(m.str()));
    }
    return numbers;
}

