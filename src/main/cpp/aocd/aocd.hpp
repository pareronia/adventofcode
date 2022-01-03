#ifndef _AOC_HPP_
#define _AOC_HPP_

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
        vector<string> getInputData(const int year, const int day);
    }
}
#endif
