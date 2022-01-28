#include "assert.h"
#include <iostream>
#include <string>
#include <vector>
#include "../../aoc/aoc.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;

const vector<string> TEST = {
"00100",
"11110",
"10110",
"10111",
"10101",
"01111",
"00111",
"11100",
"10000",
"11001",
"00010",
"01010"
};

class BitCount {
    private:
        uint ones;
        uint zeroes;

    public:
        BitCount(const uint ones, const uint zeroes) {
            this->ones = ones;
            this->zeroes = zeroes;
        }
        
        char leastCommon() const {
            return this->ones < this->zeroes ? '1' : '0';
        }

        char mostCommon() const {
            return this->ones >= this->zeroes ? '1' : '0';
        }
};

BitCount bitCounts(const vector<string> &strings, const uint pos) {
    int zeroes = 0;
    for (const string s : strings) {
        if (s[pos] == '0') {
            zeroes++;
        }
    }
    return BitCount(strings.size() - zeroes, zeroes);
}

uint ans(const string& s1, const string& s2) {
    return stoi(s1, 0, 2) * stoi(s2, 0, 2);
}

int part1(const vector<string> &input) {
    string gamma = "";
    string epsilon = "";
    for (uint i = 0; i < input[0].length(); i++) {
        BitCount bitCount = bitCounts(input, i);
        gamma += bitCount.mostCommon();
        epsilon += bitCount.leastCommon();
    }
    return ans(gamma, epsilon);
}

string reduce(vector<string> input, const bool mostCommon) {
    const uint size = input.size();
    for (uint pos = 0; pos < size; pos++) {
        const BitCount bitCount = bitCounts(input, pos);
        const char toKeep = mostCommon
            ? bitCount.mostCommon()
            : bitCount.leastCommon();
        vector<string> reduced;
        for (const string s : input) {
            if (s[pos] == toKeep) {
                reduced.push_back(s);
            }
        }
        if (reduced.size() == 1) {
            return reduced[0];
        }
        assert(!reduced.empty());
        input = move(reduced);
    }
    throw "illegal state";
}

int part2(vector<string> input) {
    const string o2 = reduce(input, true);
    const string co2 = reduce(input, false);
    return ans(o2, co2);
}

void samples() {
    assert(part1(TEST) == 198);
    assert(part2(TEST) == 230);
}

MAIN(2021, 3)
