#include <bits/stdc++.h>
#include "../../aoc/aoc.hpp"
#include "../../aocd/aocd.hpp"

using namespace std;

class Program {
    private:
        uint ip = 0;
        map<string, long> registers;
        vector<string> instructions;
        function<void(long)> outputConsumer;
        function<long(void)> inputSupplier;

    public:
        Program(const vector<string> &_instructions,
                const function<void(long)> &_outputConsumer) {
            instructions = _instructions;
            outputConsumer = _outputConsumer;
        }

        Program(const vector<string> &_instructions,
                const function<void(long)> &_outputConsumer,
                const function<long(void)> &_inputSupplier)
        : Program(_instructions, _outputConsumer) {
            inputSupplier = _inputSupplier;
        }

        long get(const string &name);
        void put(const string &name, long value);
        long getOutput();
        friend ostream& operator <<(ostream &strm, const Program &program);
        friend class VirtualMachine;
        friend class VirtualMachine1;
        friend class VirtualMachine2;
};

class VirtualMachine {
    public:
        void step(Program &program);

    protected:
        long getValue(Program &program, const string& s);
        virtual void snd(Program &program, const string &operand) = 0;
        virtual void rcv(Program &program, const string &operand) = 0;
};

class VirtualMachine1 : public VirtualMachine {
    protected:
        virtual void snd(Program &program, const string &operand);
        virtual void rcv(Program &program, const string &operand);
};

class VirtualMachine2 : public VirtualMachine {
    public:
        long in(queue<long> &q, bool &waiting);
        void out(const long x, queue<long> &q, bool &waiting, int &count);

    protected:
        const long EMPTY = -1;
        virtual void snd(Program &program, const string &operand);
        virtual void rcv(Program &program, const string &operand);
};

long Program::get(const string &name) {
    if (registers.find(name) != registers.end()) {
        return registers[name];
    }
    return 0;
}

void Program::put(const string &name, long value) {
    registers[name] = value;
}

long VirtualMachine::getValue(Program &program, const string& s) {
    try {
        return stoi(s);
    } catch (invalid_argument& ia) {
        return program.get(s);
    }
}

void VirtualMachine::step(Program &program) {
    const string &line = program.instructions[program.ip];
    const vector<string> &splits = aoc::split(line);
    const string &operation = splits[0];
    const string &op1 = splits[1];
    if (operation == "set") {
        program.put(op1, getValue(program, splits[2]));
        program.ip++;
    } else if (operation == "add") {
        program.put(op1, program.get(op1) + getValue(program, splits[2]));
        program.ip++;
    } else if (operation == "mul") {
        program.put(op1, program.get(op1) * getValue(program, splits[2]));
        program.ip++;
    } else if (operation == "mod") {
        program.put(op1, program.get(op1) % getValue(program, splits[2]));
        program.ip++;
    } else if (operation == "jgz") {
        if (getValue(program, op1) > 0) {
            program.ip += getValue(program, splits[2]);
        } else {
            program.ip++;
        }
    } else if (operation == "snd") {
        snd(program, op1);
    } else if (operation == "rcv") {
        rcv(program, op1);
    } else {
        throw invalid_argument("invalid input");
    }
}

void VirtualMachine1::snd(Program &program, const string &operand) {
    program.put("FREQ", getValue(program, operand));
    program.ip++;
}

void VirtualMachine1::rcv(Program &program, const string &operand) {
    if (getValue(program, operand) != 0) {
        program.outputConsumer(program.get("FREQ"));
    } else {
        program.ip++;
    }
}

void VirtualMachine2::snd(Program &program, const string &operand) {
    program.outputConsumer(getValue(program, operand));
    program.ip++;
}

void VirtualMachine2::rcv(Program &program, const string &operand) {
    const long x = program.inputSupplier();
    if (x != EMPTY) {
        program.put(operand, x);
        program.ip++;
    }
}

long VirtualMachine2::in(queue<long> &q, bool &waiting) {
    if (q.empty()) {
        waiting = true;
        return EMPTY;
    }
    const long x = q.front();
    q.pop();
    DEBUG("<- " + to_string(x));
    return x;
}

void VirtualMachine2::out(const long x, queue<long> &q, bool &waiting, int &count) {
    DEBUG("-> " + to_string(x));
    q.push(x);
    waiting = false;
    count++;
}

ostream& operator <<(ostream &strm, const Program &program) {
    auto first = program.registers.begin();
    auto last = program.registers.end();
    strm << program.ip << ": {";
    strm << first->first << "=" << to_string(first->second);
    while (++first != last) {
        strm << ", " << first->first << "=" << to_string(first->second);
    }
    return strm << "}";
}

long part1(const vector<string> &input) {
    queue<long> q;
    Program program(input, [&](long x) { q.push(x); });
    VirtualMachine1 vm;
    while (q.empty()) {
        vm.step(program);
    }
    return q.back();
}

int part2(const vector<string> &input) {
    queue<long> q0, q1;
    int count0 = 0, count1 = 0;
    bool waiting0 = false, waiting1 = false;
    VirtualMachine2 vm;
    Program program0(
            input,
            [&](long x) { vm.out(x, q1, waiting1, count0); },
            [&]() { return vm.in(q0, waiting0); });
    program0.put("p", 0);
    Program program1(
            input,
            [&](long x) { vm.out(x, q0, waiting0, count1); },
            [&]() { return vm.in(q1, waiting1); });
    program1.put("p", 1);
    uint cycle = 0;
    while (true) {
        if (waiting0 && waiting1) {
            break;
        }
        DEBUG("cycle: " + to_string(cycle));
        DEBUG("Stepping p0");
        vm.step(program0);
        DEBUG(program0);
        DEBUG("q0: (" + to_string(q0.size()) + ") " + to_string(q0.front()));
        DEBUG("q1: (" + to_string(q1.size()) + ") " + to_string(q1.front()));
        DEBUG("Stepping p1");
        vm.step(program1);
        DEBUG(program1);
        DEBUG("q0: (" + to_string(q0.size()) + ") " + to_string(q0.front()));
        DEBUG("q1: (" + to_string(q1.size()) + ") " + to_string(q1.front()));
        cycle++;
    }
    DEBUG("cycles: " + to_string(cycle));
    return count1;
}

const vector<string> TEST1 = {
    "set a 1",
    "add a 2",
    "mul a a",
    "mod a 5",
    "snd a",
    "set a 0",
    "rcv a",
    "jgz a -1",
    "set a 1",
    "jgz a -2"
};
const vector<string> TEST2 = {
    "snd 1",
    "snd 2" ,
    "snd p" ,
    "rcv a" ,
    "rcv b" ,
    "rcv c" ,
    "rcv d"
};

void samples() {
    assert(part1(TEST1) == 4);
    assert(part2(TEST2) == 3);
}

MAIN(2017, 18)
