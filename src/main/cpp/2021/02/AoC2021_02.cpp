#include "assert.h"
#include <iostream>
#include <string>
#include <sstream>
#include <vector>
#include <set>
#include "../../aocd/aocd.hpp"

using namespace std;

const vector<string> TEST = {
    "forward 5",
    "down 5",
    "forward 8",
    "up 3",
    "down 8",
    "forward 2"
};

const string FORWARD = "forward";
const string UP = "up";
const string DOWN = "down";
const set<string> ALLOWED_COMMANDS = { FORWARD, UP, DOWN };

class Command {
    public:
        static Command* create(const string direction, const string amount) {
            return new Command(direction, stoi(amount));
        }

        string getDirection() const {
            return this->direction;
        }

        unsigned int getAmount() const {
            return this->amount;
        }

    private:
        string direction;
        unsigned int amount;

        Command(const string direction, const unsigned int amount) {
            if (ALLOWED_COMMANDS.find(direction) == ALLOWED_COMMANDS.end()) {
                throw "Illegal value";
            }
            this->direction = direction;
            this-> amount = amount;
        }
};

vector<Command> parse(const vector<string> &input) {
    vector<Command> commands;
    for (const string line: input) {
        vector<string> splits;
        istringstream iss(line);
        do {
            string split;
            iss >> split;
            splits.push_back(split);
        } while(iss);
        // assert splits.size() == 2
        commands.push_back(*Command::create(splits[0], splits[1]));
    }
    return commands;
}

int part1(const vector<string> &input) {
    vector<Command> commands = parse(input);
    int hor = 0, ver = 0;
    for (const Command command : commands) {
        if (command.getDirection() == UP) {
            ver -= command.getAmount();
        } else if (command.getDirection() == DOWN) {
            ver += command.getAmount();
        } else {
            hor += command.getAmount();
        }
    }
    return hor * ver;
}

int part2(const vector<string> &input) {
    vector<Command> commands = parse(input);
    int hor = 0, ver = 0, aim = 0;
    for (const Command command : commands) {
        if (command.getDirection() == UP) {
            aim -= command.getAmount();
        } else if (command.getDirection() == DOWN) {
            aim += command.getAmount();
        } else {
            hor += command.getAmount();
            ver += aim * command.getAmount();
        }
    }
    return hor * ver;
}


int main() {
    assert(part1(TEST) == 150);
    assert(part2(TEST) == 900);

    const vector<string> input = aocd::puzzle::getInputData(2021, 2);
    cout << "Part 1: " << part1(input) << endl;
    cout << "Part 2: " << part2(input) << endl;

    return 0;
}
