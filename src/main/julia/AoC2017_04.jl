#! /usr/bin/env julia
#

include("aoc_main.jl")
if abspath(PROGRAM_FILE) == @__FILE__
    include("aocd.jl")
    using .Aocd
end

module AoC2017_04

include("aoc.jl")

function merge(d, key, value, func)
    d[key] = func(get(d, key, 0), value)
end

function hasNoDuplicateWords(line)
    d::Dict{String,Integer} = Dict()
    foreach(word -> merge(d, word, 1, +), split(line))
    return all(==(1), values(d))
end

function hasNoAnagrams(line)
    words = split(line)
    letterCounts::Set{Dict{Char,Int}} = Set()
    for word ∈ words
        d::Dict{Char,Integer} = Dict()
        foreach(ch -> merge(d, ch, 1, +), word)
        push!(letterCounts, d)
    end
    return length(words) == length(letterCounts)

end

function solve(input, strategy::Function)
    return count(line->strategy(line), input)
end

function part1(input)
    return solve(input, hasNoDuplicateWords)
end

function part2(input)
    return solve(input, hasNoAnagrams)
end

function samples()
    @assert part1(("aa bb cc dd ee",)) == 1
    @assert part1(("aa bb cc dd aa",)) == 0
    @assert part1(("aa bb cc dd aaa",)) == 1
    @assert part2(("abcde fghij",)) == 1
    @assert part2(("abcde xyz ecdab",)) == 0
    @assert part2(("a ab abc abd abf abj",)) == 1
    @assert part2(("iiii oiii ooii oooi oooo",)) == 1
    @assert part2(("oiii ioii iioi iiio",)) == 0
end

end # module AoC2017_04

aoc_main(@__FILE__, ARGS, 2017, 4)
