#! /usr/bin/env julia
#

include("aoc_main.jl")
if abspath(PROGRAM_FILE) == @__FILE__
    include("aocd.jl")
    using .Aocd
end

module AoC2017_01

include("aoc.jl")

function _parse(input)
    return [parse(Int, line) for line in input]
end

function count_increases(depths, window::Int)
    return count(i->(depths[i] > depths[i - window]), window+1:length(depths))
end

function sum_same_chars(string, distance::Int)
    test = string * string[1:distance]
    return sum([parse.(Int, test[i])
                for i ∈ 1:length(string)
                if test[i] == test[i + distance]])
end

function part1(input)
    return sum_same_chars(input[1], 1)
end

function part2(input)
    return sum_same_chars(input[1], length(input[1]) ÷ 2)
end

TEST1 = @aoc_splitlines("""1122""")
TEST2 = @aoc_splitlines("""1111""")
TEST3 = @aoc_splitlines("""1234""")
TEST4 = @aoc_splitlines("""91212129""")
TEST5 = @aoc_splitlines("""1212""")
TEST6 = @aoc_splitlines("""1221""")
TEST7 = @aoc_splitlines("""123425""")
TEST8 = @aoc_splitlines("""123123""")
TEST9 = @aoc_splitlines("""12131415""")

function samples()
    @assert part1(TEST1) == 3
    @assert part1(TEST2) == 4
    @assert part1(TEST3) == 0
    @assert part1(TEST4) == 9
    @assert part2(TEST5) == 6
    @assert part2(TEST6) == 0
    @assert part2(TEST7) == 4
    @assert part2(TEST8) == 12
    @assert part2(TEST9) == 4
end

end # module AoC2017_03

aoc_main(@__FILE__, ARGS, 2017, 1)
