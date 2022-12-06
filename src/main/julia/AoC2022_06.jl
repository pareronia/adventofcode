#! /usr/bin/env julia
#

include("aoc_main.jl")
if abspath(PROGRAM_FILE) == @__FILE__
    include("aocd.jl")
    using .Aocd
end

module AoC2022_06

include("aoc.jl")

function solve(buffer, size)
    return first(
        i for i ∈ size:length(buffer) if length(Set(buffer[i-size+1:i])) == size
    )
end

function part1(input)
    return solve(input[1], 4)
end

function part2(input)
    return solve(input[1], 14)
end

TEST1 = @aoc_splitlines("mjqjpqmgbljsphdztnvjfqwrcgsmlb")
TEST2 = @aoc_splitlines("bvwbjplbgvbhsrlpgdmjqwftvncz")
TEST3 = @aoc_splitlines("nppdvjthqldpwncqszvftbrmjlhg")
TEST4 = @aoc_splitlines("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg")
TEST5 = @aoc_splitlines("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")

function samples()
    @assert part1(TEST1) == 7
    @assert part1(TEST2) == 5
    @assert part1(TEST3) == 6
    @assert part1(TEST4) == 10
    @assert part1(TEST5) == 11
    @assert part2(TEST1) == 19
    @assert part2(TEST2) == 23
    @assert part2(TEST3) == 23
    @assert part2(TEST4) == 29
    @assert part2(TEST5) == 26
end

end # module AoC2022_O6

aoc_main(@__FILE__, ARGS, 2022, 6)
