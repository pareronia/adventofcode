#! /usr/bin/env julia
#

include("aoc_main.jl")
if abspath(PROGRAM_FILE) == @__FILE__
    include("aocd.jl")
    using .Aocd
end

module AoC2015_01

include("aoc.jl")

function part1(input)
    return length(input[1]) - 2 * count(==(')'), input[1])
end

function part2(input)
    sum = 0
    for (i, c) ∈ enumerate(input[1])
        sum += (c == '(' ? 1 : -1)
        sum == -1 && return i
    end
    error("Unsolvable")
end

TEST1 = @aoc_splitlines("(())")
TEST2 = @aoc_splitlines("()()")
TEST3 = @aoc_splitlines("(((")
TEST4 = @aoc_splitlines("(()(()(")
TEST5 = @aoc_splitlines("))(((((")
TEST6 = @aoc_splitlines("())")
TEST7 = @aoc_splitlines("))(")
TEST8 = @aoc_splitlines(")))")
TEST9 = @aoc_splitlines(")())())")
TEST10 = @aoc_splitlines(")")
TEST11 = @aoc_splitlines("()())")

function samples()
    @assert part1(TEST1) == 0
    @assert part1(TEST2) == 0
    @assert part1(TEST3) == 3
    @assert part1(TEST4) == 3
    @assert part1(TEST5) == 3
    @assert part1(TEST6) == -1
    @assert part1(TEST7) == -1
    @assert part1(TEST8) == -3
    @assert part1(TEST9) == -3
    @assert part2(TEST10) == 1
    @assert part2(TEST11) == 5
end

end # module AoC2015_01

aoc_main(@__FILE__, ARGS, 2015, 1)
