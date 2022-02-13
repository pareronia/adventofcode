#! /usr/bin/env julia
#

include("aoc_main.jl")
if abspath(PROGRAM_FILE) == @__FILE__
    include("aocd.jl")
    using .Aocd
end

module AoC2021_09

include("aoc.jl")

function _parse(input)
    grid = Matrix{Int}(undef, length(input), length(input[1]))
    for r ∈ 1:length(input), c ∈ 1:length(input[r])
        grid[CartesianIndex(r, c)] = parse(Int, input[r][c])
    end
    return grid
end

function neighbours(grid, r, c)
    return (CartesianIndex(r + Δr, c + Δc)
            for (Δr, Δc) ∈ CAPITALS
            if r + Δr ∈ 1:size(grid, 1) && c + Δc ∈ 1:size(grid, 2))
end

function find_lows(grid)
    return (CartesianIndex(r, c)
            for r ∈ 1:size(grid, 1), c ∈ 1:size(grid, 2)
            if all(grid[CartesianIndex(r, c)] < grid[n]
                   for n ∈ neighbours(grid, r, c)))
end

function part1(input)
    grid = _parse(input)
    return sum([grid[rc] + 1 for rc ∈ find_lows(grid)])
end

function part2(input)
    function sizeof_basin_round_low(grid, rc)
        basin = Set{CartesianIndex}()
        q = Vector{CartesianIndex}()
        push!(q, rc)
        while length(q) > 0
            c = popfirst!(q)
            push!(basin, c)
            for n ∈ neighbours(grid, c[1], c[2])
                (n ∉ basin && grid[n] != 9) && push!(q, n)
            end
        end
        return length(basin)
    end
    grid = _parse(input)
    sizes = [sizeof_basin_round_low(grid, low) for low ∈ find_lows(grid)]
    return prod(sort(sizes, rev=true)[1:3])
end

TEST = @aoc_splitlines("""
2199943210
3987894921
9856789892
8767896789
9899965678
""")

function samples()
    @assert part1(TEST) == 15
    @assert part2(TEST) == 1134
end

end # module AoC2021_09

aoc_main(@__FILE__, ARGS, 2021, 9)
