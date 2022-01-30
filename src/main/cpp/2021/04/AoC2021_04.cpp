#include "assert.h"
#include <iostream>
#include <string>
#include <vector>
#include "../../aoc/aoc.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;

const vector<string> TEST = {
"7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1",
"",
"22 13 17 11  0",
" 8  2 23  4 24",
"21  9 14 16  7",
" 6 10  3 18  5",
" 1 12 20 15 19",
"",
" 3 15  0  2 22",
" 9 18 13 17  5",
"19  8  7 25 23",
"20 11 10 24  4",
"14 21 16 12  6",
"",
"14 21 17 24  4",
"10 16 15  9 19",
"18  8 23 26 20",
"22 11 13  6  5",
" 2  0 12  3  7"
};

class Board {
    public:
        Board(const vector<string> &numbers) {
            for (const string &line : numbers) {
                this->numbers.push_back(aoc::getNumbers(line));
            }
        }

        void mark(const int number) {
            for (vector<int> &row : this->numbers) {
                for (uint i = 0; i < row.size(); i++) {
                    if (row[i] == number) {
                        row[i] = MARKED;
                    }
                }
            }
        }

        void setComplete() {
            this->complete = true;
        }

        bool isComplete() const {
            return this->complete;
        }

        uint value() const {
            uint ans = 0;
            for (const vector<int> &row : this->numbers) {
                for (const int c : row) {
                    if (c != MARKED) {
                        ans += c;
                    }
                }
            }
            return ans;
        }

        bool win() const {
            for (const vector<int> &row : this->numbers) {
                if (this->allMarked(row)) {
                    return true;
                }
            }
            for (uint i = 0; i < this->numbers[0].size(); i++) {
                if (this->allMarked(this->getColumn(i))) {
                    return true;
                }
            }
            return false;
        }

    private:
        const int MARKED = -1;
        vector<vector<int>> numbers;
        bool complete = false;

        vector<int> getColumn(const uint col) const {
            vector<int> column;
            for (uint i = 0; i < this->numbers.size(); i++) {
                column.push_back(this->numbers[i][col]);
            }
            return column;
        }

        bool allMarked(const vector<int> &rc) const {
            for (const int n: rc) {
                if (n != MARKED) {
                    return false;
                }
            }
            return true;
        }
};

int solve(const vector<string> input, const uint part) {
    const vector<vector<string>> blocks = aoc::toBlocks(input);
    const vector<int> draws = aoc::getNumbers(blocks[0][0]);
    vector<Board> boards;
    for (uint i = 1; i < blocks.size(); i++) {
        boards.push_back(Board(blocks[i]));
    }
    uint winners = 0;
    const uint stopCount = part == 1 ? 1 : boards.size();
    for (const int draw : draws) {
        for (Board& board : boards) {
            if (board.isComplete()) {
                continue;
            }
            board.mark(draw);
            if (board.win()) {
                board.setComplete();
                winners++;
            }
            if (winners == stopCount) {
                return draw * board.value();
            }
        }
    }
    assert(false);
}
int part1(const vector<string> input) {
    return solve(input, 1);
}

int part2(const vector<string> input) {
    return solve(input, 2);
}

void samples() {
    assert(part1(TEST) == 4512);
    assert(part2(TEST) == 1924);
}

MAIN(2021, 4)
