#! /usr/bin/env julia
#

include("aoc_main.jl")
if abspath(PROGRAM_FILE) == @__FILE__
    include("aocd.jl")
    using .Aocd
end

module AoC2021_11

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
            for (Δr, Δc) ∈ OCTANTS
            if r + Δr ∈ 1:size(grid, 1) && c + Δc ∈ 1:size(grid, 2))
end

function cycle!(grid)
    function flash!(grid, rc, flashes)
        grid[rc] = 0
        flashes[1] += 1
        for n ∈ neighbours(grid, rc[1], rc[2])
            grid[n] == 0 && continue
            grid[n] += 1
            grid[n] > 9 && flash!(grid, n, flashes)
        end
    end
    grid[:] .+= 1
    flashes = zeros(Int, 1)
    for r ∈ 1:size(grid, 1), c ∈ 1:size(grid, 2)
        rc = CartesianIndex(r, c)
        if grid[rc] > 9
            flash!(grid, rc, flashes)
        end
    end
    return flashes[1]
end

function part1(input)
    grid = _parse(input)
    return sum(cycle!(grid) for i ∈ 1:100)
end

function part2(input)
    grid = _parse(input)
    grid_size = prod(size(grid))
    cycle_count = 1
    while true
        flashes = cycle!(grid)
        if flashes == grid_size
            break
        end
        cycle_count += 1
    end
    return cycle_count
end

TEST = @aoc_splitlines("""\
5483143223
2745854711
5264556173
6141336146
6357385478
4167524645
2176841721
6882881134
4846848554
5283751526
""")

function samples()
    @assert part1(TEST) == 1656
    @assert part2(TEST) == 195
end

end # module AoC2021_11

aoc_main(@__FILE__, ARGS, 2021, 11)
