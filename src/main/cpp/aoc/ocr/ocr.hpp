#ifndef _AOC_OCR_HPP_
#define _AOC_OCR_HPP_

#include <unordered_map>

#include "../grid/grid.hpp"

namespace ocr {

// clang-format off
const Grid<char> A6 = Grid<char>::from(vector<string>({".##.",
                                                       "#..#",
                                                       "#..#",
                                                       "####",
                                                       "#..#",
                                                       "#..#"}));

const Grid<char> B6 = Grid<char>::from(vector<string>({"###.",
                                                       "#..#",
                                                       "###.",
                                                       "#..#",
                                                       "#..#",
                                                       "###."}));

const Grid<char> C6 = Grid<char>::from(vector<string>({".##.",
                                                       "#..#",
                                                       "#...",
                                                       "#...",
                                                       "#..#",
                                                       ".##."}));

const Grid<char> E6 = Grid<char>::from(vector<string>({"####",
                                                       "#...",
                                                       "###.",
                                                       "#...",
                                                       "#...",
                                                       "####"}));

const Grid<char> F6 = Grid<char>::from(vector<string>({"####",
                                                       "#...",
                                                       "###.",
                                                       "#...",
                                                       "#...",
                                                       "#..."}));

const Grid<char> G6 = Grid<char>::from(vector<string>({".##.",
                                                       "#..#",
                                                       "#...",
                                                       "#.##",
                                                       "#..#",
                                                       ".###"}));

const Grid<char> H6 = Grid<char>::from(vector<string>({"#..#",
                                                       "#..#",
                                                       "####",
                                                       "#..#",
                                                       "#..#",
                                                       "#..#"}));

const Grid<char> I6 = Grid<char>::from(vector<string>({".###",
                                                       "..#.",
                                                       "..#.",
                                                       "..#.",
                                                       "..#.",
                                                       ".###"}));

const Grid<char> J6 = Grid<char>::from(vector<string>({"..##",
                                                       "...#",
                                                       "...#",
                                                       "...#",
                                                       "#..#",
                                                       ".##."}));

const Grid<char> K6 = Grid<char>::from(vector<string>({"#..#",
                                                       "#.#.",
                                                       "##..",
                                                       "#.#.",
                                                       "#.#.",
                                                       "#..#"}));

const Grid<char> L6 = Grid<char>::from(vector<string>({"#...",
                                                       "#...",
                                                       "#...",
                                                       "#...",
                                                       "#...",
                                                       "####"}));

const Grid<char> O6 = Grid<char>::from(vector<string>({".##.",
                                                       "#..#",
                                                       "#..#",
                                                       "#..#",
                                                       "#..#",
                                                       ".##."}));

const Grid<char> P6 = Grid<char>::from(vector<string>({"###.",
                                                       "#..#",
                                                       "#..#",
                                                       "###.",
                                                       "#...",
                                                       "#..."}));

const Grid<char> R6 = Grid<char>::from(vector<string>({"###.",
                                                       "#..#",
                                                       "#..#",
                                                       "###.",
                                                       "#.#.",
                                                       "#..#"}));

const Grid<char> S6 = Grid<char>::from(vector<string>({".###",
                                                       "#...",
                                                       "#...",
                                                       ".##.",
                                                       "...#",
                                                       "###."}));

const Grid<char> U6 = Grid<char>::from(vector<string>({"#..#",
                                                       "#..#",
                                                       "#..#",
                                                       "#..#",
                                                       "#..#",
                                                       ".##."}));

const Grid<char> Y6 = Grid<char>::from(vector<string>({"#...",
                                                       "#...",
                                                       ".#.#",
                                                       "..#.",
                                                       "..#.",
                                                       "..#."}));

const Grid<char> Z6 = Grid<char>::from(vector<string>({"####",
                                                       "...#",
                                                       "..#.",
                                                       ".#..",
                                                       "#...",
                                                       "####"}));

const unordered_map<Grid<char>, char> GLYPHS = {
    {A6, 'A'},
    {B6, 'B'},
    {C6, 'C'},
    {E6, 'E'},
    {F6, 'F'},
    {G6, 'G'},
    {H6, 'H'},
    {I6, 'I'},
    {J6, 'J'},
    {K6, 'K'},
    {L6, 'L'},
    {O6, 'O'},
    {P6, 'P'},
    {R6, 'R'},
    {S6, 'S'},
    {U6, 'U'},
    {Y6, 'Y'},
    {Z6, 'Z'}
};
// clang-format on

char get(const Grid<char>& grid);
string convert6(const Grid<char>& grid, const char fillChar,
                const char emptyChar);

}  // namespace ocr
#endif  // _AOC_OCR_HPP_
