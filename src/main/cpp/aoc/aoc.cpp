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
            current_split.emplace_back(str);
            continue;
        }

        if (current_split.empty()) {
            continue;
        }

        splits.emplace_back(current_split);
        current_split.clear();
    }
    if (!current_split.empty()) {
        splits.emplace_back(current_split);
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

vector<string> aoc::split(const string& s) {
    istringstream iss(s);
    return {istream_iterator<string>{iss}, istream_iterator<string>{}};
}

vector<string> aoc::split(const string& s, const string& delim) {
    if (delim.empty()) {
        throw "Expected non-empty delimiter";
    }
    vector<string> ans;
    uint start = 0;
    int end = s.find(delim);
    while (end != -1) {
        ans.push_back(s.substr(start, end - start));
        start = end + delim.size();
        end = s.find(delim, start);
    }
    ans.push_back(s.substr(start, end - start));
    return ans;
}
