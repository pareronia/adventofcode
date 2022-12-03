#! /usr/bin/env julia
#

include("aoc_main.jl")
if abspath(PROGRAM_FILE) == @__FILE__
    include("aocd.jl")
    using .Aocd
end

module AoC2022_02

include("aoc.jl")

WIN = 6
DRAW = 3
LOSS = 0

function solve(vals, input)
    return sum(vals[Tuple(split(line))] for line ∈ input)
end

function part1(input)
    vals = Dict(
        ("A", "X") => DRAW + 1,
        ("A", "Y") => WIN + 2,
        ("A", "Z") => LOSS + 3,
        ("B", "X") => LOSS + 1,
        ("B", "Y") => DRAW + 2,
        ("B", "Z") => WIN + 3,
        ("C", "X") => WIN + 1,
        ("C", "Y") => LOSS + 2,
        ("C", "Z") => DRAW + 3,
    )
    return solve(vals, input)
end

function part2(input)
    vals = Dict(
        ("A", "X") => LOSS + 3,
        ("A", "Y") => DRAW + 1,
        ("A", "Z") => WIN + 2,
        ("B", "X") => LOSS + 1,
        ("B", "Y") => DRAW + 2,
        ("B", "Z") => WIN + 3,
        ("C", "X") => LOSS + 2,
        ("C", "Y") => DRAW + 3,
        ("C", "Z") => WIN + 1,
    )
    return solve(vals, input)
end

TEST = @aoc_splitlines("""\
A Y
B X
C Z
""")

function samples()
    @assert part1(TEST) == 15
    @assert part2(TEST) == 12
end

end # module AoC2022_O1

aoc_main(@__FILE__, ARGS, 2022, 2)
