#! /usr/bin/env julia
#

include("aoc_main.jl")
if abspath(PROGRAM_FILE) == @__FILE__
    include("aocd.jl")
    using .Aocd
end

module AoC2022_03

include("aoc.jl")

function priority(ch::Char)
    return islowercase(ch) ? ch - 'a' + 1 : ch - 'A' + 27
end

function part1(input)
    return sum(
        priority(
            first(
                intersect(line[1:length(line)÷2], line[length(line)÷2+1:end]),
            ),
        ) for line ∈ input
    )
end

function part2(input)
    return sum(
        priority(first(intersect(input[i], input[i+1], input[i+2]))) for
        i ∈ 1:3:length(input)
    )
end

TEST = @aoc_splitlines("""\
vJrwpWtwJgWrhcsFMMfFFhFp
jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
PmmdzqPrVvPwwTWBwg
wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
ttgJtRGJQctTZtZT
CrZsJsPPZsGzwwsLwLmpwMDw
""")

function samples()
    @assert part1(TEST) == 157
    @assert part2(TEST) == 70
end

end # module AoC2022_O3

aoc_main(@__FILE__, ARGS, 2022, 3)
