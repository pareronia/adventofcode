#! /usr/bin/env julia
#

include("aoc_main.jl")
if abspath(PROGRAM_FILE) == @__FILE__
    include("aocd.jl")
    using .Aocd
end

module AoC2020_05

include("aoc.jl")

function _parse(input)
    table = Dict('F' => '0', 'B' => '1', 'L' => '0', 'R' => '1')
    return sort([join([table[c] for c ∈ line]) for line ∈ input])
end

function asInt(string)
    return parse(Int, string, base=2)
end

function part1(input)
    translated = _parse(input)
    return asInt(last(translated))
end

function part2(input)
    translated = _parse(input)
    for i ∈ 2:length(translated)
        if last(translated[i]) == last(translated[i-1])
            return asInt(translated[i]) - 1
        end
    end
    throw(error("Unsolvable"))
end

TEST1 = @aoc_splitlines("""
FBFBBFFRLR
BFFFBBFRRR
FFFBBBFRRR
BBFFBBFRLL
""")
TEST2 = @aoc_splitlines("""
FFFFFFFLLL
FFFFFFFLLR
FFFFFFFLRL
FFFFFFFRLL
FFFFFFFRLR
""")

function samples()
    @assert part1(TEST1) == 820
    @assert part2(TEST2) == 3
end

end # module AoC2020_05

aoc_main(@__FILE__, ARGS, 2020, 5)
