#! /usr/bin/env julia
#

include("aoc_main.jl")
if abspath(PROGRAM_FILE) == @__FILE__
    include("aocd.jl")
    using .Aocd
end

module AoC2017_13

include("aoc.jl")

struct Layer
    depth::Int
    range::Int
end

function Layer((a, b)::Tuple)
    return Layer(a, b)
end

function _parse(input)
    return [Layer(Tuple(parse.(Int, split(line, ": ")))) for line ∈ input]
end

function caught(layer::Layer, delay::Int)
    return (layer.depth + delay) % ((layer.range - 1) * 2) == 0
end

function part1(input)
    layers = _parse(input)
    return sum(layer -> layer.depth * layer.range,
               (layer for layer ∈ layers if caught(layer, 0)))
end

function part2(input)
    layers = _parse(input)
    delay::Int = 0
    while any(caught(layer, delay) for layer ∈ layers)
        delay += 1
    end
    return delay
end

TEST = @aoc_splitlines("""
0: 3
1: 2
4: 4
6: 4
""")

function samples()
    @assert part1(TEST) == 24
    @assert part2(TEST) == 10
end

end # module AoC2017_13

aoc_main(@__FILE__, ARGS, 2017, 13)
