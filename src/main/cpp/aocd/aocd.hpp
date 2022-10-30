#ifndef _AOCD_HPP_
#define _AOCD_HPP_

#include <string>
#include <vector>
#include <map>

using namespace std;

namespace aocd {

    namespace sys {
        string getAocdDir();
        string getToken();
        map<string, string> getUserIds();
        void getFileContent(const string fileName, vector<string>& vecOfStrs);
    }

    namespace user {
        string getMemoDir();
    }

    namespace puzzle {
        vector<string> getInputData(const uint year, const uint day);
        string answer(const uint year, const uint day, const uint partnum);
        int check(const uint year, const uint day, const string part1, const string part2);
    }
}

#endif
