#! /usr/bin/env python3
#
# Advent of Code 2021 Day 7
#

from aoc import my_aocd


def part_1(inputs: tuple[str]) -> int:
    return sum(len(word) in {2, 3, 4, 7}
               for input_ in inputs
               for word in input_.split(' | ')[1].split())


def part_2(inputs: tuple[str]) -> int:
    return None


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
    my_aocd.print_header(2021, 8)

    assert part_1(TEST) == 26
    assert part_2(TEST) == 61229

    inputs = my_aocd.get_input(2021, 8, 200)
    result1 = part_1(inputs)
    print(f"Part 1: {result1}")
    result2 = part_2(inputs)
    print(f"Part 2: {result2}")


if __name__ == '__main__':
    main()
