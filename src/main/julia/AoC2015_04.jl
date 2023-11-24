#! /usr/bin/env julia
#

include("aoc_main.jl")
if abspath(PROGRAM_FILE) == @__FILE__
    include("aocd.jl")
    using .Aocd
end

module AoC2015_04

include("aoc.jl")

using MD5

function checkZeroes(digest, zeroes)
    chars = zeroes ÷ 2 + zeroes % 2
    cnt = 0
    for j ∈ 1:chars
        ch::UInt8 = digest[j][1]
        if (ch & 0xF0) == 0
            cnt += 1
            if (ch & 0x0F) == 0
                cnt += 1
                continue
            end
        end
        break
    end
    return cnt == zeroes
end

function solve(input, zeroes)
    i = 1
    while true
        checkZeroes(md5(input * string(i)), zeroes) && return i
        i += 1
    end
end

function part1(input)
    return solve(input[1], 5)
end

function part2(input)
    return solve(input[1], 6)
end

TEST1 = @aoc_splitlines("abcdef")
TEST2 = @aoc_splitlines("pqrstuv")

function samples()
    @assert part1(TEST1) == 609_043
    @assert part1(TEST2) == 1_048_970
end

end # module AoC2015_04

aoc_main(@__FILE__, ARGS, 2015, 4)
