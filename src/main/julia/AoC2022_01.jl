#! /usr/bin/env julia
#

include("aoc_main.jl")
if abspath(PROGRAM_FILE) == @__FILE__
    include("aocd.jl")
    using .Aocd
end

module AoC2022_01

include("aoc.jl")

function solve(input, count)
    blocks = aoc_to_blocks(input)
    sums = [sum(parse(Int, line) for line ∈ block) for block ∈ blocks]
    return sum(last(sort(sums), count))
end

function part1(input)
    return solve(input, 1)
end

function part2(input)
    return solve(input, 3)
end

TEST = @aoc_splitlines("""\
1000
2000
3000

4000

5000
6000

7000
8000
9000

10000
""")

function samples()
    @assert part1(TEST) == 24_000
    @assert part2(TEST) == 45_000
end

end # module AoC2022_O1

aoc_main(@__FILE__, ARGS, 2022, 1)
