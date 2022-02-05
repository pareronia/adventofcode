#! /usr/bin/env julia
#

include("aoc.jl")
include("aocd.jl")
using .Aocd

function solve(input, diag)
    d = Dict()
    for line ∈ input
        m = match(r"([0-9]+),([0-9]+) -> ([0-9]+),([0-9]+)", line)
        x1, y1, x2, y2 = parse.(Int, m.captures)
        @assert all(>=(0), [x1, y1, x2, y2])
        mx = x1 == x2 ? 0 : (x1 < x2 ? 1 : -1)
        my = y1 == y2 ? 0 : (y1 < y2 ? 1 : -1)
        (!diag && mx != 0 && my != 0) && continue
        for i ∈ 0:max(abs(x1 - x2), abs(y1 - y2))
            key = (x1 + mx * i, y1 + my * i)
            d[key] = get(d, key, 0) + 1
        end
    end
    return count(>(1), values(d))
end

function part1(input)
    return solve(input, false)
end

function part2(input)
    return solve(input, true)
end

TEST = @aoc_splitlines("""
0,9 -> 5,9
8,0 -> 0,8
9,4 -> 3,4
2,2 -> 2,1
7,0 -> 7,4
6,4 -> 2,0
0,9 -> 2,9
3,4 -> 1,4
0,0 -> 8,8
5,5 -> 8,2
""")

function samples()
    @assert part1(TEST) == 5
    @assert part2(TEST) == 12
end

@aoc_main 2021 5
