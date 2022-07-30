#! /usr/bin/env julia
#

include("aoc_main.jl")
if abspath(PROGRAM_FILE) == @__FILE__
    include("aocd.jl")
    using .Aocd
end

module AoC2017_02

include("aoc.jl")

function difference_highest_lowest(line)
    return maximum(line) - minimum(line)
end

function evenlyDivisibleQuotient(line)
    for n1 ∈ line
        for n2 ∈ line
            if n1 == n2
                continue
            end
            if n1 > n2
                if n1 % n2 == 0
                    return n1 ÷ n2
                end
            elseif n2 % n1 == 0
                return n2 ÷ n1
            end
        end
    end
end

function solve(strategy, input)
    return sum(strategy([parse(Int, n) for n ∈ split(line)])
               for line ∈ input)
end

function part1(input)
    return solve(difference_highest_lowest, input)
end

function part2(input)
    return solve(evenlyDivisibleQuotient, input)
end

TEST1 = @aoc_splitlines("""
5 1 9 5
7 5 3
2 4 6 8
""")
TEST2 = @aoc_splitlines("""
5 9 2 8
9 4 7 3
3 8 6 5
""")

function samples()
    @assert part1(TEST1) == 18
    @assert part2(TEST2) == 9
end

end # module AoC2017_02

aoc_main(@__FILE__, ARGS, 2017, 2)
