#! /usr/bin/env julia
#

include("aoc_main.jl")
if abspath(PROGRAM_FILE) == @__FILE__
    include("aocd.jl")
    using .Aocd
end

module AoC2017_03

include("aoc.jl")

struct HeadingsAndPeriods
    periods::Vector{Int}
    headings::Vector{Tuple{Int,Int}}

    HeadingsAndPeriods() = new([1, 1, 2, 2],
                               [Heading_E, Heading_N, Heading_W, Heading_S])
end

function getHeadingAndPeriod(headingsAndPeriods::HeadingsAndPeriods, t::Int)
    idx = t % 4 + 1
    period = headingsAndPeriods.periods[idx]
    headingsAndPeriods.periods[idx] = period + 2
    heading = headingsAndPeriods.headings[idx]
    return heading, period
end

mutable struct Coordinate
    x::Int
    y::Int
    j::Int
    k::Int
    headingsAndPeriods::HeadingsAndPeriods
    heading
    period::Int
end

Coordinate() = begin
    c = Coordinate(0, 0, 0, 0, HeadingsAndPeriods(), nothing, 0)
    c.heading, c.period = getHeadingAndPeriod(c.headingsAndPeriods, 0)
    return c
end

function next(c::Coordinate)
    if c.j == c.period
        c.k += 1
        c.heading, c.period = getHeadingAndPeriod(c.headingsAndPeriods, c.k)
        c.j = 0
    end
    c.x += c.heading[1]
    c.y += c.heading[2]
    c.j += 1
    return c
end

function _parse(input)
    @assert length(input) == 1
    return parse.(Int, input[1])
end

function part1(input)
    square::Int = _parse(input)
    square == 1 && return 0

    i::Int = 1
    c::Coordinate = Coordinate()
    while i < square
        i += 1
        c = next(c)
        i == square && return abs(c.x) + abs(c.y)
    end
    @assert false
end

function part2(input)
    square::Int = _parse(input)
    square == 1 && return 1

    squares::Dict{Tuple{Int,Int},Int} = Dict((0, 0) =>  1)
    c::Coordinate = Coordinate()
    while true
        c = next(c)
        value::Int = 0
        for (Δx, Δy) in OCTANTS
            neighbour = (c.x + Δx, c.y + Δy)
            value += haskey(squares, neighbour) ? squares[neighbour] : 0
        end
        squares[(c.x, c.y)] = value
        value > square && return value
    end
end

function samples()
    @assert part1(("1",)) == 0
    @assert part1(("12",)) == 3
    @assert part1(("23",)) == 2
    @assert part1(("1024",)) == 31
    @assert part2(("1",)) == 1
    @assert part2(("2",)) == 4
    @assert part2(("3",)) == 4
    @assert part2(("4",)) == 5
    @assert part2(("5",)) == 10
end

end # module AoC2017_03

aoc_main(@__FILE__, ARGS, 2017, 3)
