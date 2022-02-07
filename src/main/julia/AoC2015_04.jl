#! /usr/bin/env julia
#

include("aoc.jl")
include("aocd.jl")
using .Aocd
using MD5

function solve(input, zeroes)
    i = 0
    val = input
    target = repeat('0', zeroes)
    while val[1:zeroes] != target
        i += 1
        val = bytes2hex(md5(input * string(i)))
    end
    return i
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

@aoc_main 2015 4
