#! /usr/bin/env julia
#

include("aoc.jl")
include("aocd.jl")
using .Aocd

function _parse(input)
    return ([[join(sort(collect(x)))
              for x ∈ split(split(line, " | ")[i])]
             for line ∈ input]
            for i ∈ 1:2)
end

function part1(input)
    signals, outputs = _parse(input)
    return sum(length(word) ∈ [2, 3, 4, 7]
               for line in outputs
               for word in line)
end

function part2(input)
    function solve(signals, outputs)
        function find(signals, segments, expected)
            ans = filter(x -> length(x) == segments, signals)
            @assert length(ans) == expected
            return ans
        end
        function shares_all_segments(container, contained)
            return all(∈(container), contained)
        end

        digits = Dict{String,Char}()
        # 1
        twos = find(signals, 2, 1)
        one = twos[1]
        digits[one] = '1'
        # 7
        threes = find(signals, 3, 1)
        digits[threes[1]] = '7'
        # 4
        fours = find(signals, 4, 1)
        four = fours[1]
        digits[four] = '4'
        # 8
        sevens = find(signals, 7, 1)
        digits[sevens[1]] = '8'
        # 9
        sixes = find(signals, 6, 3)
        possible_nines = filter(x -> shares_all_segments(x, four), sixes)
        @assert length(possible_nines) == 1
        nine = possible_nines[1]
        digits[nine] = '9'
        # 0
        possible_zeroes = filter(x -> x != nine && shares_all_segments(x, one),
                                 sixes)
        @assert length(possible_zeroes) == 1
        zero = possible_zeroes[1]
        digits[zero] = '0'
        # 6
        possible_sixes = filter(x -> x != nine && x != zero, sixes)
        @assert length(possible_sixes) == 1
        digits[possible_sixes[1]] = '6'
        # 3
        fives = find(signals, 5, 3)
        possible_threes = filter(x -> shares_all_segments(x, one), fives)
        @assert length(possible_threes) == 1
        three = possible_threes[1]
        digits[three] = '3'
        # 5
        possible_fives = filter(x -> x != three && shares_all_segments(nine, x),
                                fives)
        @assert length(possible_fives) == 1
        five = possible_fives[1]
        digits[five] = '5'
        # 2
        possible_twos = filter(x -> x != five && x != three, fives)
        @assert length(possible_twos) == 1
        digits[possible_twos[1]] = '2'

        return parse(Int, join(digits[o] for o ∈ outputs))
    end

    signals, outputs = _parse(input)
    return sum(solve(signals[i], outputs[i])
               for i ∈ 1:length(input))
end

TEST = @aoc_splitlines("""
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
""")

function samples()
    @assert part1(TEST) == 26
    @assert part2(TEST) == 61_229
end

@aoc_main 2021 8
