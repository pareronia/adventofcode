#! /usr/bin/env julia
#

include("aoc_main.jl")
if abspath(PROGRAM_FILE) == @__FILE__
    include("aocd.jl")
    using .Aocd
end

module AoC2022_04

include("aoc.jl")

const REGEXP = r"([0-9]+)-([0-9]+),([0-9]+)-([0-9]+)"

function solve(input, test)
    parseLine(line) = Tuple(parse(Int, x) for x ∈ match(REGEXP, line))
    return count(test(parseLine(line)) for line ∈ input)
end

function part1(input)
    return solve(
        input,
        ((a, b, c, d)::Tuple) -> (a ≤ c && b ≥ d) || (c ≤ a && d ≥ b),
    )
end

function part2(input)
    return solve(input, ((a, b, c, d)::Tuple) -> b ≥ c && d ≥ a)
end

TEST = @aoc_splitlines("""\
2-4,6-8
2-3,4-5
5-7,7-9
2-8,3-7
6-6,4-6
2-6,4-8
""")

function samples()
    @assert part1(TEST) == 2
    @assert part2(TEST) == 4
end

end # module AoC2022_O4

aoc_main(@__FILE__, ARGS, 2022, 4)
