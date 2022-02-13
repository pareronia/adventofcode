#! /usr/bin/env julia
#

include("aoc_main.jl")
if abspath(PROGRAM_FILE) == @__FILE__
    include("aocd.jl")
    using .Aocd
end

module AoC2021_06

include("aoc.jl")

function solve(input, days)
    fishies = zeros(Int, 9)
    for n ∈ split(input[1], ",")
        fishies[parse(Int, n)] += 1
    end
    for i ∈ 2:days
        zeroes = popfirst!(fishies)
        push!(fishies, zeroes)
        fishies[7] += zeroes
    end
    return sum(fishies)
end

function part1(input)
    return solve(input, 80)
end

function part2(input)
    return solve(input, 256)
end

TEST = @aoc_splitlines("""
3,4,3,1,2
""")

function samples()
    @assert part1(TEST) == 5_934
    @assert part2(TEST) == 26_984_457_539
end

end # module AoC2021_06

aoc_main(@__FILE__, ARGS, 2021, 6)
