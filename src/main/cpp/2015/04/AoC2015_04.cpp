#include <assert.h>

#include <string>
#include <vector>

#include "../../aoc/aoc.hpp"
#include "../../aoc/md5/md5.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;

int findMd5StartingWithZeroes(const string& seed, const int zeroes) {
    for (int i = 0;; ++i) {
        const string& text = seed + to_string(i);
        MD5 md5;
        md5.update(text.c_str(), text.length());
        md5.finalize();
        const unsigned char* digest = md5.getDigest();
        int cnt = 0;
        for (int j = 0; j < zeroes / 2 + zeroes % 2; j++) {
            unsigned char ch = *(digest + j);
            if ((ch & 0xF0) == 0) {
                cnt++;
                if ((ch & 0x0F) == 0) {
                    cnt++;
                    continue;
                }
            }
            break;
        }
        if (cnt == zeroes) {
            return i;
        }
    }
}

int part1(const vector<string>& input) {
    return findMd5StartingWithZeroes(input[0], 5);
}

int part2(const vector<string>& input) {
    return findMd5StartingWithZeroes(input[0], 6);
}

const vector<string> TEST1 = {"abcdef"};
const vector<string> TEST2 = {"pqrstuv"};

void samples() {
    assert(part1(TEST1) == 609043);
    assert(part1(TEST2) == 1048970);
}

MAIN(2015, 4)
