#! /usr/bin/env julia
#

include("aoc.jl")
include("aocd.jl")
using .Aocd

PAREN_OPEN = '('
PAREN_CLOSE = ')'
SQUARE_OPEN = '['
SQUARE_CLOSE = ']'
CURLY_OPEN = '{'
CURLY_CLOSE = '}'
ANGLE_OPEN = '<'
ANGLE_CLOSE = '>'
OPEN = [PAREN_OPEN, SQUARE_OPEN, CURLY_OPEN, ANGLE_OPEN]
MAP = Dict([(PAREN_OPEN  , PAREN_CLOSE),
            (SQUARE_OPEN , SQUARE_CLOSE),
            (CURLY_OPEN  , CURLY_CLOSE),
            (ANGLE_OPEN  , ANGLE_CLOSE),
            (PAREN_CLOSE , PAREN_OPEN),
            (SQUARE_CLOSE, SQUARE_OPEN),
            (CURLY_CLOSE , CURLY_OPEN),
            (ANGLE_CLOSE , ANGLE_OPEN),
           ])
CORRUPTION_SCORES = Dict([(PAREN_CLOSE , 3),
                          (SQUARE_CLOSE, 57),
                          (CURLY_CLOSE , 1_197),
                          (ANGLE_CLOSE , 25_137),
                         ])
INCOMPLETE_SCORES = Dict([(PAREN_OPEN , 1),
                          (SQUARE_OPEN, 2),
                          (CURLY_OPEN , 3),
                          (ANGLE_OPEN , 4),
                         ])

function check(line)
    stack = Vector{Char}()
    for c ∈ collect(line)
        if c ∈ OPEN
            pushfirst!(stack, c)
        else
            MAP[c] != popfirst!(stack) && return (corrupt=c, incomplete=missing)
        end
    end
    return (corrupt=missing, incomplete=stack)
end

function part1(input)
    return sum(CORRUPTION_SCORES[x.corrupt]
               for x ∈ (check(line) for line in input)
               if !ismissing(x.corrupt))
end

function part2(input)
    incompletes = [c.incomplete for c ∈ (check(line) for line ∈ input)
                   if !ismissing(c.incomplete)]
    scores = [reduce((x, y) -> 5 * x + y, INCOMPLETE_SCORES[i] for i ∈ inc)
              for inc ∈ incompletes]
    @assert length(scores) % 2 == 1
    return sort(scores)[length(scores) ÷ 2 + 1]
end

TEST = @aoc_splitlines("""
[({(<(())[]>[[{[]{<()<>>
[(()[<>])]({[<{<<[]>>(
{([(<{}[<>[]}>{[]{[(<()>
(((({<>}<{<{<>}{[]{[]{}
[[<[([]))<([[{}[[()]]]
[{[{({}]{}}([{[{{{}}([]
{<[[]]>}<{[{[{[]{()[[[]
[<(<(<(<{}))><([]([]()
<{([([[(<>()){}]>(<<{{
<{([{{}}[<[[[<>{}]]]>[]]
""")

function samples()
    @assert part1(TEST) == 26_397
    @assert part2(TEST) == 288_957
end

@aoc_main 2021 10
