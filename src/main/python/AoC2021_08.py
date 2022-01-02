#! /usr/bin/env python3
#
# Advent of Code 2021 Day 8
#

import aocd
from aoc import my_aocd


def _parse(inputs: tuple[str]) -> tuple[list[list[str]], list[list[str]]]:
    return ([[''.join(sorted(_))
              for _ in input_.split(' | ')[i].split()]
             for input_ in inputs]
            for i in {0, 1})


def part_1(inputs: tuple[str]) -> int:
    _, outputs = _parse(inputs)
    return sum(len(word) in {2, 3, 4, 7}
               for line in outputs
               for word in line)


def _find(input_: list[str], segments: int, expected: int) -> set[str]:
    ans = {_ for _ in input_ if len(_) == segments}
    assert len(ans) == expected
    return ans


def _shares_all_segments(container: str, contained: str) -> bool:
    cd = set(contained)
    return set(container) & cd == cd


def _solve(signals: list[str], outputs: list[str]) -> int:
    digits = dict[str, str]()
    # 1
    twos = _find(signals, 2, 1)
    one = list(twos)[0]
    digits[one] = '1'
    # 7
    threes = _find(signals, 3, 1)
    digits[list(threes)[0]] = '7'
    # 4
    fours = _find(signals, 4, 1)
    four = list(fours)[0]
    digits[four] = '4'
    # 8
    sevens = _find(signals, 7, 1)
    digits[list(sevens)[0]] = '8'
    # 9
    sixes = _find(signals, 6, 3)
    possible_nines = {six for six in sixes if _shares_all_segments(six, four)}
    assert len(possible_nines) == 1
    nine = list(possible_nines)[0]
    digits[nine] = '9'
    # 0
    possible_zeroes = {six for six in sixes
                       if six != nine and _shares_all_segments(six, one)}
    assert len(possible_zeroes) == 1
    zero = list(possible_zeroes)[0]
    digits[zero] = '0'
    # 6
    possible_sixes = {six for six in sixes
                      if six != nine and six != zero}
    assert len(possible_sixes) == 1
    digits[list(possible_sixes)[0]] = '6'
    # 3
    fives = _find(signals, 5, 3)
    possible_threes = {five for five in fives
                       if _shares_all_segments(five, one)}
    assert len(possible_threes) == 1
    three = list(possible_threes)[0]
    digits[three] = '3'
    # 5
    possible_fives = {five for five in fives
                      if five != three and _shares_all_segments(nine, five)}
    assert len(possible_fives) == 1
    five = list(possible_fives)[0]
    digits[five] = '5'
    # 2
    possible_twos = {f for f in fives if f != five and f != three}
    assert len(possible_twos) == 1
    digits[list(possible_twos)[0]] = '2'

    return int("".join(digits[o] for o in outputs))


def part_2(inputs: tuple[str]) -> int:
    signals, outputs = _parse(inputs)
    return sum(_solve(signals[i], outputs[i]) for i in range(len(signals)))


TEST = """\
be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | \
fdgacbe cefdb cefbgd gcbe
edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | \
fcgedb cgb dgebacf gc
fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | \
cg cg fdcagb cbg
fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | \
efabcd cedba gadfec cb
aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | \
gecf egdcabf bgf bfgea
fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | \
gebdcfa ecba ca fadegcb
dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | \
cefg dcbef fcge gbcadfe
bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | \
ed bcgafe cdgba cbgef
egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | \
gbdfcae bgc cg cgb
gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | \
fgae cfgab fg bagce
""".splitlines()


def main() -> None:
    puzzle = aocd.models.Puzzle(2021, 8)
    my_aocd.print_header(puzzle.year, puzzle.day)

    assert part_1(TEST) == 26
    assert part_2(TEST) == 61229

    inputs = my_aocd.get_input(2021, 8, 200)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")
    my_aocd.check_results(puzzle, result1, result2)


if __name__ == '__main__':
    main()
