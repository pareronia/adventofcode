#! /usr/bin/env julia
#

include("aoc_main.jl")
if abspath(PROGRAM_FILE) == @__FILE__
    include("aocd.jl")
    using .Aocd
end

module AoC2015_06

include("aoc.jl")

const REGEXP = r"([ a-z]+) ([0-9]+),([0-9]+) through ([0-9]+),([0-9]+)"

function solve(input, lights, f)
    for line ∈ input
        m = match(REGEXP, line)
        start_x, end_x, start_y, end_y = [parse(Int, x) + 1
                                          for x ∈ m.captures[2:5]]
        command = m.captures[1]
    f(lights, command, start_x:start_y, end_x:end_y)
    end
    return sum(lights)
end

function part1(input)
    function process1(lights, command, start, _end)
        if command == "turn on"
            lights[start, _end] .= true
        elseif command == "turn off"
            lights[start, _end] .= false
        else
            lights[start, _end] .= .!lights[start, _end]
        end
    end
    return solve(input, falses(1000, 1000), process1)
end

function part2(input)
    function process2(lights, command, start, _end)
        if command == "turn on"
            lights[start, _end] .+= 1
        elseif command == "turn off"
            lights[start, _end] .-= 1
            lights[lights .< 0] .= 0
        else
            lights[start, _end] .+= 2
        end
    end
    return solve(input, zeros(Int, 1000, 1000), process2)
end

TEST1 = @aoc_splitlines("turn on 0,0 through 999,999")
TEST2 = @aoc_splitlines("toggle 0,0 through 999,0")
TEST3 = @aoc_splitlines("turn off 499,499 through 500,500")
TEST4 = @aoc_splitlines("turn on 0,0 through 0,0")
TEST5 = @aoc_splitlines("toggle 0,0 through 999,999")

function samples()
    @assert part1(TEST1) == 1_000_000
    @assert part1(TEST2) == 1000
    @assert part1(TEST3) == 0
    @assert part2(TEST4) == 1
    @assert part2(TEST5) == 2_000_000
end

end # module AoC2015_06

aoc_main(@__FILE__, ARGS, 2015, 6)
