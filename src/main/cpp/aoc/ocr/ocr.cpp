#include "ocr.hpp"

char ocr::get(const Grid<char>& grid) {
    auto found = ocr::GLYPHS.find(grid);
    if (found == ocr::GLYPHS.end()) {
        throw "OCR doesn't understand " + grid.toString();
    }
    return found->second;
}

string ocr::convert6(const Grid<char>& grid, const char fillChar,
                     const char emptyChar) {
    vector<char> chars;
    for (int i : aoc::Range::range(0, grid.width(), 5)) {
        const Grid<char>& subGrid =
            grid.subGrid(Cell::at(0, i), Cell::at(grid.height(), i + 4));
        const Grid<char> glyph =
            subGrid.replace(fillChar, '#').replace(emptyChar, '.');
        chars.push_back(ocr::get(glyph));
    }
    return string(chars.begin(), chars.end());
}
