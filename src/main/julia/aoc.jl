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

const OCTANTS = ((-1, 0), (-1, 1), (0, 1), (1, 1),
                 (1, 0), (1, -1), (0, -1), (-1, -1))
const CAPITALS = ((-1, 0), (0, 1), (1, 0), (0, -1))
