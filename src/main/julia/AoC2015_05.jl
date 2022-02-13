#! /usr/bin/env julia
#

include("aoc_main.jl")
if abspath(PROGRAM_FILE) == @__FILE__
    include("aocd.jl")
    using .Aocd
end

module AoC2015_05

include("aoc.jl")

function countmatches(regexp, string)
    return length(collect(eachmatch(regexp, string)))
end

function part1(input)
    return count(countmatches(r"(a|e|i|o|u)", line) >= 3
                 && countmatches(r"([a-z])\1", line) >= 1
                 && countmatches(r"(ab|cd|pq|xy)", line) == 0
                 for line ∈ input)
end

function part2(input)
    return count(countmatches(r"([a-z]{2})[a-z]*\1", line) >= 1
                 && countmatches(r"([a-z])[a-z]\1", line) >= 1
                 for line ∈ input)
end

TEST1 = @aoc_splitlines("ugknbfddgicrmopn")
TEST2 = @aoc_splitlines("aaa")
TEST3 = @aoc_splitlines("jchzalrnumimnmhp")
TEST4 = @aoc_splitlines("haegwjzuvuyypxyu")
TEST5 = @aoc_splitlines("dvszwmarrgswjxmb")
TEST6 = @aoc_splitlines("qjhvhtzxzqqjkmpb")
TEST7 = @aoc_splitlines("xxyxx")
TEST8 = @aoc_splitlines("uurcxstgmygtbstg")
TEST9 = @aoc_splitlines("ieodomkazucvgmuy")
TEST10 = @aoc_splitlines("xyxy")

function samples()
    @assert part1(TEST1) == 1
    @assert part1(TEST2) == 1
    @assert part1(TEST3) == 0
    @assert part1(TEST4) == 0
    @assert part1(TEST5) == 0
    @assert part2(TEST6) == 1
    @assert part2(TEST7) == 1
    @assert part2(TEST8) == 0
    @assert part2(TEST9) == 0
    @assert part2(TEST10) == 1
end

end # module AoC2015_05

aoc_main(@__FILE__, ARGS, 2015, 5)
