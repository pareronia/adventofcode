#! /usr/bin/env julia
#

include("aoc_main.jl")
if abspath(PROGRAM_FILE) == @__FILE__
    include("aocd.jl")
    using .Aocd
end

module AoC2015_03

include("aoc.jl")

function move(curr, ch)
    if ch == '^'
        return (x=curr.x, y=curr.y+1)
    elseif ch == '>'
        return (x=curr.x+1, y=curr.y)
    elseif ch == 'v'
        return (x=curr.x, y=curr.y-1)
    elseif ch == '<'
        return (x=curr.x-1, y=curr.y)
    end
end

function part1(input)
    visited = Set()
    curr = (x=0, y=0)
    push!(visited, curr)
    for ch ∈ input[1]
        curr = move(curr, ch)
        push!(visited, curr)
    end
    return length(visited)
end

function part2(input)
    visited = Set()
    currS = (x=0, y=0)
    push!(visited, currS)
    currR = (x=0, y=0)
    push!(visited, currR)
    for (i, ch) ∈ enumerate(input[1])
        if i % 2 == 0
            currS = move(currS, ch)
            push!(visited, currS)
        else
            currR = move(currR, ch)
            push!(visited, currR)
        end
    end
    return length(visited)
end

TEST1 = @aoc_splitlines(">")
TEST2 = @aoc_splitlines("^>v<")
TEST3 = @aoc_splitlines("^v^v^v^v^v")
TEST4 = @aoc_splitlines("^v")

function samples()
    @assert part1(TEST1) == 2
    @assert part1(TEST2) == 4
    @assert part1(TEST3) == 2
    @assert part2(TEST4) == 3
    @assert part2(TEST2) == 3
    @assert part2(TEST3) == 11
end

end # module AoC2015_03

aoc_main(@__FILE__, ARGS, 2015, 3)
