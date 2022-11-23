#include <assert.h>

#include <algorithm>
#include <climits>
#include <functional>
#include <numeric>
#include <string>
#include <vector>

#include "../../aoc/aoc.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;

class Present {
   public:
    Present(const int length, const int width, const int height)
        : length(length), width(width), height(height) {}
    const int length;
    const int width;
    const int height;
};

vector<Present> parse(const vector<string>& input) {
    vector<Present> presents;
    for (const string& line : input) {
        const vector<string>& splits = aoc::split(line, "x");
        presents.push_back(
            Present(stoi(splits[0]), stoi(splits[1]), stoi(splits[2])));
    }
    return presents;
}

int calculateRequiredArea(const Present& present) {
    const vector<int> sides = {2 * present.length * present.width,
                               2 * present.width * present.height,
                               2 * present.height * present.length};
    int sum = 0, minimum = INT_MAX;
    for (int side : sides) {
        sum += side;
        minimum = min(side, minimum);
    }
    return sum + minimum / 2;
}

int calculateRequiredLength(const Present& present) {
    const vector<int> circumferences = {2 * (present.length + present.width),
                                        2 * (present.width + present.height),
                                        2 * (present.height + present.length)};
    return *min_element(circumferences.begin(), circumferences.end()) +
           present.length * present.width * present.height;
}

int solve(const vector<string>& input, const function<int(Present)>& strategy) {
    const vector<Present>& presents = parse(input);
    return accumulate(
        presents.begin(), presents.end(), 0,
        [&strategy](const int a, const Present& b) { return a + strategy(b); });
}

int part1(const vector<string>& input) {
    return solve(input, calculateRequiredArea);
}

int part2(const vector<string>& input) {
    return solve(input, calculateRequiredLength);
}

const vector<string> TEST1 = {"2x3x4"};
const vector<string> TEST2 = {"1x1x10"};

void samples() {
    assert(part1(TEST1) == 58);
    assert(part1(TEST2) == 43);
    assert(part2(TEST1) == 34);
    assert(part2(TEST2) == 14);
}

MAIN(2015, 2)
