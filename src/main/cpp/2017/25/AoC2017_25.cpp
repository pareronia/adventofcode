#include <bits/stdc++.h>
#include "../../aoc/aoc.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;

class Step {
    public:
        Step(const uint _write, const int _move, const string _goTo) {
            write = _write;
            move = _move;
            goTo = _goTo;
        }
        friend ostream& operator <<(ostream& strm, const Step& step);
        uint write;
        int move;
        string goTo;
};

typedef shared_ptr<pair<Step, Step>> State;

ostream& operator <<(ostream& strm, const Step& step) {
    strm << "Step[write: " << step.write << ", move: ";
    strm << step.move << ", goTo: " << step.goTo << "]";
    return strm;
}

vector<string> split(const string& s) {
    istringstream iss(s);
    return {istream_iterator<string>{iss}, istream_iterator<string>{}};
}

string getLastWord(const string& line) {
    const vector<string>& splits = split(line);
    const string& last = splits[splits.size() - 1];
    return last.substr(0, last.size() - 1);
}

Step parseStep(const vector<string>& lines, const uint idx) {
    const uint write = stoi(getLastWord(lines[idx + 0]));
    const int move = getLastWord(lines[idx + 1]) == "left" ? -1 : 1;
    const string& goTo = getLastWord(lines[idx + 2]);
    return Step(write, move, goTo);
}

tuple<string, uint, unordered_map<string, State>> parse(const vector<string>& input) {
    const vector<vector<string>> blocks = aoc::toBlocks(input);
    const string start = getLastWord(blocks[0][0]);
    const uint steps = stoi(split(blocks[0][1])[5]);
    unordered_map<string, State> states;
    for (uint i = 1; i < blocks.size(); i++) {
        const vector<string>& block = blocks[i];
        const string& name = getLastWord(block[0]);
        const Step& step0 = parseStep(block, 2);
        const Step& step1 = parseStep(block, 6);
        states[name] = make_shared<pair<Step, Step>>(make_pair(step0, step1));
    }
    return make_tuple(start, steps, states);
}

int part1(const vector<string> &input) {
    string start;
    uint steps;
    unordered_map<string, State> states;
    tie(start, steps, states) = parse(input);
    unordered_map<int, uint> tape;
    int pos = 0;
    State state = states[start];
    for (uint i = 0; i < steps; i++) {
        const uint value = tape[pos];
        const Step& step = value == 0 ? state->first : state->second;
        tape[pos] = step.write;
        pos += step.move;
        state = states[step.goTo];
    }
    return accumulate(
            tape.begin(), tape.end(),
            0, [](int a, const auto& item) { return a + item.second; });
}

int part2(const vector<string> &input) {
    return 0;
}

const vector<string> TEST = {
"Begin in state A.",
"Perform a diagnostic checksum after 6 steps.",
"",
"In state A:",
"  If the current value is 0:",
"    - Write the value 1.",
"    - Move one slot to the right.",
"    - Continue with state B.",
"  If the current value is 1:",
"    - Write the value 0.",
"    - Move one slot to the left.",
"    - Continue with state B.",
"",
"In state B:",
"  If the current value is 0:",
"    - Write the value 1.",
"    - Move one slot to the left.",
"    - Continue with state A.",
"  If the current value is 1:",
"    - Write the value 1.",
"    - Move one slot to the right.",
"    - Continue with state A."
};

void samples() {
    assert(part1(TEST) == 3);
}

MAIN(2017, 25)
