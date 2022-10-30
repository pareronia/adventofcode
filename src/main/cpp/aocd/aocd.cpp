#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include <regex>
#include "aocd.hpp"

using namespace std;

void aocd::sys::getFileContent(const string fileName, vector<string>& vec) {
    ifstream in(fileName);
    if (!in || !in.good()) {
        return;
    }
    string str;
    while (getline(in, str)) {
        vec.push_back(str);
    }
    in.close();
}

string aocd::sys::getAocdDir() {
    if (const char* env_d = getenv("AOCD_DIR")) {
        const string envs_d = env_d;
        if (!envs_d.empty()) {
            return envs_d;
        }
    } else {
        if (const char* env_h = getenv("HOME")) {
            const string envs_h = env_h;
            if (!envs_h.empty()) {
                return envs_h + "/.config/aocd";
            }
        }
    }
    throw "aocd dir not found";
}

string aocd::sys::getToken() {
    if (const char* env_t = getenv("AOC_SESSION")) {
        const string envs_t = env_t;
        if (!envs_t.empty()) {
            return envs_t;
        }
    } else {
        vector<string> vec;
        sys::getFileContent(sys::getAocdDir() + "/token", vec);
        if (!vec.empty()) {
            return vec[0];
        }
    }
    throw "Missing session ID";
}

map<string, string> aocd::sys::getUserIds() {
    const string fn = sys::getAocdDir() + "/token2id.json";
    vector<string> vec;
    sys::getFileContent(fn, vec);
    if (vec.empty()) {
        throw "Missing token2id.json";
    }
    regex re(".*\"(.*)\": \"(.*)\",?");
    map<string, string> m;
    for (const string& line : vec) {
        smatch sm;
        bool matched = regex_match(line, sm, re);
        if (matched) {
            m[sm[1]] = sm[2];
        }
    }
    return m;
}

string aocd::user::getMemoDir() {
    return sys::getAocdDir() + "/" + sys::getUserIds()[sys::getToken()];
}

vector<string> aocd::puzzle::getInputData(const uint year, const uint day) {
    char buf[255];
    const string fn = "%d_%02d_input.txt";
    sprintf(buf, &fn[0], year, day);
    vector<string> input;
    sys::getFileContent(user::getMemoDir() + "/" +  buf, input);
    if (input.empty()) {
        cerr << "!! INPUT DATA MISSING !" << endl;
    }
    return input;
}

string aocd::puzzle::answer(const uint year, const uint day, const uint partnum) {
    const string part = partnum == 1 ? "a" : "b";
    char buf[255];
    const string fn = "%d_%02d" + part + "_answer.txt";
    sprintf(buf, &fn[0], year, day);
    vector<string> lines;
    sys::getFileContent(user::getMemoDir() + "/" +  buf, lines);
    if (lines.empty()) {
        return "";
    } else {
        return lines[0];
    }
}

int aocd::puzzle::check(const uint year, const uint day, const string part1, const string part2) {
    const string& answer1 = puzzle::answer(year, day, 1);
    string fail1 = "";
    if (!answer1.empty() && !part1.empty() && answer1 != part1) {
        fail1 = "Part 1: Expected: " + answer1 + ", got: " + part1;
    }
    const string& answer2 = puzzle::answer(year, day, 2);
    string fail2 = "";
    if (!answer2.empty() && !part2.empty() && answer2 != part2) {
        fail2 = "Part 2: Expected: " + answer2 + ", got: " + part2;
    }
    if (!fail1.empty() || !fail2.empty()) {
        cerr << endl << "CHECK FAILED!!" << endl;
        cerr << fail1 << endl << fail2 << endl;
        return 1;
    }
    return 0;
}
