#! /usr/bin/env julia
#

include("aoc_main.jl")
if abspath(PROGRAM_FILE) == @__FILE__
    include("aocd.jl")
    using .Aocd
end

module AoC2021_03

include("aoc.jl")

function bitcount(input, pos)
    size = length(input)
    c = count(==('1'), [input[r][pos] for r = 1:size])
    return c >= size / 2 ? ['1', '0'] : ['0', '1']
end

function ans(value1, value2)
    return prod(parse(Int, value, base=2)
                for value ∈ [value1, value2])
end

function part1(input)
    bc = [bitcount(input, pos) for pos ∈ 1:sizeof(input[1])]
    γ, ϵ = [join([x[i] for x ∈ bc]) for i ∈ 1:2]
    return ans(γ, ϵ)
end

function part2(input)
    function reduce(input, i)
        strings = copy(input)
        pos = 1
        while length(strings) > 1
            keep = bitcount(strings, pos)[i]
            strings = strings[findall(s -> s[pos] == keep, strings)]
            pos += 1
        end
        return strings[1]
    end
    o2, co2 = [reduce(input, i) for i ∈ 1:2]
    return ans(o2, co2)
end

TEST = @aoc_splitlines("""
00100
11110
10110
10111
10101
01111
00111
11100
10000
11001
00010
01010
""")

function samples()
    @assert part1(TEST) == 198
    @assert part2(TEST) == 230
end

end # module AoC2021_03

aoc_main(@__FILE__, ARGS, 2021, 3)
