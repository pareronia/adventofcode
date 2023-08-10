use crate::grid::{Cell, CharGrid, Grid};
use lazy_static::lazy_static;
use std::collections::HashMap;

#[rustfmt::skip]
lazy_static! {
    static ref A6: Vec<&'static str> = vec![".##.",
                                            "#..#",
                                            "#..#",
                                            "####",
                                            "#..#",
                                            "#..#"];

    static ref B6: Vec<&'static str> = vec!["###.",
                                            "#..#",
                                            "###.",
                                            "#..#",
                                            "#..#",
                                            "###."];

    static ref C6: Vec<&'static str> = vec![".##.",
                                            "#..#",
                                            "#...",
                                            "#...",
                                            "#..#",
                                            ".##."];

    static ref E6: Vec<&'static str> = vec!["####",
                                            "#...",
                                            "###.",
                                            "#...",
                                            "#...",
                                            "####"];

    static ref F6: Vec<&'static str> = vec!["####",
                                            "#...",
                                            "###.",
                                            "#...",
                                            "#...",
                                            "#..."];

    static ref G6: Vec<&'static str> = vec![".##.",
                                            "#..#",
                                            "#...",
                                            "#.##",
                                            "#..#",
                                            ".###"];

    static ref H6: Vec<&'static str> = vec!["#..#",
                                            "#..#",
                                            "####",
                                            "#..#",
                                            "#..#",
                                            "#..#"];

    static ref I6: Vec<&'static str> = vec![".###",
                                            "..#.",
                                            "..#.",
                                            "..#.",
                                            "..#.",
                                            ".###"];

    static ref J6: Vec<&'static str> = vec!["..##",
                                            "...#",
                                            "...#",
                                            "...#",
                                            "#..#",
                                            ".##."];

    static ref K6: Vec<&'static str> = vec!["#..#",
                                            "#.#.",
                                            "##..",
                                            "#.#.",
                                            "#.#.",
                                            "#..#"];

    static ref L6: Vec<&'static str> = vec!["#...",
                                            "#...",
                                            "#...",
                                            "#...",
                                            "#...",
                                            "####"];

    static ref O6: Vec<&'static str> = vec![".##.",
                                            "#..#",
                                            "#..#",
                                            "#..#",
                                            "#..#",
                                            ".##."];

    static ref P6: Vec<&'static str> = vec!["###.",
                                            "#..#",
                                            "#..#",
                                            "###.",
                                            "#...",
                                            "#..."];

    static ref R6: Vec<&'static str> = vec!["###.",
                                            "#..#",
                                            "#..#",
                                            "###.",
                                            "#.#.",
                                            "#..#"];

    static ref S6: Vec<&'static str> = vec![".###",
                                            "#...",
                                            "#...",
                                            ".##.",
                                            "...#",
                                            "###."];

    static ref U6: Vec<&'static str> = vec!["#..#",
                                            "#..#",
                                            "#..#",
                                            "#..#",
                                            "#..#",
                                            ".##."];

    static ref Y6: Vec<&'static str> = vec!["#...",
                                            "#...",
                                            ".#.#",
                                            "..#.",
                                            "..#.",
                                            "..#."];

    static ref Z6: Vec<&'static str> = vec!["####",
                                            "...#",
                                            "..#.",
                                            ".#..",
                                            "#...",
                                            "####"];
}

lazy_static! {
    static ref GLYPHS: HashMap<CharGrid, char> = {
        let mut m = HashMap::new();
        m.insert(CharGrid::from(&A6), 'A');
        m.insert(CharGrid::from(&B6), 'B');
        m.insert(CharGrid::from(&C6), 'C');
        m.insert(CharGrid::from(&E6), 'E');
        m.insert(CharGrid::from(&F6), 'F');
        m.insert(CharGrid::from(&G6), 'G');
        m.insert(CharGrid::from(&H6), 'H');
        m.insert(CharGrid::from(&I6), 'I');
        m.insert(CharGrid::from(&J6), 'J');
        m.insert(CharGrid::from(&K6), 'K');
        m.insert(CharGrid::from(&L6), 'L');
        m.insert(CharGrid::from(&O6), 'O');
        m.insert(CharGrid::from(&P6), 'P');
        m.insert(CharGrid::from(&R6), 'R');
        m.insert(CharGrid::from(&S6), 'S');
        m.insert(CharGrid::from(&U6), 'U');
        m.insert(CharGrid::from(&Y6), 'Y');
        m.insert(CharGrid::from(&Z6), 'Z');
        m
    };
}

pub fn convert_6(grid: &CharGrid, fill_char: char, empty_char: char) -> String {
    (0..grid.width())
        .step_by(5)
        .map(|i| {
            grid.sub_grid(&Cell::at(0, i), &Cell::at(grid.height(), i + 4))
        })
        .map(|mut sub| {
            sub.replace(fill_char, '#');
            sub
        })
        .map(|mut sub| {
            sub.replace(empty_char, '.');
            sub
        })
        .map(|sub| {
            match GLYPHS.get(&sub.clone()) {
                Some(val) => val,
                None => panic!("OCR doesn't understand\n{}", sub),
            }
        })
        .collect::<String>()
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    pub fn test() {
        #[rustfmt::skip]
        let grid = CharGrid::from(&vec![
              " **  ***   **  **** ****  **  *  *  ***   ** *  * *     **  ***  ***   *** *  * *   ***** ",
              "*  * *  * *  * *    *    *  * *  *   *     * * *  *    *  * *  * *  * *    *  * *   *   * ",
              "*  * ***  *    ***  ***  *    ****   *     * **   *    *  * *  * *  * *    *  *  * *   *  ",
              "**** *  * *    *    *    * ** *  *   *     * * *  *    *  * ***  ***   **  *  *   *   *   ",
              "*  * *  * *  * *    *    *  * *  *   *  *  * * *  *    *  * *    * *     * *  *   *  *    ",
              "*  * ***   **  **** *     *** *  *  ***  **  *  * ****  **  *    *  * ***   **    *  **** "
        ]);

        assert_eq!(
            convert_6(&grid, '*', ' '),
            "ABCEFGHIJKLOPRSUYZ".to_string()
        );
    }
}
