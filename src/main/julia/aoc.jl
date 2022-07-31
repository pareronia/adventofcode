macro aoc_splitlines(lines::String)
    split(rstrip(lines, ['\r', '\n']), r"\r?\n")
end

function aoc_to_blocks(lines)
    blocks = Vector{Vector{String}}()
    push!(blocks, Vector{String}())
    idx = 1
    for line ∈ lines
        if isempty(line)
            push!(blocks, Vector{String}())
            idx += 1
        else
            push!(blocks[idx], line)
        end
    end        
    return blocks
end

const Heading_N = (-1, 0)
const Heading_NE = (-1, 1)
const Heading_E = (0, 1)
const Heading_SE = (1, 1)
const Heading_S = (1, 0)
const Heading_SW = (1, -1)
const Heading_W = (0, -1)
const Heading_NW = (-1, -1)
const OCTANTS = (Heading_N, Heading_NE, Heading_E, Heading_SE,
                 Heading_S, Heading_SW, Heading_W, Heading_NW)
const CAPITALS = (Heading_N, Heading_E, Heading_S, Heading_W)
