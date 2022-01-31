#! /usr/bin/env julia
#

include("aocd.jl")
using .Aocd

function _parse(input)
    return [parse(Int, line) for line in input]
end

function countIncreases(depths, window::Int)
    return count(i->(depths[i] > depths[i - window]), window+1:length(depths))
end

function part1(input)
    return countIncreases(_parse(input), 1)
end

function part2(input)
    return countIncreases(_parse(input), 3)
end

TEST = split("""
199
200
208
210
200
207
240
269
260
263
""")

function samples()
    @assert part1(TEST) == 7
    @assert part2(TEST) == 5
end

function main(args)
    if isempty(args)
        samples()
        input = Aocd.getInputData(2021, 1)
        ans1 = part1(input)
        println("Part 1: $ans1")
        ans2 = part2(input)
        println("Part 2: $ans2")
    else
        if args[1] == "1"
            println(part1(readlines(args[2])))
        else
            println(part2(readlines(args[2])))
        end
    end
end

main(ARGS)
