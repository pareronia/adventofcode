#! /usr/bin/env julia
#

include("aoc_main.jl")
if abspath(PROGRAM_FILE) == @__FILE__
    include("aocd.jl")
    using .Aocd
end

module AoC2021_01

include("aoc.jl")

function _parse(input)
    return [parse(Int, line) for line in input]
end

function count_increases(depths, window::Int)
    return count(i->(depths[i] > depths[i - window]), window+1:length(depths))
end

function part1(input)
    return count_increases(_parse(input), 1)
end

function part2(input)
    return count_increases(_parse(input), 3)
end

TEST = @aoc_splitlines("""
199
200
208
210
200
207
240
269
260
263
""")

function samples()
    @assert part1(TEST) == 7
    @assert part2(TEST) == 5
end

end # module AoC2021_01

aoc_main(@__FILE__, ARGS, 2021, 1)
