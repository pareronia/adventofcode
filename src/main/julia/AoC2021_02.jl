#! /usr/bin/env julia
#

include("aoc.jl")
include("aocd.jl")
using .Aocd

const FORWARD = "forward"
const UP = "up"
const DOWN = "down"

function _parse(line)
    tokens = split(line)
    if tokens[1] ∉ Set([UP, DOWN, FORWARD])
        throw(ArgumentError("Invalid input"))
    end
    return tokens[1], parse(Int, tokens[2])
end

function part1(input)
    hor, ver = 0, 0
    for line ∈ input
        command, amount = _parse(line)
        if command == UP
            ver -= amount
        elseif command == DOWN
            ver += amount
        else
            hor += amount
        end
    end
    return hor * ver
end

function part2(input)
    hor, ver, aim = 0, 0, 0
    for line ∈ input
        command, amount = _parse(line)
        if command == UP
            aim -= amount
        elseif command == DOWN
            aim += amount
        else
            hor += amount
            ver += aim * amount
        end
    end
    return hor * ver
end

TEST = @aoc_splitlines("""
forward 5
down 5
forward 8
up 3
down 8
forward 2
""")

function samples()
    @assert part1(TEST) == 150
    @assert part2(TEST) == 900
end

@aoc_main 2021 2
