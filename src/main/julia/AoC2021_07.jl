#! /usr/bin/env julia
#

include("aoc_main.jl")
if abspath(PROGRAM_FILE) == @__FILE__
    include("aocd.jl")
    using .Aocd
end

module AoC2021_07

include("aoc.jl")

function solve(input, calc)
    positions = parse.(Int, split(input[1], ","))
    return minimum(sum(calc(a, b) for b ∈ positions)
                   for a ∈ minimum(positions):maximum(positions))
end

function part1(input)
    return solve(input, (a, b) -> abs(a - b))
end

function part2(input)
    return solve(input, (a, b) -> abs(a - b) * (abs(a - b) + 1) ÷ 2)
end

TEST = @aoc_splitlines("""
16,1,2,0,4,2,7,1,2,14
""")

function samples()
    @assert part1(TEST) == 37
    @assert part2(TEST) == 168
end

end # module AoC2021_07

aoc_main(@__FILE__, ARGS, 2021, 7)
