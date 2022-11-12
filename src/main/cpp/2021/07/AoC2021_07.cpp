#include <assert.h>

#include <algorithm>
#include <functional>
#include <numeric>

#include "../../aoc/aoc.hpp"

using namespace std;

vector<int> parse(const vector<string>& input) {
    assert(input.size() == 1);
    return aoc::getNumbers(input[0]);
}

int solve(const vector<string>& input, const function<int(int, int)>& calc) {
    const vector<int>& nums = parse(input);
    const auto sum = [&nums, &calc](int a) {
        return accumulate(
            nums.begin(), nums.end(), 0,
            [&calc, a](const int x, const int b) { return x + calc(a, b); });
    };
    const auto min_max = minmax_element(nums.begin(), nums.end());
    auto range = aoc::Range::rangeClosed(*min_max.first, *min_max.second);
    return accumulate(
        range.begin(), range.end(), INT_MAX,
        [&sum](const int y, const int a) { return min(y, sum(a)); });
}

int part1(const vector<string>& input) {
    return solve(input, [](const int a, const int b) { return abs(a - b); });
}

int part2(const vector<string>& input) {
    return solve(input, [](const int a, const int b) {
        const int diff = abs(a - b);
        return (diff * (diff + 1)) / 2;
    });
}

const vector<string> TEST = {"16,1,2,0,4,2,7,1,2,14"};

void samples() {
    assert(part1(TEST) == 37);
    assert(part2(TEST) == 168);
}

MAIN(2021, 7)
