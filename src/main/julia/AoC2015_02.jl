#! /usr/bin/env julia
#

include("aoc_main.jl")
if abspath(PROGRAM_FILE) == @__FILE__
    include("aocd.jl")
    using .Aocd
end

module AoC2015_02

include("aoc.jl")

function solve(input, f)
    ans = 0
    for line ∈ input
        l, w, h = parse.(Int, split(line, "x"))
        ans += f(l, w, h)
    end
    return ans
end

function part1(input)
    function area(l, w, h)
        sides = (2 * l * w, 2 * w * h, 2 * h * l)
        return sum(sides) + minimum(sides) ÷ 2
    end
    return solve(input, (l, w, h) -> area(l, w, h))
end

function part2(input)
    function length(l, w, h)
        circumferences = (2 * (l + w), 2 * (w + h), 2 * (h + l))
        return minimum(circumferences) + l * w * h
    end
    return solve(input, (l, w, h) -> length(l, w, h))
end

TEST1 = @aoc_splitlines("2x3x4")
TEST2 = @aoc_splitlines("1x1x10")

function samples()
    @assert part1(TEST1) == 58
    @assert part1(TEST2) == 43
    @assert part2(TEST1) == 34
    @assert part2(TEST2) == 14
end

end # module AoC2015_02

aoc_main(@__FILE__, ARGS, 2015, 2)
