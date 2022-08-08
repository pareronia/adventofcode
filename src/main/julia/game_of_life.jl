module GameOfLife

using IterTools

abstract type Type end

struct InfiniteGrid <: Type
    dim::Int
    deltas::Vector{Tuple{Int,Int,Int,Int}}
end

function InfiniteGrid(dim::Int)
    deltas = collect(p for p ∈ product(-1:1, -1:1, -1:1, -1:1)
                     if p != (0, 0, 0, 0))
    return InfiniteGrid(dim, deltas)
end

struct Rules
    alive::Function
end

classicRules = Rules((cell, cnt, alive) -> cnt == 3 || cnt == 2 && cell ∈ alive)

struct Generation
    alive::Set{Tuple{Int,Int,Int,Int}}
    type::Type
    rules::Rules
end

function next(generation::Generation)
    function isAlive(cell)
        cnt = neighbourCount(generation.type, cell, generation.alive)
        return generation.rules.alive(cell, cnt, generation.alive)
    end

    newAlive = Set{Tuple{Int,Int,Int,Int}}([
        cell for cell ∈ cells(generation.type, generation.alive)
        if isAlive(cell)])
    return Generation(newAlive, generation.type, generation.rules)
end

function cells(type::InfiniteGrid, alive)
    function expand(alive, index::Int)
        values = [a[index] for a ∈ alive]
        return (minimum(values) - 1):(maximum(values) + 1)
    end

    return product(expand(alive, 1),
                   expand(alive, 2),
                   type.dim >= 3 ? expand(alive, 3) : 0:0,
                   type.dim >= 4 ? expand(alive, 4) : 0:0)
end

function neighbourCount(type::InfiniteGrid, cell, alive)
    cnt = 0
    (x, y, z, w) = cell
    for (Δx, Δy, Δz, Δw) ∈ type.deltas
        if in((x + Δx, y + Δy, z + Δz, w + Δw), alive)
            cnt += 1
        end
    end
    return cnt
end

end  # module GameOfLife
