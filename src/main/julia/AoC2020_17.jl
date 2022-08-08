#! /usr/bin/env julia
#

include("aoc_main.jl")
if abspath(PROGRAM_FILE) == @__FILE__
    include("aocd.jl")
    using .Aocd
end

module AoC2020_17

include("aoc.jl")
include("game_of_life.jl")

ON = '#'
GENERATIONS = 6

function _parse(input, dim::Int)
    alive = Set([(r, c, 0, 0)
                 for (r, row) ∈ enumerate(input)
                 for (c, state) ∈ enumerate(row)
                 if state == ON])
    type = GameOfLife.InfiniteGrid(dim)
    return GameOfLife.Generation(alive, type, GameOfLife.classicRules)
end

function solve(input, dim::Int)
    gen = _parse(input, dim)
    for i ∈ 1:GENERATIONS
        gen = GameOfLife.next(gen)
    end
    return length(gen.alive)
end

function part1(input)
    return solve(input, 3)
end

function part2(input)
    return solve(input, 4)
end

TEST = @aoc_splitlines("""\
.#.
..#
###
""")

function samples()
    @assert part1(TEST) == 112
    @assert part2(TEST) == 848
end

end # module AoC2020_17

aoc_main(@__FILE__, ARGS, 2020, 17)
