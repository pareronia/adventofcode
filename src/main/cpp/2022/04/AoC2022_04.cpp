#include <assert.h>

#include <algorithm>
#include <string>
#include <vector>

#include "../../aoc/aoc.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;
using namespace aoc;

int solve(const vector<string>& input,
          const function<bool(const Range&, const Range&)>& test) {
    return count_if(input.begin(), input.end(), [&test](const string& line) {
        const vector<int>& nums = getNumbers(line);
        const Range range1 = Range::rangeClosed(nums[0], nums[1]);
        const Range range2 = Range::rangeClosed(nums[2], nums[3]);
        return test(range1, range2);
    });
}

int part1(const vector<string>& input) {
    return solve(input, [](const Range& range1, const Range& range2) {
        return range1.contains(range2) || range2.contains(range1);
    });
}

int part2(const vector<string>& input) {
    return solve(input, [](const Range& range1, const Range& range2) {
        return range1.isOverlappedBy(range2);
    });
}

// clang-format off
const vector<string> TEST = {
"2-4,6-8",
"2-3,4-5",
"5-7,7-9",
"2-8,3-7",
"6-6,4-6",
"2-6,4-8",
};
// clang-format on

void samples() {
    assert(part1(TEST) == 2);
    assert(part2(TEST) == 4);
}

MAIN(2022, 4)
